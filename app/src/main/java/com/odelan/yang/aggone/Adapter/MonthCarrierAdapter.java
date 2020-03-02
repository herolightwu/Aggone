package com.odelan.yang.aggone.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.odelan.yang.aggone.Model.Career;
import com.odelan.yang.aggone.R;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthCarrierAdapter extends RecyclerView.Adapter<MonthCarrierAdapter.ViewHolder> {
    Context context;
    List<List<Career>> monthCarriers;
    EventListener listener;
    public MonthCarrierAdapter(Context context, List<List<Career>> monthCarriers, EventListener listener) {
        this.context = context;
        this.monthCarriers = monthCarriers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MonthCarrierAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_month_carrier, viewGroup, false);
        MonthCarrierAdapter.ViewHolder vh = new MonthCarrierAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month - position;
        while (month <= 0) {
            year--; month += 12;
        }
        String monthName = new DateFormatSymbols().getMonths()[month-1];

        viewHolder.txt_year.setText(String.valueOf(year));
        viewHolder.txt_month.setText(monthName);
        viewHolder.setCarrier(monthCarriers.get(position));

        viewHolder.layout_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return monthCarriers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_parent;
        TextView txt_year;
        TextView txt_month;
        RecyclerView recycler_view;
        CarrierAdapter adapter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_parent   =     itemView.findViewById(R.id.layout_parent);
            txt_year        =     itemView.findViewById(R.id.txt_year);
            txt_month       =     itemView.findViewById(R.id.txt_month);
            recycler_view   =     itemView.findViewById(R.id.recycler_view);
            adapter = new CarrierAdapter(context, new ArrayList<Career>(), null);
            recycler_view.setLayoutManager(new LinearLayoutManager(context));
            recycler_view.setAdapter(adapter);
        }

        public void setCarrier(List<Career> carrier) {
            adapter.setCarriers(carrier);
            adapter.notifyDataSetChanged();
        }
    }

    public interface EventListener {
        void onClickItem(int index);
    }
}
