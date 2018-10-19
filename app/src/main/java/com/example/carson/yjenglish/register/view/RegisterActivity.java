package com.example.carson.yjenglish.register.view;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.checkcode.view.CodeActivity;
import com.example.carson.yjenglish.login.view.LoginActivity;
import com.example.carson.yjenglish.register.RegisterContract;
import com.example.carson.yjenglish.register.RegisterTask;
import com.example.carson.yjenglish.register.model.RegisterInfo;
import com.example.carson.yjenglish.register.model.RegisterModel;
import com.example.carson.yjenglish.register.presenter.RegisterPresenter;
import com.example.carson.yjenglish.utils.AES;
import com.example.carson.yjenglish.utils.StatusBarUtil;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        if (StatusBarUtil.checkDeviceHasNavigationBar(this)) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.register_back);
        phone = findViewById(R.id.edit_phone);
        confirm = findViewById(R.id.btn_confirm);

        //注册的提示框
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
                    finishAfterTransition();
                }
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(phone.getText())) {
                    Toast.makeText(getApplicationContext(), "请先输入您的注册信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                //网络访问注册接口
                RegisterTask task = RegisterTask.getInstance();
                registerPresenter = new RegisterPresenter(task, this);
                this.setPresenter(registerPresenter);
                mPresenter.getRegisterResponse(new RegisterModel("sendCode",
                        phone.getText().toString()));
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
            String status = info.getStatus();
            String msg = info.getMsg();
            RegisterInfo.Data data = info.getData();
            if (status.equals("200")) {
                Intent toCode = new Intent(RegisterActivity.this, CodeActivity.class);
                toCode.putExtra("type", 1);
                toCode.putExtra("phone", phone.getText().toString());
                toCode.putExtra("register_token", data.getRegister_token());
                Log.e("Register", data.getRegister_token());
                startActivityForResult(toCode, 1);
                overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
            } else {
                Log.e("Register", status);
                Log.e("Register", msg);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
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
                data.putExtra("phone", phone.getText().toString());
                if (isExistLoginActivity(LoginActivity.class)) {
                    setResult(100, data);
//                    onBackPressed();
                    finishAfterTransition();
                } else {
                    //跳转到登录页面并自动填写账号和密码
                    Intent toLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                    toLogin.putExtra("phone", phone.getText().toString());
                    toLogin.putExtra("password", data.getStringExtra("password"));
                    startActivity(toLogin);
                    overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                    finishAfterTransition();
                }
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
