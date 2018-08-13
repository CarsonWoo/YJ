package com.example.carson.yjenglish.register.view;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.checkcode.view.CodeActivity;
import com.example.carson.yjenglish.customviews.PasswordEditText;
import com.example.carson.yjenglish.login.view.LoginActivity;
import com.example.carson.yjenglish.register.RegisterContract;
import com.example.carson.yjenglish.register.RegisterTask;
import com.example.carson.yjenglish.register.model.RegisterInfo;
import com.example.carson.yjenglish.register.model.RegisterModel;
import com.example.carson.yjenglish.register.presenter.RegisterPresenter;
import com.jude.swipbackhelper.SwipeBackHelper;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,
        RegisterContract.View {

    private final int RESULT_REGISTER_OK = 101;

    private TextView back;
    private EditText phone;
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
        confirm = findViewById(R.id.btn_confirm);

        mDialog = new ProgressDialog(this);
        mDialog.setTitle("正在注册中");

        back.setOnClickListener(this);
        confirm.setOnClickListener(this);

        phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)}); //最大输入长度

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
                if (isExistLoginActivity(LoginActivity.class)) {
                    onBackPressed();
                } else {
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                    finish();
                }
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(phone.getText())) {
                    Toast.makeText(getApplicationContext(), "请先输入您的注册信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent toCode = new Intent(RegisterActivity.this, CodeActivity.class);
                toCode.putExtra("type", 1);
                toCode.putExtra("phone", phone.getText().toString());
                startActivityForResult(toCode, 1);
                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                break;
            default:
                break;
        }
    }

    //判断登录页面是否在活动栈中
    private boolean isExistLoginActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        ComponentName cmpName = intent.resolveActivity(getPackageManager());
        boolean flag = false;
        if (cmpName != null) {
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfos = am.getRunningTasks(3);
            for (ActivityManager.RunningTaskInfo info : taskInfos) {
                if (info.baseActivity.equals(cmpName)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
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
            //TODO 进行跳转到首页 还需要缓存Token
            //onBackPressed();
        }
    }

    @Override
    public void hideLoading() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_REGISTER_OK) {
            if (data != null) {
                Log.e("Register", "phone = " + phone.getText());
                Log.e("Register", "password = " + data.getStringExtra("password"));
                Log.e("Register", "code = " + data.getIntExtra("code", 0));
//                confirm.setEnabled(false);
//                RegisterTask task = RegisterTask.getInstance();
//                registerPresenter = new RegisterPresenter(task, RegisterActivity.this);
//                this.setPresenter(registerPresenter);
//                mPresenter.getRegisterResponse(new RegisterModel(phone.getText().toString(),
//                        data.getStringExtra("password"), data.getIntExtra("code", 0)));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registerPresenter != null) {
            registerPresenter.unsubscribe();
        }
    }
}
