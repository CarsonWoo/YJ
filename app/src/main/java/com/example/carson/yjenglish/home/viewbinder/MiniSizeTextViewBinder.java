package com.example.carson.yjenglish.home.viewbinder;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.home.model.forviewbinder.Change;
import com.example.carson.yjenglish.home.model.forviewbinder.Text;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/8/14.
 */

public class MiniSizeTextViewBinder extends ItemViewBinder<Change, MiniSizeTextViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.word_detail_change, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Change item) {
        holder.text.setText(item.getText());
        holder.former.setText(item.getFormer());
    }

    static class ViewHolder extends BaseViewHolder {
        private TextView former;
        private TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            former = itemView.findViewById(R.id.former);
        }
    }
}
