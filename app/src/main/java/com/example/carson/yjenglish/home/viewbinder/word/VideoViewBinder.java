package com.example.carson.yjenglish.home.viewbinder.word;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.customviews.RoundRectImageView;
import com.example.carson.yjenglish.home.model.forviewbinder.Video;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/8/13.
 */

public class VideoViewBinder extends ItemViewBinder<Video, VideoViewBinder.ViewHolder> {

    private OnVideoClickListener videoClickListener;

    public VideoViewBinder(OnVideoClickListener videoClickListener) {
        this.videoClickListener = videoClickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.word_detail_video, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Video item) {
        Glide.with(holder.videoBg.getContext()).load(item.getBgUrl()).thumbnail(0.3f).into(holder.videoBg);
        holder.videoBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.videoBg.getContext(), "position = " + holder.getAdapterPosition(),
                        Toast.LENGTH_SHORT).show();
                if (videoClickListener != null) {
                    videoClickListener.onVideoClick(item.getVideo_id(), item.getVideoUrl(),
                            holder.getAdapterPosition());
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        private RoundRectImageView videoBg;
        public ViewHolder(View itemView) {
            super(itemView);
            videoBg = itemView.findViewById(R.id.video_bg);
        }
    }

    public interface OnVideoClickListener {
        void onVideoClick(String video_id, String videoUrl, int pos);
    }
}
