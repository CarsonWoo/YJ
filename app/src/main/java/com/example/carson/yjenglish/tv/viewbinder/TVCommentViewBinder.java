package com.example.carson.yjenglish.tv.viewbinder;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.msg.view.ReportActivity;
import com.example.carson.yjenglish.tv.model.forviewbinder.TVComment;
import com.example.carson.yjenglish.utils.ScreenUtils;

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
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final TVComment item) {
        holder.username.setText(item.getUsername());
        holder.time.setText(item.getTime());
        holder.comment.setText(item.getComment());
        if (item.getLikes().equals("0") || item.getLikes().isEmpty()) {
            item.setLikes("");
        }
        holder.likeNum.setText(item.getLikes());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    item.setIs_like(item.getIs_like().equals("1") ? "0" : "1");
                    mListener.onLikeClick(item.getId(), holder.likeNum, item.getIs_like());
                    holder.likeButton.setSelected(item.getIs_like().equals("1"));
                    ObjectAnimator animatorX = ObjectAnimator.ofFloat(holder.likeButton,
                            "scaleX", 1.3f, 1.0f);
                    ObjectAnimator animatorY = ObjectAnimator.ofFloat(holder.likeButton,
                            "scaleY", 1.3f, 1.0f);
                    AnimatorSet set = new AnimatorSet();
                    set.play(animatorX).with(animatorY);
                    set.setDuration(400).start();
                }
            }
        });
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initWindow(holder.more, item);
            }
        });
        Glide.with(holder.itemView.getContext()).load(item.getPortrait()).thumbnail(0.5f)
                .crossFade().into(holder.portrait);
    }

    private void initWindow(final View anchorView, final TVComment item) {
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
//                Toast.makeText(anchorView.getContext(), "举报成功", Toast.LENGTH_SHORT).show();
                Intent toReport = new Intent(anchorView.getContext(), ReportActivity.class);
                toReport.putExtra("comment_id", item.getId());
                anchorView.getContext().startActivity(toReport);
                window.dismiss();
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        private CircleImageView portrait;
        private TextView username;
        private TextView time;
        private TextView comment;
        private TextView likeNum;
        private ImageView likeButton;
        private ImageView more;
        private ConstraintLayout container;
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
            container = itemView.findViewById(R.id.like_container);
        }
    }

    public interface OnCommentItemClickListener {
        void onLikeClick(String id, TextView textView, String is_like);
    }
}
