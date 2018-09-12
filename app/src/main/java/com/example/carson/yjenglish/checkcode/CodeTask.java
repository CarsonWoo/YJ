package com.example.carson.yjenglish.checkcode;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.checkcode.model.RegisterCodeBean;
import com.example.carson.yjenglish.net.LoadTasksCallback;
import com.example.carson.yjenglish.net.NetTask;
import com.example.carson.yjenglish.net.NullOnEmptyConverterFactory;
import com.example.carson.yjenglish.utils.AddCookiesInterceptor;
import com.example.carson.yjenglish.utils.CommonInfo;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.SaveCookiesInterceptor;
import com.example.carson.yjenglish.utils.UserConfig;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 84594 on 2018/7/31.
 */

//专门用来regsiter的验证
public class CodeTask implements NetTask<RegisterCodeBean> {

    private static CodeTask INSTANCE = null;

    private static final String HOST = UserConfig.HOST;
    private Retrofit retrofit;

    public static CodeTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CodeTask();
        }
        return INSTANCE;
    }

    private CodeTask() {
        createRetrofit();
    }

    private void createRetrofit() {
//        retrofit = NetUtils.getInstance().getRetrofitInstance(HOST);
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                String text;
                try {
                    text = URLDecoder.decode(message, "utf-8");
                    Log.e("======", text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("======", e.getMessage());
                }
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        builder.addInterceptor(new SaveCookiesInterceptor());
//        builder.addInterceptor(new AddCookiesInterceptor());
        builder.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String cookie = MyApplication.getContext().getSharedPreferences("cookies_prefs", Context.MODE_PRIVATE)
                        .getString(HOST + "user/register_a.do", "");
                if (!TextUtils.isEmpty(cookie)) {
                    return chain.proceed(chain.request().newBuilder().header("Cookie", cookie).build());
                }
                return chain.proceed(chain.request());
            }
        });
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .client(builder.build())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Override
    public Subscription execute(RegisterCodeBean bean, final LoadTasksCallback callback) {
        CodeService codeService = retrofit.create(CodeService.class);
        Observable<CommonInfo> mObservable;
        Subscription subscription;

        mObservable = codeService.getRegisterCode(bean.getRegister_token(), bean.getPhone_code());
        subscription = mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<CommonInfo>() {
                    @Override
                    public void onStart() {
                        callback.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        callback.onFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailed(e.getMessage());
                    }

                    @Override
                    public void onNext(CommonInfo commonInfo) {
                        callback.onSuccess(commonInfo);
                    }
                });
        return subscription;
    }
}
