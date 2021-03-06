package com.example.carson.yjenglish.home.viewbinder.word;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.home.model.forviewbinder.Header;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/8/13.
 */

public class HeaderViewBinder extends ItemViewBinder<Header, HeaderViewBinder.ViewHolder> {

    private HeaderSoundListener listener;

    public HeaderViewBinder(HeaderSoundListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.word_detail_header, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Header item) {
        holder.word.setText(item.getWord());
        holder.basicTrans.setText(item.getBasicTrans());
        holder.soundMark.setText(item.getSoundMark());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPronunciationClick();
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        private TextView word;
        private TextView basicTrans;
        private TextView soundMark;
        private ImageView sound;
        View itemView;
        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            word = itemView.findViewById(R.id.word);
            basicTrans = itemView.findViewById(R.id.basic_trans);
            soundMark = itemView.findViewById(R.id.soundmark);
            sound = itemView.findViewById(R.id.play_sound);
        }
    }

    public interface HeaderSoundListener {
        void onPronunciationClick();
    }

}
