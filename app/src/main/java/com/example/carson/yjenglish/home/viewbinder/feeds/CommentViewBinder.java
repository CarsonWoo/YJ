package com.example.carson.yjenglish.home.viewbinder.feeds;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.home.model.forviewbinder.Comment;
import com.example.carson.yjenglish.utils.ScreenUtils;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/8/20.
 */

public class CommentViewBinder extends ItemViewBinder<Comment, CommentViewBinder.ViewHolder> {

    private OnItemSelectListener mListener;

    public CommentViewBinder(OnItemSelectListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.home_detail_comment, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Comment item) {
        Log.e("BindHolder", "holder position = " + holder.getAdapterPosition());
        holder.itemView.setVisibility(View.VISIBLE);
        if (item.isAuthorReplied()) {
            Log.e("CommentHolder", "author replied");
            holder.content.setVisibility(View.VISIBLE);
            holder.content.setText("作者回复" + item.getUsername() + "：" + item.getContent());
            holder.content.setTextColor(Color.parseColor("#656565"));
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) holder.content.getLayoutParams();
            lp.width = ScreenUtils.dp2px(MyApplication.getContext(), 260);
            lp.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            lp.topToBottom = holder.menuMore.getId();
            lp.startToStart = holder.time.getId();
            holder.content.setLayoutParams(lp);
        } else {
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) holder.content.getLayoutParams();
            lp.width = 0;
            lp.height = 0;
            holder.content.setLayoutParams(lp);
            Log.e("CommentHolder", "author not replied");
        }
        if (item.isHasReply()) {
            Log.e("CommentHolder", "has sub comment");
            holder.replyComment.setVisibility(View.VISIBLE);
            View childView = LayoutInflater.from(holder.replyComment.getContext()).inflate(R.layout.home_detail_comment,
                    null, false);
            initChildView(childView, item);
            holder.replyComment.addView(childView);
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) holder.replyComment.getLayoutParams();
            lp.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            lp.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            lp.topToBottom = holder.menuMore.getId();
            if (item.isAuthorReplied()) {
                lp.setMargins(0, ScreenUtils.dp2px(holder.itemView.getContext(), 30),
                        0, 0);
            }
            holder.replyComment.setLayoutParams(lp);
        } else {
            Log.e("CommentHolder", "has not sub comment");
            holder.replyComment.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
        }
        holder.divider.setVisibility(View.VISIBLE);
        Glide.with(holder.portrait.getContext()).load(item.getPortraitUrl()).thumbnail(0.5f).into(holder.portrait);
        holder.username.setText(item.getUsername());
        holder.time.setText(item.getTime());
        holder.comment.setText(item.getComment());
        if (item.getLikeNum() == 0) {
            holder.likeNum.setText("");
        } else {
            holder.likeNum.setText(String.valueOf(item.getLikeNum()));
        }
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onLikeButtonClick(holder.likeNum, item.getComment_id(), false,
                            item.isLike());
                    item.setLike(!item.isLike());
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onReply(item.getUsername(), holder.getAdapterPosition(),
                            item.getComment_id());
                }
            }
        });
        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initWindow(holder.menuMore);
            }
        });
        holder.itemView.measure(holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());

        holder.portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onUserPortraitClick(item.getUser_id());
                }
            }
        });
    }

    private void initWindow(final View anchorView) {
        View windowView = LayoutInflater.from(anchorView.getContext())
                .inflate(R.layout.layout_common_dialog, null, false);
        ImageView img = windowView.findViewById(R.id.common_img);
        TextView text = windowView.findViewById(R.id.common_text);
        CardView card = windowView.findViewById(R.id.card_view);
        img.setVisibility(View.GONE);
        text.setTextSize(16);
        text.setTextColor(Color.parseColor("#eb000000"));
        text.setText("举报评论");
        card.setRadius(6f);
        final PopupWindow window = new PopupWindow(windowView, ScreenUtils.dp2px(anchorView.getContext(), 180),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setOutsideTouchable(true);
        window.showAsDropDown(anchorView, 0, -window.getHeight(), Gravity.BOTTOM | Gravity.END);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(anchorView.getContext(), "举报成功", Toast.LENGTH_SHORT).show();
                window.dismiss();
            }
        });
    }

    private void initChildView(View childView, final Comment item) {
        ImageView portrait = childView.findViewById(R.id.portrait);
        TextView username = childView.findViewById(R.id.username);
        TextView reply = childView.findViewById(R.id.comment);
        ImageView btnLike = childView.findViewById(R.id.like_btn);
        final TextView likeNum = childView.findViewById(R.id.like_num);
        final ImageView menuMore = childView.findViewById(R.id.menu_more);
        TextView time = childView.findViewById(R.id.time);
        TextView content = childView.findViewById(R.id.fit_content);
        content.setVisibility(View.VISIBLE);
        content.setTextColor(Color.parseColor("#5ee1c9"));
        content.setText("查看更多回复");
        Glide.with(portrait.getContext()).load(item.getReply().getPortraitUrl()).thumbnail(0.5f)
                .into(portrait);
        username.setText(item.getReply().getUsername());
        reply.setText(item.getReply().getReply());
        if (item.getReply().getLikeNum() == 0) {
            likeNum.setText("");
        } else {
            likeNum.setText(String.valueOf(item.getReply().getLikeNum()));
        }
        time.setText(item.getReply().getTime());
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onLoadMoreReply(item.getUser_id());
                }
            }
        });
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onLikeButtonClick(likeNum, item.getComment_id(), true,
                            item.isLike());
                    item.setLike(!item.isLike());
                }
            }
        });
        menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initWindow(menuMore);
            }
        });
        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onUserPortraitClick(item.getReply().getUser_id());
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {

        private CircleImageView portrait;
        private TextView username;
        private TextView comment;
        private ImageView btnLike;
        private TextView likeNum;
        private ImageView menuMore;
        private TextView content;
        private FrameLayout replyComment;
        private TextView time;
        View itemView;
        View divider;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            portrait = itemView.findViewById(R.id.portrait);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
            btnLike = itemView.findViewById(R.id.like_btn);
            likeNum = itemView.findViewById(R.id.like_num);
            menuMore = itemView.findViewById(R.id.menu_more);
            content = itemView.findViewById(R.id.fit_content);
            replyComment = itemView.findViewById(R.id.reply_comment);
            time = itemView.findViewById(R.id.time);
            divider = itemView.findViewById(R.id.divider);
        }
    }

    public interface OnItemSelectListener {
        void onLoadMoreReply(String user_id);
        void onReply(String username, int pos, String comment_id);
        void onLikeButtonClick(TextView tv, String comment_id, boolean isReply, boolean isLike);
        void onUserPortraitClick(String user_id);
    }
}
