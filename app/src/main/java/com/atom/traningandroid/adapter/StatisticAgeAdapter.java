package com.atom.traningandroid.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atom.traningandroid.R;
import com.atom.traningandroid.model.Statistic;

import java.util.List;

public class StatisticAgeAdapter extends BaseAdapter {

    private List<Statistic> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public StatisticAgeAdapter(Context aContext, List<Statistic> listData) {
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
            if(i==0){
                holder.col1.setTypeface(Typeface.DEFAULT_BOLD);
                holder.col2.setTypeface(Typeface.DEFAULT_BOLD);
                holder.col3.setTypeface(Typeface.DEFAULT_BOLD);
                holder.col4.setTypeface(Typeface.DEFAULT_BOLD);
                holder.col1.setTextSize(16);
                holder.col2.setTextSize(16);
                holder.col3.setTextSize(16);
                holder.col4.setTextSize(16);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Statistic s = this.listData.get(i);
        holder.col1.setText(s.getAuthorityName());
        holder.col2.setText(s.getTotalAgeSmaller19()==null?"0":s.getTotalAgeSmaller19());
        holder.col3.setText(s.getTotalAgeGreater20()==null?"0":s.getTotalAgeGreater20());
        holder.col4.setText(s.getTotalUnknownAge()==null?"0":s.getTotalUnknownAge());

        return convertView;
    }

    static class ViewHolder {
        TextView col1;
        TextView col2;
        TextView col3;
        TextView col4;
    }
}
