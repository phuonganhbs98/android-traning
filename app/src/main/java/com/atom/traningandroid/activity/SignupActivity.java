package com.atom.traningandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.atom.traningandroid.R;
import com.atom.traningandroid.RequestSingleton;
import com.atom.traningandroid.constant.Constant;
import com.atom.traningandroid.converter.GenderConverter;
import com.atom.traningandroid.converter.RoleConverter;
import com.atom.traningandroid.entity.Gender;
import com.atom.traningandroid.entity.Role;
import com.atom.traningandroid.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SignupActivity extends AppCompatActivity {

    private final String signupUrl = Constant.API+"/users";
    private final String LOG_TAG = "Signup Activity";
    private EditText userId;
    private EditText password;
    private EditText familyName;
    private EditText firstName;
    private EditText age;
    private Spinner gender;
    private Spinner role;
    private TextView errorMsg;
    private CheckBox admin;
    private Button signupBtn;
    private Integer genderId;
    private Integer authorityId;
    private Integer isAdmin = 0;
    private User paramUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userId = (EditText) findViewById(R.id.su_userId);
        password = (EditText) findViewById(R.id.su_password);
        familyName = (EditText) findViewById(R.id.su_familyName);
        firstName = (EditText) findViewById(R.id.su_firstName);
        age = (EditText) findViewById(R.id.su_age);
        errorMsg = (TextView) findViewById(R.id.su_errorMsg);
        gender = (Spinner) findViewById(R.id.su_gender_spinner);
        role = (Spinner) findViewById(R.id.su_role_spinner);
        admin = (CheckBox) findViewById(R.id.su_admin);
        signupBtn = (Button) findViewById(R.id.su_btn);

        this.admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) isAdmin = 1;
                else isAdmin = 0;
            }
        });

        Intent intent = getIntent();
        this.paramUser = (User) intent.getSerializableExtra("user");
        if(this.paramUser!=null) {
            Log.d(LOG_TAG, this.paramUser.toString());
            this.prepareData(this.paramUser);
        }

        this.getRoles(Constant.API + "/roles", this.paramUser);
        this.getGenders(Constant.API + "/genders", this);

        this.signupBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signup(SignupActivity.this);
            }
        });

    }

    public void prepareData(User u){
        this.userId.setText(u.getUserId());
        this.password.setText(u.getPassword());
        this.familyName.setText(u.getFamilyName());
        this.firstName.setText(u.getFirstName());
        this.age.setText(u.getAge()==null?"":u.getAge().toString());
        this.genderId = u.getGenderId();
        this.isAdmin = u.getAdmin();
        this.admin.setChecked(u.getAdmin()==0?false:true);
    }

    public void signup(Context c) {
        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject();
            jsonObj.put("userId", this.userId.getText().length() == 0 ? null : this.userId.getText());
            jsonObj.put("password", this.password.getText().length() == 0 ? null : this.password.getText());
            jsonObj.put("familyName", this.familyName.getText().length() == 0 ? null : this.familyName.getText());
            jsonObj.put("firstName", this.firstName.getText().length() == 0 ? null : this.firstName.getText());
            jsonObj.put("age", this.age.getText().length() == 0 ? null : this.age.getText());
            jsonObj.put("authorityId", this.authorityId);
            jsonObj.put("genderId", this.genderId);
            jsonObj.put("admin", this.isAdmin);

        } catch (JSONException e) {
            Log.d(LOG_TAG, "Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
        System.out.println(">>> jsonObj: " + jsonObj.toString());
        JsonObjectRequest stringRequest = new JsonObjectRequest(this.paramUser==null?Request.Method.POST:Request.Method.PUT, signupUrl, jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(SignupActivity.this, SearchActivity.class);

                        if (response.optString("message") != null) {
                            String message = null;
                            try {
                                message = response.getString("message");
                                errorMsg.setText("※" + message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                errorMsg.setText("");
                                Toast.makeText(c, (paramUser==null?"登録":"更新")+"完了しました", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            }
                        } else {
                            errorMsg.setText("");
                            Toast.makeText(c, (paramUser==null?"登録":"更新")+"完了しました", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, "Error: " + error.toString());
            }
        });
        // Add the request to the RequestQueue.
        RequestSingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void getRoles(String url, User u) {
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Role> roles = new ArrayList<>();
                        roles.add(new Role(null, "役職"));
                        JSONArray arr;
                        try {
                            if (response.has("role")) {
                                arr = response.getJSONArray("role");
                                roles.addAll(RoleConverter.convertToRoleList(arr));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<Role> adapter = new ArrayAdapter<Role>(SignupActivity.this,
                                android.R.layout.simple_spinner_item,
                                roles);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        role.setAdapter(adapter);
                        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Adapter adapter = parent.getAdapter();
                                Role role = (Role) adapter.getItem(position);
                                authorityId = role.getAuthorityId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        if(u!=null && u.getAuthorityId()!=null){
                            List<Role> r= new ArrayList<>();
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                 r= roles.stream().filter(x -> x.getAuthorityId()==u.getAuthorityId()).collect(Collectors.toList());
                            }
                            if(r.size()>0) {
                                int position = adapter.getPosition(r.get(0));
                                role.setSelection(position);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!" + error);
                Log.d(LOG_TAG, ">>Error: " + error.toString());
            }
        });

        RequestSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void getGenders(String url, Context c) {
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Gender> genders = new ArrayList<>();
                        genders.add(new Gender(null, "性別"));
                        JSONArray arr;
                        try {
                            if (response.has("gender")) {
                                arr = response.getJSONArray("gender");
                                genders.addAll(new GenderConverter().convertToList(arr));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<Gender> adapter = new ArrayAdapter<Gender>(c,
                                android.R.layout.simple_spinner_item,
                                genders);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        gender.setAdapter(adapter);
                        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Adapter adapter = parent.getAdapter();
                                Gender g = (Gender) adapter.getItem(position);
                                genderId = g.getGenderId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!" + error);
                Log.d(LOG_TAG, ">>Error: " + error.toString());
            }
        });

        RequestSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}