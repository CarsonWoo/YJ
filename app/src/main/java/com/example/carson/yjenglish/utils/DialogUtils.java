package com.example.carson.yjenglish.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.carson.yjenglish.R;

/**
 * Created by 84594 on 2018/8/13.
 */

public class DialogUtils {
    private static DialogUtils INSTANCE = null;
    private Context ctx;
    private OnTipsListener tipsListener;

    public static DialogUtils getInstance(Context ctx) {
        if (INSTANCE == null) {
            INSTANCE = new DialogUtils(ctx);
        }
        return INSTANCE;
    }

    private DialogUtils(Context ctx) {
        this.ctx = ctx;
    }

    public DialogUtils(){}

    public void setTipsListener(OnTipsListener listener) {
        this.tipsListener = listener;
    }

    public AlertDialog newTipsDialog(String text) {
        View contentView = View.inflate(ctx, R.layout.layout_tips_dialog, null);
        TextView tipsText = contentView.findViewById(R.id.tips_text);
        TextView tipsCancel = contentView.findViewById(R.id.tips_cancel);
        TextView tipsConfirm = contentView.findViewById(R.id.tips_confirm);
        tipsText.setText(text);
        final AlertDialog mDialog = new AlertDialog.Builder(ctx).setView(contentView).create();
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tipsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        tipsConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tipsListener != null) {
                    tipsListener.onConfirm();
                    mDialog.dismiss();
                }
            }
        });
        return mDialog;
    }

    public void show(AlertDialog dialog) {
        dialog.show();
    }

    public interface OnTipsListener {
        void onConfirm();
    }
}
