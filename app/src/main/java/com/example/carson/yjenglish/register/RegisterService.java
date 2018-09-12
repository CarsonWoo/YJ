package com.example.carson.yjenglish.register;

import com.example.carson.yjenglish.register.model.RegisterInfo;
import com.example.carson.yjenglish.register.model.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import rx.Observable;

import retrofit2.http.POST;

/**
 * Created by 84594 on 2018/7/29.
 */

public interface RegisterService {
    @POST("user/register_a.do")
//    @POST("user/test.do")
    @FormUrlEncoded
    Observable<RegisterInfo> getResponse(@Field("token") String token, @Field("phone") String phone);

    @POST("user/register_a.do")
    @FormUrlEncoded
    Call<RegisterInfo> getResendResponse(@Field("token") String token, @Field("phone") String phone);

}
