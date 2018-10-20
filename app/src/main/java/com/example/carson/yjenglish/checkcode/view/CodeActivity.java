package com.example.carson.yjenglish.checkcode.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.checkcode.CodeContract;
import com.example.carson.yjenglish.checkcode.CodeTask;
import com.example.carson.yjenglish.checkcode.SmsContent;
import com.example.carson.yjenglish.checkcode.model.RegisterCodeBean;
import com.example.carson.yjenglish.checkcode.presenter.CodePresenter;
import com.example.carson.yjenglish.customviews.CodeView;
import com.example.carson.yjenglish.login.model.ForgetInfo;
import com.example.carson.yjenglish.login.view.ForgetActivity;
import com.example.carson.yjenglish.net.NullOnEmptyConverterFactory;
import com.example.carson.yjenglish.register.RegisterService;
import com.example.carson.yjenglish.register.model.RegisterInfo;
import com.example.carson.yjenglish.register.view.SetupPasswordActivity;
import com.example.carson.yjenglish.utils.AddCookiesInterceptor;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.SaveCookiesInterceptor;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CodeActivity extends AppCompatActivity implements CodeView.OnInputFinishListener,
        View.OnClickListener, SmsContent.OnReadFinishListener, CodeContract.View {

    //总共有2个入口 注册、忘记密码
    private final int INTENT_TYPE_REGISTER = 1;
    private final int INTENT_TYPE_FORGET = 0;
    private final int RESULT_REGISTER_OK = 101;

    private String phone;
    private String code;
    private int mIntentType = 0;

    private TextView tvPhone;
    private TextView reload;
    private CodeView mCodeView;
    private ImageView back;
    private TextView countDown;
    private TextView tv1;

    private CountDownTimer timer;

    private Dialog mDialog;

    private CodePresenter codePresenter;
    private CodeContract.Presenter presenter;

    private SmsContent mContent;

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
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        setContentView(R.layout.activity_code);

        initViews();

    }

    private void initViews() {
        phone = getIntent().getStringExtra("phone");
        mIntentType = getIntent().getIntExtra("type", INTENT_TYPE_FORGET);
        if (mIntentType == INTENT_TYPE_REGISTER) {
            token = getIntent().getStringExtra("register_token");
        } else {
            token = getIntent().getStringExtra("forget_token");
        }

        tvPhone = findViewById(R.id.tv_phone);
        mCodeView = findViewById(R.id.code_view);
        reload = findViewById(R.id.reload_code);
        back = findViewById(R.id.code_back);
        countDown = findViewById(R.id.count_down_code);
        tv1 = findViewById(R.id.tv1);

        //隐藏掉重新发送验证码 直接进入倒数
        reload.setVisibility(View.INVISIBLE);
        reload.setEnabled(false);
        tv1.setVisibility(View.INVISIBLE);


        tvPhone.setText(phone);
        mCodeView.setmInputListener(this);
        back.setOnClickListener(this);
        reload.setOnClickListener(this);

//        sendCode();
        startCountDown();
//        doReadAction();

        initContent();

    }

    private void initContent() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_SMS},
                        1);
            }
        }
        doReadAction();
    }

    //当验证码四个框都输入完成后
    @Override
    public void onFinish(String code) {
//        Log.e("Code", "success : " + code);
        this.code = code;
        if (timer != null) {
            timer.onFinish();
            timer.cancel();
        }
        switch (mIntentType) {
            case INTENT_TYPE_REGISTER:
                doRegisterTask();
                break;
            case INTENT_TYPE_FORGET:
                doResetTask();
                break;
            default:
                break;
        }
    }

    private void doResetTask() {
        //成功则跳转
        Request request = new Request.Builder().url(UserConfig.HOST + "user/forget_password_b.do")
                .post(new FormBody.Builder().add("forget_password_token", token)
                .add("phone_code", code).build()).build();
//        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.cookieJar(cookieJar);
        builder.interceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                String cookie = MyApplication.getContext().getSharedPreferences("cookies_prefs", Context.MODE_PRIVATE)
                        .getString(UserConfig.HOST + "user/forget_password_a.do", "");
                if (!TextUtils.isEmpty(cookie)) {
                    return chain.proceed(chain.request().newBuilder().header("Cookie", cookie).build());
                }
                return chain.proceed(chain.request());
            }
        });
        builder.addInterceptor(new SaveCookiesInterceptor());
        builder.build().newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
//                Log.e("Code", e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Gson gson = new Gson();
                CommonInfo info = gson.fromJson(response.body().string(), CommonInfo.class);
                if (info.getStatus().equals("200")) {
                    Intent toReset = new Intent(CodeActivity.this, ForgetActivity.class);
                    toReset.putExtra("type", 1);
                    toReset.putExtra("forget_token", token);
//                    toReset.putExtra("phone", phone);
//                    toReset.putExtra("code", Integer.parseInt(code));
                    startActivityForResult(toReset, 1);
                    overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                } else {
//                    Log.e("Code", info.getMsg());
                }
            }
        });
