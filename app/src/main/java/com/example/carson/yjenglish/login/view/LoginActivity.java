package com.example.carson.yjenglish.login.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.BaselineTextView;
import com.example.carson.yjenglish.customviews.PasswordEditText;
import com.example.carson.yjenglish.login.ForgetActivity;
import com.example.carson.yjenglish.login.LoginContract;
import com.example.carson.yjenglish.login.LoginTask;
import com.example.carson.yjenglish.login.model.LoginInfo;
import com.example.carson.yjenglish.login.model.LoginModule;
import com.example.carson.yjenglish.login.presenter.LoginPresenter;
import com.example.carson.yjenglish.register.view.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View, View.OnClickListener {

    private final String TAG = "Login";

    private final int TYPE_SHOW_PASSWORD = InputType.TYPE_CLASS_TEXT;
    private final int TYPE_HIDE_PASSWORD = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;

    private EditText phone;
    private PasswordEditText password;
    private TextView codeLogin;
    private TextView forget;
    private Button login;
    private BaselineTextView register;
    private Dialog mDialog;
    private LoginContract.Presenter mPresenter;
    private LoginPresenter loginPresenter;

    private int clickCount = 0;//计数是否显示密码框
    private int isShowPassword = 0;//计数是否显示password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        iniViews();

    }

    private void iniViews() {
        phone = findViewById(R.id.edit_phone);
        password = findViewById(R.id.edit_password);
        codeLogin = findViewById(R.id.tv_code_login);
        forget = findViewById(R.id.tv_forget_password);
        login = findViewById(R.id.btn_login);
        register = findViewById(R.id.btn_register);

        phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)}); //最大输入长度
        password.getText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)}); //最大密码输入长度

        checkCodeLoginClick();
        checkShowPassword();

        mDialog = new ProgressDialog(this);
        mDialog.setTitle("正在登录中");
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget.setOnClickListener(this);

    }

    private void checkCodeLoginClick() {
        codeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCount += 1;
                password.getText().clear();
                //若为奇数 则隐藏
                if (clickCount % 2 != 0) {
                    password.setVisibility(View.INVISIBLE);
                    codeLogin.setText("密码登录");
                    forget.setVisibility(View.INVISIBLE);
                    forget.setEnabled(false);
                    password.setInputType(TYPE_HIDE_PASSWORD);
                    isShowPassword = 0;
                } else {
                    codeLogin.setText(R.string.code_login);
                    forget.setVisibility(View.VISIBLE);
                    forget.setEnabled(true);
                    password.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void checkShowPassword() {
        password.setShowClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowPassword += 1;
                if (isShowPassword % 2 != 0) {
                    setPasswordVisible(true);
                } else {
                    setPasswordVisible(false);
                }
            }
        });
    }

    private void setPasswordVisible(boolean clickable) {
        if (clickable) {
            password.setInputType(TYPE_SHOW_PASSWORD);
        } else {
            password.setInputType(TYPE_HIDE_PASSWORD);
        }
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    //进行ui显示
    @Override
    public void setLoginInfo(LoginInfo info) {
        if (info != null && info.getMsg() != null) {
            LoginInfo.LoginResponse response = info.getMsg();
            Log.e(TAG, "token = " + response.getToken());
        }
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //需判断直接登录还是验证码登录
                if (password.getVisibility() == View.INVISIBLE) {
                    //doCodeTask 访问验证码url
                    return;
                }
                if (phone.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "请先输入手机或密码噢", Toast.LENGTH_SHORT).show();
                    return;
                }

                //mvp模块拼装
                LoginTask loginTask = LoginTask.getInstance();
                loginPresenter = new LoginPresenter(LoginActivity.this, loginTask);
                this.setPresenter(loginPresenter);
                mPresenter.getLoginInfo(new LoginModule
                        (phone.getText().toString(), password.getText().toString()));
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                break;
            case R.id.tv_forget_password:
                Intent toForget = new Intent(LoginActivity.this, ForgetActivity.class);
                toForget.putExtra("type", 0);
                startActivity(toForget);
                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    //    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setNavigationBarColor(Color.TRANSPARENT);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//    }
}
