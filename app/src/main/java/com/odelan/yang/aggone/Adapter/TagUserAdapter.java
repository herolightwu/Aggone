package com.odelan.yang.aggone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.AppData;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TagUserAdapter extends RecyclerView.Adapter<TagUserAdapter.ViewHolder> implements Filterable {

    Context context;
    List<User> users;
    List<User> filteredList;
    EventListener listener;
    public Map<String, Boolean> sel_users = new HashMap<>();
    UserFilter userFilter;
    int mode = 0;

    public TagUserAdapter(Context context, List<User> data, int mode, EventListener listener) {
        this.context = context;
        this.users = data;
        this.listener = listener;
        this.mode = mode;
        filteredList = data;
        configureMap();
    }

    private void configureMap(){
        Boolean bVal = false;
        sel_users.clear();
        for (User one : users){
            sel_users.put(one.id, bVal);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_tag_user, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.tv_name.setText(filteredList.get(position).username);
        viewHolder.tv_position.setText(filteredList.get(position).position);
        if(filteredList.get(position).photo_url != null && !filteredList.get(position).photo_url.isEmpty()){
            if (filteredList.get(position).photo_url.contains("http")){
                Glide.with(context).load(filteredList.get(position).photo_url).into(viewHolder.img_avatar);
            } else {
                Glide.with(context).load(API.baseUrl + API.imgDirUrl + filteredList.get(position).photo_url).into(viewHolder.img_avatar);
            }
        } else{
            viewHolder.img_avatar.setImageResource(R.mipmap.default_avata);
        }

        if(mode == 0){//choose user
            viewHolder.btn_profile.setVisibility(View.GONE);
            viewHolder.img_check.setVisibility(View.VISIBLE);
            viewHolder.layout_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = filteredList.get(position).id;
                    Boolean bVal = sel_users.get(id);
                    sel_users.put(id, !bVal);
                    notifyDataSetChanged();
                }
            });
            String id = filteredList.get(position).id;
            Boolean bVal = sel_users.get(id);
            if(bVal){
                viewHolder.img_check.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
            } else{
                viewHolder.img_check.setColorFilter(context.getResources().getColor(R.color.sport_normal));
            }
            viewHolder.tv_position.setVisibility(View.GONE);
        } else{//show user
            viewHolder.btn_profile.setVisibility(View.VISIBLE);
            viewHolder.img_check.setVisibility(View.GONE);
            viewHolder.btn_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!= null){
                        listener.onClickProfile(filteredList.get(position));
                    }
                }
            });
            viewHolder.tv_position.setVisibility(View.VISIBLE);
        }

    }

    public void setDataList(List<User> data){
        users = data;
        filteredList = data;
        configureMap();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_avatar, img_check;
        TextView tv_name, tv_position, btn_profile;
        LinearLayout layout_parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_avatar   =     itemView.findViewById(R.id.img_avata);
            tv_name    =     itemView.findViewById(R.id.txt_name);
            tv_position   =     itemView.findViewById(R.id.txt_position);
            btn_profile      =     itemView.findViewById(R.id.btn_profile);
            img_check       =  itemView.findViewById(R.id.img_check);
            layout_parent   =  itemView.findViewById(R.id.layout_parent);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
        void onClickProfile(User user);
    }

    /**
     * Get custom filter
     * @return filter
     */
    @Override
    public Filter getFilter() {
        if (userFilter == null) {
            userFilter = new UserFilter();
        }
        return userFilter;
    }

    /**
     * Custom filter for friend list
     * Filter content in friend list according to the search text
     */
    private class UserFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                List<User> tempList = new ArrayList<User>();
                // search content in friend list
                for (User one : users) {
                    if (one.username.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(one);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = users.size();
                filterResults.values = users;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (List<User>) results.values;
            notifyDataSetChanged();
        }
    }

}
