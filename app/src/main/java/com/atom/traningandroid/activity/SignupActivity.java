package com.atom.traningandroid.activity;

import androidx.appcompat.app.ActionBar;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.atom.traningandroid.R;
import com.atom.traningandroid.RequestSingleton;
import com.atom.traningandroid.constant.Constant;
import com.atom.traningandroid.converter.GenderConverter;
import com.atom.traningandroid.converter.RoleConverter;
import com.atom.traningandroid.model.Gender;
import com.atom.traningandroid.model.GenderList;
import com.atom.traningandroid.model.Role;
import com.atom.traningandroid.model.RoleList;
import com.atom.traningandroid.model.User;
import com.atom.traningandroid.retrofit.RetrofitProvider;
import com.atom.traningandroid.utils.AppUtils;
import com.atom.traningandroid.utils.TokenUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

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
    private Button back;
    private Integer genderId;
    private Integer authorityId;
    private Integer isAdmin = 0;
    private User paramUser;
    private Role currentRole = null;
    private Gender currentGender = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();

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
        back = (Button) findViewById(R.id.home);

        this.admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) isAdmin = 1;
                else isAdmin = 0;
            }
        });

        Intent intent = getIntent();
        this.paramUser = (User) intent.getSerializableExtra("user");
        if (this.paramUser != null) {
            Log.d(LOG_TAG, this.paramUser.toString());
            actionBar.setTitle(getString(R.string.title_edit));
            this.prepareData(this.paramUser);
        }else{
            actionBar.setTitle(getString(R.string.title_activity_signup));
        }

        this.getRoles();
        this.getGenders();

        this.signupBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signup();
            }
        });

//        this.back.setOn

    }

    public void prepareData(User u) {
        this.userId.setText(u.getUserId());
        this.password.setText(u.getPassword());
        this.familyName.setText(u.getFamilyName());
        this.firstName.setText(u.getFirstName());
        this.age.setText(u.getAge() == null ? "" : u.getAge().toString());
        this.genderId = u.getGenderId();
        this.authorityId = u.getAuthorityId();
        this.isAdmin = u.getAdmin();
        this.admin.setChecked(u.getAdmin() == 0 ? false : true);
        this.signupBtn.setText("更新");
        this.userId.setEnabled(false);

    }

    public void signup() {
        User u = new User();
        u.setUserId(this.userId.getText().length() == 0 ? null : this.userId.getText().toString());
        u.setPassword(this.password.getText().length() == 0 ? null : this.password.getText().toString());
        u.setFamilyName(this.familyName.getText().length() == 0 ? null : this.familyName.getText().toString());
        u.setFirstName(this.firstName.getText().length() == 0 ? null : this.firstName.getText().toString());
        u.setAge(this.age.getText().length() == 0 ? null : Integer.valueOf(this.age.getText().toString()));
        u.setAuthorityId(this.authorityId);
        u.setGenderId(this.genderId);
        u.setAdmin(this.isAdmin);
        u.setEnabled(this.paramUser!=null?this.paramUser.getEnabled():1);

        if (this.paramUser == null) {
            RetrofitProvider.callAPI().createUser(TokenUtils.getInstance().getToken(), u)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                            afterSignup(response);
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            t.printStackTrace();
                            errorMsg.setText(t.getMessage());
                        }
                    });
        } else {
            RetrofitProvider.callAPI().updateUser(TokenUtils.getInstance().getToken(), u)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                            afterSignup(response);
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            t.printStackTrace();
                            errorMsg.setText(t.getMessage());
                        }
                    });
        }

    }

    public void afterSignup(retrofit2.Response<User> response) {
        if (response.code() == 200) {
            Intent intent = new Intent(SignupActivity.this, SearchActivity.class);
            AppUtils.noticeMessage(SignupActivity.this, (paramUser == null ? "登録" : "更新") + "完了しました");
            startActivity(intent);
        } else {
            errorMsg.setText(AppUtils.getErrorString(response));
        }
    }

    public void getRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(null, "役職・すべて"));
        RetrofitProvider.callAPI().findAllRoles(TokenUtils.getInstance().getToken())
                .enqueue(new Callback<RoleList>() {
                    @Override
                    public void onResponse(Call<RoleList> call, retrofit2.Response<RoleList> response) {
                        if (response.code() == 200) {
                            roles.addAll(response.body().getRoles());
                            ArrayAdapter<Role> adapter = new ArrayAdapter<Role>(SignupActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    roles);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            role.setAdapter(adapter);
                            if(authorityId!=null) {
                                role.setSelection(getPositionOfCurrentRole(roles));
                            }
                            role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Adapter adapter = parent.getAdapter();
                                    Role r = (Role) adapter.getItem(position);
                                    authorityId = r.getAuthorityId();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            AppUtils.noticeMessage(SignupActivity.this, AppUtils.getErrorString(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<RoleList> call, Throwable t) {
                        t.printStackTrace();
                        AppUtils.noticeMessage(SignupActivity.this, t.getMessage());
                    }
                });
    }

    public void getGenders() {
        List<Gender> genders = new ArrayList<>();
        genders.add(new Gender(null, "性別"));
        RetrofitProvider.callAPI().findAllGenders(TokenUtils.getInstance().getToken())
                .enqueue(new Callback<GenderList>() {
                    @Override
                    public void onResponse(Call<GenderList> call, retrofit2.Response<GenderList> response) {
                        if (response.code() == 200) {
                            genders.addAll(response.body().getGenders());
                            ArrayAdapter<Gender> adapter = new ArrayAdapter<Gender>(SignupActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    genders);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            gender.setAdapter(adapter);

                            if(genderId!=null) {
                                gender.setSelection(getPositionOfCurrentGender(genders));
                            }

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
                        } else {
                            AppUtils.noticeMessage(SignupActivity.this, AppUtils.getErrorString(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<GenderList> call, Throwable t) {
                        t.printStackTrace();
                        AppUtils.noticeMessage(SignupActivity.this, t.getMessage());
                    }
                });

    }

    public Integer getPositionOfCurrentRole(List<Role> roles){
        for(Role r:roles){
            if(r.getAuthorityId()!=null && r.getAuthorityId().equals(this.authorityId)){
                return roles.indexOf(r);
            }
        }
        return null;
    }

    public Integer getPositionOfCurrentGender(List<Gender> genders){
        for(Gender g:genders){
            if(g.getGenderId()!=null && g.getGenderId().equals(this.genderId)){
                return genders.indexOf(g);
            }
        }
        return null;
    }
}