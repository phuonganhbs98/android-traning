package com.atom.traningandroid.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.atom.traningandroid.R;
import com.atom.traningandroid.utils.AppUtils;
import com.atom.traningandroid.utils.TokenUtils;

import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
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
}
