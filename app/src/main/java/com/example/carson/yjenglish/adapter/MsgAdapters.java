package com.example.carson.yjenglish.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.msg.model.CommentMsg;
import com.example.carson.yjenglish.msg.model.LikeMsg;
import com.example.carson.yjenglish.msg.model.NoticeMsg;
import com.example.carson.yjenglish.zone.view.users.UserInfoAty;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 84594 on 2018/8/6.
 */

public class MsgAdapters {

    public static class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

        private List<CommentMsg> mComments;
        private Context ctx;

        public CommentAdapter(){}
        public CommentAdapter(Context ctx, List<CommentMsg> mComments) {
            this.ctx = ctx;
            this.mComments = mComments;
        }

        @NonNull
        @Override
        public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CommentHolder(LayoutInflater.from(ctx).inflate(R.layout.msg_comment_list, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
            holder.item = mComments.get(position);
            Glide.with(ctx).load(R.mipmap.ic_launcher_round).asBitmap().into(holder.portrait);
            holder.username.setText(holder.item.getUsername());
            holder.commentType.setText(holder.item.getType());
            holder.content.setText(holder.item.getContent());
            holder.date.setText(holder.item.getDate());
            holder.comeFrom.setText(holder.item.getOrigin());
            Glide.with(ctx).load(holder.item.getImgUrl()).into(holder.commentPic);

            holder.menuMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("MsgAdapter", "menuMore Click");
                }
            });
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        public class CommentHolder extends BaseViewHolder {

            CircleImageView portrait;
            TextView username;
            TextView commentType;
            TextView content;
            TextView date;
            TextView comeFrom;
            ImageView menuMore;
            ImageView commentPic;
            CommentMsg item;

            public CommentHolder(View itemView) {
                super(itemView);
                portrait = itemView.findViewById(R.id.portrait);
                username = itemView.findViewById(R.id.username);
                commentType = itemView.findViewById(R.id.comment_type);
                content = itemView.findViewById(R.id.content);
                date = itemView.findViewById(R.id.date);
                comeFrom = itemView.findViewById(R.id.come_from);
                menuMore = itemView.findViewById(R.id.menu_more);
                commentPic = itemView.findViewById(R.id.comment_pic);
            }
        }
    }

    public static class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.LikeHolder> {

        private List<LikeMsg> mLikes;
        private Context ctx;

        public LikeAdapter(){}
        public LikeAdapter(Context ctx, List<LikeMsg> mLikes) {
            this.ctx = ctx;
            this.mLikes = mLikes;
        }

        @NonNull
        @Override
        public LikeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new LikeHolder(LayoutInflater.from(ctx).inflate(R.layout.msg_like_list, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final LikeHolder holder, int position) {
            holder.item = mLikes.get(position);
            holder.username.setText(holder.item.getUsername());
            holder.content.setText(holder.item.getContent());
            holder.date.setText(holder.item.getSet_time());
            Glide.with(ctx).load(holder.item.getPortrait()).asBitmap().into(holder.portrait);

            holder.portrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toInfo = new Intent(holder.portrait.getContext(), UserInfoAty.class);
                    toInfo.putExtra("user_id", holder.item.getUser_id());
                    holder.portrait.getContext().startActivity(toInfo);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mLikes.size();
        }

        public class LikeHolder extends BaseViewHolder {

            CircleImageView portrait;
            TextView username;
            TextView content;
            TextView date;
            LikeMsg item;

            public LikeHolder(View itemView) {
                super(itemView);
                portrait = itemView.findViewById(R.id.portrait);
                username = itemView.findViewById(R.id.username);
                content = itemView.findViewById(R.id.content);
                date = itemView.findViewById(R.id.date);
            }
        }
    }

    public static class SysAdapter extends RecyclerView.Adapter<SysAdapter.SysHolder> {

        private Context ctx;
        private List<NoticeMsg> mNotices;

        public SysAdapter(){}
        public SysAdapter(Context ctx, List<NoticeMsg> mNotices) {
            this.ctx = ctx;
            this.mNotices = mNotices;
        }

        @NonNull
        @Override
        public SysHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SysHolder(LayoutInflater.from(ctx).inflate(R.layout.msg_notice_list, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull SysHolder holder, int position) {
            holder.item = mNotices.get(position);
            holder.username.setText(holder.item.getUsername());
            holder.content.setText(holder.item.getContent());
            holder.date.setText(holder.item.getDate());
            Glide.with(ctx).load(R.mipmap.ic_launcher_round).asBitmap().into(holder.portrait);
        }

        @Override
        public int getItemCount() {
            return mNotices.size();
        }

        public class SysHolder extends BaseViewHolder {
            TextView username;
            CircleImageView portrait;
            TextView content;
            TextView date;
            NoticeMsg item;
            public SysHolder(View itemView) {
                super(itemView);
                username = itemView.findViewById(R.id.username);
                portrait = itemView.findViewById(R.id.portrait);
                content = itemView.findViewById(R.id.content);
                date = itemView.findViewById(R.id.date);
            }
        }
    }
}
