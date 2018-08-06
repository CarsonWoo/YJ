package com.example.carson.yjenglish.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 84594 on 2018/8/1.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private View itemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public View getView() {
        return itemView;
    }
}
