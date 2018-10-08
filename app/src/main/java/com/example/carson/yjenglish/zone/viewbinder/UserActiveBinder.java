package com.example.carson.yjenglish.zone.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.customviews.RoundRectImageView;
import com.example.carson.yjenglish.zone.model.forviewbinder.UserActive;
import com.example.carson.yjenglish.zone.view.users.ActiveFragment;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/9/10.
 */

public class UserActiveBinder extends ItemViewBinder<UserActive, UserActiveBinder.ViewHolder> {

    private ActiveFragment.OnActiveItemListener mListener;

    public UserActiveBinder(ActiveFragment.OnActiveItemListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.other_user_info_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull UserActive item) {
        Glide.with(holder.portrait.getContext()).load(item.getPortrait_url()).thumbnail(0.5f).into(holder.portrait);
        holder.username.setText(item.getUsername());
        if (item.getType() == 0) {
            holder.activeType.setText("喜欢：");
            holder.text.setVisibility(View.GONE);
        } else if (item.getType() == 1) {
            holder.activeType.setText("评论：");
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(item.getText());
        } else {
            holder.activeType.setText("回复" + item.getOther_username() + " 的评论：");
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(item.getText());
        }
        addChildView(item.getItemType(), holder.mContainer, item);
    }

    private void addChildView(int type, LinearLayout layout, UserActive item) {
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

    private void initDailyView(View view, final UserActive item) {
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

    private void initWordView(View view, UserActive item) {
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

    private void initMusicView(View view, final UserActive item) {
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

    private void initTVView(View view, final UserActive item) {
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

    private void initHomeView(View view, final UserActive item) {
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

        //mHomeView.setClickListener
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
        private CircleImageView portrait;
        private TextView username;
        private TextView activeType;
        private TextView text;
        private LinearLayout mContainer;
        public ViewHolder(View itemView) {
            super(itemView);
            portrait = itemView.findViewById(R.id.user_portrait);
            username = itemView.findViewById(R.id.username);
            activeType = itemView.findViewById(R.id.active_type);
            text = itemView.findViewById(R.id.text);
            mContainer = itemView.findViewById(R.id.layout_active);
        }
    }
}
