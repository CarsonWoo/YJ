package com.example.carson.yjenglish;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by 84594 on 2018/10/7.
 */

public interface VideoService {
    @POST("home/get_subtitles.do")
    @FormUrlEncoded
    Call<VideoCaptionInfo> getVideoInfo(@Header("token") String token, @Field("video_id") String video_id);
}
