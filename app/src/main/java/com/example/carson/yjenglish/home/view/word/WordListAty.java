package com.example.carson.yjenglish.home.view.word;

import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.StatusBarUtil;
import com.example.carson.yjenglish.utils.UserConfig;

import retrofit2.Retrofit;

public class WordListAty extends AppCompatActivity {

    private final String TAG = "WordListAty";

    private TabLayout mTabLayout;
    private ImageView back;
    private TextView wordCount;

    private ConstraintLayout mContainer;

    private Retrofit retrofit;

    private RememberTab rememberTab;
    private HandleTab handleTab;
    private UncheckTab uncheckTab;

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
        setContentView(R.layout.activity_word_list);

        initViews();
        initEvents();
    }

    private void initEvents() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);

        mContainer = findViewById(R.id.word_list_container);
        mTabLayout = findViewById(R.id.tab_layout);
        back = findViewById(R.id.back);
        wordCount = findViewById(R.id.word_count);

        setDefaultTab();

        //通过fragment 切换tab
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switchTab(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setDefaultTab() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        rememberTab = RememberTab.newInstance();
        ft.add(R.id.word_list_container, rememberTab);
        wordCount.setText("单词总数：" + rememberTab.getWordCount());
        ft.commit();
    }

    private void switchTab(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        hideAllFragments(ft);
        String s = "单词总数：";
        switch (position) {
            case 0:
                ft.show(rememberTab);
                wordCount.setText(s + rememberTab.getWordCount());
                break;
            case 1:
                if (handleTab == null) {
                    handleTab = HandleTab.newInstance();
                    ft.add(R.id.word_list_container, handleTab);
                } else {
                    ft.show(handleTab);
                }
                wordCount.setText(s + handleTab.getWordCount());
                break;
            case 2:
                if (uncheckTab == null) {
                    uncheckTab = UncheckTab.newInstance();
                    ft.add(R.id.word_list_container, uncheckTab);
                } else {
                    ft.show(uncheckTab);
                }
                wordCount.setText(s + uncheckTab.getWordCount());
                break;
            default:
                break;
        }
        ft.commit();
    }

    private void hideAllFragments(FragmentTransaction ft) {
        if (rememberTab != null) {
            ft.hide(rememberTab);
        }
        if (handleTab != null) {
            ft.hide(handleTab);
        }
        if (uncheckTab != null) {
            ft.hide(uncheckTab);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
