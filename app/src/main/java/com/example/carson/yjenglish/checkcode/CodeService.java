package com.example.carson.yjenglish.checkcode;

import retrofit2.http.Field;
import rx.Observable;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 84594 on 2018/7/31.
 */

//登录或忘记密码时发送验证码的url
public interface CodeService {
    @FormUrlEncoded
    @POST("")
    Observable<Integer> getLoginCode(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("")
    Observable<Integer> getRegisterCode(@Field("phone") String phone);
}
