package com.example.carson.yjenglish.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.zone.model.PlanData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 84594 on 2018/8/15.
 * 对应于PlanActivity中的头部RecyclerView
 */

public class PlanAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private int TYPE_FOOTER = -1;
    private int TYPE_LIST = 0;

    private List<PlanData> mPlans;
    private Context ctx;
    private OnItemChangeListener mChangeListner;

    private int mLastPos = -1;//由于recyclerview的复用item的问题 需要记录上一次选择的item的position

    private Map<Integer, Boolean> mCardSelected = new HashMap<>();

    public PlanAdapter(Context ctx, List<PlanData> mPlans) {
        this.ctx = ctx;
        this.mPlans = mPlans;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == TYPE_LIST) {
            itemView = LayoutInflater.from(ctx).inflate(R.layout.plan_item, parent, false);
            for (int i = 0; i < mPlans.size(); i++) {
                mCardSelected.put(i, false);
            }
            return new ViewHolder(itemView);
        }
        itemView = LayoutInflater.from(ctx).inflate(R.layout.plan_item_add, parent, false);
        return new FooterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, final int position) {
        if (position == mPlans.size()) {//底部布局
            FooterViewHolder mFooter = (FooterViewHolder) holder;
            mFooter.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("PlanAdapter", "onAddClick");
                }
            });
        } else {
            final ViewHolder mHolder = (ViewHolder) holder;
            mHolder.item = mPlans.get(position);
            mHolder.tag.setText(mHolder.item.getTag());
            mHolder.wordCount.setText(mHolder.item.getWordCount());
            if (mHolder.item.isLearning()) {
                mHolder.isLearning.setVisibility(View.VISIBLE);
                mHolder.seekBar.setVisibility(View.VISIBLE);
                mHolder.seekBar.setProgress(mHolder.item.getProgress());
            } else {
                mHolder.isLearning.setVisibility(View.INVISIBLE);
                mHolder.seekBar.setVisibility(View.INVISIBLE);
            }
            if (mCardSelected.get(position)) {
                mHolder.mCard.setSelected(true);
                mHolder.border.setVisibility(View.VISIBLE);
            } else {
                mHolder.mCard.setSelected(false);
                mHolder.border.setVisibility(View.INVISIBLE);
            }
            if (mLastPos == position) {
                mHolder.mCard.setSelected(true);
                mHolder.border.setVisibility(View.VISIBLE);
            }
            mHolder.mCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < mCardSelected.size(); i++) {
                        if (position == i) {
                            mCardSelected.put(position, true);
                            mLastPos = position;

                            continue;
                        }
                        mCardSelected.put(i, false);
                    }
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == mPlans.size()) {
            return TYPE_FOOTER;
        }
        return TYPE_LIST;
    }

    @Override
    public int getItemCount() {
        return mPlans.size() + 1;
    }

    public void setChangeListner(OnItemChangeListener mChangeListner) {
        this.mChangeListner = mChangeListner;
    }

    public class ViewHolder extends BaseViewHolder {
        private TextView isLearning;
        private TextView tag;
        private TextView wordCount;
        private SeekBar seekBar;
        private ImageView resetOrDelete;
        private CardView mCard;
        private ImageView border;
        private
        PlanData item;
        public ViewHolder(View itemView) {
            super(itemView);
            isLearning = itemView.findViewById(R.id.is_learning);
            tag = itemView.findViewById(R.id.learn_tag);
            wordCount = itemView.findViewById(R.id.word_count);
            seekBar = itemView.findViewById(R.id.learning_seek_bar);
            resetOrDelete = itemView.findViewById(R.id.reset_or_delete);
            mCard = itemView.findViewById(R.id.item_card);
            border = itemView.findViewById(R.id.border);
        }
    }

    public class FooterViewHolder extends BaseViewHolder {
        View itemView;
        public FooterViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    public interface OnItemChangeListener {
        void onItemChange(View target, ImageView border, int targetPos);
    }
}
