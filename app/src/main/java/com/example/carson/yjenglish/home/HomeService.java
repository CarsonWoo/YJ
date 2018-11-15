package com.example.carson.yjenglish.home;

import com.example.carson.yjenglish.home.model.AuthorInfo;
import com.example.carson.yjenglish.home.model.CalendarInfo;
import com.example.carson.yjenglish.home.model.CommentDetailInfo;
import com.example.carson.yjenglish.home.model.HomeInfo;
import com.example.carson.yjenglish.home.model.HomeItemInfo;
import com.example.carson.yjenglish.utils.CommonInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 84594 on 2018/9/10.
 */

public interface HomeService {
    @POST("home/home_page_info.do")
//    Call<HomeInfo> getHomeInfo(@Header("token") String token);
    Observable<HomeInfo> getHomeInfo(@Header("token") String token);

    @FormUrlEncoded
    @POST("home/article_detail.do")
//    Observable<>
    Call<HomeItemInfo> getHomeItemInfo(@Header("token") String token,
                                       @Field("id") String id);

    @FormUrlEncoded
    @POST("home/comment_feeds.do")
    Call<CommonInfo> sendComment(@Header("token") String token,
                                 @Field("id") String id,
                                 @Field("comment") String comment);

    @FormUrlEncoded
    @POST("home/author_page.do")
    Observable<AuthorInfo> getAuthorInfo(@Header("token") String token,
                                         @Field("author_id") String author_id,
                                         @Field("page") String page,
                                         @Field("size") String size);

    @FormUrlEncoded
    @POST("home/liquidation_word.do")
    Call<CommonInfo> postWords(@Header("token") String token, @Field("word_list") String word_list);

    /**
     * 文章喜欢
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("home/favour_feeds.do")
    Call<CommonInfo> postFavours(@Header("token") String token, @Field("id") String id);

    /**
     * 文章点赞
     * @param token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("home/like_feeds.do")
    Call<CommonInfo> postLikes(@Header("token") String token, @Field("id") String id);

    /**
     * 副评论
     * @param token
     * @param id 主评论的id
     * @param comment 评论内容
     * @return
     */
    @POST("home/comment_feeds_comment.do")
    @FormUrlEncoded
    Call<CommonInfo> sendSubComments(@Header("token") String token,
                                     @Field("id") String id,
                                     @Field("comment") String comment);

    /**
     * 给主评论点赞
     * @param token
     * @param id 主评论的id
     * @return
     */
    @POST("home/like_feeds_comment.do")
    @FormUrlEncoded
    Call<CommonInfo> postCommentLike(@Header("token") String token,
                                     @Field("id") String id);

    /**
     * 删除主评论
     * @param token
     * @param id 主评论的id
     * @return
     */
    @POST("home/delete_comment.do")
    @FormUrlEncoded
    Call<CommonInfo> deleteComment(@Header("token") String token,
                                   @Field("id") String id);

    @POST("home/favour_dictionary.do")
    @FormUrlEncoded
    Call<CommonInfo> postWordFavours(@Header("token") String token,
                                     @Field("id") String id);

    @POST("home/comment_detail.do")
    @FormUrlEncoded
    Call<CommentDetailInfo> getCommentDetail(@Header("token") String token,
                                             @Field("id") String id);

    @POST("home/clock_in.do")
    Call<CommonInfo> doSignTask(@Header("token") String token);

    @POST("home/error_correction.do")
    @FormUrlEncoded
    Call<CommonInfo> sendError(@Header("token") String token,
                               @Field("type") String type,
                               @Field("text") String text,
                               @Field("word_id") String word_id);

    @POST("home/clock_history.do")
    Call<CalendarInfo> getHistory(@Header("token") String token);

    @POST("home/like_feeds_reply_comment.do")
    @FormUrlEncoded
    Call<CommonInfo> postSubCommentLike(@Header("token") String token,
                                        @Field("id") String id);

    @FormUrlEncoded
    Call<CommonInfo> uploadEditNote(@Header("token") String token,
                                    @Field("word_id") String word_id,
                                    @Field("word_note") String word_note);

    @FormUrlEncoded
    Call<CommonInfo> getEditNote(@Header("token") String token,
                                 @Field("word_id") String word_id);
}
