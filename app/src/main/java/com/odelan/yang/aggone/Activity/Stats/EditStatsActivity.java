package com.odelan.yang.aggone.Activity.Stats;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Adapter.Stats.StatsAdapter;
import com.odelan.yang.aggone.Dialog.NumberInputDialog;
import com.odelan.yang.aggone.Dialog.TextInputDialog;
import com.odelan.yang.aggone.Model.Result;
import com.odelan.yang.aggone.Model.Skill;
import com.odelan.yang.aggone.MyApp;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditStatsActivity extends BaseActivity {

    @BindView(R.id.edit_club) EditText edit_club;
    @BindView(R.id.edit_month) EditText edit_month;
    @BindView(R.id.edit_year) EditText edit_year;
    @BindView(R.id.layout_club)    LinearLayout layout_club;
    @BindView(R.id.txt_category)    TextView txt_category;

    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    StatsAdapter adapter;
    List<Skill> skills = new ArrayList<>();

    StatsAdapter.EventListener statsListener = new StatsAdapter.EventListener() {
        @Override
        public void onClickMinus(final int index) {
            if (skills.get(index).value == 0) return;
            Result result = new Result();
            result.user_id = AppData.user.id;
            result.sport_id = AppData.user.sport_id;
            result.club = edit_club.getText().toString().trim();
            result.year = Integer.parseInt(edit_year.getText().toString());
            result.month = Integer.parseInt(edit_month.getText().toString());
            result.type = skills.get(index).key;
            result.value = skills.get(index).value - 1;
            API.saveResult(EditStatsActivity.this, result, new APICallback<Result>() {
                @Override
                public void onSuccess(Result response) {
                    skills.get(index).value--;
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(String error) {
                    showToast(error);
                }
            });
        }
        @Override
        public void onClickPlus(final int index) {
            Result result = new Result();
            result.user_id = AppData.user.id;
            result.sport_id = AppData.user.sport_id;
            result.club = edit_club.getText().toString().trim();
            result.year = Integer.parseInt(edit_year.getText().toString());
            result.month = Integer.parseInt(edit_month.getText().toString());
            result.type = skills.get(index).key;
            result.value = skills.get(index).value + 1;
            if((index == 1 || index == 2 || index == 3) && skills.get(0).value < result.value) return;
            API.saveResult(EditStatsActivity.this, result, new APICallback<Result>() {
                @Override
                public void onSuccess(Result response) {
                    skills.get(index).value++;
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(String error) {
                    showToast(error);
                }
            });
        }

        @Override
        public void onClickFloat(final int index) {
            NumberInputDialog dialog = new NumberInputDialog(EditStatsActivity.this, getString(R.string.performance), String.format("%.1f", (float)skills.get(index).value / 10));
            dialog.setListener(value -> {
                final int newValue = (int)(Float.valueOf(value) * 10);

                Result result = new Result();
                result.user_id = AppData.user.id;
                result.sport_id = AppData.user.sport_id;
                result.club = edit_club.getText().toString().trim();
                result.year = Integer.parseInt(edit_year.getText().toString());
                result.month = Integer.parseInt(edit_month.getText().toString());
                result.type = skills.get(index).key;
                result.value = newValue;
                API.saveResult(EditStatsActivity.this, result, new APICallback<Result>() {
                    @Override
                    public void onSuccess(Result response) {
                        skills.get(index).value = newValue;
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure(String error) {
                        showToast(error);
                    }
                });
            });
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stats);
        ButterKnife.bind(this);

        setActivity();
    }

    void setActivity() {
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StatsAdapter(this, skills, statsListener);
        recycler_view.setAdapter(adapter);

        edit_club.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                skills.clear();
                adapter.notifyDataSetChanged();
            }
        });
        edit_month.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                skills.clear();
                adapter.notifyDataSetChanged();
            }
        });
        edit_year.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                skills.clear();
                adapter.notifyDataSetChanged();
            }
        });

        if(AppData.user.club != null){
            edit_club.setText(AppData.user.club);
        }
        if(AppData.user.group_id == Constants.TEAM_CLUB || AppData.user.group_id == Constants.STAFF){
            //layout_club.setVisibility(View.GONE);
            txt_category.setText(getString(R.string.team_category));
        } else{
            //layout_club.setVisibility(View.VISIBLE);
            txt_category.setText(getString(R.string.club_name));
        }
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
    @OnClick(R.id.btn_plus) void onClickPlus() {
        String club = edit_club.getText().toString().trim();
        String year_str = edit_year.getText().toString();
        String month_str = edit_month.getText().toString();
//        if(AppData.user.group_id == Constants.TEAM_CLUB || AppData.user.group_id == Constants.STAFF){
//            club = AppData.user.club;
//        }
        if (club.isEmpty()) {
            showToast("Please input club name");
            return;
        }
        if (year_str.isEmpty()) {
            showToast("Please input year");
            return;
        }
        if (month_str.isEmpty()) {
            showToast("Please input month");
            return;
        }
        int year = Integer.parseInt(year_str);
        int month = Integer.parseInt(month_str);
        if (isValidDate(year, month, 1) == false) {
            showToast("Invalid Date");
            return;
        }
        showProgress();
        API.getClubMonthResultSummary(EditStatsActivity.this, AppData.user, club, year, month, new APICallback<List<Pair<String, Integer>>>() {
            @Override
            public void onSuccess(List<Pair<String, Integer>> response) {
                dismissProgress();
                skills.clear();
                skills.addAll(getSportSkills(AppData.user.group_id == Constants.PLAYER ? AppData.user.sport_id : Constants.STATS_COACH, response));//== Constants.PLAYER ? AppData.user.sport_id : Constants.STATS_COACH
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(String error) {
                dismissProgress();
                showToast(error);
            }
        });
        MyApp.hideKeyboard(this);
    }

    @OnClick(R.id.btn_check) void onClickCheck() { finish(); }
}
