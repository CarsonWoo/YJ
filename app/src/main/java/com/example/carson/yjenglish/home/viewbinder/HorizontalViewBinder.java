package com.example.carson.yjenglish.home.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.home.model.forviewbinder.Text;
import com.example.carson.yjenglish.home.model.forviewbinder.Video;
import com.example.carson.yjenglish.home.model.forviewbinder.VideoList;

import java.util.List;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by 84594 on 2018/8/13.
 */

public class HorizontalViewBinder extends ItemViewBinder<VideoList, HorizontalViewBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.layout_recycler_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull VideoList item) {
        holder.setItems(item.getmList());
    }

    static class ViewHolder extends BaseViewHolder {
        private RecyclerView recyclerView;
        private MultiTypeAdapter adapter;
        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL, false));
            adapter = new MultiTypeAdapter();
            adapter.register(Video.class, new VideoViewBinder());
            recyclerView.setAdapter(adapter);
        }

        private void setItems(List<Video> urls) {
            adapter.setItems(urls);
            adapter.notifyDataSetChanged();
        }
    }
}
