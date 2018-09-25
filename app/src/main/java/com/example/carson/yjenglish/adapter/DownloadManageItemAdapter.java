package com.example.carson.yjenglish.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.zone.model.DownloadManageModel;

import java.util.List;

/**
 * Created by 84594 on 2018/9/25.
 */

public class DownloadManageItemAdapter extends RecyclerView.Adapter<DownloadManageItemAdapter.ViewHolder> {

    private List<DownloadManageModel> mList;

    public DownloadManageItemAdapter(List<DownloadManageModel> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_manage_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item = mList.get(position);
        holder.fileName.setText(holder.item.getFileName());

        if (holder.mProgressBar.getProgress() == 100) {
            holder.mProgressBar.setVisibility(View.INVISIBLE);
            holder.delete.setVisibility(View.GONE);
            holder.finish.setVisibility(View.VISIBLE);
        }
        if (holder.item.isFailed()) {
            holder.mProgressBar.setProgress(0);
            holder.mProgressBar.setVisibility(View.INVISIBLE);
            holder.hideProgressBar.setVisibility(View.VISIBLE);
            holder.hideProgressBar.setProgress(100);
            holder.tvProgress.setText("下载异常，点击重试");
        } else {
            holder.tvProgress.setText(holder.item.getCurSize() + "M/" + holder.item.getSize() + "M");
            holder.mProgressBar.setProgress((holder.item.getCurSize() * 100 / holder.item.getSize()));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends BaseViewHolder {
        private TextView fileName;
        private TextView tvProgress;
        private ProgressBar mProgressBar;
        private ProgressBar hideProgressBar;
        private TextView finish;
        private ImageView delete;
        View itemView;
        DownloadManageModel item;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            fileName = itemView.findViewById(R.id.download_file_name);
            tvProgress = itemView.findViewById(R.id.tv_progress);
            mProgressBar = itemView.findViewById(R.id.download_progress);
            finish = itemView.findViewById(R.id.download_finish);
            delete = itemView.findViewById(R.id.download_delete);
            hideProgressBar = itemView.findViewById(R.id.hide_progress_bar);
        }
    }
}
