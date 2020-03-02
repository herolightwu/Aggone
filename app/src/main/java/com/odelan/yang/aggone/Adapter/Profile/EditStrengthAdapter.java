package com.odelan.yang.aggone.Adapter.Profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.odelan.yang.aggone.R;

import java.util.ArrayList;
import java.util.List;

public class EditStrengthAdapter extends RecyclerView.Adapter<EditStrengthAdapter.ViewHolder> implements Filterable {

    Context context;
    List<String> strengths;
    List<String> filteredList;
    EventListener listener;
    public List<Integer> sel_indexes = new ArrayList<>();
    StrengthFilter strengthFilter;

    public EditStrengthAdapter(Context context, List<String> data, List<Integer> seled, EventListener listener) {
        this.context = context;
        this.strengths = data;
        this.listener = listener;
        this.sel_indexes = seled;
        filteredList = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_strength, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.tv_name.setText(filteredList.get(position));
        viewHolder.rView.setBackgroundResource(R.color.item_unselect_bg);
        if(sel_indexes.size() > 0){
            if (sel_indexes.get(0) == position) {
                viewHolder.rView.setBackgroundResource(R.color.item_selected_bg);
            }
            if(sel_indexes.size() > 1){
                if (sel_indexes.get(1) == position) {
                    viewHolder.rView.setBackgroundResource(R.color.item_selected_bg);
                }
            }
            if(sel_indexes.size() > 2){
                if (sel_indexes.get(2) == position) {
                    viewHolder.rView.setBackgroundResource(R.color.item_selected_bg);
                }
            }
        }

        viewHolder.rView.setOnClickListener(v -> {
            //if (listener != null) listener.onClickItem(position);
            if (sel_indexes.size() == 3){
                List<Integer> sel_inds = new ArrayList<>();
                sel_inds.add(sel_indexes.get(1));
                sel_inds.add(sel_indexes.get(2));
                sel_indexes.clear();
                sel_indexes = sel_inds;
            }
            sel_indexes.add(position);
            //notifyItemChanged(position);
            notifyDataSetChanged();
        });
    }

    public void setDataList(List<String> data){
        strengths = data;
        filteredList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_mark;
        TextView tv_name;
        ImageView img_plus;
        RelativeLayout rView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_mark   =     itemView.findViewById(R.id.img_mark);
            tv_name    =     itemView.findViewById(R.id.txt_name);
            img_plus   =     itemView.findViewById(R.id.img_plus);
            rView      =     itemView.findViewById(R.id.item_view);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
    }

    /**
     * Get custom filter
     * @return filter
     */
    @Override
    public Filter getFilter() {
        if (strengthFilter == null) {
            strengthFilter = new StrengthFilter();
        }
        return strengthFilter;
    }

    /**
     * Custom filter for friend list
     * Filter content in friend list according to the search text
     */
    private class StrengthFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                List<String> tempList = new ArrayList<String>();
                // search content in friend list
                for (String one : strengths) {
                    if (one.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(one);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = strengths.size();
                filterResults.values = strengths;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (List<String>) results.values;
            notifyDataSetChanged();
        }
    }
}
