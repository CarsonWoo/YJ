package com.example.carson.yjenglish.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.RoundRectImageView;
import com.example.carson.yjenglish.customviews.SoundMarkTextView;
import com.example.carson.yjenglish.tv.model.TVHeader;
import com.example.carson.yjenglish.tv.model.TVItem;

import java.util.List;

/**
 * Created by 84594 on 2018/8/3.
 */

public class TVListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public final int TYPE_HEADER = 0;
    public final int TYPE_LIST = 1;

    private Context ctx;
    private List<TVHeader> mHeaderList;
    private List<TVItem> mItemList;

    private OnHeaderClickListener headerlistener;
    private OnItemClickListener itemListener;

    public TVListAdapter(Context ctx, List<TVItem> mItemList, List<TVHeader> mHeaderList){
        this.ctx = ctx;
        this.mItemList = mItemList;
        this.mHeaderList = mHeaderList;
    }
    public TVListAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new TVHeadHolder(LayoutInflater.from(ctx).inflate(R.layout.tv_header, parent, false));
        }
        return new TVHolder(LayoutInflater.from(ctx).inflate(R.layout.tv_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (position == 0) {
            TVHeadHolder mHolder = (TVHeadHolder) holder;
            bindHeader(mHolder);
        } else {
            TVHolder mHolder = (TVHolder) holder;
            bindList(mHolder, position);
        }
    }

    private void bindList(final TVHolder holder, final int position) {
        holder.item = mItemList.get(position - 1);
        holder.word.setText(holder.item.getWord());
        holder.soundMark.setText(holder.item.getSoundMark());
        holder.ivFavour.setSelected(holder.item.isFavour());
        holder.likeNum.setText(holder.item.getLikeNum());
        holder.commentNum.setText(holder.item.getCommentNum());
        holder.tvPlayNum.setText(holder.item.getPlayNum());
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemListener != null) {
                    itemListener.onVideoClick(holder.itemView,
                            holder.item.getWord_id(), position - 1,
                            holder.item.getVideo_id());
                }
            }
        });
        holder.ivFavour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onFavourClick(holder.item.getVideo_id(),
                            holder.likeNum, holder.item.isFavour());
                    if (holder.item.isFavour()) {
                        //取消赞
                        holder.ivFavour.setSelected(false);
                        holder.item.setFavour(false);
                    } else {
                        holder.ivFavour.setSelected(true);
                        holder.item.setFavour(true);
                    }
                }
            }
        });
        holder.mClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemListener != null) {
                    itemListener.onItemClick(holder.item.getVideo_id(), false,
                            position - 1, holder.item.isFavour());
                }
            }
        });
        holder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onItemClick(holder.item.getVideo_id(), true,
                            position - 1, holder.item.isFavour());
                }
            }
        });
        Glide.with(ctx).load(holder.item.getCoverImg()).error(R.mipmap.video_place_holder_error)
                .dontAnimate().listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (holder.videoBg.getScaleType() != ImageView.ScaleType.FIT_XY) {
                    holder.videoBg.setScaleType(ImageView.ScaleType.FIT_XY);
                }
