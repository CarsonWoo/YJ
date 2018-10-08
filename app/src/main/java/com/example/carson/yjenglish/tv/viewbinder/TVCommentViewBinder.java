package com.example.carson.yjenglish.tv.viewbinder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.tv.model.forviewbinder.TVComment;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/10/2.
 */

public class TVCommentViewBinder extends ItemViewBinder<TVComment, TVCommentViewBinder.ViewHolder> {

    private OnCommentItemClickListener mListener;

    public TVCommentViewBinder(OnCommentItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.tv_comment_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final TVComment item) {
        holder.username.setText(item.getUsername());
        holder.time.setText(item.getTime());
        holder.comment.setText(item.getComment());
        holder.likeNum.setText(item.getLikes());
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onLikeClick(item.getId());
                }
            }
        });
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMoreClick(item.getId());
                }
            }
        });
        Glide.with(holder.itemView.getContext()).load(item.getPortrait()).thumbnail(0.5f)
                .crossFade().into(holder.portrait);
    }

    static class ViewHolder extends BaseViewHolder {
        private CircleImageView portrait;
        private TextView username;
        private TextView time;
        private TextView comment;
        private TextView likeNum;
        private ImageView likeButton;
        private ImageView more;
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            portrait = itemView.findViewById(R.id.portrait);
            username = itemView.findViewById(R.id.username);
            time = itemView.findViewById(R.id.time);
            comment = itemView.findViewById(R.id.comment);
            likeNum = itemView.findViewById(R.id.like_num);
            likeButton = itemView.findViewById(R.id.like_btn);
            more = itemView.findViewById(R.id.menu_more);
        }
    }

    public interface OnCommentItemClickListener {
        void onLikeClick(String id);
        void onMoreClick(String id);
    }
}
