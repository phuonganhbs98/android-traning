package com.atom.traningandroid.retrofit;

import com.atom.traningandroid.api.APIService;
import com.atom.traningandroid.constant.Constant;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitProvider {
    private static volatile RetrofitProvider mInstance = null;
    private Retrofit retrofit;

    private RetrofitProvider() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
    }

    public static RetrofitProvider self() {
        if (mInstance == null)
            mInstance = new RetrofitProvider();
        return mInstance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public static APIService callAPI(){
        return RetrofitProvider.self().getRetrofit().create(APIService.class);
    }
}
