package com.example.carson.yjenglish.home;

import com.example.carson.yjenglish.home.model.word.HandledWordInfo;
import com.example.carson.yjenglish.home.model.word.RememberWordInfo;
import com.example.carson.yjenglish.home.model.word.UncheckWordInfo;
import com.example.carson.yjenglish.home.model.word.WordDetailInfo;
import com.example.carson.yjenglish.home.model.word.WordInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 84594 on 2018/9/2.
 */

public interface WordService {
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

    @POST("home/recite_word_list.do") //获取背单词接口
    Observable<WordInfo> startLearning(@Header("token") String token);

    @FormUrlEncoded
    @POST("home/word_card.do") //单词卡片接口
    Call<WordDetailInfo> getWordDetail(@Header("token") String token,
                                       @Field("word_id") String word_id);
}
