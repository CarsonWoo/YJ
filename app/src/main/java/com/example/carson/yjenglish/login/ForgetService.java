package com.example.carson.yjenglish.login;

import com.example.carson.yjenglish.login.model.ForgetModel;
import com.example.carson.yjenglish.uitls.CommonInfo;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 84594 on 2018/7/31.
 */

public interface ForgetService {
    @POST("")
    Observable<CommonInfo> getResponse(@Body ForgetModel model);
}
