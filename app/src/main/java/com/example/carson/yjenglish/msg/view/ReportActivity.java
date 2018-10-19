package com.example.carson.yjenglish.msg.view;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.msg.MsgService;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private TextView meaningLess;
    private ImageView meaningLessButton;
    private TextView spite;
    private ImageView spiteButton;
    private TextView yellow;
    private ImageView yellowButton;
    private TextView ad;
    private ImageView adButton;
    private TextView political;
    private ImageView politicalButton;
    private EditText editText;
    private Button btnSubmit;

    private InputMethodManager imm;

    private Map<Object, Boolean> mCheckMap = new HashMap<>();

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
        setContentView(R.layout.activity_report);

        bindViews();
    }

    private void bindViews() {
        back = findViewById(R.id.back);
        meaningLess = findViewById(R.id.meaningless_reason);
        meaningLessButton = findViewById(R.id.meaningless_button);
        spite = findViewById(R.id.spite_reason);
        spiteButton = findViewById(R.id.spite_button);
        yellow = findViewById(R.id.yellow_reason);
        yellowButton = findViewById(R.id.yellow_button);
        ad = findViewById(R.id.ad_reason);
        adButton = findViewById(R.id.ad_button);
        political = findViewById(R.id.political_reason);
        politicalButton = findViewById(R.id.political_button);
        editText = findViewById(R.id.edit_report);
        btnSubmit = findViewById(R.id.btn_submit);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        mCheckMap.put(meaningLess, false);
        mCheckMap.put(spite, false);
        mCheckMap.put(yellow, false);
        mCheckMap.put(ad, false);
        mCheckMap.put(political, false);

        back.setOnClickListener(this);
        meaningLessButton.setOnClickListener(this);
        meaningLess.setOnClickListener(this);
        spite.setOnClickListener(this);
        spiteButton.setOnClickListener(this);
        yellow.setOnClickListener(this);
        yellowButton.setOnClickListener(this);
        ad.setOnClickListener(this);
        adButton.setOnClickListener(this);
        political.setOnClickListener(this);
        politicalButton.setOnClickListener(this);
        editText.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.meaningless_reason:
            case R.id.meaningless_button:
                mCheckMap.put(meaningLess, !mCheckMap.get(meaningLess));
                if (mCheckMap.get(meaningLess)) {
                    meaningLessButton.setSelected(true);
                } else {
                    meaningLessButton.setSelected(false);
                }
                break;
            case R.id.spite_reason:
            case R.id.spite_button:
                mCheckMap.put(spite, !mCheckMap.get(spite));
                if (mCheckMap.get(spite)) {
                    spiteButton.setSelected(true);
                } else {
                    spiteButton.setSelected(false);
                }
                break;
            case R.id.yellow_reason:
            case R.id.yellow_button:
                mCheckMap.put(yellow, !mCheckMap.get(yellow));
                if (mCheckMap.get(yellow)) {
                    yellowButton.setSelected(true);
                } else {
                    yellowButton.setSelected(false);
                }
                break;
            case R.id.ad_reason:
            case R.id.ad_button:
                mCheckMap.put(ad, !mCheckMap.get(ad));
                if (mCheckMap.get(ad)) {
                    adButton.setSelected(true);
                } else {
                    adButton.setSelected(false);
                }
                break;
            case R.id.political_reason:
            case R.id.political_button:
                mCheckMap.put(political, !mCheckMap.get(political));
                if (mCheckMap.get(political)) {
                    politicalButton.setSelected(true);
                } else {
                    politicalButton.setSelected(false);
                }
                break;
            case R.id.btn_submit:
                executeSubmitTask();
                break;
            case R.id.edit_report:
                imm.toggleSoftInput(0, editText.getPaintFlags());
                break;
            default:
                break;
        }
    }

    private void executeSubmitTask() {
        String type = "0";
        if (spiteButton.isSelected()) {
            type = "1";
        }
        if (yellowButton.isSelected()) {
            type = "2";
        }
        if (adButton.isSelected()) {
            type = "3";
        }
        if (politicalButton.isSelected()) {
            type = "4";
        }
        String reportReason = editText.getText().toString();
        if (reportReason.isEmpty()) {
            reportReason = "无";
        }
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(MsgService.class).doReport(UserConfig.getToken(this),
                type, reportReason).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    Toast.makeText(getApplicationContext(), "小呗已经收到了您的举报~我们会尽快处理~",
                            Toast.LENGTH_SHORT).show();
                    finishAfterTransition();
                    overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
                    //可以sendBroadcast
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
