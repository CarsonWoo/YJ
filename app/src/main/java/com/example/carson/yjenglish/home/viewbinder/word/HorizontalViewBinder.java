package com.example.carson.yjenglish.home.viewbinder.word;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.home.model.forviewbinder.Video;
import com.example.carson.yjenglish.home.model.forviewbinder.VideoList;

import java.util.List;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by 84594 on 2018/8/13.
 */

public class HorizontalViewBinder extends ItemViewBinder<VideoList, HorizontalViewBinder.ViewHolder> {

    private VideoViewBinder.OnVideoClickListener mListener;

    public HorizontalViewBinder(VideoViewBinder.OnVideoClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.layout_recycler_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull VideoList item) {
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        holder.adapter = new MultiTypeAdapter();
        holder.adapter.register(Video.class, new VideoViewBinder(mListener));
        holder.recyclerView.setAdapter(holder.adapter);
        holder.setItems(item.getmList());
    }

    static class ViewHolder extends BaseViewHolder {
        private RecyclerView recyclerView;
        private MultiTypeAdapter adapter;
        private View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            recyclerView = itemView.findViewById(R.id.recycler_view);
        }

        private void setItems(List<Video> urls) {
            adapter.setItems(urls);
            adapter.notifyDataSetChanged();
        }
    }
}