//                ViewGroup.LayoutParams lp = holder.videoBg.getLayoutParams();
//                int vw = holder.videoBg.getWidth() - holder.videoBg.getPaddingLeft() - holder.videoBg.getPaddingRight();
//
//                float scale = (float) vw / (float) resource.getIntrinsicWidth();
//
//                int vh = Math.round(resource.getIntrinsicHeight() * scale);
//
//                lp.height = vh + holder.videoBg.getPaddingTop() + holder.videoBg.getPaddingBottom();
//
//                holder.videoBg.setLayoutParams(lp);
                return false;
            }
        })
                .thumbnail(0.5f).into(holder.videoBg);
    }

    private void bindHeader(TVHeadHolder holder) {
        TVHeader firstItem = mHeaderList.get(0);
        TVHeader secondItem = mHeaderList.get(1);
        TVHeader thirdItem = mHeaderList.get(2);
        TVHeader fourthItem = mHeaderList.get(3);
        if (mHeaderList.size() > 4) {
            TVHeader fifthItem = mHeaderList.get(4);
            Glide.with(ctx).load(fifthItem.getImgUrl())
                    .thumbnail(0.5f).error(R.mipmap.video_place_holder_error).into(holder.fifth);
            holder.viewNumFifth.setText(fifthItem.getPlayNum());
            holder.wordFifth.setText(fifthItem.getWord());
            holder.fifth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (headerlistener != null) {
                        headerlistener.onHeaderClick(mHeaderList.get(4).getVideo_id());
                    }
                }
            });
        }

        Glide.with(ctx).load(firstItem.getImgUrl()).thumbnail(0.5f).error(R.mipmap.video_place_holder_error).dontAnimate().into(holder.first);
        Glide.with(ctx).load(secondItem.getImgUrl()).thumbnail(0.5f).error(R.mipmap.video_place_holder_error).dontAnimate().into(holder.second);
        Glide.with(ctx).load(thirdItem.getImgUrl()).thumbnail(0.5f).error(R.mipmap.video_place_holder_error).dontAnimate().into(holder.third);
        Glide.with(ctx).load(fourthItem.getImgUrl()).thumbnail(0.5f).error(R.mipmap.video_place_holder_error).dontAnimate().into(holder.fourth);


        holder.viewNumFirst.setText(firstItem.getPlayNum());
        holder.viewNumSecond.setText(secondItem.getPlayNum());
        holder.viewNumThird.setText(thirdItem.getPlayNum());
        holder.viewNumFourth.setText(fourthItem.getPlayNum());


        holder.wordFirst.setText(firstItem.getWord());
        holder.wordSecond.setText(secondItem.getWord());
        holder.wordThird.setText(thirdItem.getWord());
        holder.wordFourth.setText(fourthItem.getWord());


        //可以考虑放到HomeAty跳转
        holder.first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (headerlistener != null) {
                    headerlistener.onHeaderClick(mHeaderList.get(0).getVideo_id());
                }
            }
        });
        holder.second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (headerlistener != null) {
                    headerlistener.onHeaderClick(mHeaderList.get(0).getVideo_id());
                }
            }
        });

        holder.third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (headerlistener != null) {
                    headerlistener.onHeaderClick(mHeaderList.get(0).getVideo_id());
                }
            }
        });

        holder.fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (headerlistener != null) {
                    headerlistener.onHeaderClick(mHeaderList.get(0).getVideo_id());
                }
            }
        });


    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HEADER;
        return TYPE_LIST;
    }

    @Override
    public int getItemCount() {
        return mItemList.size() + 1;
    }

    public void setmHeaderList(List<TVHeader> mHeaderList) {
        this.mHeaderList = mHeaderList;
    }

    public void setmItemList(List<TVItem> mItemList) {
        this.mItemList = mItemList;
    }

    public void setHeaderlistener(OnHeaderClickListener headerlistener) {
        this.headerlistener = headerlistener;
    }

    public void setItemListener(OnItemClickListener itemListener) {
        this.itemListener = itemListener;
    }

    public class TVHolder extends BaseViewHolder {

        FrameLayout video;
        ImageView play;
        public ImageView videoBg;
        TextView word;
        SoundMarkTextView soundMark;
        public TextView commentNum;
        public TextView likeNum;
        TVItem item;
        RelativeLayout mClickView;
        ConstraintLayout itemView;
        ImageView ivComment;
        public ImageView ivFavour;
        ImageView ivPlayNum;
        public TextView tvPlayNum;

        public TVHolder(View itemView) {
            super(itemView);
            this.itemView = (ConstraintLayout) itemView;
            videoBg = itemView.findViewById(R.id.video_bg);
            video = itemView.findViewById(R.id.video);
            play = itemView.findViewById(R.id.item_video_play);
            word = itemView.findViewById(R.id.word);
            soundMark = itemView.findViewById(R.id.soundmark);
            commentNum = itemView.findViewById(R.id.comment_num);
            likeNum = itemView.findViewById(R.id.like_num);
            mClickView = itemView.findViewById(R.id.rl_click_view);
            ivComment = itemView.findViewById(R.id.iv_comment);
            ivFavour = itemView.findViewById(R.id.iv_favour);
            ivPlayNum = itemView.findViewById(R.id.iv_play_num);
            tvPlayNum = itemView.findViewById(R.id.tv_play_num);
        }
    }

    public class TVHeadHolder extends BaseViewHolder {

        RoundRectImageView first;
        RoundRectImageView second;
        RoundRectImageView third;
        RoundRectImageView fourth;
        RoundRectImageView fifth;

        TextView wordFirst;
        TextView wordSecond;
        TextView wordThird;
        TextView wordFourth;
        TextView wordFifth;

        TextView viewNumFirst;
        TextView viewNumSecond;
        TextView viewNumThird;
        TextView viewNumFourth;
        TextView viewNumFifth;



        public TVHeadHolder(View itemView) {
            super(itemView);
            first = itemView.findViewById(R.id.header_first);
            second = itemView.findViewById(R.id.header_second);
            third = itemView.findViewById(R.id.header_third);
            fourth = itemView.findViewById(R.id.header_fourth);
            fifth = itemView.findViewById(R.id.header_fifth);

            viewNumFirst = itemView.findViewById(R.id.first_view_num);
            viewNumSecond = itemView.findViewById(R.id.second_view_num);
            viewNumThird = itemView.findViewById(R.id.third_view_num);
            viewNumFourth = itemView.findViewById(R.id.fourth_view_num);
            viewNumFifth = itemView.findViewById(R.id.fifth_view_num);

            wordFirst = itemView.findViewById(R.id.word_first);
            wordSecond = itemView.findViewById(R.id.word_second);
            wordThird = itemView.findViewById(R.id.word_third);
            wordFourth = itemView.findViewById(R.id.word_fourth);
            wordFifth = itemView.findViewById(R.id.word_fifth);
        }
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(String video_id);
    }

    public interface OnItemClickListener {
        void onVideoClick(View view, String word_id, int position, String video_id);//点击了直接播放
        void onItemClick(String id, boolean requestComment, int position, boolean isFavour);//点击了跳转
        void onFavourClick(String video_id, TextView tv, boolean is_favour);
    }


}
