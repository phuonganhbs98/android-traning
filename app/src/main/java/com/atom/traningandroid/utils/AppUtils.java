package com.atom.traningandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.atom.traningandroid.activity.MainActivity;

import java.io.IOException;

import retrofit2.Response;

public class AppUtils {
    public static String getErrorString(Response res) {
        String error = null;
        try {
            error = res.errorBody().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "※ "+error;
    }

    public static void noticeMessage(Context c, String msg){
        Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
    }






}
