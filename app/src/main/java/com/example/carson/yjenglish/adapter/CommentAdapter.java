package com.example.carson.yjenglish.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.example.carson.yjenglish.home.model.forviewbinder.Comment;
import com.example.carson.yjenglish.msg.view.ReportActivity;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.zone.view.users.UserInfoAty;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 84594 on 2018/8/21.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context ctx;
    private List<Comment> mList;

    private OnSelectItemListener mListener;
    public CommentAdapter(Context ctx, List<Comment> comments) {
        this.ctx = ctx;
        this.mList = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(ctx).inflate(R.layout.home_detail_comment, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.item = mList.get(position);
        if (holder.item.isAuthorReplied()) {
            holder.content.setVisibility(View.VISIBLE);
            holder.content.setText("作者回复：" + holder.item.getContent());
            holder.content.setTextColor(Color.parseColor("#656565"));
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) holder.content.getLayoutParams();
            lp.width = ScreenUtils.dp2px(MyApplication.getContext(), 260);
            lp.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            holder.content.setLayoutParams(lp);
        } else {
            holder.content.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
        }
        if (holder.item.isHasReply()) {
            holder.replyComment.setVisibility(View.VISIBLE);
            View childView = LayoutInflater.from(holder.replyComment.getContext()).inflate(R.layout.home_detail_comment,
                    null, false);
//            initChildView(childView, holder.item);
            holder.replyComment.addView(childView);
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) holder.replyComment.getLayoutParams();
            lp.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            lp.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            holder.replyComment.setLayoutParams(lp);
        } else {
            holder.replyComment.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
        }
        holder.divider.setVisibility(View.VISIBLE);
        Glide.with(ctx).load(holder.item.getPortraitUrl()).thumbnail(0.8f).into(holder.portrait);
        holder.username.setText(holder.item.getUsername());
        holder.comment.setText(holder.item.getComment());
        holder.time.setText(holder.item.getTime());
        if (holder.item.getLikeNum() == 0) {
            holder.likeNum.setText("");
        } else {
            holder.likeNum.setText("" + holder.item.getLikeNum());
        }
        Log.e("CommentAdapter", "isLike = " + holder.item.isLike());
        holder.btnLike.setSelected(holder.item.isLike());
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onLikeButtonClick(holder.item.getComment_id(), holder.likeNum, holder.item.isLike());
                    holder.item.setLike(!holder.item.isLike());
                    holder.btnLike.setSelected(holder.item.isLike());
                    ObjectAnimator animatorX = ObjectAnimator.ofFloat(holder.btnLike,
                            "scaleX", 1.3f, 1.0f);
                    ObjectAnimator animatorY = ObjectAnimator.ofFloat(holder.btnLike,
                            "scaleY", 1.3f, 1.0f);
                    AnimatorSet set = new AnimatorSet();
                    set.play(animatorX).with(animatorY);
                    set.setDuration(400).start();
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onReply(holder.item.getUsername(), holder.getAdapterPosition());
                }
            }
        });
        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initWindow(holder.menuMore, holder.item);
            }
        });
        holder.portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.portrait.getContext(), UserInfoAty.class);
                intent.putExtra("user_id", holder.item.getUser_id());
                holder.portrait.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setListener(OnSelectItemListener mListener) {
        this.mListener = mListener;
    }

    private void initChildView(View childView, final Comment item) {
        ImageView portrait = childView.findViewById(R.id.portrait);
        TextView username = childView.findViewById(R.id.username);
        TextView reply = childView.findViewById(R.id.comment);
        ImageView btnLike = childView.findViewById(R.id.like_btn);
        TextView likeNum = childView.findViewById(R.id.like_num);
        final ImageView menuMore = childView.findViewById(R.id.menu_more);
        TextView time = childView.findViewById(R.id.time);
        TextView content = childView.findViewById(R.id.fit_content);
        content.setVisibility(View.VISIBLE);
        content.setTextColor(Color.parseColor("#5ee1c9"));
        content.setText("查看更多回复");
        Glide.with(portrait.getContext()).load(item.getReply().getPortraitUrl()).thumbnail(0.8f)
                .into(portrait);
        username.setText(item.getReply().getUsername());
        reply.setText(item.getReply().getReply());
        if (item.getReply().getLikeNum() == 0) {
            likeNum.setText("");
        } else {
            likeNum.setText(item.getReply().getLikeNum() + "");
        }
        time.setText(item.getReply().getTime());
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onLoadMoreReply();
                }
            }
        });
        menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initWindow(menuMore, item);
            }
        });

    }

    private void initWindow(final View anchorView, final Comment item) {
        View windowView = LayoutInflater.from(anchorView.getContext())
                .inflate(R.layout.layout_common_dialog, null, false);
        ImageView img = windowView.findViewById(R.id.common_img);
        TextView text = windowView.findViewById(R.id.common_text);
        CardView card = windowView.findViewById(R.id.card_view);
        img.setVisibility(View.GONE);
        text.setTextSize(13);
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
                Intent toReport = new Intent(anchorView.getContext(), ReportActivity.class);
                toReport.putExtra("comment_id", item.getComment_id());
                anchorView.getContext().startActivity(toReport);
                window.dismiss();
            }
        });
    }

    public class ViewHolder extends BaseViewHolder {

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
        Comment item;

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

    public interface OnSelectItemListener {
        void onLoadMoreReply();
        void onReply(String username, int pos);
        void onLikeButtonClick(String comment_id, TextView textView, boolean is_like);
    }
}
