package com.example.carson.yjenglish.register.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.checkcode.model.RegisterCodeBean;
import com.example.carson.yjenglish.customviews.PasswordEditText;
import com.example.carson.yjenglish.login.view.LoginActivity;
import com.example.carson.yjenglish.register.SetupContract;
import com.example.carson.yjenglish.register.SetupTask;
import com.example.carson.yjenglish.register.presenter.SetupPresenter;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;

public class SetupPasswordActivity extends AppCompatActivity implements SetupContract.View {

    private ImageView back;
    private TextView title;
    private PasswordEditText edit;
    private Button confirm;

    private SetupContract.Presenter mPresenter;
    private SetupPresenter presenter;

    private String registerToken;
    private String password;

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
        setContentView(R.layout.activity_setup_password);
        Intent intent = getIntent();
        registerToken = intent.getStringExtra("register_token");



        initViews();

    }

    private void initViews() {
        back = findViewById(R.id.back);
        title = findViewById(R.id.toolbar_title);
        edit = findViewById(R.id.edit_password);
        confirm = findViewById(R.id.btn_confirm);

        edit.setHint("请输入密码");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doConfirmWork();
            }
        });


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        edit.requestFocus();
        if (imm != null) {
            imm.showSoftInput(edit, 0);
        }

    }

    private void doConfirmWork() {
        if (TextUtils.isEmpty(edit.getText())) {
            Toast.makeText(this, "请先输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edit.getText().toString().length() < 6) {
            Toast.makeText(this, "密码设置太过简单, 请重新设置", Toast.LENGTH_SHORT).show();
            return;
        }
        //组装mvp
        SetupTask task = SetupTask.getInstance();
        presenter = new SetupPresenter(task, this);
        this.setPresenter(presenter);
        password = edit.getText().toString();
        mPresenter.getSetupResponse(new RegisterCodeBean(registerToken, password));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    public void setPresenter(SetupContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void getResponse(CommonInfo info) {
        String status = info.getStatus();
        String msg = info.getMsg();
        if (status.equals("200")) {
            //跳转HomeActivity并且缓存token及密码
            Intent backIntent = new Intent();
            backIntent.putExtra("password", password);
            Log.e("Setup", password);
            setResult(RESULT_OK, backIntent);
            onBackPressed();
        } else {
//            Log.e("Setup", msg);
        }
    }

    @Override
    public void showError(String msg) {
//        Log.e("Setup", msg);
        if (msg != null && !msg.isEmpty()) {
            Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
