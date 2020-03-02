package com.odelan.yang.aggone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.Model.Skill;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    Context context;
    List<Skill> summary;
    Listener listener;
    public ResultAdapter(Context context, List<Skill> summary, Listener listener) {
        this.context = context;
        this.summary = summary;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_result, viewGroup, false);
        ResultAdapter.ViewHolder vh = new ResultAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        viewHolder.txt_value.setText(String.valueOf(summary.get(position).value));
        viewHolder.txt_type.setText(summary.get(position).summary);
        viewHolder.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return summary.size();
    }

    public void setDataList(List<Skill> data){
        summary = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_parent;
        TextView txt_type;
        TextView txt_value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent   =     itemView.findViewById(R.id.layout_parent);
            txt_value       =     itemView.findViewById(R.id.txt_value);
            txt_type        =     itemView.findViewById(R.id.txt_type);

        }
    }

    public interface Listener {
        void onClickItem(int index);
    }
}
