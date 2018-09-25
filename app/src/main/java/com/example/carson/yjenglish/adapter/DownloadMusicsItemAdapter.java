package com.example.carson.yjenglish.adapter;

import android.content.Context;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.home.model.word.BaseWord;
import com.example.carson.yjenglish.utils.ScreenUtils;

import java.util.List;

/**
 * Created by 84594 on 2018/9/15.
 */

public class DownloadMusicsItemAdapter extends RecyclerView.Adapter<DownloadMusicsItemAdapter.ViewHolder> {

    private Context ctx;
    private List<BaseWord> mList;
    private OnMusicItemSlideListener listener;

    private int downX;
    private int mStartX;
    private boolean isDeleteVisible = false;

    public DownloadMusicsItemAdapter(Context ctx, List<BaseWord> mList) {
        this.ctx = ctx;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_music_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.item = mList.get(position);
        holder.word.setText(holder.item.getWord());
        holder.meaning.setText(holder.item.getTrans());
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = (int) event.getX();
                        Log.e("Adapter", "downX = " + (int) event.getX());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        /* 存在bug */
                        int scrollX = holder.itemView.getScrollX();
                        int newScrollX = mStartX - x;
                        if (newScrollX < 0 && scrollX <= 0) {
                            newScrollX = 0;
                            isDeleteVisible = false;
                        } else if (newScrollX > 0 && scrollX >= ScreenUtils.dp2px(ctx, 100)) {
                            newScrollX = 0;
                            isDeleteVisible = true;
                        }
                        holder.itemView.scrollBy(newScrollX, 0);
                        break;

//                        holder.itemView.scrollBy(-deltaX, 0);
                    case MotionEvent.ACTION_UP:
                        Log.e("Adapter", "upX = " + (int) event.getX());
                        int upX = (int) event.getX();
                        if (isDeleteVisible) {
                            if (upX > downX) {
                                isDeleteVisible = false;
                                holder.itemView.scrollTo(0, 0);
                            }
                        } else {
                            if (upX - downX < -ScreenUtils.dp2px(ctx, 60)) {
                                isDeleteVisible = true;
                                holder.itemView.scrollTo(ScreenUtils.dp2px(ctx, 100), 0);
                            }
                        }
                        Log.e("Adapter", "isDeleteVisible = " + isDeleteVisible);
                        break;
                }
                mStartX = x;
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setListener(OnMusicItemSlideListener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends BaseViewHolder {
        private TextView word;
        private TextView meaning;
        private Button delete;
        View itemView;
        BaseWord item;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            word = itemView.findViewById(R.id.word);
            meaning = itemView.findViewById(R.id.meaning);
            delete = itemView.findViewById(R.id.music_delete);
        }
    }

    public interface OnMusicItemSlideListener {
        void onSlide();
    }
}
