package com.example.carson.yjenglish.zone.viewbinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.carson.yjenglish.ImageShowActivity;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.StartActivity;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.customviews.RoundRectImageView;
import com.example.carson.yjenglish.zone.model.forviewbinder.DailyCardItem;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/9/6.
 */

public class DailyCardItemBinder extends ItemViewBinder<DailyCardItem, DailyCardItemBinder.ViewHolder> {

    private OnDailyCardItemClickListener listener;

    public DailyCardItemBinder(OnDailyCardItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected DailyCardItemBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.zone_like_daily_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull final DailyCardItemBinder.ViewHolder holder, @NonNull final DailyCardItem item) {
        holder.time.setText(item.getTime());
        Glide.with(holder.coverImg.getContext())
                .load(item.getSmallImgUrl())
                .thumbnail(0.5f)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (holder.coverImg.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            holder.coverImg.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        return false;
                    }
                })
                .into(holder.coverImg);
        holder.delete.setVisibility(View.VISIBLE);

        holder.coverImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.coverImg.getContext(), ImageShowActivity.class);
                intent.putExtra("img_url", item.getImgUrl());
                holder.coverImg.getContext().startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDailyCardDelete(item.getId(), holder.getAdapterPosition());
                }
            }
        });

    }

    static class ViewHolder extends BaseViewHolder {
        private RoundRectImageView coverImg;
        private TextView time;
        private ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            coverImg = itemView.findViewById(R.id.cover_img);
            time = itemView.findViewById(R.id.like_time);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface OnDailyCardItemClickListener {
        void onDailyCardDelete(String id, int pos);
    }
}
