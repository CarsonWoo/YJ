package com.example.carson.yjenglish.zone.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.customviews.RoundRectImageView;
import com.example.carson.yjenglish.zone.model.forviewbinder.MusicItem;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/9/6.
 */

public class MusicItemBinder extends ItemViewBinder<MusicItem, MusicItemBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.zone_like_music, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MusicItem item) {
        holder.word.setText(item.getWord());
        holder.time.setText(item.getTime());
        holder.meaning.setText(item.getMeaning());
        holder.delete.setVisibility(View.VISIBLE);
    }

    static class ViewHolder extends BaseViewHolder {
        private TextView word;
        private TextView meaning;
        private TextView time;
        private ImageView delete;
        private RoundRectImageView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.word_tag);
            meaning = itemView.findViewById(R.id.meaning);
            time = itemView.findViewById(R.id.like_time);
            delete = itemView.findViewById(R.id.delete);
            mCardView = itemView.findViewById(R.id.my_card_view);
        }
    }
}
