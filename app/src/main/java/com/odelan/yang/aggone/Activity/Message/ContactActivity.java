package com.odelan.yang.aggone.Activity.Message;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.odelan.yang.aggone.Activity.LookUp.LookUpActivity;
import com.odelan.yang.aggone.Activity.MainActivity;
import com.odelan.yang.aggone.Activity.Profile.ProfileActivity;
import com.odelan.yang.aggone.Activity.Stats.ClubChartActivity;
import com.odelan.yang.aggone.Activity.Stats.StatsActivity;
import com.odelan.yang.aggone.Adapter.PagerAdapter;
import com.odelan.yang.aggone.Fragment.Message.MessageFragment;
import com.odelan.yang.aggone.Fragment.Message.NotificationFragment;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactActivity extends AppCompatActivity {

    @BindView(R.id.btn_chats) ImageView btn_chats;
    @BindView(R.id.txt_chats) TextView txt_chats;

    @BindView(R.id.layout_message) LinearLayout layout_message;
    @BindView(R.id.layout_notification) LinearLayout layout_notification;
    @BindView(R.id.txt_message) TextView txt_message;
    @BindView(R.id.txt_notification) TextView txt_notification;

    @BindView(R.id.view_pager) ViewPager view_pager;
    PagerAdapter adapter;
    Fragment[] fragments;

    MessageFragment.Listener messageListener = new MessageFragment.Listener() {
        @Override
        public void onChat(User user) {
            Intent intent = new Intent(ContactActivity.this, ChatActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) user);
            startActivity(intent);
        }
    };

    NotificationFragment.Listener notificationListener = new NotificationFragment.Listener() {
        @Override
        public void onClickProfile(User user) {
            Intent intent = new Intent(ContactActivity.this, ProfileActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) user);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        setActivity();
    }

    void setActivity() {
        btn_chats.setImageResource(R.mipmap.tab_chats_active);
        txt_chats.setTextColor(getResources().getColor(R.color.tab_active));

        fragments = new Fragment[] {
                new MessageFragment(this, messageListener),
                new NotificationFragment(this, notificationListener),
        };
        adapter = new PagerAdapter(this, fragments, getSupportFragmentManager());
        view_pager.setAdapter(adapter);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                setTabTitle(i);
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        view_pager.setCurrentItem(0);
        setTabTitle(0);
    }

    void setTabTitle(int index) {
        if (index == 0) {
            layout_message.setBackgroundResource(R.mipmap.message_tab_active);
            layout_notification.setBackgroundResource(R.mipmap.message_tab_normal);
            txt_message.setAlpha(1.0f);
            txt_notification.setAlpha(0.7f);
        } else if (index == 1) {
            layout_message.setBackgroundResource(R.mipmap.message_tab_normal);
            layout_notification.setBackgroundResource(R.mipmap.message_tab_active);
            txt_message.setAlpha(0.7f);
            txt_notification.setAlpha(1.0f);
            if (fragments[1] != null){
                ((NotificationFragment) fragments[1]).loadData();
            }
        }
    }

    @OnClick(R.id.btn_add) void onClickAdd(){
        Intent intent = new Intent(ContactActivity.this, AddContactActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
    @OnClick(R.id.layout_message) void onClickMessage() {
        view_pager.setCurrentItem(0);
    }
    @OnClick(R.id.layout_notification) void onClickNotification() {
        view_pager.setCurrentItem(1);
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
    @OnClick(R.id.btn_lookup) void onClickChats() {
        Intent intent = new Intent(this, LookUpActivity.class);
        startActivity(intent);
    }
}
