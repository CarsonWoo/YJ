package com.example.carson.yjenglish.home.viewbinder.feeds;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/8/20.
 */

public class EmptyViewBinder extends ItemViewBinder<EmptyValue, EmptyViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.layout_error, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull EmptyValue item) {
        holder.text.setText(item.getText());
        holder.img.setImageResource(item.getDrawableRes());
    }

    static class ViewHolder extends BaseViewHolder {
        ImageView img;
        TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.error_img);
            text = itemView.findViewById(R.id.error_text);
        }
    }
}
