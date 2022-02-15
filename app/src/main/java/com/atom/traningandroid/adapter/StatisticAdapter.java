package com.atom.traningandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atom.traningandroid.R;
import com.atom.traningandroid.entity.Statistic;

import java.util.List;

public class StatisticAdapter extends BaseAdapter {

    private List<Statistic> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public StatisticAdapter(Context aContext, List<Statistic> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.statistic_layout, null);
            holder = new ViewHolder();
            holder.col1 = (TextView) convertView.findViewById(R.id.st_col1);
            holder.col2 = (TextView) convertView.findViewById(R.id.st_col2);
            holder.col3 = (TextView) convertView.findViewById(R.id.st_col3);
            holder.col4 = (TextView) convertView.findViewById(R.id.st_col4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Statistic s = this.listData.get(i);
        holder.col1.setText(s.getAuthorityName());
        holder.col2.setText(s.getTotalMale()==null?"0":s.getTotalMale());
        holder.col3.setText(s.getTotalFemale()==null?"0":s.getTotalFemale());
        holder.col4.setText(s.getTotalUnknown()==null?"0":s.getTotalUnknown());

        return convertView;
    }

    static class ViewHolder {
        TextView col1;
        TextView col2;
        TextView col3;
        TextView col4;
    }
}
