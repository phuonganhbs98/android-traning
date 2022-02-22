package com.atom.traningandroid.api;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.atom.traningandroid.activity.MainActivity;
import com.atom.traningandroid.activity.SearchActivity;
import com.atom.traningandroid.model.User;
import com.atom.traningandroid.retrofit.RetrofitProvider;
import com.atom.traningandroid.utils.AppUtils;
import com.atom.traningandroid.utils.TokenUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIServiceImpl {
    /**
     * login
     */
    public static void login(User u, TextView errMsg) {
        RetrofitProvider.callAPI().login(u)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String token = response.body();
                        if (response.code() != 200) {
                            String err = AppUtils.getErrorString(response);
                            errMsg.setText(err);
                        }
                        TokenUtils.getInstance().setToken(token);

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        t.printStackTrace();
                        errMsg.setText(t.getMessage());
                    }
                });
    }
}
