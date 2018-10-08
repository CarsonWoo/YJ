package com.example.carson.yjenglish.tv.viewbinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.BaseViewHolder;
import com.example.carson.yjenglish.customviews.RoundRectImageView;
import com.example.carson.yjenglish.tv.model.forviewbinder.TVRecommendation;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 84594 on 2018/10/1.
 */

public class RecommendViewBinder extends ItemViewBinder<TVRecommendation, RecommendViewBinder.ViewHolder> {

    private OnRecommendListener mListener;

    public RecommendViewBinder(OnRecommendListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.tv_detail_recommend, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final TVRecommendation item) {
        holder.viewNum.setText(item.getViews());
        holder.word.setText(item.getWord());
        Glide.with(holder.mCardView.getContext()).load(item.getImg()).thumbnail(0.5f).crossFade()
                .into(holder.bgImg);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRecommendClick(item.getVideo_id());
                }
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {

        private CardView mCardView;
        private RoundRectImageView bgImg;
        private TextView viewNum;
        private TextView word;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.tv_detail_card);
            bgImg = itemView.findViewById(R.id.tv_detail_header);
            viewNum = itemView.findViewById(R.id.tv_detail_view_num);
            word = itemView.findViewById(R.id.tv_detail_word);
        }
    }

    public interface OnRecommendListener {
        void onRecommendClick(String video_id);
    }
}
