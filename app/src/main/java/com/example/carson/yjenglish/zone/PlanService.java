package com.example.carson.yjenglish.zone;

import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.zone.model.MyLearningPlanInfo;
import com.example.carson.yjenglish.zone.model.MyPlanDailyInfo;
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

    /**
     * 用户获取学习计划的天数
     * @param token
     * @return
     */
    @POST("user/get_plan_day.do")
    @FormUrlEncoded
    Call<MyPlanDailyInfo> getPlanDaily(@Header("token") String token, @Field("plan") String plan);

    @POST("user/decide_plan_days.do")
    @FormUrlEncoded
    Call<CommonInfo> changePlanDaily(@Header("token") String token,
                                     @Field("daily_word_number") String daily_word_number,
                                     @Field("days") String days);

    @POST("user/delete_plan.do")
    @FormUrlEncoded
    Call<CommonInfo> deletePlan(@Header("token") String token, @Field("plan") String plan);

    @POST("user/decide_selected_plan.do")
    @FormUrlEncoded
    Call<CommonInfo> setSelectedPlan(@Header("token") String token, @Field("plan") String plan);

}
