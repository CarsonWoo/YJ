package com.example.carson.yjenglish.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.music.model.MusicBean;
import com.example.carson.yjenglish.music.view.DownloadTab;
import com.example.carson.yjenglish.music.view.LikeTab;
import com.example.carson.yjenglish.music.view.RememberTab;

import java.util.List;
import java.util.Map;

/**
 * Created by 84594 on 2018/8/23.
 */

public class MusicListAdapters {
    public static class RememberAdapter extends RecyclerView.Adapter<RememberAdapter.ViewHolder> {
        private List<MusicBean> mList;
        private RememberTab.OnMusicSelectListener mListener;
        private Context ctx;

        private int lastPos = -1;

        public RememberAdapter(Context ctx, List<MusicBean> mList, RememberTab.OnMusicSelectListener mListener) {
            this.ctx = ctx;
            this.mList = mList;
            this.mListener = mListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_music_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.word.setText(mList.get(position).getWord());
//            if (mList.get(position).isCurPlaying()) {
//                holder.sound.setVisibility(View.VISIBLE);
//            }
            if (lastPos == position) {
                holder.sound.setVisibility(View.VISIBLE);
                holder.word.setTextColor(Color.parseColor("#5ee1c9"));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onMusicSelect(mList.get(position).getMusicUrl());
                    }
                    lastPos = position;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }


        static class ViewHolder extends BaseViewHolder {
            private TextView word;
            private ImageView sound;
            View itemView;
            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                word = itemView.findViewById(R.id.music_word);
                sound = itemView.findViewById(R.id.music_sound);
            }
        }
    }

    public static class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {
        private List<MusicBean> mList;
        private LikeTab.OnMusicSelectListener mListener;
        private Context ctx;

        private int lastPos = -1;

        public LikeAdapter(Context ctx, List<MusicBean> mList, LikeTab.OnMusicSelectListener mListener) {
            this.ctx = ctx;
            this.mList = mList;
            this.mListener = mListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_music_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.word.setText(mList.get(position).getWord());
            holder.delete.setVisibility(View.VISIBLE);
            if (lastPos == position) {
                holder.sound.setVisibility(View.VISIBLE);
                holder.word.setTextColor(Color.parseColor("#5ee1c9"));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onMusicSelect(mList.get(position).getMusicUrl());
                    }
                    lastPos = position;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }


        public class ViewHolder extends BaseViewHolder {
            private TextView word;
            private ImageView sound;
            private ImageView delete;
            View itemView;
            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                word = itemView.findViewById(R.id.music_word);
                sound = itemView.findViewById(R.id.music_sound);
                delete = itemView.findViewById(R.id.music_cancel);
            }
        }
    }

    public static class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {
        private List<MusicBean> mList;
        private DownloadTab.OnMusicSelectListener mListener;
        private Context ctx;

        private int lastPos = -1;

        public DownloadAdapter(Context ctx, List<MusicBean> mList, DownloadTab.OnMusicSelectListener mListener) {
            this.ctx = ctx;
            this.mList = mList;
            this.mListener = mListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.layout_music_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.word.setText(mList.get(position).getWord());
            holder.delete.setVisibility(View.VISIBLE);
            if (lastPos == position) {
                holder.sound.setVisibility(View.VISIBLE);
                holder.word.setTextColor(Color.parseColor("#5ee1c9"));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onMusicSelect(mList.get(position).getMusicUrl());
                    }
                    lastPos = position;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }


        public class ViewHolder extends BaseViewHolder {
            private TextView word;
            private ImageView sound;
            private ImageView delete;
            View itemView;
            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                word = itemView.findViewById(R.id.music_word);
                sound = itemView.findViewById(R.id.music_sound);
                delete = itemView.findViewById(R.id.music_cancel);
            }
        }
    }
}
