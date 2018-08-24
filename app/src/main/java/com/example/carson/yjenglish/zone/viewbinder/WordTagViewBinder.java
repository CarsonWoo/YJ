package com.example.carson.yjenglish.zone.viewbinder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.zone.model.forviewbinder.WordTag;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/8/17.
 */

public class WordTagViewBinder extends ItemViewBinder<WordTag, WordTagViewBinder.ViewHolder> {

    private OnTagClickListener onTagClickListener;

    public WordTagViewBinder(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.layout_word_tag, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final WordTag item) {
        holder.wordTag.setText(item.getWordTag());
        holder.wordCount.setText(item.getWordCount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTagClickListener != null) {
                    onTagClickListener.onTagClick(item.getWordTag());
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        private TextView wordTag;
        private TextView wordCount;
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            wordCount = itemView.findViewById(R.id.word_count);
            wordTag = itemView.findViewById(R.id.word_tag);
        }
    }

    public interface OnTagClickListener {
        void onTagClick(String tag);
    }
}