//        NetUtils.getClientInstance(this)

    }

    private void doRegisterTask() {
        CodeTask codeTask = CodeTask.getInstance();
        codePresenter = new CodePresenter(codeTask, this);
        this.setPresenter(codePresenter);
        presenter.getResponse(new RegisterCodeBean(token, code));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.code_back) {
            onBackPressed();
        } else if (view.getId() == R.id.reload_code) {
            reload.setVisibility(View.INVISIBLE);
            reload.setEnabled(false);
            tv1.setVisibility(View.INVISIBLE);
            mCodeView.setText("");
//            sendCode();
            //需要做的是setResult() 使其返回上一级重发验证码
            executeResendTask();
        }
    }

    private void executeResendTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        if (mIntentType == INTENT_TYPE_REGISTER) {
            RegisterService registerService = retrofit.create(RegisterService.class);
            registerService.getResendResponse("sendCode", phone).enqueue(new Callback<RegisterInfo>() {
                @Override
                public void onResponse(Call<RegisterInfo> call, Response<RegisterInfo> response) {
                    RegisterInfo info = response.body();
                    if (info.getStatus().equals("200")) {
                        token = info.getData().getRegister_token();
                    } else {
//                        Log.e("Code", info.getMsg());
                    }
                }

                @Override
                public void onFailure(Call<RegisterInfo> call, Throwable t) {
//                    Log.e("Code", t.getMessage());
                }
            });
        } else {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.addInterceptor(new AddCookiesInterceptor());
            builder.addInterceptor(new SaveCookiesInterceptor());
            Request request = new Request.Builder().url(UserConfig.HOST + "user/forget_password_a.do")
                    .post(new FormBody.Builder().add("token", "forgetPassword")
                            .add("phone", phone).build()).build();
            builder.build().newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
//                    Log.e("Code", e.getMessage());
                }

                @Override
                public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                    Gson gson = new Gson();
                    ForgetInfo info = gson.fromJson(response.body().string(), ForgetInfo.class);
                    if (info.getStatus().equals("200")) {
                        token = info.getData().getForget_password_token();
                    } else {
//                        Log.e("Code", info.getMsg());
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.e("Code", "intent type = " + mIntentType);
        if (mIntentType == INTENT_TYPE_FORGET) {
//            Log.e("Code", "requestCode = " + requestCode + " resultCode = " + resultCode);
            if (requestCode == 1 && resultCode == RESULT_OK) {
//                Log.e("Code", "in");
                if (data != null) {
                    setResult(RESULT_OK, data);
                }
                finishAfterTransition();
            }
        } else {
            if (requestCode == 2 && resultCode == RESULT_OK) {
                if (data != null) {
                    setResult(RESULT_REGISTER_OK, data);
//                    Log.e("Code", data.getStringExtra("password"));
                }
//                onBackPressed();
//                Log.e("Code", "onBackPressed");
                finishAfterTransition();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(CodeActivity.this, "请手动输入验证码噢..", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void doReadAction() {
        mContent = SmsContent.getInstance(new Handler(), this, this);
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"),
                true, mContent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mContent != null) {
            this.getContentResolver().unregisterContentObserver(mContent);
        }
        if (codePresenter != null) {
            codePresenter.unsubscribe();
        }
    }

    @Override
    public void onReadFinish(String smsMsg) {
//        Log.e("CodeAty", smsMsg);
        //进行setText()
    }

    @Override
    public void setPresenter(CodeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showCodeLoading() {
        //show dialog
    }

    @Override
    public void hideCodeLoading() {
        //hide dialog
    }

    @Override
    public void getCode(CommonInfo info) {
        if (info.getStatus().equals("200")) {
            if (mIntentType == INTENT_TYPE_REGISTER) {
                Intent toSetPwd = new Intent(this, SetupPasswordActivity.class);
                toSetPwd.putExtra("register_token", token);
//            startActivityForResult(toSetPwd, 2);
                startActivityForResult(toSetPwd, 2);
                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
            }

        } else {
//            Log.e("Code", info.getStatus());
//            Log.e("Code", info.getMsg());
            Toast.makeText(getApplicationContext(), info.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    private void startCountDown() {
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                countDown.setVisibility(View.VISIBLE);
                countDown.setText(l / 1000 + "s后重新获取");
            }

            @Override
            public void onFinish() {
                countDown.setVisibility(View.INVISIBLE);
                reload.setVisibility(View.VISIBLE);
                reload.setEnabled(true);
                tv1.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @Override
    public void showCodeError(String msg) {
//        Log.e("CodeAty", msg);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.e("Code", "onPause");
        timer.cancel();
    }
}
