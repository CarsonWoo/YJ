package com.example.carson.yjenglish.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.net.InDiskCookieStore;
import com.example.carson.yjenglish.net.NullOnEmptyConverterFactory;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLDecoder;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 84594 on 2018/8/18.
 */

public class NetUtils {
    private static NetUtils INSTANCE = null;
    public static NetUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetUtils();
        }
        return INSTANCE;
    }
    /**
     * 没有连接网络
     */
    private static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    private static final int NETWORK_MOBILE = 0;
    /**
     * Wifi网络
     */
    private static final int NETWORK_WIFI = 1;

    public static int getNetWorkState(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }

    public OkHttpClient getClientInstance() {
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
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        CookieManager cookieManager = new CookieManager(new InDiskCookieStore(ctx),
//                CookiePolicy.ACCEPT_ORIGINAL_SERVER);
//        builder.cookieJar(new JavaNetCookieJar(cookieManager));
//        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),
//                new SharedPrefsCookiePersistor(MyApplication.getContext()));
//        builder.cookieJar(cookieJar);
        builder.addInterceptor(interceptor);
        builder.addInterceptor(new SaveCookiesInterceptor());
        builder.addInterceptor(new AddCookiesInterceptor());
        return builder.build();
    }

    public Retrofit getRetrofitInstance(String url) {
        OkHttpClient client = getClientInstance();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url).addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    public interface NetEvent {
        void onNetworkChange(int netMobile);
    }
}
