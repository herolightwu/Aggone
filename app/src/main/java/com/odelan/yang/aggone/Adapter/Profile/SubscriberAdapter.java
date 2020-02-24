package com.odelan.yang.aggone.Adapter.Profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubscriberAdapter extends RecyclerView.Adapter<SubscriberAdapter.ViewHolder> {
    Context context;
    List<User> users;
    EventListener listener;
    public SubscriberAdapter(Context context, List<User> users, EventListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubscriberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_subscriber, viewGroup, false);
        SubscriberAdapter.ViewHolder vh = new SubscriberAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if (!users.get(position).photo_url.isEmpty()) {
            if (users.get(position).photo_url.contains("http")){
                Glide.with(context).load(users.get(position).photo_url).into(viewHolder.img_avata);
            } else {
                Glide.with(context).load(API.baseUrl + API.imgDirUrl + users.get(position).photo_url).into(viewHolder.img_avata);
            }
        }
        viewHolder.txt_name.setText(users.get(position).username);
        viewHolder.txt_position.setText(users.get(position).position);
        viewHolder.txt_club.setText(users.get(position).club);
        viewHolder.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_parent;
        CircleImageView img_avata;
        TextView txt_name;
        TextView txt_club;
        TextView txt_position;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent       =     itemView.findViewById(R.id.layout_parent);
            img_avata           =     itemView.findViewById(R.id.img_avata);
            txt_name            =     itemView.findViewById(R.id.txt_name);
            txt_position        =     itemView.findViewById(R.id.txt_position);
            txt_club            =     itemView.findViewById(R.id.txt_club);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
    }
}
