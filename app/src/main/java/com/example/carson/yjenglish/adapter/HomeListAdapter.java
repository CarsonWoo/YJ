package com.example.carson.yjenglish.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.example.carson.yjenglish.customviews.MyProgressView;
import com.example.carson.yjenglish.home.model.HomeItem;
import com.example.carson.yjenglish.home.model.LoadHeader;
import com.example.carson.yjenglish.home.model.PicHeader;
import com.example.carson.yjenglish.home.view.HomeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 84594 on 2018/8/1.
 */

public class HomeListAdapter extends RecyclerView.Adapter {

    private final int TYPE_HEADER = 0;
    private final int TYPE_LIST = 1;

    private final int HEADER_STYLE_PIC = 2;
    private final int HEADER_STYLE_LOAD = 3;

    private Context mCtx;
    private List<HomeItem> mList;
    private PicHeader picItem;
    private LoadHeader loadItem;
    private HomeFragment.OnHomeInteractListener mListener;
    private ArrayList<HomeItem> itemSelected = new ArrayList<>();

    private OnVideoListener videoListener;
    private OnStartListener startListener;
    private OnLoadHeaderListener loadListener;

    private int headerStyle;

    public HomeListAdapter() {

    }

    public HomeListAdapter(Context context, List<HomeItem> list, HomeFragment.OnHomeInteractListener listener,
                           int headerStyle, OnVideoListener videoListener) {
        this.mCtx = context;
        this.mList = list;
        this.mListener = listener;
//        this.picItem = picHeader;
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
            } else {
                return new LoadHolder(LayoutInflater.from(mCtx).inflate
                        (R.layout.home_header_load, parent, false));
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
            } else {
                PicHolder picHolder = (PicHolder) holder;
                bindPicHolder(picHolder);
            }

        } else {
            final HomeViewHolder homeHolder = (HomeViewHolder) holder;
            bindListHolder(homeHolder, position);
        }
    }

    private void bindPicHolder(PicHolder picHolder) {
        if (picItem.getNumber() != null) {
            picHolder.learnerNum.setText(picItem.getNumber());
        }
        if (picItem.getImgUrl() != null) {
            Glide.with(mCtx).load(picItem.getImgUrl()).into(picHolder.bg);
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
            loadHolder.more.setVisibility(View.VISIBLE);
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
        homeHolder.commentNum.setText(String.valueOf(homeHolder.item.getCommentNum()));
        homeHolder.likeNum.setText(String.valueOf(homeHolder.item.getLikeNum()));
        if (homeHolder.item.getTitle() != null) {
            homeHolder.title.setText(homeHolder.item.getTitle());
        }
        if (homeHolder.item.getUsername() != null) {
            homeHolder.name.setText(homeHolder.item.getUsername());
        }
        if (homeHolder.item.getVideoUrl() != null) {
            homeHolder.img.setVisibility(View.GONE);
            homeHolder.img.setBackgroundColor(Color.parseColor("#f9feff"));
            homeHolder.video.setVisibility(View.VISIBLE);
            homeHolder.play.setVisibility(View.VISIBLE);
            homeHolder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (videoListener != null) {
                        videoListener.onVideoClick(homeHolder.mCardView, homeHolder.item.getVideoUrl());
                    }
                }
            });
        } else {
            homeHolder.video.setVisibility(View.GONE);
            homeHolder.play.setVisibility(View.GONE);
            homeHolder.img.setVisibility(View.VISIBLE);
            Glide.with(mCtx).load(homeHolder.item.getImgUrl()).asBitmap().error(R.drawable.ic_warning).into(homeHolder.img);
            homeHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        itemSelected.clear();
                        itemSelected.add(homeHolder.item);
                        mListener.onItemClick(itemSelected);
                    }
                }
            });
        }
        homeHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    itemSelected.clear();
                    itemSelected.add(homeHolder.item);
                    mListener.onItemClick(itemSelected);
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

    public class HomeViewHolder extends BaseViewHolder {

        TextView title;
        TextView name;
        TextView commentNum;
        TextView likeNum;
        FrameLayout video;
        ImageView img;
        ImageView play;
        HomeItem item;
        ConstraintLayout container;
        CardView mCardView;

        public HomeViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView;
            title = itemView.findViewById(R.id.content_title);
            name = itemView.findViewById(R.id.username);
            commentNum = itemView.findViewById(R.id.comment_num);
            likeNum = itemView.findViewById(R.id.like_num);
            video = itemView.findViewById(R.id.video);
            container = itemView.findViewById(R.id.item_card);
            img = itemView.findViewById(R.id.img_content);

            play = itemView.findViewById(R.id.item_video_play);
        }
    }

    public class PicHolder extends BaseViewHolder {

        ImageView bg;
        TextView learnerNum;
        Button start;
        RelativeLayout toMusic;

        public PicHolder(View itemView) {
            super(itemView);
            bg = itemView.findViewById(R.id.pic_bg);
            learnerNum = itemView.findViewById(R.id.learner_num);
            start = itemView.findViewById(R.id.btn_start);
            toMusic = itemView.findViewById(R.id.to_music);
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
}
