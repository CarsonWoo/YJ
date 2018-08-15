package com.example.carson.yjenglish.home.viewbinder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.home.model.forviewbinder.Sentence;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/8/14.
 */

public class SentenceViewBinder extends ItemViewBinder<Sentence, SentenceViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.word_detail_sentence, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Sentence item) {
        holder.sentence.setText(item.getSentence());
        holder.trans.setText(item.getSentenceTrans());
        holder.sound.setVisibility(View.GONE);
    }

    static class ViewHolder extends BaseViewHolder {
        private TextView sentence;
        private TextView trans;
        private ImageView sound;
        public ViewHolder(View itemView) {
            super(itemView);
            sentence = itemView.findViewById(R.id.sentence);
            trans = itemView.findViewById(R.id.sentence_trans);
            sound = itemView.findViewById(R.id.play_sound);
        }
    }
}
