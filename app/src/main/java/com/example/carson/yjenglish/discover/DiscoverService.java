package com.example.carson.yjenglish.discover;

import com.example.carson.yjenglish.discover.model.DailyCardInfo;
import com.example.carson.yjenglish.discover.model.DiscoverInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 84594 on 2018/10/3.
 */

public interface DiscoverService {
    @POST("various/found_page.do")
    Observable<DiscoverInfo> getDiscoverInfo(@Header("token") String token);

    @POST("various/daily_pic.do")
    @FormUrlEncoded
    Call<DailyCardInfo> getMorePics(@Header("token") String token, @Field("page") String page,
                                    @Field("size") String size);
}
