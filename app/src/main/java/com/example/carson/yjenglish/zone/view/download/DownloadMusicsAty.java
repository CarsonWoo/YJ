package com.example.carson.yjenglish.zone.view.download;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.DownloadMusicsItemAdapter;
import com.example.carson.yjenglish.home.model.word.BaseWord;
import com.example.carson.yjenglish.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class DownloadMusicsAty extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private ImageView back;

    private RecyclerView recyclerView;
    private DownloadMusicsItemAdapter mAdapter;

    private int x, y, pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
        }
        setContentView(R.layout.activity_download_musics);
        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<BaseWord> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            BaseWord word = new BaseWord();
            word.setWord("travel");
            word.setTrans("n.旅游；游玩");
            list.add(word);
        }
        mAdapter = new DownloadMusicsItemAdapter(this, list);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
