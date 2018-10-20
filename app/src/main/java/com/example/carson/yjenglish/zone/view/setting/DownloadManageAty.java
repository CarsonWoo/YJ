package com.example.carson.yjenglish.zone.view.setting;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.DownloadManageItemAdapter;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.zone.model.DownloadManageModel;

import java.util.ArrayList;
import java.util.List;

public class DownloadManageAty extends AppCompatActivity {

    private ImageView back;
    private RecyclerView recyclerView;

    private DownloadManageItemAdapter mAdapter;

    private List<DownloadManageModel> mList;

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
        setContentView(R.layout.activity_download_manage_aty);

        initViews();
    }

    private void initViews() {
        back = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recycler_view);

//        mList = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            DownloadManageModel model = new DownloadManageModel();
//            model.setCurSize(30);
//            model.setSize(60);
//            model.setFileName("item" + i);
//            model.setFailed(false);
//            mList.add(model);
//        }
//
//        DownloadManageModel model = new DownloadManageModel();
//        model.setFailed(true);
//        model.setFileName("item test");
//        mList.add(model);
//
//        initRecycler();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initRecycler() {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        mAdapter = new DownloadManageItemAdapter(mList);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
