package com.example.carson.yjenglish.home.viewbinder.feeds;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.home.model.forviewbinder.Recommend;
import com.example.carson.yjenglish.home.model.forviewbinder.RecommendList;

import java.util.List;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by 84594 on 2018/8/18.
 */

public class RecommendListViewBinder extends ItemViewBinder<RecommendList, RecommendListViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.layout_recycler_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull RecommendList item) {
        holder.setItems(item.getList());
    }

    static class ViewHolder extends BaseViewHolder {
        private RecyclerView recyclerView;
        private MultiTypeAdapter mAdapter;
        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            LinearLayoutManager manager = new LinearLayoutManager(itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(manager);
            mAdapter = new MultiTypeAdapter();
            recyclerView.setAdapter(mAdapter);

            mAdapter.register(Recommend.class, new RecommendViewBinder());
        }

        private void setItems(List<Recommend> mList) {
            mAdapter.setItems(mList);
            mAdapter.notifyDataSetChanged();
        }
    }
}
