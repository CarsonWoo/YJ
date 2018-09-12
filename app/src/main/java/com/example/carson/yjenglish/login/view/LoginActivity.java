package com.example.carson.yjenglish.login.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.HomeActivity;
import com.example.carson.yjenglish.MyApplication;
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
import com.example.carson.yjenglish.utils.AES;
import com.example.carson.yjenglish.utils.UserConfig;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class LoginActivity extends AppCompatActivity implements LoginContract.View,
        View.OnClickListener {

    private final String TAG = "Login";
    private static final String QQ_APP_ID = "1107820868";
    private Tencent mTencent;
    private QQUIListener mQQListener;
    private UserInfo mQQUserInfo;

    private final int TYPE_SHOW_PASSWORD = InputType.TYPE_CLASS_TEXT;
    private final int TYPE_HIDE_PASSWORD = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
    private final int RESULT_REGISTER_OK = 100;
    private final int RESULT_FORGET_OK = 102;

    private EditText phone;
    private PasswordEditText password;
    private TextView forget;
    private Button login;
    private TextView register;
    private TextView testUse;
    private ImageButton btnQQ;
    private ImageButton btnWechat;

    private Dialog mDialog;
    private LoginContract.Presenter mPresenter;
    private LoginPresenter loginPresenter;

    private int isShowPassword = 0;//计数是否显示password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTencent = Tencent.createInstance(QQ_APP_ID, MyApplication.getContext());
        iniViews();
        if (getIntent() != null) {
            String phoneStr = getIntent().getStringExtra("phone");
            String passwordStr = getIntent().getStringExtra("password");
            if (!TextUtils.isEmpty(phoneStr) && phoneStr.length() > 0) {
                phone.setText(phoneStr);
            }
            if (!TextUtils.isEmpty(passwordStr) && passwordStr.length() > 0) {
                password.setText(passwordStr);
            }
        }
    }

    private void iniViews() {
        phone = findViewById(R.id.edit_phone);
        password = findViewById(R.id.edit_password);
        forget = findViewById(R.id.tv_forget_password);
        login = findViewById(R.id.btn_login);
        register = findViewById(R.id.btn_register);
        testUse = findViewById(R.id.test_use);
        btnQQ = findViewById(R.id.btn_qq);
        btnWechat = findViewById(R.id.btn_wechat);

        phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)}); //最大输入长度
        password.getText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)}); //最大密码输入长度

        checkShowPassword();

        mDialog = new ProgressDialog(this);
        mDialog.setTitle("正在登录中");
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget.setOnClickListener(this);
        testUse.setOnClickListener(this);
        btnQQ.setOnClickListener(this);
        btnWechat.setOnClickListener(this);
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
        if (info != null) {
            String status = info.getStatus();
            String msg = info.getMsg();
            String data = info.getData();
            Log.e(TAG, status);
            Log.e(TAG, msg);
            if (status.equals("200")) {
                Log.e(TAG, data);
                UserConfig.cacheToken(this, data);
                UserConfig.cachePhone(this, phone.getText().toString());
                UserConfig.cacheIsFirstTimeUser(this, false);
                Intent toHome = new Intent(this, HomeActivity.class);
                startActivity(toHome);
                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                finishAfterTransition();
            }
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
                if (phone.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "请先输入手机或密码噢", Toast.LENGTH_SHORT).show();
                    return;
                }
                //mvp模块拼装
                LoginTask loginTask = LoginTask.getInstance();
                loginPresenter = new LoginPresenter(LoginActivity.this, loginTask);
                this.setPresenter(loginPresenter);
                try {
                    String phoneStr = AES.Encrypt(phone.getText().toString(), AES.sKey);
                    String passStr = AES.Encrypt(password.getText().toString(), AES.sKey);
                    Log.e(TAG, phoneStr);
                    Log.e(TAG, passStr);
                    mPresenter.getLoginInfo(new LoginModule(phoneStr.trim(), passStr.trim()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_register:
                Intent toRegis = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(toRegis, 3);
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
            case R.id.btn_qq:
                executeQQLogin();
                break;
            case R.id.btn_wechat:
                break;
            default:
                break;
        }
    }

    private void executeQQLogin() {
        mQQListener = new QQUIListener();
        mTencent.login(LoginActivity.this, "all", mQQListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mQQListener);
        } else if (requestCode == 3 && resultCode == RESULT_REGISTER_OK) {
            if (data != null) {
                phone.setText(data.getStringExtra("phone"));
                password.setText(data.getStringExtra("password"));
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

    private class QQUIListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Log.e(TAG, "授权QQ成功");
            Log.e(TAG, "response = " + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mQQUserInfo = new UserInfo(MyApplication.getContext(), qqToken);
                mQQUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Log.e(TAG, "登陆成功， response = " + o);
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, uiError.errorMessage);
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "cancel");
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Log.e(TAG, "fail : " + uiError.errorMessage);
        }

        @Override
        public void onCancel() {
            Log.e(TAG, "授权取消");
        }
    }
}
