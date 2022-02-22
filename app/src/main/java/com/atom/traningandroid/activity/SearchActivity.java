package com.atom.traningandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.atom.traningandroid.adapter.CustomListAdapter;
import com.atom.traningandroid.R;
import com.atom.traningandroid.RequestSingleton;
import com.atom.traningandroid.constant.Constant;
import com.atom.traningandroid.converter.RoleConverter;
import com.atom.traningandroid.converter.UserConverter;
import com.atom.traningandroid.model.Role;
import com.atom.traningandroid.model.RoleList;
import com.atom.traningandroid.model.User;
import com.atom.traningandroid.model.UserList;
import com.atom.traningandroid.retrofit.RetrofitProvider;
import com.atom.traningandroid.utils.AppUtils;
import com.atom.traningandroid.utils.TokenUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private final String LOG_TAG = "Search Activity";
    private ListView userList;
    private Spinner roleSpinner;
    private SearchView nameSearch;
    private FloatingActionButton menuBtn;
    private Role searchRole = new Role();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.userList = (ListView) findViewById(R.id.userList);
        this.roleSpinner = (Spinner) findViewById(R.id.roleSpinner);
        this.nameSearch = (SearchView) findViewById(R.id.searchByName);
        this.menuBtn = (FloatingActionButton) findViewById(R.id.menu1);
        this.getRoles();
        this.search();
        this.nameSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search();
                return true;
            }
        });

        this.menuBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menuBtnClicked();
            }
        });
    }

    private void menuBtnClicked() {
        PopupMenu popup = new PopupMenu(this, this.menuBtn);
        popup.inflate(R.menu.popup_menu_layout);

        Menu menu = popup.getMenu();
        // com.android.internal.view.menu.MenuBuilder
        Log.i(LOG_TAG, "Menu class: " + menu.getClass().getName());

        // Register Menu Item Click event.
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return menuItemClicked(item, null);
            }
        });

        // Show the PopupMenu.
        popup.show();
    }

    private void userClicked(View v, User u) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.user_action_menu);

        Menu menu = popup.getMenu();
        Log.i(LOG_TAG, "Menu class: " + menu.getClass().getName());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return menuItemClicked(item, u);
            }
        });

        // Show the PopupMenu.
        popup.show();
    }

    private boolean menuItemClicked(MenuItem item, User u) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.signup:
                intent = new Intent(SearchActivity.this, SignupActivity.class);
                break;
            case R.id.statistic:
                intent = new Intent(SearchActivity.this, StatisticActivity.class);
                break;
            case R.id.profile:
                intent = new Intent(SearchActivity.this, DetailActivity.class);
                break;
            case R.id.edit:
                intent = new Intent(SearchActivity.this, SignupActivity.class);
                intent.putExtra("user", u);
                break;
            case R.id.delete:
                intent = new Intent(SearchActivity.this, DeleteConfirmActivity.class);
                intent.putExtra("user", u);
                break;
            default: //detail
                intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra("user", u);
                break;
        }
        startActivity(intent);
        return true;
    }

    public void getRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(null, "役職・すべて"));
        RetrofitProvider.callAPI().findAllRoles(TokenUtils.getInstance().getToken())
                .enqueue(new Callback<RoleList>() {
                    @Override
                    public void onResponse(Call<RoleList> call, Response<RoleList> response) {
                        if (response.code() == 200) {
                            roles.addAll(response.body().getRoles());
                            ArrayAdapter<Role> adapter = new ArrayAdapter<Role>(SearchActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    roles);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            roleSpinner.setAdapter(adapter);
                            roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    onItemSelectedHandler(parent, view, position, id);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        } else {
                            AppUtils.noticeMessage(SearchActivity.this, AppUtils.getErrorString(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<RoleList> call, Throwable t) {
                        t.printStackTrace();
                        AppUtils.noticeMessage(SearchActivity.this, t.getMessage());
                    }
                });
    }

    public void search() {
        User u = new User(this.nameSearch.getQuery().toString(), this.searchRole.getAuthorityId());
        RetrofitProvider.callAPI().search(TokenUtils.getInstance().getToken(), u)
                .enqueue(new Callback<UserList>() {
                    @Override
                    public void onResponse(Call<UserList> call, retrofit2.Response<UserList> response) {
                        if (response.code() == 200) {
                            if(response.body()==null){
                                userList.setAdapter(new CustomListAdapter(SearchActivity.this, new ArrayList<User>()));
                                return;
                            }
                            List<User> users = response.body().getUsers();
                            userList.setAdapter(new CustomListAdapter(SearchActivity.this, users));
                            userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                                    Object o = userList.getItemAtPosition(position);
                                    User u = (User) o;
                                    userClicked(v, u);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<UserList> call, Throwable t) {
                        t.printStackTrace();
//                        AppUtils.noticeMessage(SearchActivity.this, t.getMessage());
                        userList.setAdapter(new CustomListAdapter(SearchActivity.this, new ArrayList<User>()));
                    }
                });

    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
        this.searchRole = (Role) adapter.getItem(position);
        this.search();
    }

}