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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.atom.traningandroid.adapter.CustomListAdapter;
import com.atom.traningandroid.R;
import com.atom.traningandroid.RequestSingleton;
import com.atom.traningandroid.constant.Constant;
import com.atom.traningandroid.converter.RoleConverter;
import com.atom.traningandroid.converter.UserConverter;
import com.atom.traningandroid.entity.Role;
import com.atom.traningandroid.entity.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private final String searchUrl = Constant.API + "/users/search";
    private final String LOG_TAG = "Search Activity";
    private ListView userList;
    private Spinner roleSpinner;
    private SearchView nameSearch;
    private FloatingActionButton menuBtn;
    private Role searchRole = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.userList = (ListView) findViewById(R.id.userList);
        this.roleSpinner = (Spinner) findViewById(R.id.roleSpinner);
        this.nameSearch = (SearchView) findViewById(R.id.searchByName);
        this.menuBtn = (FloatingActionButton) findViewById(R.id.menu1);
        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject();
            jsonObj.put("familyName", "");
            jsonObj.put("firstName", "");
            jsonObj.put("authorityId", null);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        this.search(searchUrl, jsonObj, this);
        this.getRoles(Constant.API + "/roles", this);

        this.nameSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onSearch();
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
        // com.android.internal.view.menu.MenuBuilder
        Log.i(LOG_TAG, "Menu class: " + menu.getClass().getName());

        // Register Menu Item Click event.
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

    public void getRoles(String url, Context c) {
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Role> roles = new ArrayList<>();
                        roles.add(new Role(null, "役職・すべて"));
                        JSONArray arr;
                        try {
                            if (response.has("role")) {
                                arr = response.getJSONArray("role");
                                roles.addAll(RoleConverter.convertToRoleList(arr));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<Role> adapter = new ArrayAdapter<Role>(c,
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

    public void search(String url, JSONObject objRequest, Context c) {
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, objRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<User> users = new ArrayList<>();
                        JSONArray arr;
                        try {
                            if (response.has("user")) {
                                if (response.optJSONArray("user") != null) {
                                    arr = response.getJSONArray("user");
                                    users = UserConverter.convertToUserList(arr);
                                } else {
                                    users.add(UserConverter.convertToUser(response.getJSONObject("user")));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        userList.setAdapter(new CustomListAdapter(c, users));
                        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                                Object o = userList.getItemAtPosition(position);
                                User u = (User) o;
                                userClicked(v, u);
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!" + error);
                System.out.println("........pa: " + "That didn't work!" + error);
                userList.setAdapter(new CustomListAdapter(c, new ArrayList<User>()));
            }
        });
        // Add the request to the RequestQueue.
        RequestSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
        this.searchRole = (Role) adapter.getItem(position);
        this.onSearch();
    }

    private void onSearch() {
        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject();
            jsonObj.put("familyName", nameSearch.getQuery());
            jsonObj.put("firstName", nameSearch.getQuery());
            jsonObj.put("authorityId", this.searchRole == null ? null : this.searchRole.getAuthorityId());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        this.search(searchUrl, jsonObj, this);
    }
}