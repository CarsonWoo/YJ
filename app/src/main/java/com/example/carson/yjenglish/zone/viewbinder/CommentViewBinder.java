package com.example.carson.yjenglish.zone.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.customviews.RoundRectImageView;
import com.example.carson.yjenglish.zone.model.CommentModel;

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
        if (item.getReply_comment() != null && !item.getReply_comment().isEmpty()) {
            holder.reply.setVisibility(View.VISIBLE);
            String s = "<font color = \"#5ee1c9\">@" + item.getReply_comment() + ":</font>";
            holder.reply.setText(Html.fromHtml(s));
        } else {
            holder.reply.setVisibility(View.GONE);
        }
        addChildView(item.getItemType(), holder.mContainer, item);
    }

    private void addChildView(int type, LinearLayout layout, CommentModel item) {
        View childView;
        LayoutInflater inflater = LayoutInflater.from(layout.getContext());
        switch (type) {
            case 0:
                childView = inflater.inflate(R.layout.zone_like_home_feeds, layout, false);
                initHomeView(childView, item);
                layout.addView(childView);
                break;
            case 1:
                childView = inflater.inflate(R.layout.zone_like_tv_feeds, layout, false);
                initTVView(childView, item);
                layout.addView(childView);
                break;
            case 2:
                childView = inflater.inflate(R.layout.zone_like_music, layout, false);
                initMusicView(childView, item);
                layout.addView(childView);
                break;
            case 3:
                childView = inflater.inflate(R.layout.zone_like_word, layout, false);
                initWordView(childView, item);
                layout.addView(childView);
                break;
            case 4:
                childView = inflater.inflate(R.layout.zone_like_daily_card, layout, false);
                initDailyView(childView, item);
                layout.addView(childView);
                break;
            default:
                break;
        }
    }

    private void initDailyView(View view, final CommentModel item) {
        RoundRectImageView img = view.findViewById(R.id.cover_img);
        TextView time = view.findViewById(R.id.like_time);

        Glide.with(view.getContext()).load(item.getDailyCardItem().getImgUrl()).thumbnail(0.5f).into(img);
        time.setText(item.getDailyCardItem().getTime());
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(item.getItemType());
                }
            }
        });
    }

    private void initWordView(View view, CommentModel item) {
        CardView mWordView = view.findViewById(R.id.my_card_view);
        RoundRectImageView img = view.findViewById(R.id.cover_img);
        TextView word = view.findViewById(R.id.word_tag);
        TextView soundMark = view.findViewById(R.id.soundmark);
        TextView time = view.findViewById(R.id.like_time);

        Glide.with(view.getContext()).load(item.getWordItem().getImgUrl()).thumbnail(0.5f).into(img);
        word.setText(item.getWordItem().getWord());
        soundMark.setText(item.getWordItem().getSoundMark());
        time.setText(item.getWordItem().getTime());
    }

    private void initMusicView(View view, final CommentModel item) {
        RoundRectImageView mMusicView = view.findViewById(R.id.my_card_view);
        TextView word = view.findViewById(R.id.word_tag);
        TextView meaning = view.findViewById(R.id.meaning);
        TextView time = view.findViewById(R.id.like_time);

        word.setText(item.getMusicItem().getWord());
        meaning.setText(item.getMusicItem().getMeaning());
        time.setText(item.getMusicItem().getTime());
        mMusicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(item.getItemType());
                }
            }
        });
    }

    private void initTVView(View view, final CommentModel item) {
        CardView mTVView = view.findViewById(R.id.my_card_view);
        RoundRectImageView img = view.findViewById(R.id.cover_img);
        TextView viewNum = view.findViewById(R.id.view_num);
        TextView word = view.findViewById(R.id.word_tag);
        TextView soundMark = view.findViewById(R.id.soundmark);
        TextView time = view.findViewById(R.id.like_time);

        Glide.with(view.getContext()).load(item.getTvFeeds().getImgUrl()).thumbnail(0.5f).into(img);
        viewNum.setText(item.getTvFeeds().getViewNum());
        word.setText(item.getTvFeeds().getTag());
        soundMark.setText(item.getTvFeeds().getSoundMark());
        time.setText(item.getTvFeeds().getTime());
        mTVView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(item.getItemType());
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

        title.setText(item.getHomeFeeds().getTitle());
        Glide.with(view.getContext()).load(item.getHomeFeeds().getPortraitUrl()).thumbnail(0.5f).into(authorPortrait);
        Glide.with(view.getContext()).load(item.getHomeFeeds().getImgUrl()).thumbnail(0.5f).into(img);
        authorName.setText(item.getHomeFeeds().getAuthorName());
        time.setText(item.getHomeFeeds().getTime());
        mHomeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(item.getItemType());
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        private TextView comment;
        private TextView reply;
        private LinearLayout mContainer;
        public ViewHolder(View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment);
            reply = itemView.findViewById(R.id.reply_comment);
            mContainer = itemView.findViewById(R.id.add_view_container);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int itemType);
    }
}
