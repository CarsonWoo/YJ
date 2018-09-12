package com.example.carson.yjenglish.checkcode;

import com.example.carson.yjenglish.utils.CommonInfo;

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
    @POST("user/register_b.do")
    Observable<CommonInfo> getRegisterCode(@Field("register_token") String register_token,
                                           @Field("phone_code") String phone_code);
}
