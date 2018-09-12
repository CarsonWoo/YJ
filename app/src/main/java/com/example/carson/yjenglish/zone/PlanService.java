package com.example.carson.yjenglish.zone;

import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.zone.model.MyLearningPlanInfo;
import com.example.carson.yjenglish.zone.model.PlanInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 84594 on 2018/9/2.
 */

public interface PlanService {
    @POST("user/get_plans.do")
    @FormUrlEncoded
    Observable<PlanInfo> getPlans(@Field("type") String type);

    @POST("user/decide_plan.do")
    @FormUrlEncoded
    Call<CommonInfo> addPlan(@Header("token") String token,
                             @Field("plan") String plan,
                             @Field("days") String days,
                             @Field("daily_word_number") String daily_word_number);

    @POST("user/get_my_plan.do")
    Call<MyLearningPlanInfo> getMyLearningPlans(@Header("token") String token);
}
