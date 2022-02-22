package com.atom.traningandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.atom.traningandroid.R;
import com.atom.traningandroid.RequestSingleton;
import com.atom.traningandroid.api.APIServiceImpl;
import com.atom.traningandroid.constant.Constant;
import com.atom.traningandroid.converter.UserConverter;
import com.atom.traningandroid.model.User;
import com.atom.traningandroid.retrofit.RetrofitProvider;
import com.atom.traningandroid.utils.AppUtils;
import com.atom.traningandroid.utils.TokenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private EditText userId;
    private EditText password;
    private TextView message;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = findViewById(R.id.userId);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginBtn);
        message = findViewById(R.id.message);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u = new User();
                u.setUserId(userId.getText().toString());
                u.setPassword(password.getText().toString());
                login(u);
            }
        });

    }

    public void login(User u) {
        RetrofitProvider.callAPI().login(u)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        String token = response.body();
                        System.out.println(call.request().toString());
                        if (response.code() != 200) {
                            System.out.println("code: " + response.code());
                            String err = AppUtils.getErrorString(response);
                            message.setText(err);
                            return;
                        }
                        System.out.println("------" + u.toString());
                        TokenUtils.getInstance().setToken(token);
                        AppUtils.noticeMessage(MainActivity.this, "ログインに成功しました");
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        t.printStackTrace();
                        message.setText(t.getMessage());
                    }
                });
    }

}