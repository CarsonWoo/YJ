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
import com.example.carson.yjenglish.home.model.HandledWordInfo;

import java.util.List;

/**
 * Created by 84594 on 2018/8/3.
 */

public class HandledListAdapter extends RecyclerView.Adapter<HandledListAdapter.ListViewHolder> {

    private Context context;
    private List<HandledWordInfo.HandleWord> mList;

    private onButtonClickListener listener;

    public HandledListAdapter(){}
    public HandledListAdapter(Context ctx, List<HandledWordInfo.HandleWord> list) {
        this.context = ctx;
        this.mList = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_word_list_recover, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.item = mList.get(position);
        holder.word.setText(holder.item.getWord());
        holder.trans.setText(holder.item.getMeaning());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    //TODO 可能还需要一些参数
                    listener.onTransClick(view);
                }
            }
        });
        holder.recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onRecoverClick(view);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setListener(onButtonClickListener listener) {
        this.listener = listener;
    }

    public class ListViewHolder extends BaseViewHolder {

        TextView word;
        TextView trans;
        Button recover;
        HandledWordInfo.HandleWord item;
        View itemView;

        public ListViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            word = itemView.findViewById(R.id.tv_word);
            trans = itemView.findViewById(R.id.tv_trans);
            recover = itemView.findViewById(R.id.btn_recover);
        }
    }

    public interface onButtonClickListener {
        void onRecoverClick(View view);
        void onTransClick(View view);
    }
}
