package com.odelan.yang.aggone.Activity.LookUp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.odelan.yang.aggone.Activity.MainActivity;
import com.odelan.yang.aggone.Activity.Message.ContactActivity;
import com.odelan.yang.aggone.Activity.Stats.ClubChartActivity;
import com.odelan.yang.aggone.Activity.Stats.StatsActivity;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LookUpActivity extends AppCompatActivity {

    @BindView(R.id.btn_lookup) ImageView btn_lookup;
    @BindView(R.id.txt_lookup) TextView txt_lookup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_up);
        ButterKnife.bind(this);
        setActivity();
    }

    void setActivity() {
        btn_lookup.setImageResource(R.mipmap.tab_lookup_active);
        txt_lookup.setTextColor(getResources().getColor(R.color.tab_active));
    }

    /**
     * Tab bar Event
     * */
    @OnClick(R.id.btn_home) void onClickHome() {
        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @OnClick(R.id.btn_stats) void onClickStats() {
        if(AppData.user.group_id == Constants.TEAM_CLUB || AppData.user.group_id == Constants.STAFF ){
            Intent intent = new Intent(this, ClubChartActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) AppData.user);
            startActivity(intent);
        } else{
            Intent intent = new Intent(this, StatsActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) AppData.user);
            startActivity(intent);
        }
    }
    @OnClick(R.id.btn_chats) void onClickChats() {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    /**
     * Activity Event
     * */
    @OnClick(R.id.btn_filter) void onClickFilter() {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_create_ad) void onClickCreateAd() {
        Intent intent = new Intent(this, CreateAdActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_looking_player) void onClickLookingPlayer() {
        Intent intent = new Intent(this, LookingPlayerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_looking_club) void onClickLookingClub() {
        Intent intent = new Intent(this, LookingClubActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_search) void onClickSearch(){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
