package com.example.carson.yjenglish.home.view.word;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.R;

public class EditAty extends AppCompatActivity {

    private ImageView back;
    private TextView wordCount;
    private EditText edit;
    private TextView word;
    private TextView soundMark;

    private String wordStr;
    private String soundMarkStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_aty);
        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);
        wordCount = findViewById(R.id.word_count);
        word = findViewById(R.id.word);
        edit = findViewById(R.id.edit_mark);
        soundMark = findViewById(R.id.soundmark);

        Intent fromData = getIntent();
        wordStr = fromData.getStringExtra("tag");
        soundMarkStr = fromData.getStringExtra("soundMark");

        word.setText(wordStr);
        soundMark.setText(soundMarkStr);

        edit.addTextChangedListener(new MyTextWatcher());
        SharedPreferences pref = getSharedPreferences("YJEnglish", MODE_PRIVATE);
        if (pref != null) {
            edit.setText(pref.getString(wordStr, ""));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("YJEnglish", MODE_PRIVATE);
                if (!edit.getText().toString().isEmpty()) {
                    sp.edit().putString(wordStr, edit.getText().toString()).apply();
                } else {
                    if (sp != null) {
                        sp.edit().clear().apply();
                    }
                }
                onBackPressed();
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
