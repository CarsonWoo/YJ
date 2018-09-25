package com.example.carson.yjenglish.zone.view.download;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carson.yjenglish.R;

import java.io.File;

public class MyDownloadAty extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    public static final int REQUEST_CODE_WORDS = 100;
    public static final int REQUEST_CODE_MUSICS = 101;
    public static final int RESULT_CODE_WORDS = 200;
    public static final int RESULT_CODE_MUSICS = 200;

    private ImageView back;

    private TextView downloadWord;
    private TextView downloadMusic;

    private int lexiconSize;
    private int musicSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_download);
        initViews();
        loadFromFolder();
    }

    private void loadFromFolder() {
        String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//        Log.e(TAG, path);

        //词库下载路径
        File folderLexicon = new File(path + "/lexicon");
        //音乐下载路径
        File folderMusic = new File(path + "/music");

        if (!folderLexicon.exists()) {
            folderLexicon.mkdirs();
        } /*else {
            Log.e(TAG, folderLexicon.getAbsolutePath());
        }*/

        if (!folderMusic.exists()) {
            folderMusic.mkdirs();
        } /*else {
            Log.e(TAG, folderMusic.getAbsolutePath());
        }*/

        //获取每个子文件夹中的文件数
        lexiconSize = folderLexicon.list().length;
        musicSize = folderMusic.list().length;

        downloadWord.setText(String.valueOf(lexiconSize));
        downloadMusic.setText(String.valueOf(musicSize));

    }

    private void initViews() {
        back = findViewById(R.id.back);
        downloadWord = findViewById(R.id.offline_words);
        downloadMusic = findViewById(R.id.offline_music);

        back.setOnClickListener(this);
        downloadWord.setOnClickListener(this);
        downloadMusic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.offline_words:
                toDownloadWords();
                break;
            case R.id.offline_music:
                toDownloadMusics();
                break;
            default:
                break;
        }
    }

    private void toDownloadWords() {
        Intent toWords = new Intent(this, DownloadWordsAty.class);
        startActivityForResult(toWords, REQUEST_CODE_WORDS);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    private void toDownloadMusics() {
        Intent toMusics = new Intent(this, DownloadMusicsAty.class);
        startActivityForResult(toMusics, REQUEST_CODE_MUSICS);
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WORDS && resultCode == RESULT_CODE_WORDS) {
            if (data != null) {
                int wordSize = data.getIntExtra("word_size", lexiconSize);
                downloadWord.setText(String.valueOf(wordSize));
            }
        } else if (requestCode == REQUEST_CODE_MUSICS && resultCode == RESULT_CODE_MUSICS) {
            if (data != null) {
                int mMusicSize = data.getIntExtra("music_size", musicSize);
                downloadMusic.setText(String.valueOf(mMusicSize));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }
}
