package com.example.carson.yjenglish.login.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.checkcode.view.CodeActivity;
import com.example.carson.yjenglish.customviews.PasswordEditText;
import com.example.carson.yjenglish.login.ForgetContract;
import com.example.carson.yjenglish.login.ForgetTask;
import com.example.carson.yjenglish.login.model.ForgetInfo;
import com.example.carson.yjenglish.login.model.ForgetModel;
import com.example.carson.yjenglish.login.presenter.ForgetPresenter;
import com.example.carson.yjenglish.utils.AddCookiesInterceptor;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.SaveCookiesInterceptor;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener,
        ForgetContract.View {

    //判断从哪里跳转来
    private final int INTENT_FROM_CODE_FORGET = 1;
    private final int INTENT_FROM_LOGIN = 0;

    private final int RESULT_FORGET_OK = 102;

    private ImageView back;
    private PasswordEditText phone;
    private Button confirm;

    private TextView toolbarTitle;

    private String phoneStr;

    private Dialog mDialog;

    private ForgetContract.Presenter mPresenter;
    private ForgetPresenter forgetPresenter;

    private int mIntentType;//默认为从login来

    private int mClickCount = 0;//密码显示计数

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        if (StatusBarUtil.checkDeviceHasNavigationBar(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        setContentView(R.layout.activity_forget);

        initViews();

    }

    private void initViews() {

        back = findViewById(R.id.forget_back);
        phone = findViewById(R.id.edit_phone);
        confirm = findViewById(R.id.btn_confirm);
        toolbarTitle = findViewById(R.id.toolbar_title);

        confirm.setOnClickListener(this);
        back.setOnClickListener(this);

        mIntentType = getIntent().getIntExtra("type", INTENT_FROM_LOGIN);

        toolbarTitle.setText(R.string.find_password);

        if (mIntentType == INTENT_FROM_CODE_FORGET) {
            initPasswordView();
        } else {
            initPhoneView();
        }

    }

    private void initPhoneView() {
        mClickCount = 0;
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        phone.getText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        phone.setStartDrawable(getResources().getDrawable(R.drawable.ic_phone_20dp));
        phone.setHint("请输入手机号码");
        phone.setShowVisible(false);
    }

    private void initPasswordView() {
        phoneStr = getIntent().getStringExtra("phone");
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("正在上传中");

        phone.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        phone.getText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        phone.setStartDrawable(getResources().getDrawable(R.drawable.ic_password_20dp));
        phone.setHint("请输入密码");
        phone.setShowVisible(true);
        phone.setShowClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickCount ++;
                if (mClickCount % 2 != 0) {
                    phone.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    phone.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                if (mIntentType == INTENT_FROM_LOGIN) {
                    if (checkEnable()) {
                        sendCode();
                    } else {
                        Toast.makeText(getApplicationContext(), "请检查手机号码是否填写正确", Toast.LENGTH_SHORT).show();
                    }
                } else if (mIntentType == INTENT_FROM_CODE_FORGET) {
                    if (checkEnable()) {
                        executeTask();
                    } else {
                        Toast.makeText(getApplicationContext(), "请先填写密码", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.forget_back:
                onBackPressed();
                break;
        }
    }

    //发送验证码
    private void sendCode() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.addInterceptor(new AddCookiesInterceptor());
        builder.addInterceptor(new SaveCookiesInterceptor());
        Request request = new Request.Builder().url(UserConfig.HOST + "user/forget_password_a.do")
                .post(new FormBody.Builder().add("token", "forgetPassword")
                .add("phone", phone.getText().toString()).build()).build();
        builder.build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Forget", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                ForgetInfo info = gson.fromJson(response.body().string(), ForgetInfo.class);
                Log.e("Forget", info.getStatus());
                Log.e("Forget", info.getMsg());
                if (info.getData() != null) {
                    token = info.getData().getForget_password_token();
                }
                if (info.getStatus().equals("200")) {
                    Intent toCode = new Intent(ForgetActivity.this,
                            CodeActivity.class);
                    toCode.putExtra("phone", phone.getText().toString());
                    toCode.putExtra("forget_token", info.getData().getForget_password_token());
                    toCode.putExtra("type", 0);
                    startActivityForResult(toCode, 2);
                    overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                } else {
                    //提示用户信息
                    Toast.makeText(ForgetActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void executeTask() {
        ForgetTask task = ForgetTask.getInstance();
        forgetPresenter = new ForgetPresenter(task, this);
        this.setPresenter(forgetPresenter);
        String mToken = getIntent().getStringExtra("forget_token");
        Log.e("Forget", mToken);
        mPresenter.getCommonInfo(new ForgetModel(mToken, phone.getText().toString()));
    }

    private boolean checkEnable() {
        if (TextUtils.isEmpty(phone.getText())) {
            return false;
        } else {
            if (mIntentType == INTENT_FROM_LOGIN && phone.getText().toString().length() == 11) {
                //填写手机号
                return true;
            } else if (mIntentType == INTENT_FROM_CODE_FORGET && phone.getText().toString().length() >= 6) {
                //设置密码
                return true;
            } else {
                return false;
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data != null) {
                data.putExtra("phone", phone.getText().toString());
                setResult(RESULT_FORGET_OK, data);
                finishAfterTransition();
                overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
            }

        }
    }

    @Override
    public void setPresenter(ForgetContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        mDialog.dismiss();
    }

    @Override
    public void showError(String msg) {
        Log.e("ForgetAty", msg);
    }

    @Override
    public void setCommonInfo(CommonInfo commonInfo) {
        if (commonInfo != null) {
            String checkCode = commonInfo.getStatus();
            if (checkCode.equals("200")) {
                //成功 跳转回登录页面
                Intent backIntent = new Intent();
                backIntent.putExtra("password", phone.getText().toString());
                setResult(RESULT_OK, backIntent);
                onBackPressed();
            } else {
                Log.e("Forget", commonInfo.getMsg());

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (forgetPresenter != null) {
            forgetPresenter.unsubscribe();
        }
    }
}
