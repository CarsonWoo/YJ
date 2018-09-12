package com.example.carson.yjenglish.zone.viewbinder;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.utils.ScreenUtils;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/9/9.
 */

public class EmptyBinder extends ItemViewBinder<String, EmptyBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.layout_error_min, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull String item) {
        holder.text.setText(item);
        holder.img.setImageResource(R.mipmap.bg_plan_box);
    }

    static class ViewHolder extends BaseViewHolder {
        ConstraintLayout itemView;
        private ImageView img;
        private TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.error_img);
            text = itemView.findViewById(R.id.error_text);
            this.itemView = (ConstraintLayout) itemView;
        }
    }
}
