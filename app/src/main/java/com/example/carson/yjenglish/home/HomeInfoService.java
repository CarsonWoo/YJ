package com.example.carson.yjenglish.home;

import com.example.carson.yjenglish.home.model.HomeInfo;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 84594 on 2018/9/10.
 */

public interface HomeInfoService {
    @POST("home/home_page_info.do")
//    Call<HomeInfo> getHomeInfo(@Header("token") String token);
    Observable<HomeInfo> getHomeInfo(@Header("token") String token);
}
