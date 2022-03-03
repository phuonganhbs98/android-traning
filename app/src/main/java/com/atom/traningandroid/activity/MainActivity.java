package com.atom.traningandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.atom.traningandroid.R;
import com.atom.traningandroid.model.User;
import com.atom.traningandroid.retrofit.RetrofitProvider;
import com.atom.traningandroid.utils.AppUtils;
import com.atom.traningandroid.utils.TokenUtils;

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

        if(TokenUtils.getInstance().getToken()!= null){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }

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

    @Override
    protected void onStart() {
        super.onStart();
        if(TokenUtils.getInstance().getToken()!= null){
            Intent intent = new Intent(this, Search3Activity.class);
            startActivity(intent);
        }
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
                            if(response.code()==500){
                                message.setText(AppUtils.getUnknownErrorString());
                            }else{
                                String err = AppUtils.getErrorString(response);
                                message.setText(err);
                            }
                            return;
                        }
                        System.out.println("------" + u.toString());
                        TokenUtils.getInstance().setToken(token);
                        AppUtils.noticeMessage(MainActivity.this, "ログインに成功しました");
                        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                        intent.putExtra("activity", "login");
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        t.printStackTrace();
                        message.setText(AppUtils.getUnknownErrorString());
                    }
                });
    }

}