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

import com.odelan.yang.aggone.Model.Sport;
import com.odelan.yang.aggone.R;

import java.util.List;

public class SportWheelAdapter extends RecyclerView.Adapter<SportWheelAdapter.ViewHolder> {
    Context context;
    List<Sport> sports;
    EventListener listener;
    public SportWheelAdapter(Context context, List<Sport> sports, EventListener listener) {
        this.context = context;
        this.sports = sports;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SportWheelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_sport_wheel, viewGroup, false);
        SportWheelAdapter.ViewHolder vh = new SportWheelAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        if (sports.get(position).selected) {
            viewHolder.txt_name.setTextColor(context.getResources().getColor(R.color.wheel_active));
            viewHolder.txt_name.setTextSize(16);
            if (listener != null) {
                listener.onSelectItem(position);
            }
        } else {
            viewHolder.txt_name.setTextColor(context.getResources().getColor(R.color.wheel_normal));
            viewHolder.txt_name.setTextSize(14);
        }
        viewHolder.txt_name.setText(sports.get(position).name);
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_parent;
        TextView txt_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent   =     itemView.findViewById(R.id.layout_parent);
            txt_name        =     itemView.findViewById(R.id.txt_name);
        }
    }

    public interface EventListener {
        void onSelectItem(int index);
    }
}
