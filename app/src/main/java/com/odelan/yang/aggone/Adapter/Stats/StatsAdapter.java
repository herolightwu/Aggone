package com.odelan.yang.aggone.Adapter.Stats;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Model.Description;
import com.odelan.yang.aggone.Model.Skill;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.List;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {
    Context context;
    List<Skill> skills;
    EventListener listener;
    public StatsAdapter(Context context, List<Skill> skills, EventListener listener) {
        this.context = context;
        this.skills = skills;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_stats, viewGroup, false);
        StatsAdapter.ViewHolder vh = new StatsAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.txt_skill.setText(skills.get(position).description);
        if (skills.get(position).key.equals(Constants.PERFORMANCE)) {
            viewHolder.layout_integer.setVisibility(View.GONE);
            viewHolder.layout_float.setVisibility(View.VISIBLE);
            viewHolder.txt_float_value.setText(String.format("%.1f", (float)skills.get(position).value / 10));
        } else {
            viewHolder.layout_integer.setVisibility(View.VISIBLE);
            viewHolder.layout_float.setVisibility(View.GONE);
            viewHolder.txt_value.setText(String.valueOf(skills.get(position).value));
        }
        viewHolder.layout_float.setOnClickListener(v -> {
            if (listener != null) listener.onClickFloat(position);
        });
        viewHolder.btn_minus.setOnClickListener(v -> {
            if (listener != null) listener.onClickMinus(position);
        });
        viewHolder.btn_plus.setOnClickListener(v -> {
            if (listener != null) listener.onClickPlus(position);
        });
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    public void setDataList(List<Skill> skills){
        this.skills.clear();
        this.skills.addAll(skills);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_parent;
        RelativeLayout layout_integer;
        RelativeLayout layout_float;
        TextView txt_skill;
        TextView txt_value;
        TextView txt_float_value;
        Button btn_plus;
        Button btn_minus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent   =     itemView.findViewById(R.id.layout_parent);
            layout_integer  =     itemView.findViewById(R.id.layout_integer);
            layout_float    =     itemView.findViewById(R.id.layout_float);
            txt_skill       =     itemView.findViewById(R.id.txt_skill);
            txt_value       =     itemView.findViewById(R.id.txt_value);
            txt_float_value =     itemView.findViewById(R.id.txt_float_value);
            btn_minus       =     itemView.findViewById(R.id.btn_minus);
            btn_plus        =     itemView.findViewById(R.id.btn_plus);
        }
    }

    public interface EventListener {
        void onClickMinus(int index);
        void onClickPlus(int index);
        void onClickFloat(int index);
    }
}
