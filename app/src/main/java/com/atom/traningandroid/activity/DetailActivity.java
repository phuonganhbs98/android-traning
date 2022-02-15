package com.atom.traningandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;

import com.atom.traningandroid.R;
import com.atom.traningandroid.entity.User;

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG = "Detail Activity";
    private TextView userId;
    private TextView name;
    private TextView gender;
    private TextView age;
    private TextView role;
    private TextView state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        Log.d(LOG_TAG, user.toString());

        userId = (TextView) findViewById(R.id.detail_userId);
        name = (TextView) findViewById(R.id.detail_name);
        gender = (TextView) findViewById(R.id.detail_gender);
        age = (TextView) findViewById(R.id.detail_age);
        role = (TextView) findViewById(R.id.detail_role);
        state = (TextView) findViewById(R.id.detail_state);

        userId.setText(user.getUserId());
        name.setText(user.getFamilyName() + " " + user.getFirstName());
        gender.setText(user.getGenderName());
        age.setText(user.getAge()==null?"":user.getAge().toString());
        role.setText(user.getRoleName());
        state.setText("アクティブ");
    }
}