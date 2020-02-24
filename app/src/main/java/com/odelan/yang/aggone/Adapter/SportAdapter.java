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

public class SportAdapter extends RecyclerView.Adapter<SportAdapter.ViewHolder> {
    Context context;
    List<Sport> sports;
    EventListener listener;
    public SportAdapter(Context context, List<Sport> sports, EventListener listener) {
        this.context = context;
        this.sports = sports;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_sport, viewGroup, false);
        SportAdapter.ViewHolder vh = new SportAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.txt_name.setText(sports.get(position).name);
        viewHolder.txt_detail.setText("Select " + sports.get(position).name);
        viewHolder.img_icon.setImageResource(sports.get(position).icon);
        viewHolder.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_parent;
        TextView txt_name;
        TextView txt_detail;
        ImageView img_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent   =     itemView.findViewById(R.id.layout_parent);
            txt_name        =     itemView.findViewById(R.id.txt_name);
            txt_detail      =     itemView.findViewById(R.id.txt_detail);
            img_icon        =     itemView.findViewById(R.id.img_icon);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
    }
}
