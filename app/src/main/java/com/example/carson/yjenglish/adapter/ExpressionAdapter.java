package com.example.carson.yjenglish.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.carson.yjenglish.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 84594 on 2018/8/20.
 */

public class ExpressionAdapter extends RecyclerView.Adapter<ExpressionAdapter.ViewHolder> {

    private List<Integer> mExpressionRes = new ArrayList<>();
    private Context ctx;

    public ExpressionAdapter(Context ctx) {
        this.ctx = ctx;
        mExpressionRes.add(R.drawable.ic_like_pink_fill);
        mExpressionRes.add(R.drawable.ic_notice_yellow_fill);
        mExpressionRes.add(R.drawable.ic_edit);
        mExpressionRes.add(R.drawable.ic_sign_in);
        mExpressionRes.add(R.drawable.ic_comment_black);
        mExpressionRes.add(R.drawable.ic_home_sel);
        mExpressionRes.add(R.drawable.ic_tv_sel);
        mExpressionRes.add(R.drawable.ic_add_gray);
        mExpressionRes.add(R.drawable.ic_discover_sel);
        mExpressionRes.add(R.drawable.ic_arrow_left);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_expression, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img.setImageResource(mExpressionRes.get(position));
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends BaseViewHolder {
        private ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.expression_img);
        }
    }
}
