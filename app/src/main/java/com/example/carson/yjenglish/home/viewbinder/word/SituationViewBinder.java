package com.example.carson.yjenglish.home.viewbinder.word;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.customviews.RoundRectImageView;
import com.example.carson.yjenglish.home.model.forviewbinder.WordSituation;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/8/13.
 */

public class SituationViewBinder extends ItemViewBinder<WordSituation, SituationViewBinder.ViewHolder> {

    private SituationListener listener;

    public SituationViewBinder(SituationListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.word_detail_situation, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final WordSituation item) {
        holder.sentence.setText(item.getText());
        holder.sentenceTrans.setText(item.getTrans());
        Glide.with(holder.img.getContext())
                .load(item.getImgUrl())
                .placeholder(R.mipmap.word_placeholder)
                .dontAnimate()
                .thumbnail(0.05f)
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSentenceSoundClick(item.getSoundUrl());
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        private RoundRectImageView img;
        private TextView sentence;
        private TextView sentenceTrans;
        private ImageView sound;
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            img = itemView.findViewById(R.id.img);
            sentence = itemView.findViewById(R.id.sentence);
            sentenceTrans = itemView.findViewById(R.id.sentence_trans);
            sound = itemView.findViewById(R.id.play_sound);
        }
    }

    public interface SituationListener {
        void onSentenceSoundClick(String mAACFile);
    }
}
