package com.example.carson.yjenglish.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.PickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 84594 on 2018/8/13.
 */

public class DialogUtils {
    private static DialogUtils INSTANCE = null;
    private Context ctx;
    private OnTipsListener tipsListener;
    private OnPickerListener mPickerListener;
    private OnSortListener sortListener;

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

    /**
     * tips dialog
     * @param text
     * @return
     */
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

    /**
     * common dialog
     * @param text
     * @param imgRes
     */
    public AlertDialog newCommonDialog(String text, int imgRes) {
        View contentView = View.inflate(ctx, R.layout.layout_common_dialog, null);
        ImageView commonImg = contentView.findViewById(R.id.common_img);
        TextView commonText = contentView.findViewById(R.id.common_text);
        commonText.setText(text);
        Glide.with(ctx).load(imgRes).into(commonImg);
        AlertDialog mDialog = new AlertDialog.Builder(ctx).setView(contentView).create();
        Window window = mDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return mDialog;
    }

    public void show(AlertDialog dialog) {
        dialog.show();
    }

    /**
     * bottom picker dialog
     */
    public Dialog newPickerDialog(String wordTag) {
        Dialog mDialog = new Dialog(ctx);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View dialogView = LayoutInflater.from(ctx).inflate(R.layout.layout_double_picker_dialog, null, false);
        mDialog.setContentView(dialogView);
        mDialog.setCanceledOnTouchOutside(true);

        ViewGroup.LayoutParams lp = dialogView.getLayoutParams();
        lp.width = ctx.getResources().getDisplayMetrics().widthPixels;
        dialogView.setLayoutParams(lp);
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        initPickerViews(dialogView, wordTag);
        return mDialog;
    }

    private void initPickerViews(View view, String tag) {
        TextView wordTag = view.findViewById(R.id.word_tag);
        final TextView planDate = view.findViewById(R.id.plan_date);
        TextView confirm = view.findViewById(R.id.btn_confirm);
        final PickerView dayPicker = view.findViewById(R.id.picker_day);
        final PickerView wordPicker = view.findViewById(R.id.picker_word);
        final TextView planStr = view.findViewById(R.id.plan_string);

        /** 先设置data */
        List<String> days = new ArrayList<>();
        List<String> words = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            days.add((i * 3) + "天");
            words.add((i * 20) + "单词");
        }

        dayPicker.setData(days);
        wordPicker.setData(words);

        wordTag.setText(tag);
        String planText = "每天" + wordPicker.getText() + "，计划" + dayPicker.getText() + "完成";
        planStr.setText(planText);

        Date afterDate = CalculateUtils.getDateAfter(new Date(), Integer.
                parseInt(dayPicker.getText().replace("天", "")));
        String dateStr = date2Str(afterDate);
        planDate.setText(dateStr);

        dayPicker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int pos) {
                String s = "每天" + wordPicker.getText() + "，计划" + text + "完成";
                planStr.setText(s);
                Date afterDate = CalculateUtils.getDateAfter(new Date(), Integer.
                        parseInt(dayPicker.getText().replace("天", "")));
                String dateStr = date2Str(afterDate);
                planDate.setText(dateStr);

            }
        });

        wordPicker.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int pos) {
                String s = "每天" + text + "，计划" + dayPicker.getText() + "完成";
                planStr.setText(s);
                Date afterDate = CalculateUtils.getDateAfter(new Date(), Integer.
                        parseInt(dayPicker.getText().replace("天", "")));
                String dateStr = date2Str(afterDate);
                planDate.setText(dateStr);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPickerListener != null) {
                    mPickerListener.onConfirm(dayPicker.getText(), wordPicker.getText(),
                            planDate.getText().toString());
                }
            }
        });
    }

    private String date2Str(Date afterDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        return df.format(afterDate);
    }

    public void show(Dialog mDialog) {
        mDialog.show();
    }

    public Dialog newSortDialog(View anchorView) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final View sortView = View.inflate(ctx, R.layout.popup_window_photo, null);
        dialog.setContentView(sortView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView timeSort = sortView.findViewById(R.id.take_photo);
        TextView heatSort = sortView.findViewById(R.id.pick_photo);
        timeSort.setText("按时间");
        heatSort.setText("按热度");
        sortView.setBackgroundResource(R.drawable.bg_with_corn_white);
        sortView.setPadding(ScreenUtils.dp2px(ctx, 5), ScreenUtils.dp2px(ctx, 15),
                ScreenUtils.dp2px(ctx, 5), ScreenUtils.dp2px(ctx, 3));
        ViewGroup.LayoutParams lp = sortView.getLayoutParams();
        lp.width = ScreenUtils.dp2px(ctx, 180);
        lp.height = ScreenUtils.dp2px(ctx, 100);
        sortView.setLayoutParams(lp);

        dialog.getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        WindowManager.LayoutParams winLp = dialog.getWindow().getAttributes();
        winLp.x = ScreenUtils.getScreenWidth(ctx) - anchorView.getRight();
        winLp.y = 2 * anchorView.getBottom() - anchorView.getTop()
                + sortView.getPaddingTop() + sortView.getPaddingBottom();
        Log.e("Dialog", "anchorView.getRight() = " + anchorView.getRight());
        Log.e("Dialog", "anchorView.getBottom() = " + anchorView.getBottom());
        Log.e("Dialog", "anchorView.getTop() = " + anchorView.getTop());
        Log.e("Dialog", "windowLayoutParam.y = " + winLp.y);

        timeSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortListener != null) {
                    sortListener.onItemSelected(0);
                }
                dialog.dismiss();
            }
        });
        heatSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sortListener != null) {
                    sortListener.onItemSelected(1);
                }
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public void setPickerListener(OnPickerListener mPickerListener) {
        this.mPickerListener = mPickerListener;
    }

    public void setSortListener(OnSortListener sortListener) {
        this.sortListener = sortListener;
    }

    public interface OnTipsListener {
        void onConfirm();
    }

    public interface OnPickerListener {
        void onConfirm(String day, String count, String date);
    }

    public interface OnSortListener {
        void onItemSelected(int pos);
    }
}
