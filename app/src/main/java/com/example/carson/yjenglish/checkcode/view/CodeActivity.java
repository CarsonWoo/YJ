package com.example.carson.yjenglish.checkcode.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.customviews.CodeView;
import com.example.carson.yjenglish.login.ForgetActivity;

public class CodeActivity extends AppCompatActivity implements CodeView.OnInputFinishListener,
        View.OnClickListener {

    //总共有3个入口 登录、注册、忘记密码
    private final int INTENT_TYPE_LOGIN = 0;
    private final int INTENT_TYPE_REGISTER = 1;
    private final int INTENT_TYPE_FORGET = 2;

    private String phone;
    private int mIntentType = 0;

    private TextView tvPhone;
    private TextView reload;
    private CodeView mCodeView;
    private ImageView back;


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

    }

    @Override
    public void onFinish(String code) {
        Log.e("Code", "success : " + code);
        //TODO 进行联网操作 若失败直接置空
        switch (mIntentType) {
            case INTENT_TYPE_LOGIN:
                doLoginTask(code);
                break;
            case INTENT_TYPE_REGISTER:
                doRegisterTask(code);
                break;
            case INTENT_TYPE_FORGET:
                doResetTask(code);
                break;
            default:
                break;
        }
    }

    private void doResetTask(String code) {
        //验证code url
        //假设成功了
        Intent toReset = new Intent(this, ForgetActivity.class);
        toReset.putExtra("type", 1);
        startActivityForResult(toReset, 1);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    private void doRegisterTask(String code) {

    }

    private void doLoginTask(String code) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.code_back) {
            onBackPressed();
        } else if (view.getId() == R.id.reload_code) {
            mCodeView.setText("");
            //TODO 再次进行联网操作
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
                setResult(RESULT_OK);
                onBackPressed();
            }
        }
    }
}
