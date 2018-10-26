package com.example.carson.yjenglish.zone.view.setting;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.tencent.bugly.beta.Beta;

public class AboutAty extends AppCompatActivity {

    private ImageView back;
    private TextView versionCode;
    private Button btnCheck;

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
        setContentView(R.layout.activity_about);
        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);
        versionCode = findViewById(R.id.version_code);
        btnCheck = findViewById(R.id.btn_check_update);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        versionCode.setText("版本号：" + MyApplication.getVersionCode(this));

        btnCheck.setOnClickListener(v -> {
            //检查更新
            Beta.checkUpgrade();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
