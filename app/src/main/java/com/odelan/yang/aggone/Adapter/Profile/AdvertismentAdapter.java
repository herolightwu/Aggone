package com.odelan.yang.aggone.Adapter.Profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Model.Admob;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.AppData;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdvertismentAdapter extends RecyclerView.Adapter<AdvertismentAdapter.ViewHolder> {
    Context context;
    List<Admob> admobs = new ArrayList<>();
    EventListener listener;
    public AdvertismentAdapter(Context context, List<Admob> admobs, EventListener listener) {
        this.context = context;
        this.admobs = admobs;
        this.listener = listener;
    }

    public void setData(List<Admob> admobs) {
        this.admobs.clear();
        this.admobs.addAll(admobs);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_advertisment, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Admob admob = admobs.get(position);
        if (admob.user.photo_url != null && !admob.user.photo_url.isEmpty()) {
            if (admob.user.photo_url.contains("http")){
                Glide.with(context).load(admob.user.photo_url).into(viewHolder.img_avata);
            } else{
                Glide.with(context).load(API.baseUrl + API.imgDirUrl + admob.user.photo_url).into(viewHolder.img_avata);
            }
        } else {
            viewHolder.img_avata.setImageResource(R.mipmap.default_avata);
        }
        viewHolder.txt_name.setText(admob.user.username);
        viewHolder.btn_sport.setText(((BaseActivity)context).getSportName(admob.sport_id));
        viewHolder.btn_position.setText(admob.position);
        viewHolder.txt_description.setText(admob.description);
        if(admob.user != null && admob.user.id.equals(AppData.user.id)){
            viewHolder.btn_delete.setVisibility(View.VISIBLE);
        } else{
            viewHolder.btn_delete.setVisibility(View.GONE);
        }
        viewHolder.btn_delete.setOnClickListener(v -> {
            if (listener != null) listener.onClickDelete(admob);
        });
        viewHolder.layout_parent.setOnClickListener(v -> {
            if (listener != null) listener.onClickItem(admob);
        });
    }

    @Override
    public int getItemCount() {
        return admobs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_parent;
        CircleImageView img_avata;
        TextView txt_name;
        Button btn_position;
        Button btn_sport;
        TextView txt_description;
        ImageView btn_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent       =     itemView.findViewById(R.id.layout_parent);
            img_avata           =     itemView.findViewById(R.id.img_avata);
            txt_name            =     itemView.findViewById(R.id.txt_name);
            btn_position        =     itemView.findViewById(R.id.btn_position);
            btn_sport           =     itemView.findViewById(R.id.btn_sport);
            txt_description     =     itemView.findViewById(R.id.txt_description);
            btn_delete          =       itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface EventListener {
        void onClickItem(Admob admob);
        void onClickDelete(Admob admob);
    }
}
