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
import com.atom.traningandroid.constant.Constant;
import com.atom.traningandroid.converter.UserConverter;
import com.atom.traningandroid.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
            JsonObjectRequest jsonObjectRequest;
            @Override
            public void onClick(View v) {
//                if (userId.getText().length() > 0 && password.getText().length() > 0) {
//                    String toastMessage = "※ ログインに失敗しました";
//                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
//                } else {
//                    String toastMessage = "※ ログインに成功しました";
//                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
//                }
                String url = Constant.API+"/users/login";
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject();
                    jsonObj.put("userId", userId.getText());
                    jsonObj.put("password", password.getText());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(jsonObj.toString());
                login(url, jsonObj);

            }
        });

    }

    public void login (String url, JSONObject objRequest){
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url,objRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
//                        textView.setText("Response is: "+ response.substring(0,500));
                        String toastMessage = null;
                        try {
                            toastMessage = response.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(toastMessage != null){
//                            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
                            message.setText(toastMessage);
                        }else{
                            toastMessage = "success";
                            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                            startActivity(intent);
                        }
                        System.out.println(">>>bbbbb: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!" + error);
                System.out.println("........pa: " + "That didn't work!" + error);
            }
        });

        // Add the request to the RequestQueue.
        RequestSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public JSONObject connectURL(String url) {
        // Request a string response from the provided URL.
        final List<JSONObject> result=  new ArrayList<>();
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
//                        textView.setText("Response is: "+ response.substring(0,500));
                        if(response != null){
                            System.out.println(">>>>> aaaaaaaaaaaaaaaaaaaa");
                            User u = UserConverter.convertToUser(response);
                            System.out.println(">>>>>User: ....");
                            System.out.println(u.toString());
                        }
                        System.out.println(">>>bbbbb: " + response.toString());
//                        try {
//                            System.out.println(">>>>>total count: " + response.getString("total_count"));
//                            System.out.println(">>>>>incomplete_results: " + response.getString("incomplete_results"));
//                            System.out.println(">>>>>login: " + response.getJSONArray("items").getJSONObject(0).getString("login"));
//                            System.out.println(">>>>>url: " + response.getJSONArray("items").getJSONObject(0).getString("url"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        result.add(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!" + error);
                System.out.println("........pa: " + "That didn't work!" + error);
            }
        });

        // Add the request to the RequestQueue.
        RequestSingleton.getInstance(this).addToRequestQueue(stringRequest);
        System.out.println(">>>bbbbb2222: " + result.toString());
        return result.size()>0?result.get(0):null;
    }
}