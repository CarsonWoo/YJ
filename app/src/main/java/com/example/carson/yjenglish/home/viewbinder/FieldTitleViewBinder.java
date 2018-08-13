package com.example.carson.yjenglish.home.viewbinder;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/8/13.
 */

public class FieldTitleViewBinder extends ItemViewBinder<EmptyValue, FieldTitleViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.word_detail_text, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull EmptyValue item) {
        holder.text.setText(item.getText());
        holder.mRoot.setBackgroundResource(R.drawable.bg_alpha);
    }

    static class ViewHolder extends BaseViewHolder {
        private TextView text;
        private ConstraintLayout mRoot;

        public ViewHolder(View itemView) {
            super(itemView);
            mRoot = (ConstraintLayout) itemView;
            text = itemView.findViewById(R.id.text);
        }
    }
}
