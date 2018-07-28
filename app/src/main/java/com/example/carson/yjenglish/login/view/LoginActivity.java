package com.example.carson.yjenglish.login.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.WithBtnEditText;
import com.example.carson.yjenglish.login.LoginContract;
import com.example.carson.yjenglish.login.LoginTask;
import com.example.carson.yjenglish.login.model.LoginInfo;
import com.example.carson.yjenglish.login.model.LoginModule;
import com.example.carson.yjenglish.login.presenter.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginContract.View, View.OnClickListener {

    private final String TAG = "Login";

    private final int TYPE_TEXT_PASSWORD = 1;
    private final int TYPE_NUMBER = 2;

    private EditText phone;
    private WithBtnEditText password;
    private TextView codeLogin;
    private TextView forget;
    private Button login, register, wechat, qq;
    private Dialog mDialog;
    private LoginContract.Presenter mPresenter;
    private LoginPresenter loginPresenter;

    private int clickCount = 0;

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
        wechat = findViewById(R.id.btn_login_wechat);
        qq = findViewById(R.id.btn_login_qq);

        checkCodeLoginClick();



        mDialog = new ProgressDialog(this);
        mDialog.setTitle("正在登录中");
        login.setOnClickListener(this);

        //mvp模块拼装
        LoginTask loginTask = LoginTask.getInstance();
        loginPresenter = new LoginPresenter(this, loginTask);
        this.setPresenter(loginPresenter);
    }

    private void checkCodeLoginClick() {
        codeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCount += 1;
                password.getText().clear();
                //若为奇数 则显示btn
                if (clickCount % 2 != 0) {
                    setPasswordClickable(true);
                } else {
                    setPasswordClickable(false);
                }
            }
        });
    }

    private void setPasswordClickable(boolean clickable) {
        if (clickable) {
            password.setInputType(TYPE_NUMBER);
            password.setBtnIsVisible(true);
            password.setBtnClickStatus(true);
            password.setHint("请输入验证码");
            password.setBtnText("发送验证码");
            password.setBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!phone.getText().toString().isEmpty()) {
                        //TODO 联网操作
                        Log.e(TAG, "send code");
                        //进行倒数 且必须禁止返回到输入密码的状态
                        codeLogin.setEnabled(false);
                        codeLogin.setClickable(false);
                        codeLogin.setAlpha(0.5f);
                        startCountDown();
                    } else {
                        Snackbar.make(login, "请先输入手机号", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            password.setInputType(TYPE_TEXT_PASSWORD);
            password.setBtnIsVisible(true);
            password.setBtnClickStatus(false);
            password.setHint("请输入密码");
        }
    }

    private void startCountDown() {
        CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                password.setBtnClickStatus(false);
                password.setBtnText(l / 1000 + "S");
            }

            @Override
            public void onFinish() {
                password.setBtnClickStatus(true);
                password.setBtnText("重新发送");
                codeLogin.setEnabled(true);
                codeLogin.setClickable(true);
                codeLogin.setAlpha(1.0f);
            }
        }.start();
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
                if (phone.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Snackbar.make(login, "请输入手机或密码", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                mPresenter.getLoginInfo(new LoginModule
                        (phone.getText().toString(), password.getText().toString()));
                break;
        }
    }

}
