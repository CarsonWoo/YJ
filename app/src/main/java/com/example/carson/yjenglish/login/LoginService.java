package com.example.carson.yjenglish.login;

import com.example.carson.yjenglish.login.model.LoginInfo;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 84594 on 2018/7/28.
 */

public interface LoginService {
    @POST("user/login.do")
    Observable<LoginInfo> getLoginResponse(@Header("username") String username,
                                           @Header("password") String password);
}
