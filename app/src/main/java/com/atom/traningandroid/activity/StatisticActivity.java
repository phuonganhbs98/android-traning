package com.atom.traningandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.atom.traningandroid.adapter.CustomListAdapter;
import com.atom.traningandroid.R;
import com.atom.traningandroid.RequestSingleton;
import com.atom.traningandroid.adapter.StatisticAdapter;
import com.atom.traningandroid.adapter.StatisticAgeAdapter;
import com.atom.traningandroid.constant.Constant;
import com.atom.traningandroid.converter.StatisticConverter;
import com.atom.traningandroid.entity.Role;
import com.atom.traningandroid.entity.Statistic;
import com.atom.traningandroid.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StatisticActivity extends AppCompatActivity {

    private final String LOG_TAG = "Statistic Activity";
    private final String statisticUrl = Constant.API + "/users/statistics";
    private final StatisticConverter converter = new StatisticConverter();
    private List<Statistic> statistics = new ArrayList<>();
    private Spinner spinner;
    private ListView statisticTable;
    private Integer type=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        this.spinner = (Spinner) findViewById(R.id.s_spinner);
        this.statisticTable = (ListView) findViewById(R.id.statistic);
        this.loadSpinner();
//        this.statistic();
    }

    private void statistic(){
        statistics=new ArrayList<>();
        if(this.type==1){
            statistics.add(new Statistic(0, "役職","女","男", "未登録"));
        }else{
            statistics.add(new Statistic("役職","0-19","20以上", "未登録"));
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, statisticUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray arr;
                        try {
                            if (response.has("statistics")) {
                                if (response.optJSONArray("statistics") != null) {
                                    arr = response.getJSONArray("statistics");
                                    statistics.addAll(converter.convertToList(arr));
                                } else {
                                    statistics.add(converter.convertToEntity(response.getJSONObject("statistic")));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(type==1) {
                            statisticTable.setAdapter(new StatisticAdapter(StatisticActivity.this, statistics));
                        }else{
                            statisticTable.setAdapter(new StatisticAgeAdapter(StatisticActivity.this, statistics));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, error.getMessage());
//                statisticTable.setAdapter(new StatisticAdapter(StatisticActivity.this, statistics));
                statisticTable.setAdapter(new StatisticAgeAdapter(StatisticActivity.this, statistics));
            }
        });
        // Add the request to the RequestQueue.
        RequestSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void loadSpinner(){
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

    private void onSelectType(AdapterView<?> adapterView, View view, int position){
        Adapter adapter = adapterView.getAdapter();
        Role r = (Role) adapter.getItem(position);
        this.type = r.getAuthorityId();
        this.statistic();
    }
}