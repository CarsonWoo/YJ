package com.example.carson.yjenglish.msg;

import com.example.carson.yjenglish.msg.model.FavoursInfo;
import com.example.carson.yjenglish.utils.CommonInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 84594 on 2018/10/18.
 */

public interface MsgService {

    @POST("message/receive_likes.do")
    Observable<FavoursInfo> getFavoursInfo(@Header("token") String token);

    @POST("message/receive_likes.do")
    Call<FavoursInfo> refreshFavoursInfo(@Header("token") String token);

    @POST("message/tip_off.do")
    @FormUrlEncoded
    Call<CommonInfo> doReport(@Header("token") String token,
                              @Field("type") String type,
                              @Field("report_reason") String report_reason);

}
