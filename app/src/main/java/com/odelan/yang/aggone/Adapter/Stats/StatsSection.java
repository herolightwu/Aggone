package com.odelan.yang.aggone.Adapter.Stats;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.odelan.yang.aggone.Activity.Auth.LoginActivity;
import com.odelan.yang.aggone.Activity.Auth.SplashActivity;
import com.odelan.yang.aggone.Activity.Stats.ClubResultActivity;
import com.odelan.yang.aggone.Activity.Stats.EditStatsActivity;
import com.odelan.yang.aggone.Dialog.NumberInputDialog;
import com.odelan.yang.aggone.Model.Result;
import com.odelan.yang.aggone.Model.Skill;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class StatsSection extends StatelessSection {
    Context context;
    String club, club_name;
    List<Skill> stats;
    List<Pair<String, List<String>>> years_data = new ArrayList<>();
    EventListener listener;
    Boolean bEdit = false;
    int nMonth = 0, nYear = 0;
    List<String> cate_years = new ArrayList<String>();
    List<String> month_list = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
    ArrayAdapter<String> monthAdapter;
    ArrayAdapter<String> dataAdapter;
    public StatsSection(Context ctx, String club, List<Skill> stats, List<Pair<String, List<String>>> years, EventListener listener) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_item)
                .headerResourceId(R.layout.item_header)
                .footerResourceId(R.layout.item_footer)
                .build());
        this.club = club;
        this.stats = stats;
        this.years_data.addAll(years);
        this.listener = listener;
        this.context = ctx;
        this.club_name = club;

        for(int i = 0; i < years.size(); i++){
            cate_years.add(years.get(i).first);
        }
        dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, cate_years);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monthAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, month_list);//new ArrayList<>(years.get(nYear).second)
        // Drop down layout style - list view with radio button
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if(AppData.user.group_id == Constants.TEAM_CLUB || AppData.user.group_id == Constants.STAFF){
            String[] ss_str = club.split("-");
            club_name = club.substring(ss_str[0].length()+1);
        }
    }

    @Override
    public int getContentItemsTotal() {
        return stats.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;
        itemHolder.txt_stats.setText(stats.get(position).description + " (" + stats.get(position).summary + ")");
        if (stats.get(position).key.equals(Constants.PERFORMANCE)) {
            itemHolder.txt_value.setText(String.format("%.1f", (float)stats.get(position).value / 10));
            if (bEdit){
                itemHolder.float_panel.setVisibility(View.VISIBLE);
                itemHolder.integer_panel.setVisibility(View.GONE);
            } else{
                itemHolder.float_panel.setVisibility(View.GONE);
                itemHolder.integer_panel.setVisibility(View.GONE);
            }
        } else {
            itemHolder.txt_value.setText(String.valueOf(stats.get(position).value));
            if (bEdit){
                itemHolder.float_panel.setVisibility(View.GONE);
                itemHolder.integer_panel.setVisibility(View.VISIBLE);
            } else{
                itemHolder.float_panel.setVisibility(View.GONE);
                itemHolder.integer_panel.setVisibility(View.GONE);
            }
        }
        itemHolder.btn_plus.setOnClickListener(v ->{
            if(cate_years.size() > 0){
                String year = cate_years.get(nYear);
                String month = month_list.get(nMonth);//years_data.get(nYear).second.get(nMonth);
                //if (listener != null) listener.onClickPlus(club, year, month, position);
                Result result = new Result();
                result.user_id = AppData.user.id;
                result.sport_id = AppData.user.sport_id;
                result.club = club_name;
                result.year = Integer.parseInt(year);
                result.month = Integer.parseInt(month);
                result.value_type = 1;
                result.type = stats.get(position).key;
                API.adjustStatValue(context, result, new APICallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean response) {
                        stats.get(position).value++;
                        itemHolder.txt_value.setText(String.valueOf(stats.get(position).value));
                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });
            }
        });
        itemHolder.btn_minus.setOnClickListener(v ->{
            if(cate_years.size() > 0) {
                String year = cate_years.get(nYear);
                String month = month_list.get(nMonth);//years_data.get(nYear).second.get(nMonth);
                Result result = new Result();
                result.user_id = AppData.user.id;
                result.sport_id = AppData.user.sport_id;
                result.club = club_name;
                result.year = Integer.parseInt(year);
                result.month = Integer.parseInt(month);
                result.value_type = -1;
                result.type = stats.get(position).key;
                API.adjustStatValue(context, result, new APICallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean response) {
                        stats.get(position).value--;
                        itemHolder.txt_value.setText(String.valueOf(stats.get(position).value));
                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });
            }
        });
        itemHolder.float_panel.setOnClickListener(v ->{
            if(cate_years.size() > 0) {
                String year = cate_years.get(nYear);
                String month = month_list.get(nMonth);//years_data.get(nYear).second.get(nMonth);
                NumberInputDialog dialog = new NumberInputDialog(context, context.getString(R.string.performance), String.format("%.1f", (float)stats.get(position).value / 10));
                dialog.setListener(value -> {
                    final int newValue = (int)(Float.valueOf(value) * 10);
                    Result result = new Result();
                    result.user_id = AppData.user.id;
                    result.sport_id = AppData.user.sport_id;
                    result.club = club_name;
                    result.year = Integer.parseInt(year);
                    result.month = Integer.parseInt(month);
                    result.type = stats.get(position).key;
                    result.value_type = 0;
                    result.value = newValue;
                    API.adjustStatValue(context, result, new APICallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean response) {
                            stats.get(position).value = newValue;
                            itemHolder.txt_value.setText(String.format("%.1f", (float)stats.get(position).value / 10));
                        }
                        @Override
                        public void onFailure(String error) {
                        }
                    });
                });
                View decorView = dialog.getWindow().getDecorView();
                decorView.setBackgroundResource(android.R.color.transparent);
                dialog.show();
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        headerHolder.txt_club.setText(club);
        headerHolder.btn_edit.setOnClickListener(v ->{
            if(headerHolder.btn_delete.getVisibility() == View.GONE){
                bEdit = true;
            } else{
                bEdit = false;
            }
            if (listener != null) listener.onClickEdit();
        });
        headerHolder.btn_delete.setOnClickListener(v -> {
            if (listener != null) listener.onClickDelete(club);
        });
        // attaching data adapter to spinner
        if(headerHolder.spinner_year.getAdapter() == null)
            headerHolder.spinner_year.setAdapter(dataAdapter);
        headerHolder.spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(nYear != i){
                    nYear = i;
//                    monthAdapter.clear();
//                    monthAdapter.addAll(new ArrayList<>(years_data.get(nYear).second));
                    nMonth = 0;
                    monthAdapter.notifyDataSetChanged();
                    headerHolder.spinner_month.setSelection(nMonth);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        headerHolder.spinner_year.setSelection(nYear);

        // attaching data adapter to spinner
        if (headerHolder.spinner_month.getAdapter() == null)
            headerHolder.spinner_month.setAdapter(monthAdapter);
        headerHolder.spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nMonth = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //if (nMonth >= years_data.get(nYear).second.size()) nMonth = 0;
        headerHolder.spinner_month.setSelection(nMonth);

        if (bEdit){
            headerHolder.btn_edit.setImageResource(R.mipmap.check);
            headerHolder.btn_delete.setVisibility(View.VISIBLE);
            headerHolder.spinner_month.setVisibility(View.VISIBLE);
            headerHolder.spinner_year.setVisibility(View.VISIBLE);
        } else{
            headerHolder.btn_edit.setImageResource(R.mipmap.ic_edit_pen);
            headerHolder.btn_delete.setVisibility(View.GONE);
            headerHolder.spinner_month.setVisibility(View.GONE);
            headerHolder.spinner_year.setVisibility(View.GONE);
        }

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_club;
        public ImageView btn_delete;
        public ImageView btn_edit;
        public RelativeLayout edit_layout;
        public Spinner spinner_year;
        public Spinner spinner_month;
        HeaderViewHolder(View view) {
            super(view);
            txt_club = view.findViewById(R.id.txt_club);
            btn_delete = view.findViewById(R.id.btn_delete);
            btn_edit = view.findViewById(R.id.btn_edit);
            edit_layout = view.findViewById(R.id.edit_layout);
            spinner_year = view.findViewById(R.id.spinner_year);
            spinner_month = view.findViewById(R.id.spinner_month);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_stats;
        public TextView txt_value;
        public RelativeLayout float_panel;
        public RelativeLayout integer_panel;
        public Button btn_plus;
        public Button btn_minus;
        ItemViewHolder(View view) {
            super(view);
            txt_stats = view.findViewById(R.id.txt_stats);
            txt_value = view.findViewById(R.id.txt_value);
            float_panel = view.findViewById(R.id.layout_float);
            integer_panel = view.findViewById(R.id.layout_integer);
            btn_plus = view.findViewById(R.id.btn_plus);
            btn_minus = view.findViewById(R.id.btn_minus);
        }
    }

    public interface EventListener{
        void onClickDelete(String club);
//        void onClickPlus(String sClub, String sYear, String sMonth, String sKey);
//        void onClickMinus(String sClub, String sYear, String sMonth, String sKey);
//        void onClickFloat(String sClub, String sYear, String sMonth, String sKey);
        void onClickEdit();
    }
}
