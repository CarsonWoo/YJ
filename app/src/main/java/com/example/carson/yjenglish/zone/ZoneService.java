package com.example.carson.yjenglish.zone;

import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.zone.model.ZoneInfo;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by 84594 on 2018/9/19.
 */

public interface ZoneService {
    @POST("user/my_info.do")
    Observable<ZoneInfo> getUserInfo(@Header("token") String token);

    @POST("user/edit_my_info.do")
    @FormUrlEncoded
    Observable<CommonInfo> changeUserBasicInfo(@Header("token") String token,
                                         @Field("gender") String gender,
                                         @Field("username") String username,
                                         @Field("personality_signature") String personality_signature);

    @Multipart
    @POST("user/edit_portrait.do")
    Call<CommonInfo> changeUserPortrait(@Header("token") String token,
                                        @Part MultipartBody.Part img);

    @POST("various/advice.do")
    @FormUrlEncoded
    Observable<CommonInfo> sendAdvice(@Header("token") String token,
                                      @Field("advice") String advice,
                                      @Field("level") String level);
}
