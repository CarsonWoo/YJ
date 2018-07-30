package com.example.carson.yjenglish.register.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.PasswordEditText;
import com.example.carson.yjenglish.register.RegisterContract;
import com.example.carson.yjenglish.register.RegisterTask;
import com.example.carson.yjenglish.register.model.RegisterInfo;
import com.example.carson.yjenglish.register.model.RegisterModel;
import com.example.carson.yjenglish.register.presenter.RegisterPresenter;
import com.jude.swipbackhelper.SwipeBackHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,
        RegisterContract.View {

    private final int TYPE_SHOW_PASSWORD = InputType.TYPE_CLASS_TEXT;
    private final int TYPE_HIDE_PASSWORD = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;

    private TextView back;
    private EditText phone;
    private PasswordEditText password;
    private Button confirm;

    private RegisterPresenter registerPresenter;
    private RegisterContract.Presenter mPresenter;
    private Dialog mDialog;

    private int isShowPwd = 0;//计数是否显示密码

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
        confirm = findViewById(R.id.btn_confirm);

        mDialog = new ProgressDialog(this);

        back.setOnClickListener(this);
        confirm.setOnClickListener(this);

        phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)}); //最大输入长度
        password.getText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});

        setPwdVisible();

    }

    private void setPwdVisible() {
        password.setShowClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowPwd += 1;
                if (isShowPwd % 2 != 0) {
                    //显示
                    password.setInputType(TYPE_SHOW_PASSWORD);
                } else {
                    password.setInputType(TYPE_HIDE_PASSWORD);
                }
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
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(phone.getText()) || TextUtils.isEmpty(password.getText())) {
                    Toast.makeText(getApplicationContext(), "请先输入您的注册信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                //doCodeTask 访问验证码url 并进行跳转
                RegisterTask task = RegisterTask.getInstance();
                registerPresenter = new RegisterPresenter(task, RegisterActivity.this);
                this.setPresenter(registerPresenter);
                mPresenter.getRegisterResponse(new RegisterModel(phone.getText().toString(),
                        password.getText().toString()));
                break;
        }
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getResponse(RegisterInfo info) {
        if (info != null) {
            String msg = info.getMsg();
            //TODO 进行跳转工作
        }
    }

    @Override
    public void hideLoading() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
