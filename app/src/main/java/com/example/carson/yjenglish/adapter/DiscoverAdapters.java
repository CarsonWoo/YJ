package com.example.carson.yjenglish.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.R;

import java.util.List;

/**
 * Created by 84594 on 2018/8/8.
 */

public class DiscoverAdapters {
    public static class AtyAdapter extends RecyclerView.Adapter<AtyAdapter.AtyHolder> {

        private Context ctx;
        private List<String> mList;

        public AtyAdapter(Context ctx, List<String> mList) {
            this.ctx = ctx;
            this.mList = mList;
        }

        @NonNull
        @Override
        public AtyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_error_card, parent,
                    false);
            return new AtyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AtyHolder holder, int position) {
            holder.img.setImageResource(R.mipmap.ic_launcher_round);
            holder.text.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
        public class AtyHolder extends BaseViewHolder {

            ImageView img;
            TextView text;
            public AtyHolder(View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.error_img);
                text = itemView.findViewById(R.id.error_text);
            }
        }
    }
}
