package com.example.carson.yjenglish.zone.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.customviews.RoundRectImageView;
import com.example.carson.yjenglish.zone.model.forviewbinder.TVFeeds;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/9/6.
 */

public class TVFeedsBinder extends ItemViewBinder<TVFeeds, TVFeedsBinder.ViewHolder> {

    private OnTVFeedsClickListener listener;

    public TVFeedsBinder(OnTVFeedsClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.zone_like_tv_feeds, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final TVFeeds item) {
        holder.tag.setText(item.getTag());
        holder.viewNum.setText(item.getViewNum());
        holder.time.setText(item.getTime());
        holder.soundMark.setText(item.getSoundMark());
        Glide.with(holder.coverImg.getContext()).load(item.getImgUrl()).thumbnail(0.5f).into(holder.coverImg);
        holder.delete.setVisibility(View.VISIBLE);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onVideoItemClick(item.getId());
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onVideoDelete(item.getId(), holder.getAdapterPosition());
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        private RoundRectImageView coverImg;
        private TextView tag;
        private TextView soundMark;
        private TextView viewNum;
        private TextView time;
        private ImageView delete;
        private CardView mCardView;
        public ViewHolder(View itemView) {
            super(itemView);
            coverImg = itemView.findViewById(R.id.cover_img);
            tag = itemView.findViewById(R.id.word_tag);
            soundMark = itemView.findViewById(R.id.soundmark);
            viewNum = itemView.findViewById(R.id.view_num);
            time = itemView.findViewById(R.id.like_time);
            delete = itemView.findViewById(R.id.delete);
            mCardView = itemView.findViewById(R.id.my_card_view);
        }
    }

    public interface OnTVFeedsClickListener {
        void onVideoItemClick(String video_id);
        void onVideoDelete(String video_id, int pos);
    }
}
