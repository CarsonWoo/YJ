package com.example.carson.yjenglish;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by 84594 on 2018/8/31.
 */

public interface TestService {
    @GET("user/test2.do")
    Call<String> getToken();
}
