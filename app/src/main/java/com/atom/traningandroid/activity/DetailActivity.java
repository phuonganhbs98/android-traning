package com.atom.traningandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atom.traningandroid.R;
import com.atom.traningandroid.model.User;
import com.atom.traningandroid.retrofit.RetrofitProvider;
import com.atom.traningandroid.utils.AppUtils;
import com.atom.traningandroid.utils.TokenUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends BaseActivity implements SimpleGestureFilter.SimpleGestureListener {

    private final String LOG_TAG = "Detail Activity";
    private TextView userId;
    private TextView name;
    private TextView gender;
    private TextView age;
    private TextView role;
    private TextView state;
    private Button edit;
    private Button delete;
    private Button lock;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        checkLogin();
// Detect touched area
        super.setDetector(this, this);
        super.setLoad();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        this.user = (User) intent.getSerializableExtra("user");

        userId = (TextView) findViewById(R.id.detail_userId);
        name = (TextView) findViewById(R.id.detail_name);
        gender = (TextView) findViewById(R.id.detail_gender);
        age = (TextView) findViewById(R.id.detail_age);
        role = (TextView) findViewById(R.id.detail_role);
        state = (TextView) findViewById(R.id.detail_state);
        edit = (Button) findViewById(R.id.dt_edit_btn);
        delete = (Button) findViewById(R.id.dt_delete_btn);
        lock = (Button) findViewById(R.id.lockButton);

        if (this.user == null) {
            this.getProfile();
        } else {
            this.getByUserId();
        }

        this.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, SignupActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        this.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, DeleteConfirmActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        this.lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lockUser();
            }
        });

    }

    public void setInforOfUser() {
        userId.setText(user.getUserId());
        name.setText(user.getFamilyName() + " " + user.getFirstName());
        gender.setText(user.getGenderName());
        age.setText(user.getAge() == null ? "" : user.getAge().toString());
        role.setText((user.getAdmin() == 1 ? "★" : "") + (user.getRoleName() == null ? "" : user.getRoleName()));
        System.out.println(">>>>edit ...." + user.toString());
        if (user.getEnabled() != null && user.getEnabled() == 0) {
            state.setText("ロック");
            state.setTextColor(getResources().getColor(R.color.red));
        } else {
            state.setText("アクティブ");
            state.setTextColor(getResources().getColor(R.color.green));
        }
    }

    public void lockUser() {
        Integer active = this.user.getEnabled();
        this.user.setEnabled(this.user.getEnabled() == 1 ? 0 : 1);
        System.out.println("......" + this.user.getEnabled());
        RetrofitProvider.callAPI().updateUser(TokenUtils.getInstance().getToken(), this.user)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {
                            getByUserId();
                            AppUtils.noticeMessage(DetailActivity.this, active == 1 ? "ユーザがロックされました" : "ユーザがアクティブになりました");
                        } else if (response.code() != 500) {
                            AppUtils.noticeMessage(DetailActivity.this, AppUtils.getErrorString(response));
                        } else {
                            AppUtils.noticeMessage(DetailActivity.this, AppUtils.getUnknownErrorString());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        t.printStackTrace();
                        AppUtils.noticeMessage(DetailActivity.this, AppUtils.getUnknownErrorString());
                    }
                });
    }

    public void getProfile() {
        RetrofitProvider.callAPI().getProfile(TokenUtils.getInstance().getToken())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {
                            user = response.body();
                            setInforOfUser();
                        } else if (response.code() != 500) {
                            AppUtils.noticeMessage(DetailActivity.this, AppUtils.getErrorString(response));
                        } else {
                            AppUtils.noticeMessage(DetailActivity.this, AppUtils.getUnknownErrorString());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        t.printStackTrace();
                        AppUtils.noticeMessage(DetailActivity.this, AppUtils.getUnknownErrorString());
                    }
                });
    }

    public void getByUserId() {
        RetrofitProvider.callAPI().getUserByUserId(TokenUtils.getInstance().getToken(), this.user.getUserId())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {
                            user = response.body();
                            setInforOfUser();
                        } else if (response.code() != 500) {
                            AppUtils.noticeMessage(DetailActivity.this, AppUtils.getErrorString(response));
                        } else {
                            AppUtils.noticeMessage(DetailActivity.this, AppUtils.getUnknownErrorString());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        t.printStackTrace();
                        AppUtils.noticeMessage(DetailActivity.this, AppUtils.getUnknownErrorString());
                    }
                });
    }

    @Override
    public void swipeDown() {
        super.swipeDown();
        this.getByUserId();
    }
}