package com.odelan.yang.aggone.Adapter.Search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PeopleSearchAdapter extends RecyclerView.Adapter<PeopleSearchAdapter.UserViewHolder> {
    Context context;
    List<User> users;
    EventListener listener;

    public PeopleSearchAdapter(Context context, List<User> data, EventListener listener) {
        this.context = context;
        this.users = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext()) .inflate(R.layout.item_search_user, viewGroup, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder viewHolder, final int position) {
        User one = users.get(position);
        if (one.photo_url != null && !one.photo_url.isEmpty()) {
            if (one.photo_url.contains("http")){
                Glide.with(context).load(one.photo_url).into(viewHolder.img_avatar);
            } else {
                Glide.with(context).load(API.baseUrl + API.imgDirUrl + one.photo_url).into(viewHolder.img_avatar);
            }
        } else {
            viewHolder.img_avatar.setImageResource(R.mipmap.profile);
        }
        if (one.username!= null){
            viewHolder.txt_name.setText(one.username);
        } else{
            viewHolder.txt_name.setText("");
        }
        if (one.category != null){
            viewHolder.txt_category.setText(one.category);
        } else {
            viewHolder.txt_category.setText("");
        }
        viewHolder.btn_profile.setOnClickListener(view -> {
            if (listener != null)
                listener.onClickProfile(position);
        });

        viewHolder.img_avatar.setOnClickListener(view -> {
            if (listener != null)
                listener.onClickProfile(position);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setDataList(List<User> data){
        users = data;
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;
        TextView txt_category;
        CircleImageView img_avatar;
        TextView btn_profile;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_category = itemView.findViewById(R.id.txt_category);
            img_avatar = itemView.findViewById(R.id.img_avatar);
            btn_profile = itemView.findViewById(R.id.btn_view_profile);
        }
    }

    public interface EventListener {
        void onClickProfile(int index);
    }
}
