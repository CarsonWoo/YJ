package com.example.carson.yjenglish.home.view.word;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.home.HomeService;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditAty extends AppCompatActivity {

    private ImageView back;
    private TextView wordCount;
    private EditText edit;
    private TextView word;
    private TextView soundMark;
    private TextView submit;

    private String word_id;

    private String wordStr;
    private String soundMarkStr;

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
        setContentView(R.layout.activity_edit_aty);
        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);
        wordCount = findViewById(R.id.word_count);
        word = findViewById(R.id.word);
        edit = findViewById(R.id.edit_mark);
        soundMark = findViewById(R.id.soundmark);
        submit = findViewById(R.id.btn_submit);

        Intent fromData = getIntent();
        wordStr = fromData.getStringExtra("tag");
        soundMarkStr = fromData.getStringExtra("soundMark");
        word_id = fromData.getStringExtra("word_id");

        word.setText(wordStr);
        soundMark.setText(soundMarkStr);

        edit.addTextChangedListener(new MyTextWatcher());
        SharedPreferences pref = getSharedPreferences("YJEnglish", MODE_PRIVATE);
        String note = pref.getString(wordStr, "");
        if (note != null && !note.isEmpty()) {
            edit.setText(note);
        } else {
            //如果没有则访问网络查看是否存在
            executeGetTask();
        }

        back.setOnClickListener(view -> onBackPressed());

        submit.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("YJEnglish", MODE_PRIVATE);
            if (!edit.getText().toString().isEmpty()) {
                executeSubmitTask();
                sp.edit().putString(wordStr, edit.getText().toString()).apply();
                Toast.makeText(MyApplication.getContext(), "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                if (sp != null) {
                    sp.edit().clear().apply();
                }
            }
        });

    }

    private void executeSubmitTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).uploadEditNote(UserConfig.getToken(this),
                word_id, edit.getText().toString()).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
//                CommonInfo info = response.body();
//                if (info != null && info.getStatus().equals("200")) {
//
//                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {

            }
        });
    }

    private void executeGetTask() {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(HomeService.class).getEditNote(UserConfig.getToken(this),
                word_id).enqueue(new Callback<CommonInfo>() {
            @Override
            public void onResponse(Call<CommonInfo> call, Response<CommonInfo> response) {
                CommonInfo info = response.body();
                if (info != null && info.getStatus() != null) {
                    if (info.getStatus().equals("200")) {
                        String note = info.getData();
                        edit.setText(note);
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonInfo> call, Throwable t) {

            }
        });
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String tag = editable.length() + "/800";
            wordCount.setText(tag);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
