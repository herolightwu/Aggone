package com.odelan.yang.aggone.Activity.Stats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Adapter.Stats.StatsSection;
import com.odelan.yang.aggone.Dialog.DeleteDialog;
import com.odelan.yang.aggone.Model.Skill;
import com.odelan.yang.aggone.Model.Summary;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class ClubResultActivity extends BaseActivity {

    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    @BindView(R.id.btn_plus)    Button btn_add_stat;
    private SectionedRecyclerViewAdapter adapter;

    StatsSection.EventListener listener = new StatsSection.EventListener() {
        @Override
        public void onClickDelete(String club) {
            DeleteDialog dialog = new DeleteDialog(ClubResultActivity.this);
            dialog.setListener(new DeleteDialog.Listener() {
                @Override
                public void onClickDelete() {
                    if(AppData.user.group_id == Constants.TEAM_CLUB || AppData.user.group_id == Constants.STAFF ){
                        API.deleteClubYearSummary(ClubResultActivity.this, AppData.user, club, new APICallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean response) {
                                refreshActivity();
                            }

                            @Override
                            public void onFailure(String error) {
                                showToast(error);
                            }
                        });
                    } else{
                        API.deleteUserClubSummary(ClubResultActivity.this, AppData.user.id, club, new APICallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean response) {
                                refreshActivity();
                            }

                            @Override
                            public void onFailure(String error) {
                                showToast(error);
                            }
                        });
                    }
                }
            });
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();

        }

        @Override
        public void onClickEdit(){
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_result);
        ButterKnife.bind(this);
        setActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshActivity();
    }

    void setActivity() {
        adapter = new SectionedRecyclerViewAdapter();
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);

        if(AppData.user.group_id == Constants.TEAM_CLUB || AppData.user.group_id == Constants.STAFF ){
            btn_add_stat.setBackgroundResource(R.mipmap.add_new_category);
        } else{
            btn_add_stat.setBackgroundResource(R.mipmap.add_new_club);
        }
    }

    private void refreshActivity(){
        showProgress();
        if(AppData.user.group_id == Constants.TEAM_CLUB || AppData.user.group_id == Constants.STAFF ){
            API.getClubYearSummary(this, AppData.user, new APICallback<List<Summary>>() {
                @Override
                public void onSuccess(List<Summary> response) {
                    dismissProgress();
                    adapter.removeAllSections();
                    for (Summary one : response) {
                        List<Skill> skills = getSportSkills(Constants.STATS_COACH, one.stats);
                        adapter.addSection(new StatsSection(ClubResultActivity.this, one.club_name,  skills, one.years,listener));
                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(String error) {
                    dismissProgress();
                    showToast(error);
                }
            });
        } else{
            API.getUserResultSummary(this, AppData.user, new APICallback<List<Summary>>() {
                @Override
                public void onSuccess(List<Summary> response) {
                    dismissProgress();
                    adapter.removeAllSections();
                    for (Summary one : response) {
                        List<Skill> skills = getSportSkills(AppData.user.group_id == Constants.PLAYER ? AppData.user.sport_id : Constants.STATS_COACH, one.stats);
                        adapter.addSection(new StatsSection(ClubResultActivity.this, one.club_name, skills, one.years, listener));
                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(String error) {
                    dismissProgress();
                    showToast(error);
                }
            });
        }
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
    @OnClick(R.id.btn_plus) void onClickPlus() {
        startActivity(new Intent(this, EditStatsActivity.class));
    }

    @OnClick(R.id.btn_check) void onClickCheck() { finish(); }
}
