package com.example.carson.yjenglish.home.view;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.adapter.HandledListAdapter;
import com.example.carson.yjenglish.adapter.RememberListAdapter;
import com.example.carson.yjenglish.adapter.UncheckListAdapter;
import com.example.carson.yjenglish.home.model.HandledWord;
import com.example.carson.yjenglish.home.model.RememberWord;
import com.example.carson.yjenglish.home.model.UncheckWord;

import java.util.ArrayList;
import java.util.List;

public class WordListAty extends AppCompatActivity {

    private final String TAG = "WordListAty";

    private RecyclerView recyclerView;
    private TabLayout mTabLayout;
    private ImageView back;
    private TextView wordCount;

    private List<RememberWord> mRememList;
    private List<HandledWord> mHandleList;
    private List<UncheckWord> mUncheckList;

    private RememberListAdapter rAdapter;
    private HandledListAdapter hAdapter;
    private UncheckListAdapter uAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        recyclerView = findViewById(R.id.recycler_view);
        mTabLayout = findViewById(R.id.tab_layout);
        back = findViewById(R.id.back);
        wordCount = findViewById(R.id.word_count);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //届时会通过数据库查找
        mHandleList = new ArrayList<>();
        mRememList = new ArrayList<>();
        mUncheckList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            RememberWord word = new RememberWord();
            word.setWord("travel" + i);
            word.setTrans("n. 旅行；旅游");
            mRememList.add(word);
        }
        for (int i = 0; i < 15; i++) {
            HandledWord word = new HandledWord();
            word.setWord("handle");
            word.setTrans("vt. 掌握；掌控");
            mHandleList.add(word);
        }
        for (int i = 0; i < 20; i++) {
            UncheckWord word = new UncheckWord();
            word.setWord("original");
            word.setTrans("adj. 原始的");
            mUncheckList.add(word);
        }
        setDefaultAdapter();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switchAdapter(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setDefaultAdapter() {
        rAdapter = new RememberListAdapter(this, mRememList);
        initRememAdapter();
        recyclerView.setAdapter(rAdapter);
    }

    private void switchAdapter(int position) {
        switch (position) {
            case 0:
                if (rAdapter == null) {
                    rAdapter = new RememberListAdapter(this, mRememList);
                }
                initRememAdapter();
                recyclerView.setAdapter(rAdapter);
                break;
            case 1:
                if (hAdapter == null) {
                    hAdapter = new HandledListAdapter(this, mHandleList);
                }
                initHandleAdapter();
                recyclerView.setAdapter(hAdapter);
                break;
            case 2:
                if (uAdapter == null) {
                    uAdapter = new UncheckListAdapter(this, mUncheckList);
                }
                initUncheckAdapter();
                recyclerView.setAdapter(uAdapter);
                break;
            default:
                break;
        }
    }

    private void initUncheckAdapter() {
        wordCount.setText("已背单词：" + mUncheckList.size());
    }

    private void initHandleAdapter() {
        wordCount.setText("已背单词：" + mHandleList.size());
    }

    private void initRememAdapter() {
        wordCount.setText("已背单词：" + mRememList.size());
        rAdapter.setListener(new RememberListAdapter.OnButtonClickListener() {
            @Override
            public void onTransClick(View view, int pos) {
                Log.i(TAG, "onTransClick");
            }

            @Override
            public void onPassClick(View view, int pos) {
                Log.i(TAG, "onPassClick");
//                int position = mRememList.size();
                HandledWord handledWord = new HandledWord();
                handledWord.setWord(mRememList.get(pos).getWord());
                handledWord.setTrans(mRememList.get(pos).getTrans());
                mHandleList.add(handledWord);
                mRememList.remove(pos);
                rAdapter.notifyItemRemoved(pos);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rAdapter.notifyDataSetChanged();
                    }
                }, 1000);
                if (hAdapter != null) {
                    hAdapter.notifyDataSetChanged();
                }
                wordCount.setText("已背单词：" + mRememList.size());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
