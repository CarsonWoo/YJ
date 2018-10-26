package com.example.carson.yjenglish.home.viewbinder.feeds;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.home.model.forviewbinder.Content;
import com.example.carson.yjenglish.home.view.feeds.AuthorAty;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/8/18.
 */

public class ContentViewBinder extends ItemViewBinder<Content, ContentViewBinder.ViewHolder> {

    private OnLikeFabClickListener mListener;
    private LinearLayout mLinearLayout;

    public ContentViewBinder(OnLikeFabClickListener listener, LinearLayout layout) {
        this.mListener = listener;
        this.mLinearLayout = layout;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.home_detail_content, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Content item) {
        holder.title.setText(item.getTitle());
        holder.username.setText(item.getUsername());
        holder.text.removeAllViews();
        holder.text.addView(mLinearLayout);
        //点赞数
        holder.likeNum.setText(item.getLikeNum());
        Glide.with(holder.portrait.getContext()).load(item.getPortraitUrl()).thumbnail(0.6f).into(holder.portrait);
        holder.fabLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onLikeClick(holder.fabLike, holder.likeNum, item.getLikeNum());
                }
            }
        });
        holder.portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toPortrait = new Intent(holder.portrait.getContext(), AuthorAty.class);
                toPortrait.putExtra("portrait_url", item.getPortraitUrl());
                toPortrait.putExtra("username", item.getUsername());
                toPortrait.putExtra("author_id", item.getAuthor_id());
                holder.portrait.getContext().startActivity(toPortrait);
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        private TextView title;
        private CircleImageView portrait;
        private TextView username;
        private FrameLayout text;
        private TextView likeNum;
        private Button fabLike;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            portrait = itemView.findViewById(R.id.portrait);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.content);
            likeNum = itemView.findViewById(R.id.like_num);
            fabLike = itemView.findViewById(R.id.fab_like);
        }
    }

    public interface OnLikeFabClickListener {
        void onLikeClick(Button btn, TextView textView, String likeNum);
    }
}
