package com.example.carson.yjenglish.home.viewbinder;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.adapter.HomeListAdapter;
import com.example.carson.yjenglish.home.model.forviewbinder.Text;
import com.example.carson.yjenglish.utils.ScreenUtils;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/8/14.
 */

public class TextDrawableViewBinder extends ItemViewBinder<Text, TextDrawableViewBinder.ViewHolder> {

    private OnEditSelectListener mListener;

    public TextDrawableViewBinder(OnEditSelectListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.word_detail_text_drawable, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Text item) {
        holder.text.setTextSize(16);
        holder.text.setText(item.getText());
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onEditSelect();
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        private TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }

    public interface OnEditSelectListener {
        void onEditSelect();
    }
}
