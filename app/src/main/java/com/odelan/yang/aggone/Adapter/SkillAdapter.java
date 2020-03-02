package com.odelan.yang.aggone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Model.Description;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;

import java.util.List;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.ViewHolder> {
    Context context;
    List<Description> skills;
    User user;
    EventListener listener;
    public SkillAdapter(Context context, List<Description> skills, EventListener listener) {
        this.context = context;
        this.skills = skills;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SkillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_skills, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.txt_skill.setText(BaseActivity.getDescriptionName(context, skills.get(position).type));
        viewHolder.txt_value.setText(String.valueOf(skills.get(position).value));
        viewHolder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickMinus(position);
            }
        });
        viewHolder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickPlus(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_parent;
        TextView txt_skill;
        TextView txt_value;
        Button btn_plus;
        Button btn_minus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent   =     itemView.findViewById(R.id.layout_parent);
            txt_skill       =     itemView.findViewById(R.id.txt_skill);
            txt_value       =     itemView.findViewById(R.id.txt_value);
            btn_minus       =     itemView.findViewById(R.id.btn_minus);
            btn_plus        =     itemView.findViewById(R.id.btn_plus);
        }
    }

    public interface EventListener {
        void onClickMinus(int index);
        void onClickPlus(int index);
    }
}
