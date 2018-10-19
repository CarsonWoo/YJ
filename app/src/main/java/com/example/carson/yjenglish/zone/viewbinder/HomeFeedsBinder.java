package com.example.carson.yjenglish.zone.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.customviews.RoundRectImageView;
import com.example.carson.yjenglish.zone.model.forviewbinder.HomeFeeds;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/9/6.
 */

public class HomeFeedsBinder extends ItemViewBinder<HomeFeeds, HomeFeedsBinder.ViewHolder> {

    private OnHomeFeedItemClickListener listener;

    public HomeFeedsBinder(OnHomeFeedItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.zone_like_home_feeds, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final HomeFeeds item) {
        Glide.with(holder.portrait.getContext()).load(item.getPortraitUrl()).crossFade().thumbnail(0.3f).into(holder.portrait);
        Glide.with(holder.coverImg.getContext()).load(item.getImgUrl()).thumbnail(0.3f).into(holder.coverImg);
        holder.title.setText(item.getTitle());
        holder.time.setText(item.getTime());
        holder.authorName.setText(item.getAuthorName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onHomeFeedClick(item.getId());
                }
            }
        });
        holder.delete.setVisibility(View.VISIBLE);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onHomeFeedDelete(item.getId(), holder.getAdapterPosition());
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        private RoundRectImageView coverImg;
        private TextView title;
        private TextView authorName;
        private CardView mCardView;
        private CircleImageView portrait;
        private TextView time;
        private ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            coverImg = itemView.findViewById(R.id.cover_img);
            title = itemView.findViewById(R.id.title);
            authorName = itemView.findViewById(R.id.author_name);
            mCardView = itemView.findViewById(R.id.my_card_view);
            portrait = itemView.findViewById(R.id.portrait);
            time = itemView.findViewById(R.id.like_time);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface OnHomeFeedItemClickListener {
        void onHomeFeedDelete(String id, int position);
        void onHomeFeedClick(String id);
    }
}
