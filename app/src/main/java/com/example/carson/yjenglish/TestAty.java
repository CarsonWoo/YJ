package com.example.carson.yjenglish;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.carson.yjenglish.net.NullOnEmptyConverterFactory;
import com.example.carson.yjenglish.utils.FileUtils;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.ReadAACFileThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestAty extends AppCompatActivity {

    private Button btn;
    private ReadAACFileThread mThread;

    private AudioTrack player;
    private int audioBufSize;
    private byte[] audioData;

//    Player mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_aty);

        startDownload();

        audioBufSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        player = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, audioBufSize, AudioTrack.MODE_STREAM);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mThread == null) {
//                    mThread = new ReadAACFileThread();
//                }

//                player.play();
//                mThread.start();

//                mThread.start();
                MediaPlayer player = new MediaPlayer();
                try {
                    player.setDataSource(Environment.getExternalStorageDirectory().getPath() + "/Carson/test.aac");
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                try {
//                    FileInputStream fis = new FileInputStream(new File(Environment.getExternalStorageDirectory().getPath()
//                     + "/Carson/test.aac"));
//                    InputStreamReader reader = new InputStreamReader(fis);
//                    BufferedReader br = new BufferedReader(reader);
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        Log.e("Test", line);
//                    }
//                    fis.close();
//                    reader.close();
//                    br.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//
//                }
            }
        });
    }

    private void startDownload() {
        String downloadUrl = "l_e/2/tts_test.pcm";
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://47.107.62.22/").
                addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .build();
        DownloadService service = retrofit.create(DownloadService.class);
        service.downFile(downloadUrl).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/Carson");
                if (!directory.mkdirs()) {
                    directory.mkdirs();
                }
                final File file = new File(Environment.getExternalStorageDirectory().getPath() +
                "/Carson/test.pcm");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        try {
//                            Log.e("Test", response.body().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        FileUtils.writeFile2Disk(response, file);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("test", t.getMessage());
            }
        });
    }

    class Player extends Thread {
        byte[] data = new byte[audioBufSize * 2];
//        File file = new File(path);
        int offset1 = 0;
        FileInputStream fis;

        @Override
        public void run() {
            super.run();
            while (true) {
                try {
//                    fis = new FileInputStream(file);
                    fis.skip((long) offset1);
                    fis.read(data, 0, audioBufSize * 2);
                    offset1 += audioBufSize * 2;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.write(data, offset1, audioBufSize * 2);
            }

        }
    }
}
