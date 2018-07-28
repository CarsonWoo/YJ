package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.carson.yjenglish.R;

/**
 * Created by 84594 on 2018/7/28.
 */

public class WithBtnEditText extends RelativeLayout {

    private EditText content;
    private Button mBtn;
    private RelativeLayout mRoot;

    private final int TYPE_TEXT_PASSWORD = 1;
    private final int TYPE_NUMBER = 2;

    private boolean isVisible;

    public WithBtnEditText(Context context) {
        super(context);
        initView(context);
    }

    public WithBtnEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.WithBtnEditText);
        //默认不显示btn
        isVisible = a.getBoolean(R.styleable.WithBtnEditText_btn_visible, false);
        a.recycle();
        initView(context);
    }

    public WithBtnEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_send_code, this, true);
        content = findViewById(R.id.edit_content);
        mBtn = findViewById(R.id.btn_send_code);
        mRoot = findViewById(R.id.root_send_code);
        setBtnIsVisible(isVisible);
        setInputType(TYPE_TEXT_PASSWORD);
    }

    public void setHint(String hintText) {
        if (!TextUtils.isEmpty(hintText)) {
            content.setHint(hintText);
        }
    }

    public void setBtnClickListener(OnClickListener listener) {
        mBtn.setOnClickListener(listener);
    }

    public Editable getText() {
        return content.getText();
    }

    public void setBtnText(String text) {
        if (!TextUtils.isEmpty(text)) {
            mBtn.setText(text);
        }
    }

    public void setBtnIsVisible(boolean isVisible) {
        if (isVisible) {
            mBtn.setVisibility(VISIBLE);
        } else {
            mBtn.setVisibility(INVISIBLE);
        }
    }

    public void setBtnClickStatus(boolean isVisible) {
        if (isVisible) {
            mBtn.setEnabled(true);
            mBtn.setClickable(true);
        } else {
            mBtn.setEnabled(false);
            mBtn.setClickable(false);
        }
    }

    public void setInputType(int type) {
        switch (type) {
            case TYPE_NUMBER:
                content.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case TYPE_TEXT_PASSWORD:
                content.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
        }

    }

}
