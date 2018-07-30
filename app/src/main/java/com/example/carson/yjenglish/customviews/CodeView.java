package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.carson.yjenglish.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 84594 on 2018/7/30.
 */

public class CodeView extends LinearLayout implements View.OnFocusChangeListener {

    private EditText first, second, third, fourth;

    private String text;

    private OnInputFinishListener mInputListener;

    private List<EditText> mEdits = new ArrayList<EditText>();

    public CodeView(Context context) {
        super(context);
        initView();
    }

    public CodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CodeView);
        text = a.getString(R.styleable.CodeView_setText);
        a.recycle();
        initView();
    }

    public CodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_code, this, true);
        first = findViewById(R.id.edit_first);
        second = findViewById(R.id.edit_second);
        third = findViewById(R.id.edit_third);
        fourth = findViewById(R.id.edit_fourth);

        mEdits.add(first);
        mEdits.add(second);
        mEdits.add(third);
        mEdits.add(fourth);

        first.setFocusable(true);
        first.addTextChangedListener(new MyTextWatcher());
        second.addTextChangedListener(new MyTextWatcher());
        third.addTextChangedListener(new MyTextWatcher());
        fourth.addTextChangedListener(new MyTextWatcher());

        if (!TextUtils.isEmpty(text)) {
            setText(text);
        }


        first.setOnFocusChangeListener(this);
        second.setOnFocusChangeListener(this);
        third.setOnFocusChangeListener(this);
        fourth.setOnFocusChangeListener(this);
    }

    @Override
    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i ++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            focus();
        }
    }

    public void setmInputListener(OnInputFinishListener mInputListener) {
        this.mInputListener = mInputListener;
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() != 0) {
                focus();
            }
        }
    }
    private void focus() {
        EditText editText;
        //利用for循环找出前面还没被输入字符的EditText
        for (int i = 0; i < 4; i++) {
            editText = mEdits.get(i);
            if (editText.getText().length() < 1) {
//                editText.setCursorVisible(true);
                editText.requestFocus();

                return;
            } else {
                editText.setCursorVisible(false);
            }
        }
        EditText lastEditText = mEdits.get(3);
        if (lastEditText.getText().length() > 0) {
            //收起软键盘 并不允许编辑
            getResponse();
            lastEditText.setCursorVisible(false);
            InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void getResponse() {
        Log.e("CodeView", "ok");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mEdits.size(); i++) {
            sb.append(mEdits.get(i).getText().toString());
        }
        if (mInputListener != null) {
            mInputListener.onFinish(sb.toString());
        }
    }

    public void setText(String text) {
        if (text.length() == 4) {
            StringBuilder sb = new StringBuilder(text);
            first.setText(sb.substring(0, 1));
            second.setText(sb.substring(1, 2));
            third.setText(sb.substring(2, 3));
            fourth.setText(sb.substring(3, 4));
        } else {
            first.setText("");
            second.setText("");
            third.setText("");
            fourth.setText("");
//            first.setCursorVisible(true);
            first.requestFocus();
        }
    }

    public interface OnInputFinishListener {
        void onFinish(String code);
    }
}
