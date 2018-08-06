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
import com.example.carson.yjenglish.home.model.UncheckWord;

import java.util.List;

/**
 * Created by 84594 on 2018/8/3.
 */

public class UncheckListAdapter extends RecyclerView.Adapter<UncheckListAdapter.ListViewHolder> {

    private Context ctx;
    private List<UncheckWord> mList;

    private OnButtonClickListener listener;

    public UncheckListAdapter(){}
    public UncheckListAdapter(Context context, List<UncheckWord> list) {
        this.ctx = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_word_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.item = mList.get(position);
        holder.word.setText(holder.item.getWord());
        holder.trans.setText(holder.item.getTrans());
        holder.pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onPassClick(view);
                }
            }
        });
        holder.btnTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onTransClick(view);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ListViewHolder extends BaseViewHolder {
        TextView word;
        TextView trans;
        Button btnTrans;
        Button pass;
        UncheckWord item;
        public ListViewHolder(View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.tv_word);
            trans = itemView.findViewById(R.id.tv_trans);
            btnTrans = itemView.findViewById(R.id.btn_meaning);
            pass = itemView.findViewById(R.id.btn_pass);
        }
    }

    public interface OnButtonClickListener {
        void onTransClick(View view);
        void onPassClick(View view);
    }
}
