package com.atom.traningandroid.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.atom.traningandroid.R;
import com.atom.traningandroid.adapter.StatisticAdapter;
import com.atom.traningandroid.adapter.StatisticAgeAdapter;
import com.atom.traningandroid.constant.Constant;
import com.atom.traningandroid.converter.StatisticConverter;
import com.atom.traningandroid.model.Role;
import com.atom.traningandroid.model.Statistic;
import com.atom.traningandroid.model.StatisticList;
import com.atom.traningandroid.retrofit.RetrofitProvider;
import com.atom.traningandroid.utils.AppUtils;
import com.atom.traningandroid.utils.TokenUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class StatisticActivity extends BaseActivity {

    private final String LOG_TAG = "Statistic Activity";
    private List<Statistic> statistics = new ArrayList<>();
    private Spinner spinner;
    private ListView statisticTable;
    private Button statisticButton;
    private Integer type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        checkLogin();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.spinner = (Spinner) findViewById(R.id.s_spinner);
        this.statisticTable = (ListView) findViewById(R.id.statistic);
        this.statisticButton = (Button) findViewById(R.id.s_button);
        this.loadSpinner();
        this.statisticButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                statistic();
            }
        });

    }

    private void statistic() {
        statistics = new ArrayList<>();
        if (this.type == 1) {
            statistics.add(new Statistic(0, "役職", "女", "男", "未登録"));
        } else {
            statistics.add(new Statistic("役職", "0-19", "20以上", "未登録"));
        }

        RetrofitProvider.callAPI().statistic(TokenUtils.getInstance().getToken())
                .enqueue(new Callback<StatisticList>() {
                    @Override
                    public void onResponse(Call<StatisticList> call, retrofit2.Response<StatisticList> response) {
                        if (response.code() == 200) {
                            if(response.body()==null){
                                return;
                            }
                            statistics.addAll(response.body().getStatistics());
                            if (type == 1) {
                                statisticTable.setAdapter(new StatisticAdapter(StatisticActivity.this, statistics));
                            } else {
                                statisticTable.setAdapter(new StatisticAgeAdapter(StatisticActivity.this, statistics));
                            }
                        } else {
                            AppUtils.noticeMessage(StatisticActivity.this, AppUtils.getErrorString(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<StatisticList> call, Throwable t) {
                        t.printStackTrace();
                        AppUtils.noticeMessage(StatisticActivity.this, t.getMessage());
                    }
                });

    }

    private void loadSpinner() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, "役職と性別毎に集計"));
        roles.add(new Role(2, "役職と年齢毎に集計"));
        ArrayAdapter<Role> adapter = new ArrayAdapter<Role>(this,
                android.R.layout.simple_spinner_item,
                roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onSelectType(parent, view, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onSelectType(AdapterView<?> adapterView, View view, int position) {
        Adapter adapter = adapterView.getAdapter();
        Role r = (Role) adapter.getItem(position);
        this.type = r.getAuthorityId();
        this.statistic();
    }
}