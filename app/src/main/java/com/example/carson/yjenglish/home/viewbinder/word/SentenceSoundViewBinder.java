package com.example.carson.yjenglish.home.viewbinder.word;

import android.support.annotation.NonNull;
import android.text.Html;
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

public class SentenceSoundViewBinder extends ItemViewBinder<Sentence, SentenceSoundViewBinder.ViewHolder> {

    private OnSentenceSoundListener listener;

    public SentenceSoundViewBinder(OnSentenceSoundListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.word_detail_sentence, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Sentence item) {
        holder.sentence.setText(Html.fromHtml(item.getSentence()));
        holder.trans.setTextColor(holder.trans.getResources().getColor(R.color.colorTextWord));
        holder.trans.setText(item.getSentenceTrans());
        holder.origin.setText(item.getSentenceOrigin());
        holder.sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onVideoSentenceSoundClick(item.getSentenceSound());
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        private TextView sentence;
        private TextView trans;
        private ImageView sound;
        private TextView origin;
        public ViewHolder(View itemView) {
            super(itemView);
            sentence = itemView.findViewById(R.id.sentence);
            trans = itemView.findViewById(R.id.sentence_trans);
            sound = itemView.findViewById(R.id.play_sound);
            origin = itemView.findViewById(R.id.sentence_origin);
        }
    }

    public interface OnSentenceSoundListener {
        void onVideoSentenceSoundClick(String url);
    }
}
