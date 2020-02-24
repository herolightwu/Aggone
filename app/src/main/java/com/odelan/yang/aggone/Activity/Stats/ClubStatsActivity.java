package com.odelan.yang.aggone.Activity.Stats;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Adapter.Stats.ClubChartAdapter;
import com.odelan.yang.aggone.Adapter.Stats.ClubStatsAdapter;
import com.odelan.yang.aggone.Model.ClubStat;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClubStatsActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    List<ClubStat> club_lists = new ArrayList<>();
    ClubStatsAdapter adapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_stats);

        ButterKnife.bind(this);

        user = getIntent().getExtras().getParcelable(Constants.USER);
        setActivity();
    }

    void setActivity(){
        adapter = new ClubStatsAdapter(this, club_lists, null);
        adapter.user = user;
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);
    }

    private void refreshActivity(){
        showProgress();
        API.getSummaryByClub(this, user.id, new APICallback<List<ClubStat>>() {
            @Override
            public void onSuccess(List<ClubStat> response) {
                dismissProgress();
                club_lists.clear();
                club_lists.addAll(response);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                dismissProgress();
                showToast(error);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshActivity();
    }

    @OnClick(R.id.btn_back) void onClickBack(){
        finish();
    }
}
