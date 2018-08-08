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

    public TVListAdapter(){}
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

    private void bindList(final TVHolder holder, int position) {
        holder.item = mItemList.get(position - 1);
        holder.word.setText(holder.item.getWord());
        holder.soundMark.setText(holder.item.getSoundMark());
        holder.likeNum.setText(holder.item.getLikeNum());
        holder.commentNum.setText(holder.item.getCommentNum());
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemListener != null) {
                    itemListener.onVideoClick(holder.itemView, holder.item.getVideoUrl());
                }
            }
        });
        holder.mClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemListener != null) {
                    itemListener.onItemClick(view);
                }
            }
        });
        holder.videoBg.setBackgroundColor(Color.parseColor("#eeeeff"));
    }

    private void bindHeader(TVHeadHolder holder) {
        TVHeader firstItem = mHeaderList.get(0);
        TVHeader secondItem = mHeaderList.get(1);
        Glide.with(ctx).load(firstItem.getImgUrl()).asBitmap().into(holder.first);
        Glide.with(ctx).load(secondItem.getImgUrl()).asBitmap().into(holder.second);
        holder.viewNumFirst.setText(firstItem.getPlayNum());
        holder.viewNumSecond.setText(secondItem.getPlayNum());
        holder.wordFirst.setText(firstItem.getWord());
        holder.wordSecond.setText(secondItem.getWord());

        //可以考虑放到HomeAty跳转
        holder.first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("adapter", "img click");
                if (headerlistener != null) {
                    headerlistener.onHeaderClick(view);
                }
            }
        });
        holder.second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("adapter", "img click");
                if (headerlistener != null) {
                    headerlistener.onHeaderClick(view);
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
        return 5;
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
        ImageView videoBg;
        TextView word;
        SoundMarkTextView soundMark;
        TextView commentNum;
        TextView likeNum;
        TVItem item;
        RelativeLayout mClickView;
        ConstraintLayout itemView;

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
        }
    }

    public class TVHeadHolder extends BaseViewHolder {

        RoundRectImageView first;
        RoundRectImageView second;
        TextView wordFirst;
        TextView wordSecond;
        TextView viewNumFirst;
        TextView viewNumSecond;

        public TVHeadHolder(View itemView) {
            super(itemView);
            first = itemView.findViewById(R.id.header_first);
            second = itemView.findViewById(R.id.header_second);
            viewNumFirst = itemView.findViewById(R.id.first_view_num);
            viewNumSecond = itemView.findViewById(R.id.second_view_num);
            wordFirst = itemView.findViewById(R.id.word_first);
            wordSecond = itemView.findViewById(R.id.word_second);
        }
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(View view);
    }

    public interface OnItemClickListener {
        void onVideoClick(View view, String path);//点击了直接播放
        void onItemClick(View view);//点击了跳转
    }
}
