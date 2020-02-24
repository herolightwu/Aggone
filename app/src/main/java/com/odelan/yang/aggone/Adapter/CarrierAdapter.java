package com.odelan.yang.aggone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.odelan.yang.aggone.Model.Career;
import com.odelan.yang.aggone.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CarrierAdapter extends RecyclerView.Adapter<CarrierAdapter.ViewHolder> {
    Context context;
    List<Career> carriers;
    EventListener listener;
    public CarrierAdapter(Context context, List<Career> carriers, EventListener listener) {
        this.context = context;
        this.carriers = carriers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarrierAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_carrier, viewGroup, false);
        CarrierAdapter.ViewHolder vh = new CarrierAdapter.ViewHolder(v);
        return vh;
    }

    public void setCarriers(List<Career> carriers) {
        this.carriers = carriers;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if (position == 0 || position == getItemCount() - 1) {
            viewHolder.img_circle.setImageResource(R.mipmap.profile_carrier_end);
        } else {
            viewHolder.img_circle.setImageResource(R.mipmap.profile_carrier_middle);
        }
        if (position == getItemCount() - 1) {
            viewHolder.img_line.setVisibility(View.GONE);
        }
        viewHolder.txt_day.setText(String.valueOf(carriers.get(position).day));

        Calendar calendar = Calendar.getInstance();
        calendar.set(carriers.get(position).year, carriers.get(position).month - 1, carriers.get(position).day);
        String weekName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());

        viewHolder.txt_week.setText(weekName);
        viewHolder.txt_location.setText(carriers.get(position).location);

        viewHolder.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carriers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_parent;
        ImageView img_line;
        ImageView img_circle;
        TextView txt_day;
        TextView txt_week;
        TextView txt_opponent;
        TextView txt_location;
        TextView txt_tournament;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent   =     itemView.findViewById(R.id.layout_parent);
            img_line        =     itemView.findViewById(R.id.img_line);
            img_circle      =     itemView.findViewById(R.id.img_circle);
            txt_day         =     itemView.findViewById(R.id.txt_day);
            txt_week        =     itemView.findViewById(R.id.txt_week);
            txt_opponent    =     itemView.findViewById(R.id.txt_opponent);
            txt_location    =     itemView.findViewById(R.id.txt_location);
            txt_tournament  =     itemView.findViewById(R.id.txt_tournament);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
    }
}