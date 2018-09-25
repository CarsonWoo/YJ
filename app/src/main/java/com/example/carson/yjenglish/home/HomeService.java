package com.example.carson.yjenglish.home;

import com.example.carson.yjenglish.home.model.HomeInfo;
import com.example.carson.yjenglish.home.model.HomeItemInfo;
import com.example.carson.yjenglish.utils.CommonInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 84594 on 2018/9/10.
 */

public interface HomeService {
    @POST("home/home_page_info.do")
//    Call<HomeInfo> getHomeInfo(@Header("token") String token);
    Observable<HomeInfo> getHomeInfo(@Header("token") String token);

    @FormUrlEncoded
    @POST("home/article_detail.do")
//    Observable<>
    Call<HomeItemInfo> getHomeItemInfo(@Header("token") String token,
                                       @Field("id") String id);

    @FormUrlEncoded
    @POST("home/comment_feeds.do")
    Call<CommonInfo> sendComment(@Header("token") String token,
                                 @Field("id") String id,
                                 @Field("comment") String comment);


}
