package com.example.carson.yjenglish.home.viewbinder.word;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.home.model.forviewbinder.Text;
import com.example.carson.yjenglish.utils.ScreenUtils;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/8/13.
 */

public class TextViewBinder extends ItemViewBinder<Text, TextViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.word_detail_text, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Text item) {
        holder.tv.setTextColor(Color.parseColor("#eb000000"));
        holder.tv.setTextSize(16);
        holder.tv.setText(item.getText());
        holder.mRoot.setBackgroundColor(Color.WHITE);
        holder.mRoot.setPadding(0, ScreenUtils.dp2px(holder.mRoot.getContext(), 4),
                0, ScreenUtils.dp2px(holder.mRoot.getContext(), 4));
    }

    static class ViewHolder extends BaseViewHolder {
        private TextView tv;
        private ConstraintLayout mRoot;
        public ViewHolder(View itemView) {
            super(itemView);
            mRoot = (ConstraintLayout) itemView;
            tv = itemView.findViewById(R.id.text);
        }
    }
}
