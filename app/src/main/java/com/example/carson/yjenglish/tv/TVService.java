package com.example.carson.yjenglish.tv;

import com.example.carson.yjenglish.tv.model.LoadCommentInfo;
import com.example.carson.yjenglish.tv.model.TVInfo;
import com.example.carson.yjenglish.tv.model.TVItemInfo;
import com.example.carson.yjenglish.tv.model.TVMoreInfo;
import com.example.carson.yjenglish.utils.CommonInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 84594 on 2018/9/29.
 */

public interface TVService {

    @POST("environment/yu_video.do")
    Observable<TVInfo> getTvInfo(@Header("token") String token);

    @POST("environment/more_yu_video.do")
    @FormUrlEncoded
    Call<TVMoreInfo> getMoreTvInfo(@Header("token") String token,
                                   @Field("page") String page,
                                   @Field("size") String size);

    @POST("environment/single_yu_video.do")
    @FormUrlEncoded
    Call<TVItemInfo> getItemInfo(@Header("token") String token,
                                 @Field("video_id") String video_id);

    @POST("environment/comment_video.do")
    @FormUrlEncoded
    Call<CommonInfo> sendComment(@Header("token") String token, @Field("id") String id,
                                 @Field("comment") String comment);

    @POST("environment/favour_yj.do")
    @FormUrlEncoded
    Call<CommonInfo> postFavours(@Header("token") String token, @Field("id") String id);

    @POST("environment/single_yu_new_comment.do")
    @FormUrlEncoded
    Call<LoadCommentInfo> loadMoreComment(@Header("token") String token,
                                          @Field("video_id") String video_id,
                                          @Field("page") String page);

    @POST("environment/like_video_comment.do")
    @FormUrlEncoded
    Call<CommonInfo> postCommentFavours(@Header("token") String token,
                                        @Field("id") String id);

}
