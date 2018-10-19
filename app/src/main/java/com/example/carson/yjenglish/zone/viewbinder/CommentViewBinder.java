package com.example.carson.yjenglish.zone.viewbinder;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.customviews.RoundRectImageView;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.zone.model.CommentModel;
import com.example.carson.yjenglish.zone.model.MyCommentInfo;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/9/26.
 */

public class CommentViewBinder extends ItemViewBinder<CommentModel, CommentViewBinder.ViewHolder> {

    private OnItemClickListener mListener;

    public CommentViewBinder(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.zone_comment_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull CommentModel item) {
        holder.comment.setText(item.getComment());
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) holder.mContainer.getLayoutParams();
        if (item.getReply_comment() != null && !item.getReply_comment().isEmpty()) {
            String s = "<font color = \"#5ee1c9\">@" + item.getReply_comment() + ":</font>";
            holder.reply.setText(Html.fromHtml(s));
            lp.topToBottom = holder.reply.getId();
        } else {
            lp.topToBottom = holder.comment.getId();
        }
        lp.setMargins(0, 10, 0, 0);
        lp.bottomToBottom = holder.itemView.getId();
        holder.mContainer.setLayoutParams(lp);
        addChildView(item.getItemType(), holder.mContainer, item);
    }

    private void addChildView(int type, LinearLayout layout, CommentModel item) {
        View childView;
        LayoutInflater inflater = LayoutInflater.from(layout.getContext());
        switch (type) {
            case 0:
                childView = inflater.inflate(R.layout.zone_like_home_feeds, layout, false);
                initHomeView(childView, item);
                layout.removeAllViews();
                layout.addView(childView);
                break;
            case 1:
                childView = inflater.inflate(R.layout.zone_like_tv_feeds, layout, false);
                initTVView(childView, item);
                layout.removeAllViews();
                layout.addView(childView);
                break;
            default:
                break;
        }
    }

    private void initTVView(View view, final CommentModel item) {
        CardView mTVView = view.findViewById(R.id.my_card_view);
        RoundRectImageView img = view.findViewById(R.id.cover_img);
        TextView viewNum = view.findViewById(R.id.view_num);
        TextView word = view.findViewById(R.id.word_tag);
        TextView soundMark = view.findViewById(R.id.soundmark);
        TextView time = view.findViewById(R.id.like_time);
        final ImageView delete = view.findViewById(R.id.delete);

//        delete.setVisibility(View.VISIBLE);
        Glide.with(view.getContext()).load(item.getTvFeeds().getImgUrl()).thumbnail(0.5f).into(img);
        viewNum.setText(item.getTvFeeds().getViewNum());
        word.setText(item.getTvFeeds().getTag());
        soundMark.setText(item.getTvFeeds().getSoundMark());
        time.setText(item.getTvFeeds().getTime());
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogUtils utils = DialogUtils.getInstance(delete.getContext());
//                Dialog dialog = utils.newTipsDialog("确定删除吗", View.TEXT_ALIGNMENT_CENTER);
//                dialog.show();
//
//                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//                lp.height = ScreenUtils.dp2px(MyApplication.getContext(), 240);
//                lp.width = ScreenUtils.dp2px(MyApplication.getContext(), 260);
//                lp.y = ScreenUtils.getScreenHeight(MyApplication.getContext()) / 2 -
//                        ScreenUtils.dp2px(MyApplication.getContext(), 260);
//                dialog.getWindow().setAttributes(lp);
//
//                utils.setTipsListener(new DialogUtils.OnTipsListener() {
//                    @Override
//                    public void onConfirm() {
//                        Toast.makeText(delete.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
        mTVView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(item.getItemType(), item.getTvFeeds().getId());
                }
            }
        });
    }

    private void initHomeView(View view, final CommentModel item) {
        CardView mHomeView = view.findViewById(R.id.my_card_view);
        TextView title = view.findViewById(R.id.title);
        CircleImageView authorPortrait = view.findViewById(R.id.portrait);
        TextView authorName = view.findViewById(R.id.author_name);
        TextView time = view.findViewById(R.id.like_time);
        RoundRectImageView img = view.findViewById(R.id.cover_img);
        final ImageView delete = view.findViewById(R.id.delete);

//        delete.setVisibility(View.VISIBLE);
        title.setText(item.getHomeFeeds().getTitle());
        Glide.with(view.getContext()).load(item.getHomeFeeds().getPortraitUrl()).thumbnail(0.5f).into(authorPortrait);
        Glide.with(view.getContext()).load(item.getHomeFeeds().getImgUrl()).thumbnail(0.5f).into(img);
        authorName.setText(item.getHomeFeeds().getAuthorName());
        time.setText(item.getHomeFeeds().getTime());
        mHomeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(item.getItemType(), item.getHomeFeeds().getId());
                }
            }
        });
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogUtils utils = DialogUtils.getInstance(delete.getContext());
//                Dialog dialog = utils.newTipsDialog("确定删除吗", View.TEXT_ALIGNMENT_CENTER);
//                dialog.show();
//
//                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//                lp.height = ScreenUtils.dp2px(MyApplication.getContext(), 240);
//                lp.width = ScreenUtils.dp2px(MyApplication.getContext(), 260);
//                lp.y = ScreenUtils.getScreenHeight(MyApplication.getContext()) / 2 -
//                        ScreenUtils.dp2px(MyApplication.getContext(), 260);
//                dialog.getWindow().setAttributes(lp);
//
//                utils.setTipsListener(new DialogUtils.OnTipsListener() {
//                    @Override
//                    public void onConfirm() {
//                        Toast.makeText(delete.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
    }

    static class ViewHolder extends BaseViewHolder {
        private View itemView;
        private TextView comment;
        private TextView reply;
        private LinearLayout mContainer;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView.findViewById(R.id.zone_comment_layout);
            comment = itemView.findViewById(R.id.comment);
            reply = itemView.findViewById(R.id.reply_comment);
            mContainer = itemView.findViewById(R.id.add_view_container);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int itemType, String id);
    }
}
