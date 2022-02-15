package com.atom.traningandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.atom.traningandroid.R;
import com.atom.traningandroid.RequestSingleton;
import com.atom.traningandroid.constant.Constant;
import com.atom.traningandroid.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

public class DeleteConfirmActivity extends AppCompatActivity {

    private final String LOG_TAG = "Delete Confirm Activity";
    private final String deleteUrl = Constant.API + "/users";
    private TextView name;
    private TextView userId;
    private Button deleteButton;
    private TextView errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_confirm);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        Log.d(LOG_TAG, user.toString());

        this.name = (TextView) findViewById(R.id.d_name);
        this.userId = (TextView) findViewById(R.id.d_userId);
        this.name.setText(user.getFamilyName()+" " + user.getFirstName());
        this.userId.setText(user.getUserId());
        this.errorMsg= (TextView) findViewById(R.id.d_errMsg);
        this.deleteButton = (Button) findViewById(R.id.d_button);

        this.deleteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                delete();
            }
        });

    }

    private void delete(){
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.DELETE, deleteUrl+"/"+this.userId.getText(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(DeleteConfirmActivity.this, SearchActivity.class);

                        if (response.optString("message") != null) {
                            String message = null;
                            try {
                                message = response.getString("message");
                                errorMsg.setText("※" + message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                errorMsg.setText("");
                                Toast.makeText(DeleteConfirmActivity.this, "削除完了しました", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            }
                        } else {
                            errorMsg.setText("");
                            Toast.makeText(DeleteConfirmActivity.this, "削除完了しました", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!" + error);
                Log.d(LOG_TAG, error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        RequestSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}