package com.atom.traningandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atom.traningandroid.R;
import com.atom.traningandroid.model.User;

import java.util.List;

public class ListUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;

    private List<User> listUser;
    private boolean isLoadingAdd;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setData(List<User> list) {
        this.listUser = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (listUser != null && position == listUser.size() - 1 && isLoadingAdd) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ITEM == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_layout, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM) {
            User u = listUser.get(position);
            UserViewHolder userViewHolder = (UserViewHolder) holder;
            userViewHolder.name.setText(u.getFamilyName() + " " + u.getFirstName());
            userViewHolder.gender.setText(u.getGenderName() == null ? "" : u.getGenderName());
            userViewHolder.role.setText(u.getAdmin() == null ? "" : ((u.getAdmin() == 1 ? "★" : "") + (u.getRoleName() == null ? "" : u.getRoleName())));
//            userViewHolder.role.setText(u.getRoleName() == null ? "" : u.getRoleName());
            ((UserViewHolder) holder).setItemClickListener(this.itemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        if (listUser != null) {
            return listUser.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private TextView gender;
        private TextView role;

        private ItemClickListener itemClickListener;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            gender = (TextView) itemView.findViewById(R.id.gender);
            role = (TextView) itemView.findViewById(R.id.role);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition());
        }

    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.load_more);
        }

    }

    public void addFooterLoading() {
        isLoadingAdd = true;
        listUser.add(new User());
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;
        if(listUser.size()>0) {
            int position = listUser.size() - 1;
            User u = listUser.get(position);
            if (u != null) {
                listUser.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

}
