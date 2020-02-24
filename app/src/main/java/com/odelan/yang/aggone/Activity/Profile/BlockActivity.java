package com.odelan.yang.aggone.Activity.Profile;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Adapter.Profile.BlockAdapter;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BlockActivity extends BaseActivity {

    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    BlockAdapter adapter;
    List<User> users = new ArrayList<>();
    BlockAdapter.EventListener blockListener = new BlockAdapter.EventListener() {
        @Override
        public void onClickItem(int index) {
            Intent intent = new Intent(BlockActivity.this, ProfileActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) users.get(index));
            startActivity(intent);
        }
        @Override
        public void onClickBlock(final int index) {
            API.deleteBlock(BlockActivity.this, users.get(index).id, new APICallback<Boolean>() {
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

    @BindView(R.id.refresh_layout) SmartRefreshLayout refresh_layout;
    OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
            API.getBlocks(BlockActivity.this, new APICallback<List<User>>() {
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
        setContentView(R.layout.activity_block);

        ButterKnife.bind(this);
        setActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh_layout.autoRefresh();
    }

    void setActivity() {
        adapter = new BlockAdapter(this, users, blockListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);

        refresh_layout.setOnRefreshListener(refreshListener);
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
}
