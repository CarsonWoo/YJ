package com.example.carson.yjenglish.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.carson.yjenglish.R;

/**
 * Created by 84594 on 2018/7/28.
 */

public class PasswordEditText extends RelativeLayout {

    private EditText content;
    private ImageView showPassword;
    private RelativeLayout mRoot;

    private boolean visible;
    private String textHint;
    private int drawableRes;

    public PasswordEditText(Context context) {
        super(context);
        initView(context);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText);
        textHint = a.getString(R.styleable.PasswordEditText_edit_hint);
        visible = a.getBoolean(R.styleable.PasswordEditText_btn_visible, true);
        drawableRes = a.getResourceId(R.styleable.PasswordEditText_drawableStart, R.drawable.ic_password);
        initView(context);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_btn_edit, this, true);
        content = findViewById(R.id.edit_content);
        showPassword = findViewById(R.id.btn_show_password);
        mRoot = findViewById(R.id.root_send_code);
        if (!TextUtils.isEmpty(textHint)) {
            setHint(textHint);
        }
        setShowVisible(visible);
        setStartDrawable(getResources().getDrawable(drawableRes));
    }

    public void setShowClickListener(OnClickListener listener) {
        showPassword.setOnClickListener(listener);
    }

    public Editable getText() {
        return content.getText();
    }

    public void setInputType(int type) {
        if (type == InputType.TYPE_CLASS_TEXT) {
            showPassword.setImageResource(R.drawable.ic_uncover);
        } else if (type == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            showPassword.setImageResource(R.drawable.ic_cover);
        }
        content.setInputType(type);
    }

    public void setShowVisible(boolean isVisible) {
        if (isVisible) {
            showPassword.setVisibility(VISIBLE);
        } else {
            showPassword.setVisibility(INVISIBLE);
        }
    }

    public void setStartDrawable(Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        content.setCompoundDrawables(drawable, null, null, null);
    }

    public void setHint(String textHint) {
        content.setHint(textHint);
    }

    public void setText(String text) {
        content.setText(text);
    }

}
