package com.example.carson.yjenglish;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by 84594 on 2018/9/26.
 */

public interface DownloadService {
    @Streaming
    @GET
    Call<ResponseBody> downFile(@Url String fileUrl);
}
