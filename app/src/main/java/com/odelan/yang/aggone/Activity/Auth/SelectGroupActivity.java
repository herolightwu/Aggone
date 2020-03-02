package com.odelan.yang.aggone.Activity.Auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectGroupActivity extends AppCompatActivity {

    @BindView(R.id.btn_player)      ImageView btn_player;
    @BindView(R.id.btn_coach)       ImageView btn_coach;
    @BindView(R.id.btn_club)        ImageView btn_club;
    @BindView(R.id.btn_agent)       ImageView btn_agent;
    @BindView(R.id.btn_staff)       ImageView btn_staff;
    @BindView(R.id.btn_company)     ImageView btn_company;

    private String email;
    private String password;
    private int user_type = Constants.PLAYER;
    private int sign_mode = Constants.SIGNUP_EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        sign_mode = intent.getIntExtra(Constants.SIGNUP_MODE, Constants.SIGNUP_EMAIL);
        email = intent.getStringExtra(Constants.EMAIL);
        password = intent.getStringExtra(Constants.PASSWORD);
    }

    @OnClick(R.id.btn_go) void onClickGo() {
        AppData.user.group_id = user_type;
        Intent intent = new Intent(this, SelectSportActivity.class);
        intent.putExtra(Constants.EMAIL, email);
        intent.putExtra(Constants.PASSWORD, password);
        intent.putExtra(Constants.SIGNUP_MODE, sign_mode);
        startActivity(intent);
    }

    @OnClick(R.id.btn_player) void onClickPlayer() {
        user_type = Constants.PLAYER;
        btn_player.setImageResource(R.mipmap.auth_player_active);
        btn_coach.setImageResource(R.mipmap.auth_coach_normal);
        btn_club.setImageResource(R.mipmap.auth_club_normal);
        btn_agent.setImageResource(R.mipmap.auth_agent_normal);
        btn_staff.setImageResource(R.mipmap.auth_staff_normal);
        btn_company.setImageResource(R.mipmap.auth_company_normal);
    }

    @OnClick(R.id.btn_coach) void onClickCoach() {
        user_type = Constants.COACH;
        btn_player.setImageResource(R.mipmap.auth_player_normal);
        btn_coach.setImageResource(R.mipmap.auth_coach_active);
        btn_club.setImageResource(R.mipmap.auth_club_normal);
        btn_agent.setImageResource(R.mipmap.auth_agent_normal);
        btn_staff.setImageResource(R.mipmap.auth_staff_normal);
        btn_company.setImageResource(R.mipmap.auth_company_normal);
    }

    @OnClick(R.id.btn_club) void onClickClub() {
        user_type = Constants.TEAM_CLUB;
        btn_player.setImageResource(R.mipmap.auth_player_normal);
        btn_coach.setImageResource(R.mipmap.auth_coach_normal);
        btn_club.setImageResource(R.mipmap.auth_club_active);
        btn_agent.setImageResource(R.mipmap.auth_agent_normal);
        btn_staff.setImageResource(R.mipmap.auth_staff_normal);
        btn_company.setImageResource(R.mipmap.auth_company_normal);
    }

    @OnClick(R.id.btn_agent) void onClickAgent() {
        user_type = Constants.AGENT;
        btn_player.setImageResource(R.mipmap.auth_player_normal);
        btn_coach.setImageResource(R.mipmap.auth_coach_normal);
        btn_club.setImageResource(R.mipmap.auth_club_normal);
        btn_agent.setImageResource(R.mipmap.auth_agent_active);
        btn_staff.setImageResource(R.mipmap.auth_staff_normal);
        btn_company.setImageResource(R.mipmap.auth_company_normal);
    }

    @OnClick(R.id.btn_staff) void onClickStaff() {
        user_type = Constants.STAFF;
        btn_player.setImageResource(R.mipmap.auth_player_normal);
        btn_coach.setImageResource(R.mipmap.auth_coach_normal);
        btn_club.setImageResource(R.mipmap.auth_club_normal);
        btn_agent.setImageResource(R.mipmap.auth_agent_normal);
        btn_staff.setImageResource(R.mipmap.auth_staff_active);
        btn_company.setImageResource(R.mipmap.auth_company_normal);
    }

    @OnClick(R.id.btn_company) void onClickCompany() {
        user_type = Constants.COMPANY;
        btn_player.setImageResource(R.mipmap.auth_player_normal);
        btn_coach.setImageResource(R.mipmap.auth_coach_normal);
        btn_club.setImageResource(R.mipmap.auth_club_normal);
        btn_agent.setImageResource(R.mipmap.auth_agent_normal);
        btn_staff.setImageResource(R.mipmap.auth_staff_normal);
        btn_company.setImageResource(R.mipmap.auth_company_active);
    }
}
