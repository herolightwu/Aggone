package com.odelan.yang.aggone.Activity.Message;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Activity.Profile.ProfileActivity;
import com.odelan.yang.aggone.Activity.Profile.SubscriptionActivity;
import com.odelan.yang.aggone.Adapter.Profile.SubscriptionAdapter;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddContactActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    SubscriptionAdapter adapter;
    List<User> users = new ArrayList<>();
    SubscriptionAdapter.EventListener subscriberListener = new SubscriptionAdapter.EventListener() {
        @Override
        public void onClickItem(int index) {
            Intent intent = new Intent(AddContactActivity.this, ChatActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) users.get(index));
            startActivity(intent);
        }
        @Override
        public void onClickSubscribe(final int index) {
            API.deleteFollow(AddContactActivity.this, users.get(index).id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    users.remove(index);
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(String error) {
                    showToast(error);
                }
            });
        }
    };

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
            API.getFollowing(AddContactActivity.this , new APICallback<List<User>>() {
                @Override
                public void onSuccess(List<User> response) {
                    refresh_layout.finishRefresh();
                    users.clear();
                    users.addAll(response);
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(String error) {
                    refresh_layout.finishRefresh();
                    showToast(error);
                }
            });
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);

        setActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh_layout.autoRefresh();
    }

    void setActivity() {
        adapter = new SubscriptionAdapter(this, users, subscriberListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);

        refresh_layout.setOnRefreshListener(refreshListener);
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
}
