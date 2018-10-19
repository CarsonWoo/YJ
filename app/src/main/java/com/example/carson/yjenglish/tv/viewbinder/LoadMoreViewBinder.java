package com.example.carson.yjenglish.tv.viewbinder;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.home.model.forviewbinder.Text;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/10/14.
 */

public class LoadMoreViewBinder extends ItemViewBinder<Text, LoadMoreViewBinder.ViewHolder> {

    private OnLoadListener listener;

    public LoadMoreViewBinder(OnLoadListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.layout_load_more, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Text item) {
        holder.textView.setText(item.getText());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLoad();
                }
            }
        });
    }

    public class ViewHolder extends BaseViewHolder {

        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_load_more);
        }
    }

    public interface OnLoadListener {
        void onLoad();
    }

}
