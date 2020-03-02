package com.odelan.yang.aggone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.odelan.yang.aggone.Model.Sport;
import com.odelan.yang.aggone.R;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {
    Context context;
    List<Pair<String, String>> details;
    EventListener listener;
    public DetailsAdapter(Context context, List<Pair<String, String>> details, EventListener listener) {
        this.context = context;
        this.details = details;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_detail, viewGroup, false);
        DetailsAdapter.ViewHolder vh = new DetailsAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.txt_name.setText(details.get(position).first);
        viewHolder.txt_value.setText(details.get(position).second);
        if (position % 2 == 0) {
            viewHolder.layout_parent.setGravity(Gravity.LEFT);
        } else {
            viewHolder.layout_parent.setGravity(Gravity.RIGHT);
        }
        viewHolder.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public void setDetails(List<Pair<String, String>> details) {
        this.details = details;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_parent;
        TextView txt_name;
        TextView txt_value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent   =     itemView.findViewById(R.id.layout_parent);
            txt_name        =     itemView.findViewById(R.id.txt_name);
            txt_value       =     itemView.findViewById(R.id.txt_value);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
    }
}
