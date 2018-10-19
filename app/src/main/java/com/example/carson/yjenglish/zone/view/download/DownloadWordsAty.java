package com.example.carson.yjenglish.zone.view.download;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carson.yjenglish.EmptyValue;
import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.home.viewbinder.word.FieldTitleViewBinder;
import com.example.carson.yjenglish.utils.DialogUtils;
import com.example.carson.yjenglish.utils.ScreenUtils;
import com.example.carson.yjenglish.zone.model.forviewbinder.WordTag;
import com.example.carson.yjenglish.zone.viewbinder.WordTagViewBinder;

import java.io.File;
import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class DownloadWordsAty extends AppCompatActivity implements WordTagViewBinder.OnTagClickListener {

    private final String TAG = getClass().getSimpleName();

    private ImageView back;

    private TextView count;

    private RecyclerView recyclerView;
    private MultiTypeAdapter mAdapter;
    private Items mItems;

    private int size, originSize;//后者拿来判断是否有删除操作

    private String[] mLexicon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setTheme(R.style.AppThemeWithoutTranslucent);
        }
        setContentView(R.layout.activity_download_words);
        loadFromFile();
        initViews();
    }

    private void loadFromFile() {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath() + "/lexicon");

        mLexicon = file.list();

        size = mLexicon.length;
        originSize = mLexicon.length;

    }

    private void initViews() {
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        count = findViewById(R.id.count);
        count.setText("单词包：" + size);

        recyclerView = findViewById(R.id.recycler_view);
        mItems = new Items();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mItems.get(position) instanceof WordTag) {
                    return 1;
                } else {
                    return 3;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new MultiTypeAdapter(mItems);
        mAdapter.register(WordTag.class, new WordTagViewBinder(this));

        for (int i = 0; i < size; i++) {
            String[] str = mLexicon[i].split("_");
            mItems.add(new WordTag(str[0], str[1].split("单词")[0] + "单词"));
        }

        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void onTagClick(final String tag, final int pos) {
        DialogUtils dialogUtils = DialogUtils.getInstance(this);
        AlertDialog mDialog = dialogUtils.newTipsDialog("确定删除资源?", View.TEXT_ALIGNMENT_CENTER);
        dialogUtils.show(mDialog);
        dialogUtils.setTipsListener(new DialogUtils.OnTipsListener() {
            @Override
            public void onConfirm() {
                String lexiconName = tag + "_" + mLexicon[pos].split("_")[1];
                File mFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
                    + "/lexicon/" + lexiconName);
                if (mFile.exists()) {
                    mFile.delete();
                } else {
                    Toast.makeText(DownloadWordsAty.this,
                            "删除错误", Toast.LENGTH_SHORT).show();
                }
                mItems.remove(pos);
                size--;
                count.setText("单词包：" + size);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {

            }
        });
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.height = ScreenUtils.dp2px(this, 260);
        lp.width = ScreenUtils.dp2px(this, 240);
        mDialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onBackPressed() {
        //如果两者不相等 则需要通知前一个页面改动数据
        if (originSize != size) {
            Intent backIntent = new Intent();
            backIntent.putExtra("word_size", size);
            setResult(MyDownloadAty.RESULT_CODE_WORDS, backIntent);
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
