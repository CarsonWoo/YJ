package com.example.carson.yjenglish.home.viewbinder.feeds;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.home.model.forviewbinder.Recommend;
import com.example.carson.yjenglish.home.model.forviewbinder.RecommendList;
import com.example.carson.yjenglish.home.view.feeds.HomeItemAty;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/8/18.
 */

public class RecommendViewBinder extends ItemViewBinder<Recommend, RecommendViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.home_detail_item_recommend, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Recommend item) {
        holder.username.setText(item.getUsername());
        holder.tag.setText(item.getTag());
        holder.title.setText(item.getTitle());
        Glide.with(holder.img.getContext()).load(item.getImgUrl()).thumbnail(0.8f).into(holder.img);
        Glide.with(holder.portrait.getContext())
                .load(item.getPortraitUrl())
                .thumbnail(0.8f)
                .into(holder.portrait);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDetail = new Intent(holder.itemView.getContext(), HomeItemAty.class);
                toDetail.putExtra("id", item.getId());
                toDetail.putExtra("video_url", item.getVideo_url());
                toDetail.putExtra("img_url", item.getImgUrl());
                toDetail.putExtra("username", item.getUsername());
                toDetail.putExtra("title", item.getTitle());
                toDetail.putExtra("portrait_url", item.getPortraitUrl());
                toDetail.putExtra("like_num", item.getLikes());
                holder.itemView.getContext().startActivity(toDetail);
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        private ImageView img;
        private TextView title;
        private TextView tag;
        private TextView username;
        private CircleImageView portrait;
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            img = itemView.findViewById(R.id.recommend_img);
            title = itemView.findViewById(R.id.title);
            tag = itemView.findViewById(R.id.recommend_tag);
            username = itemView.findViewById(R.id.username);
            portrait = itemView.findViewById(R.id.portrait);
        }
    }

}
