package com.example.carson.yjenglish.login.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.example.carson.yjenglish.HomeActivity;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.checkcode.CodeContract;
import com.example.carson.yjenglish.checkcode.presenter.CodePresenter;
import com.example.carson.yjenglish.checkcode.view.CodeActivity;
import com.example.carson.yjenglish.customviews.BaselineTextView;
import com.example.carson.yjenglish.customviews.PasswordEditText;
import com.example.carson.yjenglish.login.LoginContract;
import com.example.carson.yjenglish.login.LoginTask;
import com.example.carson.yjenglish.login.model.LoginInfo;
import com.example.carson.yjenglish.login.model.LoginModule;
import com.example.carson.yjenglish.login.presenter.LoginPresenter;
import com.example.carson.yjenglish.register.view.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View,
        View.OnClickListener {

    private final String TAG = "Login";

    private final int TYPE_SHOW_PASSWORD = InputType.TYPE_CLASS_TEXT;
    private final int TYPE_HIDE_PASSWORD = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
    private final int RESULT_LOGIN_OK = 100;
    private final int RESULT_FORGET_OK = 102;

    private EditText phone;
    private PasswordEditText password;
    private TextView codeLogin;
    private TextView forget;
    private Button login;
    private BaselineTextView register;
    private TextView testUse;

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
        testUse = findViewById(R.id.test_use);

        phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)}); //最大输入长度
        password.getText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)}); //最大密码输入长度

        checkCodeLoginClick();
        checkShowPassword();

        mDialog = new ProgressDialog(this);
        mDialog.setTitle("正在登录中");
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget.setOnClickListener(this);
        testUse.setOnClickListener(this);
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
        //进行跳转主页操作
        if (info != null && info.getMsg() != null) {
            LoginInfo.LoginResponse response = info.getMsg();
            Log.e(TAG, "token = " + response.getToken());
        }
//        login.setClickable(true);
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
                    //跳转验证码
                    if (!phone.getText().toString().isEmpty()) {
                        Intent toCode = new Intent(LoginActivity.this, CodeActivity.class);
                        toCode.putExtra("phone", phone.getText().toString());
                        toCode.putExtra("type", 0);
                        startActivityForResult(toCode, 0);
                        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                    } else {
                        Toast.makeText(getApplicationContext(), "请先输入手机号", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (phone.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "请先输入手机或密码噢", Toast.LENGTH_SHORT).show();
                    return;
                }

                //mvp模块拼装
                LoginTask loginTask = LoginTask.getInstance(0);
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
                startActivityForResult(toForget, 2);
                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                break;
            case R.id.test_use:
                Intent toHome = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(toHome);
                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_LOGIN_OK) {
            if (data != null) {
                login.setClickable(false);
                //进行访问网络
                LoginTask loginTask = LoginTask.getInstance(1);
                loginPresenter = new LoginPresenter(LoginActivity.this, loginTask);
                this.setPresenter(loginPresenter);
                mPresenter.getLoginInfo(new LoginModule
                        (phone.getText().toString(), data.getIntExtra("code", 0)));
            }
        } else if (requestCode == 2 && resultCode == RESULT_FORGET_OK) {
            if (data != null) {
                phone.setText(data.getStringExtra("phone"));
                password.setText(data.getStringExtra("password"));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loginPresenter != null) {
            loginPresenter.unsubscribe();
        }
    }


}
