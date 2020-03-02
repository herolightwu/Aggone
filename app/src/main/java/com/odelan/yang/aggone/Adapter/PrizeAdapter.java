package com.odelan.yang.aggone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.Model.Prize;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PrizeAdapter extends RecyclerView.Adapter<PrizeAdapter.ViewHolder> {
    Context context;
    List<Prize> prizes;
    EventListener listener;
    public PrizeAdapter(Context context, List<Prize> prizes, EventListener listener) {
        this.context = context;
        this.prizes = prizes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PrizeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_prize, viewGroup, false);
        PrizeAdapter.ViewHolder vh = new PrizeAdapter.ViewHolder(v);
        return vh;
    }

    int getIconResouce(int icon) {
        if (icon == Constants.PRIZE1) return R.mipmap.icon_prize1;
        if (icon == Constants.PRIZE2) return R.mipmap.icon_prize2;
        if (icon == Constants.PRIZE3) return R.mipmap.icon_prize3;
        if (icon == Constants.PRIZE4) return R.mipmap.icon_prize4;
        if (icon == Constants.PRIZE5) return R.mipmap.icon_prize5;
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.img_icon.setImageResource(getIconResouce(prizes.get(position).icon));
        viewHolder.txt_title.setText(prizes.get(position).title);
        viewHolder.txt_description.setText(prizes.get(position).club + " " + prizes.get(position).year);
        viewHolder.layout_parent.setOnClickListener(v -> {
            if (listener != null) listener.onClickItem(position);
        });
        if (prizes.get(position).user_id.equals(AppData.user.id)) {
            viewHolder.layout_parent.setOnLongClickListener(v -> {
                if (listener != null) listener.onLongClickItem(position);
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return prizes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout_parent;
        TextView txt_title;
        TextView txt_description;
        ImageView img_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent   =     itemView.findViewById(R.id.layout_parent);
            txt_description        =     itemView.findViewById(R.id.txt_description);
            txt_title       =     itemView.findViewById(R.id.txt_title);
            img_icon        =     itemView.findViewById(R.id.img_icon);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
        void onLongClickItem(int index);
    }
}
