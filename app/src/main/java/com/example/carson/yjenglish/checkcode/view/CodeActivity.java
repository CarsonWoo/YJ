package com.example.carson.yjenglish.checkcode.view;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.checkcode.CodeContract;
import com.example.carson.yjenglish.checkcode.CodeTask;
import com.example.carson.yjenglish.checkcode.SmsContent;
import com.example.carson.yjenglish.checkcode.presenter.CodePresenter;
import com.example.carson.yjenglish.customviews.CodeView;
import com.example.carson.yjenglish.login.view.ForgetActivity;

public class CodeActivity extends AppCompatActivity implements CodeView.OnInputFinishListener,
        View.OnClickListener, SmsContent.OnReadFinishListener, CodeContract.View {

    //总共有3个入口 登录、注册、忘记密码
    private final int INTENT_TYPE_LOGIN = 0;
    private final int INTENT_TYPE_REGISTER = 1;
    private final int INTENT_TYPE_FORGET = 2;

    private final int RESULT_LOGIN_OK = 100;
    private final int RESULT_REGISTER_OK = 101;

    private String phone;
    private String code;
    private int mIntentType = 0;

    private TextView tvPhone;
    private TextView reload;
    private CodeView mCodeView;
    private ImageView back;

    private Dialog mDialog;

    private CodePresenter codePresenter;
    private CodeContract.Presenter presenter;

    private SmsContent mContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        initViews();

    }

    private void initViews() {
        phone = getIntent().getStringExtra("phone");
        mIntentType = getIntent().getIntExtra("type", INTENT_TYPE_LOGIN);

        tvPhone = findViewById(R.id.tv_phone);
        mCodeView = findViewById(R.id.code_view);
        reload = findViewById(R.id.reload_code);
        back = findViewById(R.id.code_back);

        tvPhone.setText(phone);
        mCodeView.setmInputListener(this);
        back.setOnClickListener(this);
        reload.setOnClickListener(this);

        sendCode();

        initContent();

    }

    private void sendCode() {

        if (mIntentType == INTENT_TYPE_LOGIN || mIntentType == INTENT_TYPE_FORGET) {
            //从登录或者忘记密码来时访问一个接口
            CodeTask codeTask = CodeTask.getInstance(0);
            codePresenter = new CodePresenter(codeTask, this);
            this.setPresenter(codePresenter);
            presenter.getResponse(phone);
        } else {
            //从注册来
            CodeTask codeTask = CodeTask.getInstance(1);
            codePresenter = new CodePresenter(codeTask, this);
            this.setPresenter(codePresenter);
            presenter.getResponse(phone);
        }
    }

    private void initContent() {
        if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_SMS},
                    1);
        }
//        doReadAction();
    }

    //当验证码四个框都输入完成后
    @Override
    public void onFinish(String code) {
        Log.e("Code", "success : " + code);
        this.code = code;
        switch (mIntentType) {
            case INTENT_TYPE_LOGIN:
                doLoginTask();
                break;
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
        Intent toReset = new Intent(this, ForgetActivity.class);
        toReset.putExtra("type", 1);
        toReset.putExtra("phone", phone);
        toReset.putExtra("code", Integer.parseInt(code));
        startActivityForResult(toReset, 1);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    private void doRegisterTask() {
        Intent backIntent = new Intent();
        backIntent.putExtra("code", Integer.parseInt(code));
        setResult(RESULT_REGISTER_OK, backIntent);
        onBackPressed();
    }

    private void doLoginTask() {
        Intent backIntent = new Intent();
        backIntent.putExtra("code", Integer.parseInt(code));
        setResult(RESULT_LOGIN_OK, backIntent);
        onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.code_back) {
            onBackPressed();
        } else if (view.getId() == R.id.reload_code) {
            mCodeView.setText("");
            sendCode();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mIntentType == INTENT_TYPE_FORGET) {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                if (data != null) {
                    setResult(RESULT_OK, data);
                    onBackPressed();
                }
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
        Log.e("CodeAty", smsMsg);
        //进行setText()
    }

    @Override
    public void setPresenter(CodeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showCodeLoading() {
        //show dialog
        reload.setEnabled(false);
        reload.setClickable(false);
        reload.setTextColor(Color.LTGRAY);
    }

    @Override
    public void hideCodeLoading() {
        //hide dialog
        reload.setEnabled(true);
        reload.setClickable(true);
        reload.setTextColor(Color.parseColor("#d1ff3939"));
    }

    @Override
    public void getCode(int code) {
        if (code == 200) {
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
            doReadAction();
        } else {
            reload.setEnabled(true);
            reload.setClickable(true);
            reload.setTextColor(Color.parseColor("#d1ff3939"));
        }
    }

    @Override
    public void showCodeError(String msg) {
        Log.e("CodeAty", msg);
        reload.setEnabled(true);
        reload.setClickable(true);
        reload.setTextColor(Color.parseColor("#d1ff3939"));
    }
}
