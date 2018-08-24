package com.example.carson.yjenglish.home.viewbinder.feeds;



import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.home.model.forviewbinder.Video;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/8/18.
 */

public class VideoViewBinder extends ItemViewBinder<Video, VideoViewBinder.ViewHolder> {

    private OnVideoClickListener mListener;

    public VideoViewBinder(OnVideoClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.home_detail_header, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Video item) {
        Glide.with(holder.img.getContext()).load(item.getBgUrl()).thumbnail(0.5f).into(holder.img);
        if (item.getVideoUrl() != null && !item.getVideoUrl().isEmpty()) {
            holder.mVideo.setVisibility(View.VISIBLE);
            holder.play.setVisibility(View.VISIBLE);
            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onVideoClick(holder.itemView, item.getVideoUrl());
                    }
                }
            });
        } else {
            holder.mVideo.setVisibility(View.GONE);
            holder.play.setVisibility(View.GONE);
        }
    }

    static class ViewHolder extends BaseViewHolder {
        private ImageView img;
        private FrameLayout mVideo;
        private ImageView play;
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            img = itemView.findViewById(R.id.img);
            mVideo = itemView.findViewById(R.id.video);
            play = itemView.findViewById(R.id.item_video_play);
        }
    }

    public interface OnVideoClickListener {
        void onVideoClick(View view, String url);
    }
}
