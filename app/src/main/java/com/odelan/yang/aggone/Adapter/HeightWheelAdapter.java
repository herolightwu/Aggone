package com.odelan.yang.aggone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.odelan.yang.aggone.Model.Sport;
import com.odelan.yang.aggone.R;

import org.w3c.dom.Text;

import java.util.List;

public class HeightWheelAdapter extends RecyclerView.Adapter<HeightWheelAdapter.ViewHolder> {
    Context context;
    List<Pair<Integer, Boolean>> heights;
    EventListener listener;
    public HeightWheelAdapter(Context context, List<Pair<Integer, Boolean>> heights, EventListener listener) {
        this.context = context;
        this.heights = heights;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HeightWheelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_height_wheel, viewGroup, false);
        HeightWheelAdapter.ViewHolder vh = new HeightWheelAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.txt_height.setText(String.valueOf(heights.get(position).first));
        if (heights.get(position).second) {
            viewHolder.txt_height.setTextSize(14);
            viewHolder.img_selected.setVisibility(View.VISIBLE);
            if (listener != null) {
                listener.onSelectItem(position);
            }
        } else {
            viewHolder.txt_height.setTextSize(7);
            viewHolder.img_selected.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return heights.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_parent;
        TextView txt_height;
        ImageView img_selected;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent   =     itemView.findViewById(R.id.layout_parent);
            txt_height      =     itemView.findViewById(R.id.txt_height);
            img_selected    =     itemView.findViewById(R.id.img_selected);
        }
    }

    public interface EventListener {
        void onSelectItem(int index);
    }
}
