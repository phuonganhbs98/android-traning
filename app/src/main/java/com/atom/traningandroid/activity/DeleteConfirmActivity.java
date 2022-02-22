package com.atom.traningandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.atom.traningandroid.R;

import com.atom.traningandroid.constant.Constant;
import com.atom.traningandroid.model.User;
import com.atom.traningandroid.retrofit.RetrofitProvider;
import com.atom.traningandroid.utils.AppUtils;
import com.atom.traningandroid.utils.TokenUtils;

import retrofit2.Call;
import retrofit2.Callback;

public class DeleteConfirmActivity extends BaseActivity {

    private final String LOG_TAG = "Delete Confirm Activity";
    private final String deleteUrl = Constant.BASE_URL + "/users";
    private TextView name;
    private TextView userId;
    private Button deleteButton;
    private TextView errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_confirm);
        checkLogin();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        Log.d(LOG_TAG, user.toString());

        this.name = (TextView) findViewById(R.id.d_name);
        this.userId = (TextView) findViewById(R.id.d_userId);
        this.name.setText(user.getFamilyName() + " " + user.getFirstName());
        this.userId.setText(user.getUserId());
        this.errorMsg = (TextView) findViewById(R.id.d_errMsg);
        this.deleteButton = (Button) findViewById(R.id.d_button);

        this.deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                delete();
            }
        });

    }

    private void delete() {
        RetrofitProvider.callAPI().deleteUser(TokenUtils.getInstance().getToken(), this.userId.getText().toString())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                        if (response.code() == 200) {
                            AppUtils.noticeMessage(DeleteConfirmActivity.this, "削除完了しました");
                            Intent intent = new Intent(DeleteConfirmActivity.this, SearchActivity.class);
                            startActivity(intent);
                        } else {
                            AppUtils.noticeMessage(DeleteConfirmActivity.this, AppUtils.getErrorString(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        t.printStackTrace();
                        AppUtils.noticeMessage(DeleteConfirmActivity.this, t.getMessage());
                    }
                });
    }
}