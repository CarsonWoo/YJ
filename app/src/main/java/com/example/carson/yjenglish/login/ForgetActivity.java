package com.example.carson.yjenglish.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.checkcode.view.CodeActivity;
import com.example.carson.yjenglish.customviews.PasswordEditText;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {

    //判断从哪里跳转来
    private final int INTENT_FROM_CODE = 1;
    private final int INTENT_FROM_LOGIN = 0;

    private ImageView back;
    private PasswordEditText phone;
    private Button confirm;

    private int mIntentType;//默认为从login来

    private int mClickCount = 0;//密码显示计数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        initViews();

    }

    private void initViews() {

        back = findViewById(R.id.forget_back);
        phone = findViewById(R.id.edit_phone);
        confirm = findViewById(R.id.btn_confirm);

        confirm.setOnClickListener(this);
        back.setOnClickListener(this);

        mIntentType = getIntent().getIntExtra("type", INTENT_FROM_LOGIN);
        if (mIntentType == INTENT_FROM_CODE) {
            initPasswordView();
        } else {
            initPhoneView();
        }

    }

    private void initPhoneView() {
        mClickCount = 0;
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        phone.getText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        phone.setStartDrawable(getResources().getDrawable(R.drawable.ic_phone));
        phone.setHint("请输入手机号码");
        phone.setShowVisible(false);
    }

    private void initPasswordView() {
        phone.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        phone.getText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        phone.setStartDrawable(getResources().getDrawable(R.drawable.ic_password));
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
                        //TODO 进行验证 跳转 并传输手机号码的信息
                        Intent toCode = new Intent(ForgetActivity.this,
                                CodeActivity.class);
                        toCode.putExtra("phone", phone.getText().toString());
                        toCode.putExtra("type", 2);
                        startActivityForResult(toCode, 2);
                        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
                    } else {
                        Toast.makeText(getApplicationContext(), "请检查手机号码是否填写正确", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (checkEnable()) {
                        //TODO 上传服务器新密码 并进行跳转到登录界面
                        //假设成功 跳转回登录页面
                        setResult(RESULT_OK);
                        onBackPressed();
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

    private boolean checkEnable() {
        if (TextUtils.isEmpty(phone.getText())) {
            return false;
        } else {
            if (mIntentType == INTENT_FROM_LOGIN && phone.getText().toString().length() == 11) {
                return true;
            } else if (mIntentType == INTENT_FROM_CODE) {
                return true;
            }
            return false;
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
            onBackPressed();
        }
    }
}
