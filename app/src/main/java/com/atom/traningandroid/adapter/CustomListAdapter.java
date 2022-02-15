package com.atom.traningandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atom.traningandroid.R;
import com.atom.traningandroid.entity.User;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {

    private List<User> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListAdapter(Context aContext,  List<User> listData) {
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
            convertView = layoutInflater.inflate(R.layout.list_user_layout, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.gender = (TextView) convertView.findViewById(R.id.gender);
            holder.role = (TextView) convertView.findViewById(R.id.role);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User u = this.listData.get(i);
        holder.name.setText(u.getFamilyName() + " " + u.getFirstName());
        holder.gender.setText(u.getGenderName());
        holder.role.setText((u.getAdmin()==1?"★":"")+u.getRoleName());

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView gender;
        TextView role;
    }
}
