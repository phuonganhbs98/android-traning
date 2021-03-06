package com.atom.traningandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atom.traningandroid.R;
import com.atom.traningandroid.adapter.ListUserAdapter;
import com.atom.traningandroid.model.Role;
import com.atom.traningandroid.model.RoleList;
import com.atom.traningandroid.model.User;
import com.atom.traningandroid.model.UserList;
import com.atom.traningandroid.retrofit.RetrofitProvider;
import com.atom.traningandroid.utils.AppUtils;
import com.atom.traningandroid.utils.TokenUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity {

    private final String LOG_TAG = "Search Activity";
    private RecyclerView userList;
    private ListUserAdapter userAdapter;
    private List<User> users;
    private boolean isLoading;
    private boolean isLastPage;
    //    private int totalPage = 2;
    private int currentPage = 1;

    private Spinner roleSpinner;
    private SearchView nameSearch;
    private FloatingActionButton menuBtn;

    private Role searchRole = new Role();

    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isLastPage) {
                    currentPage += 1;
                    search();
                }
                isLoading = false;
            }
        }, 1500);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        super.setDetector(this, this);
        super.setLoad();
        checkLogin();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.userList = (RecyclerView) findViewById(R.id.rec_user_list);
        this.roleSpinner = (Spinner) findViewById(R.id.roleSpinner);
        this.nameSearch = (SearchView) findViewById(R.id.searchByName);
        this.menuBtn = (FloatingActionButton) findViewById(R.id.menu1);
        this.getRoles();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        userList.setLayoutManager(linearLayoutManager);
        userAdapter = new ListUserAdapter();
        userList.setAdapter(userAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        userList.addItemDecoration(itemDecoration);

        userList.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                loadNextPage();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });

        userAdapter.setItemClickListener(new ListUserAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                User u = users.get(position);
                userClicked(view, u);
            }
        });

        this.nameSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getFirstPageData();
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

    @Override
    protected void onStart() {
        super.onStart();
        getFirstPageData();
    }

    @Override
    public void onBackPressed() {
        String preAct = getIntent().getStringExtra("activity");
        if (preAct != null && preAct.equals("login") && TokenUtils.getInstance().getToken() != null) {
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    private void menuBtnClicked() {
        PopupMenu popup = new PopupMenu(this, this.menuBtn);
        popup.inflate(R.menu.popup_menu_layout);

        Menu menu = popup.getMenu();
        Log.i(LOG_TAG, "Menu class: " + menu.getClass().getName());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return menuItemClicked(item, null);
            }
        });

        // Show the PopupMenu.
        popup.show();
    }

    public void userClicked(View v, User u) {
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
        roles.add(new Role(null, "??????????????????"));
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
                        } else if (response.code() != 500) {
                            AppUtils.noticeMessage(SearchActivity.this, AppUtils.getErrorString(response));
                        } else {
                            AppUtils.noticeMessage(SearchActivity.this, AppUtils.getUnknownErrorString());
                        }
                    }

                    @Override
                    public void onFailure(Call<RoleList> call, Throwable t) {
                        t.printStackTrace();
                        AppUtils.noticeMessage(SearchActivity.this, AppUtils.getUnknownErrorString());
                    }
                });
    }

    public void search() {
        User u = new User(this.nameSearch.getQuery().toString(), this.searchRole.getAuthorityId());
        RetrofitProvider.callAPI().search(TokenUtils.getInstance().getToken(), u, currentPage)
                .enqueue(new Callback<UserList>() {
                    @Override
                    public void onResponse(Call<UserList> call, Response<UserList> response) {
                        System.out.println("url-search: " + call.request().toString());
                        if (response.code() == 200) {
                            List<User> list = null;
                            if (response.body() == null) {
                                System.out.println("Khong co data nao");
                                isLastPage = true;
                            } else {
                                list = response.body().getUsers();
                                list = removeNullUser(list);
                                if(list.size()<10){
                                    isLastPage = true;
                                }
                            }
                            updateData(list);
                        } else if (response.code() == 500) {
                            AppUtils.noticeMessage(SearchActivity.this, AppUtils.getUnknownErrorString());
                        } else {
                            AppUtils.noticeMessage(SearchActivity.this, AppUtils.getErrorString(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<UserList> call, Throwable t) {
                        t.printStackTrace();
                        AppUtils.noticeMessage(SearchActivity.this, AppUtils.getUnknownErrorString());
//                        userList.setAdapter(new CustomListAdapter(Search2Activity.this, new ArrayList<User>()));
                    }
                });

    }

    public void updateData(List<User> list) {
        if (list != null) {
            if (currentPage == 1) {
                users = list;
                userAdapter.setData(users);
                userAdapter.addFooterLoading();
            } else {
                userAdapter.removeFooterLoading();
                users.addAll(list);
                userAdapter.setData(users);
                userAdapter.addFooterLoading();
            }
            userAdapter.notifyDataSetChanged();
        }else if(currentPage==1){
            users = new ArrayList<>();
            userAdapter.setData(users);
            userAdapter.addFooterLoading();
        }
        if (isLastPage) {
            userAdapter.removeFooterLoading();
        }
    }

    public List<User> removeNullUser(List<User> users) {
        User nullUser = null;
        for (User u : users) {
            if (u.getAdmin() == null) {
                nullUser = u;
            }
        }
        if (nullUser != null) {
            users.remove(nullUser);
        }
        return users;
    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
        this.searchRole = (Role) adapter.getItem(position);
        this.getFirstPageData();
    }

    @Override
    public void swipeDown() {
        super.swipeDown();
        getFirstPageData();
    }

    public void getFirstPageData() {
        currentPage = 1;
        isLastPage = false;
        this.search();
    }


}