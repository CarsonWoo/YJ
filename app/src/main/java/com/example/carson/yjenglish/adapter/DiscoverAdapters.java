package com.example.carson.yjenglish.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.RoundRectImageView;
import com.example.carson.yjenglish.discover.model.DiscoverInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 84594 on 2018/8/8.
 */

public class DiscoverAdapters {
    public static class AtyAdapter extends RecyclerView.Adapter<AtyAdapter.AtyHolder> {

        private Context ctx;
        private List<DiscoverInfo.DiscoverItem.WelfareService> mList;

        private OnDiscoverItemClickListener listener;

        private static final int VIEW_TYPE_EMPTY = 0;
        private static final int VIEW_TYPE_COMMON = 1;

        public AtyAdapter(Context ctx, List<DiscoverInfo.DiscoverItem.WelfareService> mList) {
            this.ctx = ctx;
            this.mList = mList;
        }

        @NonNull
        @Override
        public AtyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_welfare_card, parent, false);

            return new AtyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AtyHolder holder, int position) {
            holder.item = mList.get(position);
            //到时需要将此换成title
            holder.title.setText(holder.item.getId());
            holder.slogan.setText("");
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String info;
            if (holder.item.getEt() == null || holder.item.getEt().isEmpty()) {
                info = "活动策划中...";
            } else {
                info = "结束时间:" + df.format(new Date(Long.parseLong(holder.item.getEt())));
            }
            holder.info.setText(info);
            Glide.with(holder.bg.getContext()).load(holder.item.getPic()).thumbnail(0.1f)
                    .into(holder.bg);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onActivityClick(holder.item.getUrl());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public void setListener(OnDiscoverItemClickListener listener) {
            this.listener = listener;
        }

        public class AtyHolder extends BaseViewHolder {

            View itemView;
            RoundRectImageView bg;
            TextView title, slogan, info;
            DiscoverInfo.DiscoverItem.WelfareService item;

            public AtyHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                bg = itemView.findViewById(R.id.bg_welfare);
                title = itemView.findViewById(R.id.welfare_title);
                slogan = itemView.findViewById(R.id.welfare_slogan);
                info = itemView.findViewById(R.id.welfare_info);
            }
        }
    }

    public interface OnDiscoverItemClickListener {
        void onActivityClick(String url);
    }
}
