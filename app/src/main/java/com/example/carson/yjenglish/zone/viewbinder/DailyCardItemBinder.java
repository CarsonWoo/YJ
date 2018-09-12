package com.example.carson.yjenglish.zone.viewbinder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.customviews.RoundRectImageView;
import com.example.carson.yjenglish.zone.model.forviewbinder.DailyCardItem;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/9/6.
 */

public class DailyCardItemBinder extends ItemViewBinder<DailyCardItem, DailyCardItemBinder.ViewHolder> {
    @NonNull
    @Override
    protected DailyCardItemBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.zone_like_daily_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull DailyCardItemBinder.ViewHolder holder, @NonNull DailyCardItem item) {
        holder.time.setText(item.getTime());
        Glide.with(holder.coverImg.getContext()).load(item.getImgUrl()).thumbnail(0.5f).into(holder.coverImg);
        holder.delete.setVisibility(View.VISIBLE);
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
}
