package com.odelan.yang.aggone.Activity.Stats;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Adapter.Stats.ClubChartAdapter;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClubChartActivity extends BaseActivity {

    @BindView(R.id.recycler_view)    RecyclerView    recycler_view;

    List<Pair<String, List<List<Pair<String, Integer>>>>> stats = new ArrayList<>();
    ClubChartAdapter adapter;
    private User user;

    ClubChartAdapter.EventListener chartListener = new ClubChartAdapter.EventListener() {
        @Override
        public void onClickItem(int index) {
            Pair<String, List<List<Pair<String, Integer>>>> one = stats.get(index);
            Intent intent = new Intent(ClubChartActivity.this, ClubDetailActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) user);
            intent.putExtra("year", one.first);
            startActivity(intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_chart);

        ButterKnife.bind(this);

        user = getIntent().getExtras().getParcelable(Constants.USER);
        setActivity();
    }

    private void setActivity(){
        adapter = new ClubChartAdapter(this, stats, chartListener);
        adapter.user = user;
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);

    }

    private void refreshActivity(){
        showProgress();
        API.getClubSummary(this, user, new APICallback<List<Pair<String, List<List<Pair<String, Integer>>>>>>() {
            @Override
            public void onSuccess(List<Pair<String, List<List<Pair<String, Integer>>>>> response) {
                dismissProgress();
                if(response.size() == 0){
                    gotoNewActivity();
                    return;
                }
                stats.clear();
                stats.addAll(response);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                dismissProgress();
                showToast(error);
            }
        });
    }

    private void gotoNewActivity(){
        int nYear = Calendar.getInstance().get(Calendar.YEAR);
        Intent intent = new Intent(ClubChartActivity.this, ClubDetailActivity.class);
        intent.putExtra(Constants.USER, (Parcelable) user);
        intent.putExtra("year", nYear + "");
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshActivity();
    }

    @OnClick(R.id.btn_back) void onClickBack(){
        finish();
    }

    @OnClick(R.id.btn_category) void onClickCategory(){
        Intent intent = new Intent(ClubChartActivity.this, ClubStatsActivity.class);
        intent.putExtra(Constants.USER, (Parcelable) user);
        startActivity(intent);
    }
}
