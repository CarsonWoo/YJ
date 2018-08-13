package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.carson.yjenglish.R;

/**
 * Created by 84594 on 2018/8/13.
 */

public class CorrectOrWrongTextView extends RelativeLayout {
    private RelativeLayout root;
    private TextView text;
    private ImageView img;
    public CorrectOrWrongTextView(Context context) {
        super(context);
        init(context);
    }

    public CorrectOrWrongTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CorrectOrWrongTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context ctx) {
        LayoutInflater.from(ctx).inflate(R.layout.layout_correct_or_wrong_list, this, true);
        root = findViewById(R.id.root);
        text = findViewById(R.id.text);
        img = findViewById(R.id.check_img);
    }

    public void setText(String str) {
        text.setText(str);
    }

    public void resetCheckImg() {
        img.setImageResource(R.drawable.bg_alpha);
        text.setTextColor(Color.parseColor("#656565"));
        root.setBackgroundColor(Color.WHITE);
    }

    public void setAction(boolean isCorrect) {
        if (isCorrect) {
            img.setImageResource(R.drawable.ic_correct);
            text.setTextColor(Color.parseColor("#5ee1c9"));
            root.setBackgroundColor(Color.parseColor("#785ee1c9"));
        } else {
            img.setImageResource(R.drawable.ic_wrong);
            text.setTextColor(Color.parseColor("#f1ff817a"));
            root.setBackgroundColor(Color.parseColor("#aae8918d"));
        }
    }

    public void clearWrong() {
        text.setText("");
        root.setBackgroundColor(getResources().getColor(R.color.colorTextHint));
    }
}
