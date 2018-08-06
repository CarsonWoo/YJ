package com.example.carson.yjenglish.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;

/**
 * Created by 84594 on 2018/8/1.
 */

public abstract class BaseDelegateAdapter<T extends BaseViewHolder> extends DelegateAdapter.Adapter<T> {

    private Context mContext;
    private LayoutHelper mLayoutHelper;
    private VirtualLayoutManager.LayoutParams mParams;

    public BaseDelegateAdapter() {

    }

    public BaseDelegateAdapter(Context ctx, LayoutHelper helper) {
        this(ctx, helper, null);
    }

    public BaseDelegateAdapter(Context ctx, LayoutHelper layoutHelper,
                               VirtualLayoutManager.LayoutParams params) {
        this.mContext = ctx;
        this.mLayoutHelper = layoutHelper;
        this.mParams = params;
    }

    public Context getContext() {
        return this.mContext;
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return this.mLayoutHelper;
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return doOnCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {
        if (this.mParams != null) {
            holder.getView().setLayoutParams(this.mParams);
        }
        doOnBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return doGetItemCount();
    }

    public abstract T doOnCreateViewHolder(ViewGroup parent, int viewType);
    public abstract void doOnBindViewHolder(T holder, int position);
    public abstract int doGetItemCount();

}
