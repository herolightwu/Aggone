package com.odelan.yang.aggone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Model.Career;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.AppData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CareerAdapter extends RecyclerView.Adapter<CareerAdapter.ViewHolder> {
    Context context;
    List<Career> careers;
    EventListener listener;
    public CareerAdapter(Context context, List<Career> careers, EventListener listener) {
        this.context = context;
        this.careers = careers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CareerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_career, viewGroup, false);
        CareerAdapter.ViewHolder vh = new CareerAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Career career = careers.get(position);
        if(career.logo != null){
            if(career.logo.contains("http")){
                Glide.with(context).load(career.logo).into(viewHolder.img_logo);
            } else{
                Glide.with(context).load(API.baseUrl + API.imgDirUrl + career.logo).into(viewHolder.img_logo);
            }
        } else{
            viewHolder.img_logo.setImageDrawable(null);
        }

        viewHolder.txt_location.setText(career.club + " - " + career.location);
        viewHolder.txt_position.setText(career.position);

        Date from = new Date(career.year - 1900, career.month - 1, career.day);
        Date to = new Date();
        if (career.tyear > 0) {
            to = new Date(career.tyear - 1900, career.tmonth - 1, career.tday);
        }
        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        viewHolder.txt_date.setText(dateFormat.format(from) + " - " + (career.tyear > 0 ? dateFormat.format(to) : ""));

        viewHolder.txt_sport.setText(career.sport_id);
        if (career.user_id.equals(AppData.user.id)) {
            viewHolder.btn_delete.setOnClickListener(v -> {
                if (listener != null) listener.onClickDelete(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return careers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout btn_delete;
        ImageView img_logo;
        TextView txt_location;
        TextView txt_position;
        TextView txt_date;
        TextView txt_sport;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            img_logo = itemView.findViewById(R.id.img_logo);
            txt_location = itemView.findViewById(R.id.txt_location);
            txt_position = itemView.findViewById(R.id.txt_position);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_sport = itemView.findViewById(R.id.txt_sport);
        }
    }

    public interface EventListener {
        void onClickDelete(int index);
    }
}
