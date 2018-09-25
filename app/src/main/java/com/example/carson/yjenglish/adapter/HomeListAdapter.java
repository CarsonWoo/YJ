package com.example.carson.yjenglish.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.MultiPortraitView;
import com.example.carson.yjenglish.customviews.MyProgressView;
import com.example.carson.yjenglish.home.model.HomeInfo;
import com.example.carson.yjenglish.home.model.HomeItem;
import com.example.carson.yjenglish.home.model.LoadHeader;
import com.example.carson.yjenglish.home.model.PicHeader;
import com.example.carson.yjenglish.home.view.HomeFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 84594 on 2018/8/1.
 */

public class HomeListAdapter extends RecyclerView.Adapter {

    private final int TYPE_HEADER = 0;
    private final int TYPE_LIST = 1;

    private final int HEADER_STYLE_PIC = 2;
    private final int HEADER_STYLE_LOAD = 3;
    private final int HEADER_EMPTY = -1;

    private Context mCtx;
    private List<HomeInfo.Data.Feed> mList;
    private PicHeader picItem;
    private LoadHeader loadItem;
    private HomeFragment.OnHomeInteractListener mListener;
    private ArrayList<HomeInfo.Data.Feed> itemSelected = new ArrayList<>();

    private List<String> mPortraitUrls;

    private OnVideoListener videoListener;
    private OnStartListener startListener;
    private OnLoadHeaderListener loadListener;
    private OnItemClickListener itemListener;

    private int headerStyle;

    public HomeListAdapter() {

    }

    public HomeListAdapter(Context ctx, List<HomeInfo.Data.Feed> list, int headerStyle, OnVideoListener listener) {
        this.mCtx = ctx;
        this.mList = list;
        this.videoListener = listener;
        this.headerStyle = headerStyle;
    }

