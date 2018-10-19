package com.example.carson.yjenglish.zone.view.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.zone.AdviceContract;
import com.example.carson.yjenglish.zone.AdviceTask;
import com.example.carson.yjenglish.zone.model.AdviceModel;
import com.example.carson.yjenglish.zone.presenter.AdvicePresenter;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class AdviceAty extends AppCompatActivity implements View.OnClickListener, AdviceContract.View {

    private ImageView bg;
    private ImageView back;
    private EditText edit;
    private Button submit;
    private TextView star1, star3, star6, star10;

    private AdviceContract.Presenter presenter;
    private AdvicePresenter advicePresenter;

    private Dialog mDialog;

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
        setContentView(R.layout.layout_advice);

        mDialog = DialogUtils.getInstance(this).newAnimatedLoadingDialog();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = ScreenUtils.dp2px(this, 260);
        lp.height = ScreenUtils.dp2px(this, 240);
        mDialog.getWindow().setAttributes(lp);

        initViews();
    }

    private void initViews() {
        bg = findViewById(R.id.bg_img);
        back = findViewById(R.id.back);
        edit = findViewById(R.id.edit_advice);
        submit = findViewById(R.id.btn_submit);
        star1 = findViewById(R.id.star_one);
        star3 = findViewById(R.id.star_three);
        star6 = findViewById(R.id.star_six);
        star10 = findViewById(R.id.star_ten);

        Glide.with(this).load(R.drawable.start_img).thumbnail(0.5f)
                .bitmapTransform(new BlurTransformation(this, 5, 5))
                .into(bg);

        back.setOnClickListener(this);

        submit.setOnClickListener(this);

        star1.setOnClickListener(this);
        star3.setOnClickListener(this);
        star6.setOnClickListener(this);
        star10.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.btn_submit:
                if (edit.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "请先填写意见或建议", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!star1.isSelected() && !star3.isSelected() && !star6.isSelected() && !star10.isSelected()) {
                    Toast.makeText(getApplicationContext(), "请先对我们的应用打一下分吧~", Toast.LENGTH_SHORT).show();
                    return;
                }
                executeSubmitTask();
                break;
            case R.id.star_one:
                resetPoint();
                star1.setSelected(true);
                star1.setTextColor(Color.parseColor("#5ee1c9"));
                break;
            case R.id.star_three:
                resetPoint();
                star3.setSelected(true);
                star3.setTextColor(Color.parseColor("#5ee1c9"));
                break;
            case R.id.star_six:
                resetPoint();
                star6.setSelected(true);
                star6.setTextColor(Color.parseColor("#5ee1c9"));
                break;
            case R.id.star_ten:
                resetPoint();
                star10.setSelected(true);
                star10.setTextColor(Color.parseColor("#5ee1c9"));
                break;
        }
    }

    private void executeSubmitTask() {
        AdviceTask task = AdviceTask.getInstance();
        advicePresenter = new AdvicePresenter(task, this);
        this.setPresenter(advicePresenter);
        presenter.sendAdvice(new AdviceModel(edit.getText().toString(), switchStars()));
    }

    private String switchStars() {
        if (star1.isSelected()) {
            return "1";
        } else if (star3.isSelected()) {
            return "2";
        } else if (star6.isSelected()) {
            return "3";
        } else {
            return "4";
        }
    }

    private void resetPoint() {
        star1.setSelected(false);
        star3.setSelected(false);
        star6.setSelected(false);
        star10.setSelected(false);

        star1.setTextColor(Color.parseColor("#858585"));
        star3.setTextColor(Color.parseColor("#858585"));
        star6.setTextColor(Color.parseColor("#858585"));
        star10.setTextColor(Color.parseColor("#858585"));
    }

    @Override
    public void setPresenter(AdviceContract.Presenter presenter) {
        this.presenter = presenter;
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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(CommonInfo info) {
        if (info.getStatus().equals("200")) {
            Toast.makeText(this, "小语反馈成功，谢谢您的提议", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }
}
