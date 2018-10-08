package com.example.carson.yjenglish.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by 84594 on 2018/9/26.
 */

public class FileUtils {

    public static File createFile(Context context, String fileName){

        File file = null;
        String state = Environment.getExternalStorageState();

        if(state.equals(Environment.MEDIA_MOUNTED)){
            file = new File(Environment.getExternalStorageDirectory().getPath()+"/YuJingRecorder/" + fileName);
        }else {
            file = new File(context.getCacheDir().getPath()+"/YuJingRecorder/" + fileName);
        }

        Log.e("vivi","file "+file.getPath());

        return file;

    }

    public static void writeFile2Disk(Response<ResponseBody> response, File file){

        long currentLength = 0;
        OutputStream os =null;

        ResponseBody body = response.body();

        InputStream is = body.byteStream();
        long totalLength = body.contentLength();

        try {

            os = new FileOutputStream(file);

            int len ;

            byte [] buff = new byte[1024];

            while((len=is.read(buff))!=-1){

                os.write(buff,0,len);
                currentLength+=len;
                Log.e("vivi","当前进度:"+currentLength);
//                httpCallBack.onLoading(currentLength,totalLength);
            }
            // httpCallBack.onLoading(currentLength,totalLength,true);

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(os!=null){
                try {
                    os.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
