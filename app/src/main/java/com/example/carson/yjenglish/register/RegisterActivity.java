package com.example.carson.yjenglish.register;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.WithBtnEditText;
import com.jude.swipbackhelper.SwipeBackHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private EditText phone, password;
    private WithBtnEditText code;
    private Button confirm;

    private int isShowPwd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.register_back);
        phone = findViewById(R.id.edit_phone);
        password = findViewById(R.id.edit_password);
        code = findViewById(R.id.edit_code);
        confirm = findViewById(R.id.btn_confirm);

        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setClosePercent(0.5f)
                .setSwipeEdgePercent(0.15f)
                .setSwipeRelateOffset(300)
                .setSwipeSensitivity(1.0f)
                .setSwipeBackEnable(true)
                .setSwipeRelateEnable(true);

        back.setOnClickListener(this);

        phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)}); //最大输入长度
        password.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        code.getText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        setPwdVisible();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setPwdVisible() {
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Drawable drawable = password.getCompoundDrawables()[2];
                if (drawable == null) {
                    return false;
                }
                if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (motionEvent.getX() > password.getWidth() -
                        password.getPaddingRight() -
                        drawable.getIntrinsicWidth()) {
                    isShowPwd++;
                    if (isShowPwd % 2 != 0) {
                        //显示明文
                        password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        drawable = getResources().getDrawable(R.drawable.ic_uncover);
                    } else {
                        password.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        drawable = getResources().getDrawable(R.drawable.ic_cover);
                    }
                }
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                password.setCompoundDrawables(null, null, drawable, null);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_back:
                onBackPressed();
                break;
        }
    }
}
