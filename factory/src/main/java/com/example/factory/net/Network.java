package com.example.factory.net;

import android.text.TextUtils;

import com.example.factory.Factory;
import com.example.factory.persistence.Account;

import java.io.IOException;

import me.maxandroid.common.Common;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mxz on 18-3-17.
 */

public class Network {
    private static Network instance;
    private Retrofit retrofit;

    static {
        instance = new Network();
    }

    private Network() {

    }

    public static Retrofit getRetrofit() {
        if (instance.retrofit != null) return instance.retrofit;
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();
                        if (!TextUtils.isEmpty(Account.getToken())) {
                            builder.addHeader("token", Account.getToken());
                        }
                        builder.addHeader("Content-Type", "application/json");
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();
        Retrofit.Builder builder = new Retrofit.Builder();
        instance.retrofit = builder.baseUrl(Common.Constance.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();

        return instance.retrofit;
    }

    public static RemoteService remote() {
        return Network.getRetrofit().create(RemoteService.class);
    }
}
