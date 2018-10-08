package com.example.carson.yjenglish.tv.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.tv.model.forviewbinder.RecommendList;
import com.example.carson.yjenglish.tv.model.forviewbinder.TVRecommendation;

import java.util.List;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by 84594 on 2018/10/1.
 */

public class RecommendListViewBinder extends ItemViewBinder<RecommendList, RecommendListViewBinder.ViewHolder> {

    private RecommendViewBinder.OnRecommendListener mListener;

    public RecommendListViewBinder(RecommendViewBinder.OnRecommendListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.layout_recycler_view, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull RecommendList item) {
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        holder.mAdapter = new MultiTypeAdapter();
        holder.mAdapter.register(TVRecommendation.class, new RecommendViewBinder(mListener));
        holder.recyclerView.setAdapter(holder.mAdapter);
        holder.setItems(item.getList());
    }

    static class ViewHolder extends BaseViewHolder {

        private RecyclerView recyclerView;
        private MultiTypeAdapter mAdapter;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            this.itemView = itemView;
        }

        public void setItems(List<TVRecommendation> list) {
            mAdapter.setItems(list);
            mAdapter.notifyDataSetChanged();
        }
    }
}
