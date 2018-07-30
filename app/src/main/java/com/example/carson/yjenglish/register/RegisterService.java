package com.example.carson.yjenglish.register;

import com.example.carson.yjenglish.register.model.RegisterInfo;
import com.example.carson.yjenglish.register.model.RegisterModel;

import retrofit2.http.Body;
import rx.Observable;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 84594 on 2018/7/29.
 */

public interface RegisterService {
    @POST("register")
    Observable<RegisterInfo> getResponse(@Body RegisterModel model);

}
