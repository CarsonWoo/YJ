package com.example.carson.yjenglish.zone.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.customviews.CannotDragSeekBar;
import com.example.carson.yjenglish.zone.model.forviewbinder.OtherUsersPlan;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/9/10.
 */

public class OtherUsersPlanBinder extends ItemViewBinder<OtherUsersPlan, OtherUsersPlanBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.plan_item_min, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull OtherUsersPlan item) {
        holder.learnTag.setText(item.getTag());
        holder.wordCount.setText(item.getWord_count());
        holder.mSeekBar.setProgress(item.getProgress());
    }

    static class ViewHolder extends BaseViewHolder {
        private CardView mCardView;
        private TextView learnTag;
        private TextView wordCount;
        private CannotDragSeekBar mSeekBar;
        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.item_card);
            learnTag = itemView.findViewById(R.id.learn_tag);
            wordCount = itemView.findViewById(R.id.word_count);
            mSeekBar = itemView.findViewById(R.id.learning_seek_bar);
        }
    }
}