    public HomeListAdapter(Context context, List<HomeInfo.Data.Feed> list, HomeFragment.OnHomeInteractListener listener,
                           int headerStyle, OnVideoListener videoListener) {
        this.mCtx = context;
        this.mList = list;
        this.mListener = listener;
        this.headerStyle = headerStyle;
        this.videoListener = videoListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_LIST;
        }
    }

    public void setPicItem(PicHeader picHeader) {
        this.picItem = picHeader;
    }

    public void setLoadItem(LoadHeader loadItem) {
        this.loadItem = loadItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            if (headerStyle == HEADER_STYLE_PIC) {
                return new PicHolder(LayoutInflater.from(mCtx).inflate
                        (R.layout.home_header_pic, parent, false));
            } else if (headerStyle == HEADER_STYLE_LOAD) {
                return new LoadHolder(LayoutInflater.from(mCtx).inflate
                        (R.layout.home_header_load, parent, false));
            } else {
                //无头部时
                return new EmptyHeader(LayoutInflater.from(mCtx).inflate
                        (R.layout.word_detail_text, parent, false));
            }
        } else if (viewType == TYPE_LIST) {
            return new HomeViewHolder(LayoutInflater.from(mCtx).inflate
                    (R.layout.home_list, parent, false));
        }
        return null;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            if (headerStyle == HEADER_STYLE_LOAD) {
                LoadHolder loadHolder = (LoadHolder) holder;
                bindLoadHolder(loadHolder);
            } else if (headerStyle == HEADER_STYLE_PIC) {
                PicHolder picHolder = (PicHolder) holder;
                bindPicHolder(picHolder);
            }
        } else {
            final HomeViewHolder homeHolder = (HomeViewHolder) holder;
            bindListHolder(homeHolder, position);
        }
    }

    private void bindPicHolder(PicHolder picHolder) {
        if (picItem != null && picItem.getNumber() != null) {
            picHolder.learnerNum.setText(picItem.getNumber());
        }
        picHolder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startListener != null) {
                    startListener.onStartClick(view);
                }
            }
        });
        picHolder.toMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onMusicPressed(view);
                }
            }
        });
        Log.e("adapter", "bind portraits");
        picHolder.multiPortraitView.removeAllViews();
        MultiPortraitView mView = new MultiPortraitView(picHolder.multiPortraitView.getContext(), mPortraitUrls);
        mView.setListener(new MultiPortraitView.LoadMorePortraitListener() {
            @Override
            public void loadMorePortraits() {
                //TODO go to the new tab
            }
        });
        picHolder.multiPortraitView.addView(mView);
    }

    private void bindLoadHolder(LoadHolder loadHolder) {
        loadHolder.remember.setText(String.valueOf(loadItem.getWordsCount()));
        String s = "目标" + loadItem.getTargetCount() + "个";
        loadHolder.target.setText(s);
        s = "剩余" + loadItem.getCountDown() + "天";
        loadHolder.countDown.setText(s);
        if (loadItem.getCountDown() > 10) {
            s = "已坚持" + loadItem.getInsistCount() + "天啦！小坚持有大收获！";
        } else {
            s = "再坚持" + loadItem.getCountDown() + "天，胜利近在咫尺！";
        }
        loadHolder.tips.setText(s);
        if (loadItem.isTodayFinish()) {
            //今天完成了背单词
            loadHolder.start.setVisibility(View.GONE);
            loadHolder.sign.setVisibility(View.VISIBLE);
            loadHolder.more.setVisibility(View.INVISIBLE);
            if (loadItem.isSignClick()) {
                loadHolder.sign.setVisibility(View.INVISIBLE);
                loadHolder.more.setVisibility(View.VISIBLE);
            }
        } else {
            loadHolder.start.setVisibility(View.VISIBLE);
            loadHolder.sign.setVisibility(View.GONE);
            loadHolder.more.setVisibility(View.GONE);
        }
        loadHolder.progressView.setProgress(loadItem.getProgress());
        loadHolder.progressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onProgressClick(view);
                }
            }
        });
        loadHolder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startListener != null) {
                    startListener.onStartClick(view);
                }
            }
        });
        loadHolder.sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loadListener != null) {
                    loadListener.onSignClick(view);
                }
            }
        });
        loadHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loadListener != null) {
                    loadListener.onMoreClick(view);
                }
            }
        });
        loadHolder.toMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onMusicPressed(view);
                }
            }
        });
    }

    private void bindListHolder(final HomeViewHolder homeHolder, int position) {
        homeHolder.item = mList.get(position - 1);
        homeHolder.commentNum.setText(String.valueOf(homeHolder.item.getComments()));
        homeHolder.likeNum.setText(homeHolder.item.getLikes());
        if (homeHolder.item.getTitle() != null) {
            homeHolder.title.setText(homeHolder.item.getTitle());
        }
        if (homeHolder.item.getAuthor_username() != null) {
            homeHolder.name.setText(homeHolder.item.getAuthor_username());
        }
        if (homeHolder.item.getVideo() != null && !homeHolder.item.getVideo().isEmpty()) {
            Glide.with(mCtx).load(homeHolder.item.getPic()).thumbnail(0.6f).into(homeHolder.img);
            homeHolder.video.setVisibility(View.VISIBLE);
            homeHolder.play.setVisibility(View.VISIBLE);
            homeHolder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (videoListener != null) {
                        videoListener.onVideoClick(homeHolder.mCardView, homeHolder.item.getVideo());
                    }
                }
            });
        } else {
            homeHolder.video.setVisibility(View.GONE);
            homeHolder.play.setVisibility(View.GONE);
            Glide.with(mCtx).load(homeHolder.item.getPic()).thumbnail(0.8f).into(homeHolder.img);
        }
        if (homeHolder.item.getAuthor_portrait() != null && !homeHolder.item.getAuthor_portrait().isEmpty()) {
            Glide.with(mCtx).load(homeHolder.item.getAuthor_portrait()).thumbnail(0.8f).into(homeHolder.portrait);
        } else {
            Glide.with(mCtx).load(R.mipmap.ic_launcher_round).into(homeHolder.portrait);
        }
        homeHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    itemSelected.clear();
                    itemSelected.add(homeHolder.item);
                    mListener.onItemClick(itemSelected, false);
                } else {
                    if (itemListener != null) {
                        itemSelected.clear();
                        itemSelected.add(homeHolder.item);
                        itemListener.onClick(itemSelected, false);
                    }
                }
            }
        });
        homeHolder.commentNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    itemSelected.clear();
                    itemSelected.add(homeHolder.item);
                    mListener.onItemClick(itemSelected, true);
                } else {
                    if (itemListener != null) {
                        itemSelected.clear();
                        itemSelected.add(homeHolder.item);
                        itemListener.onClick(itemSelected, true);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    public void setStartListener(OnStartListener startListener) {
        this.startListener = startListener;
    }

    public void setLoadListener(OnLoadHeaderListener loadListener) {
        this.loadListener = loadListener;
    }

    public void setItemListener(OnItemClickListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setmPortraitUrls(List<String> mPortraitUrls) {
        this.mPortraitUrls = mPortraitUrls;
    }

    public class HomeViewHolder extends BaseViewHolder {

        TextView title;
        TextView name;
        TextView commentNum;
        TextView likeNum;
        FrameLayout video;
        ImageView img;
        ImageView play;
        HomeInfo.Data.Feed item;
        ConstraintLayout container;
        CardView mCardView;
        CircleImageView portrait;
        View itemView;

        public HomeViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mCardView = (CardView) itemView;
            title = itemView.findViewById(R.id.content_title);
            name = itemView.findViewById(R.id.username);
            commentNum = itemView.findViewById(R.id.comment_num);
            likeNum = itemView.findViewById(R.id.like_num);
            video = itemView.findViewById(R.id.video);
            container = itemView.findViewById(R.id.item_card);
            img = itemView.findViewById(R.id.img_content);
            play = itemView.findViewById(R.id.item_video_play);
            portrait = itemView.findViewById(R.id.portrait);
        }
    }

    public class PicHolder extends BaseViewHolder {

        ImageView bg;
        TextView learnerNum;
        Button start;
        RelativeLayout toMusic;
        FrameLayout multiPortraitView;

        public PicHolder(View itemView) {
            super(itemView);
            bg = itemView.findViewById(R.id.pic_bg);
            learnerNum = itemView.findViewById(R.id.learner_num);
            start = itemView.findViewById(R.id.btn_start);
            toMusic = itemView.findViewById(R.id.to_music);
            multiPortraitView = itemView.findViewById(R.id.multi_portrait_view);
        }
    }

    public class LoadHolder extends BaseViewHolder {
        TextView tips;
        MyProgressView progressView;
        LinearLayout mDownloadView;//到时需要拆分 因为要显示下载的进度
        TextView remember;
        TextView target;
        TextView countDown;
        Button start;
        Button sign;
        Button more;
        RelativeLayout toMusic;

        public LoadHolder(View itemView) {
            super(itemView);
            tips = itemView.findViewById(R.id.home_tips);
            progressView = itemView.findViewById(R.id.learning_progress_view);
            mDownloadView = itemView.findViewById(R.id.download_control);
            remember = itemView.findViewById(R.id.tv_already_remember);
            target = itemView.findViewById(R.id.tv_target_words);
            countDown = itemView.findViewById(R.id.tv_count_down);
            start = itemView.findViewById(R.id.btn_start);
            sign = itemView.findViewById(R.id.btn_sign_in);
            more = itemView.findViewById(R.id.btn_more_remember);
            toMusic = itemView.findViewById(R.id.to_music);
        }
    }

    public class EmptyHeader extends BaseViewHolder {
        public EmptyHeader(View itemView) {
            super(itemView);
        }
    }

    public interface OnVideoListener {
        void onVideoClick(View view, String url);
    }

    public interface OnStartListener {
        void onStartClick(View view);
    }

    public interface OnLoadHeaderListener {
        void onSignClick(View view);
        void onMoreClick(View view);
    }

    public interface OnItemClickListener {
        void onClick(ArrayList item, boolean requestComment);
    }
}
