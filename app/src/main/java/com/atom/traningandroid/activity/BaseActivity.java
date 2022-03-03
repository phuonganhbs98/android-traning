package com.atom.traningandroid.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.atom.traningandroid.R;
import com.atom.traningandroid.utils.AppUtils;
import com.atom.traningandroid.utils.TokenUtils;

import retrofit2.Response;

public class BaseActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener, SwipeRefreshLayout.OnRefreshListener {
    private SimpleGestureFilter detector;
    private ProgressBar load;

    public ProgressBar getLoad() {
        return load;
    }

    public void setLoad() {
        this.load = (ProgressBar) findViewById(R.id.progressBar);
    }

    public SimpleGestureFilter getDetector() {
        return detector;
    }

    public void setDetector(Activity a, SimpleGestureFilter.SimpleGestureListener s) {
        this.detector = new SimpleGestureFilter(a, s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.logout_menu:
                TokenUtils.getInstance().setToken(null);
                AppUtils.noticeMessage(this, "ログアウトしました");
                moveToLoginDisplay();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkLogin() {
        if (TokenUtils.getInstance().getToken() == null) {
            this.moveToLoginDisplay();
        }
    }

    public void moveToLoginDisplay() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void checkAuthorization(Response response) {
        if (response.code() == 401) {
            AppUtils.noticeMessage(this, AppUtils.getErrorString(response));
            this.moveToLoginDisplay();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        System.out.println("11111111111");
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT:
                onBackPressed();
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                break;
            case SimpleGestureFilter.SWIPE_DOWN:
                onRefresh();
                break;
            case SimpleGestureFilter.SWIPE_UP:
                break;

        }
    }

    public void swipeDown() {
    }

    @Override
    public void onRefresh() {
        load.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeDown();
                load.setVisibility(View.INVISIBLE);
            }
        }, 2500);
    }
}
