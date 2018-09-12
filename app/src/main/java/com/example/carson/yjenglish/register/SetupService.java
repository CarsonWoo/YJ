package com.example.carson.yjenglish.register;

import com.example.carson.yjenglish.checkcode.model.RegisterCodeBean;
import com.example.carson.yjenglish.utils.CommonInfo;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 84594 on 2018/8/31.
 */

public interface SetupService {
    @POST("user/register_c.do")
    @FormUrlEncoded
    Observable<CommonInfo> getResponse(@Field("register_token") String register_token,
                                       @Field("password") String password);
}
