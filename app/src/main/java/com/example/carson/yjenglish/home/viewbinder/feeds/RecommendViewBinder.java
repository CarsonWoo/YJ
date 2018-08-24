package com.example.carson.yjenglish.home.viewbinder.feeds;

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
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Recommend item) {
        holder.username.setText(item.getUsername());
        holder.tag.setText(item.getTag());
        holder.title.setText(item.getTitle());
        Glide.with(holder.img.getContext()).load(item.getImgUrl()).thumbnail(0.8f).into(holder.img);
        Glide.with(holder.portrait.getContext()).load(item.getPortraitUrl()).thumbnail(0.8f).into(holder.portrait);
    }

    static class ViewHolder extends BaseViewHolder {
        private ImageView img;
        private TextView title;
        private TextView tag;
        private TextView username;
        private CircleImageView portrait;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.recommend_img);
            title = itemView.findViewById(R.id.title);
            tag = itemView.findViewById(R.id.recommend_tag);
            username = itemView.findViewById(R.id.username);
            portrait = itemView.findViewById(R.id.portrait);
        }
    }
}
