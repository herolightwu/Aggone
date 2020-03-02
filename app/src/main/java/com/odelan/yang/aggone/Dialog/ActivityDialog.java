package com.odelan.yang.aggone.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityDialog extends Dialog {
    @BindView(R.id.btn_player) ImageView btn_player;
    @BindView(R.id.btn_coach) ImageView btn_coach;
    @BindView(R.id.btn_club)        ImageView btn_club;
    @BindView(R.id.btn_agent)       ImageView btn_agent;
    @BindView(R.id.btn_staff)       ImageView btn_staff;
    @BindView(R.id.btn_company)     ImageView btn_company;

    Listener mListener;
    private int type;
    public ActivityDialog(@NonNull Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_activity);
        ButterKnife.bind(this);

        refreshLayout();
        setCancelable(false);
    }

    private void refreshLayout(){
        switch (type){
            case Constants.COACH:
                btn_player.setImageResource(R.mipmap.auth_player_normal);
                btn_coach.setImageResource(R.mipmap.auth_coach_active);
                btn_club.setImageResource(R.mipmap.auth_club_normal);
                btn_agent.setImageResource(R.mipmap.auth_agent_normal);
                btn_staff.setImageResource(R.mipmap.auth_staff_normal);
                btn_company.setImageResource(R.mipmap.auth_company_normal);
                break;
            case Constants.TEAM_CLUB:
                btn_player.setImageResource(R.mipmap.auth_player_normal);
                btn_coach.setImageResource(R.mipmap.auth_coach_normal);
                btn_club.setImageResource(R.mipmap.auth_club_active);
                btn_agent.setImageResource(R.mipmap.auth_agent_normal);
                btn_staff.setImageResource(R.mipmap.auth_staff_normal);
                btn_company.setImageResource(R.mipmap.auth_company_normal);
                break;
            case Constants.AGENT:
                btn_player.setImageResource(R.mipmap.auth_player_normal);
                btn_coach.setImageResource(R.mipmap.auth_coach_normal);
                btn_club.setImageResource(R.mipmap.auth_club_normal);
                btn_agent.setImageResource(R.mipmap.auth_agent_active);
                btn_staff.setImageResource(R.mipmap.auth_staff_normal);
                btn_company.setImageResource(R.mipmap.auth_company_normal);
                break;
            case Constants.STAFF:
                btn_player.setImageResource(R.mipmap.auth_player_normal);
                btn_coach.setImageResource(R.mipmap.auth_coach_normal);
                btn_club.setImageResource(R.mipmap.auth_club_normal);
                btn_agent.setImageResource(R.mipmap.auth_agent_normal);
                btn_staff.setImageResource(R.mipmap.auth_staff_active);
                btn_company.setImageResource(R.mipmap.auth_company_normal);
                break;
            case Constants.COMPANY:
                btn_player.setImageResource(R.mipmap.auth_player_normal);
                btn_coach.setImageResource(R.mipmap.auth_coach_normal);
                btn_club.setImageResource(R.mipmap.auth_club_normal);
                btn_agent.setImageResource(R.mipmap.auth_agent_normal);
                btn_staff.setImageResource(R.mipmap.auth_staff_normal);
                btn_company.setImageResource(R.mipmap.auth_company_active);
                break;
            default:
                btn_player.setImageResource(R.mipmap.auth_player_active);
                btn_coach.setImageResource(R.mipmap.auth_coach_normal);
                btn_club.setImageResource(R.mipmap.auth_club_normal);
                btn_agent.setImageResource(R.mipmap.auth_agent_normal);
                btn_staff.setImageResource(R.mipmap.auth_staff_normal);
                btn_company.setImageResource(R.mipmap.auth_company_normal);
                break;
        }
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @OnClick(R.id.btn_close) void onClickClose() {
        dismiss();
    }
    @OnClick(R.id.btn_player) void onClickPlayer() {
        if (mListener != null) mListener.onSelectType(Constants.PLAYER);
        dismiss();
    }
    @OnClick(R.id.btn_coach) void onClickCoach() {
        if (mListener != null) mListener.onSelectType(Constants.COACH);
        dismiss();
    }
    @OnClick(R.id.btn_club) void onClickClub() {
        if (mListener != null) mListener.onSelectType(Constants.TEAM_CLUB);
        dismiss();
    }
    @OnClick(R.id.btn_agent) void onClickAgent() {
        if (mListener != null) mListener.onSelectType(Constants.AGENT);
        dismiss();
    }
    @OnClick(R.id.btn_staff) void onClickStaff() {
        if (mListener != null) mListener.onSelectType(Constants.STAFF);
        dismiss();
    }
    @OnClick(R.id.btn_company) void onClickCompany() {
        if (mListener != null) mListener.onSelectType(Constants.COMPANY);
        dismiss();
    }

    public interface Listener {
        void onSelectType(int type);
    }
}
