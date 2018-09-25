package com.example.carson.yjenglish.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.home.model.word.RememberWordInfo;

import java.util.List;

/**
 * Created by 84594 on 2018/8/3.
 */

public class RememberListAdapter extends RecyclerView.Adapter<RememberListAdapter.ListViewHolder> {

    private List<RememberWordInfo.RememberWord> mList;
    private Context ctx;

    private OnButtonClickListener listener;

    public RememberListAdapter(){}

    public RememberListAdapter(Context ctx, List<RememberWordInfo.RememberWord> mList) {
        this.ctx = ctx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_word_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, final int position) {
        holder.item = mList.get(position);
        holder.word.setText(holder.item.getWord());
        holder.trans.setText(holder.item.getMeaning());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onTransClick(view, position);
                }
            }
        });
        holder.pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onPassClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    public class ListViewHolder extends BaseViewHolder {

        TextView word;
        TextView trans;
        Button pass;
        RememberWordInfo.RememberWord item;
        View itemView;

        public ListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            word = itemView.findViewById(R.id.tv_word);
            trans = itemView.findViewById(R.id.tv_trans);
            pass = itemView.findViewById(R.id.btn_pass);
        }
    }

    public interface OnButtonClickListener {
        void onTransClick(View view, int pos);
        void onPassClick(View view, int pos);
    }
}
