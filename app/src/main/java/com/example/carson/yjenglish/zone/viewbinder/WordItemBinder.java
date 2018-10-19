package com.example.carson.yjenglish.zone.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.customviews.RoundRectImageView;
import com.example.carson.yjenglish.zone.model.forviewbinder.WordItem;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/9/6.
 */

public class WordItemBinder extends ItemViewBinder<WordItem, WordItemBinder.ViewHolder> {

    private OnWordItemClickListener listener;

    public WordItemBinder(OnWordItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.zone_like_word, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final WordItem item) {
        holder.time.setText(item.getTime());
        holder.word.setText(item.getWord());
        holder.soundMark.setText(item.getSoundMark());
        Glide.with(holder.coverImg.getContext())
                .load(item.getImgUrl())
                .placeholder(R.mipmap.word_placeholder)
                .thumbnail(0.5f)
                .into(holder.coverImg);
        holder.delete.setVisibility(View.VISIBLE);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onWordClick(item.getId());
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onWordDelete(item.getId(), holder.getAdapterPosition());
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {

        private RoundRectImageView coverImg;
        private TextView word;
        private TextView soundMark;
        private TextView time;
        private CardView mCardView;
        private ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            coverImg = itemView.findViewById(R.id.cover_img);
            word = itemView.findViewById(R.id.word_tag);
            soundMark = itemView.findViewById(R.id.soundmark);
            time = itemView.findViewById(R.id.like_time);
            mCardView = itemView.findViewById(R.id.my_card_view);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface OnWordItemClickListener {
        void onWordClick(String id);
        void onWordDelete(String id, int pos);
    }
}
