package com.example.carson.yjenglish.home;

import com.example.carson.yjenglish.home.model.HandledWordInfo;
import com.example.carson.yjenglish.home.model.RememberWordInfo;
import com.example.carson.yjenglish.home.model.UncheckWordInfo;
import com.example.carson.yjenglish.utils.UserConfig;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * Created by 84594 on 2018/9/2.
 */

public interface WordListService {
    @FormUrlEncoded
    @POST("home/reciting_words.do") //已背单词接口
    Call<RememberWordInfo> getRememberWords(@Header("token") String token,
                                            @Field("page") String page,
                                            @Field("size") String size);

    @FormUrlEncoded
    @POST("home/mastered_words.do") //已掌握单词接口
    Call<HandledWordInfo> getHandleWords(@Header("token") String token,
                                         @Field("page") String page,
                                         @Field("size") String size);

    @FormUrlEncoded
    @POST("home/not_memorizing_words.do") //未背单词接口
    Call<UncheckWordInfo> getUncheckWords(@Header("token") String token,
                                          @Field("page") String page,
                                          @Field("size") String size);
}
