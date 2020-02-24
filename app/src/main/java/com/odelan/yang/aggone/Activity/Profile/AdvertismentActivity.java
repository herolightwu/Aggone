package com.odelan.yang.aggone.Activity.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Adapter.LookUp.LookingClubAdapter;
import com.odelan.yang.aggone.Adapter.Profile.AdvertismentAdapter;
import com.odelan.yang.aggone.Dialog.DeleteDialog;
import com.odelan.yang.aggone.Model.Admob;
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

public class AdvertismentActivity extends BaseActivity {

    @BindView(R.id.recycler_view)    RecyclerView recycler_view;
    @BindView(R.id.refresh_layout)    SmartRefreshLayout refresh_layout;

    AdvertismentAdapter adapter;
    List<Admob> admobs = new ArrayList<>();

    AdvertismentAdapter.EventListener clubListener = new AdvertismentAdapter.EventListener() {
        @Override
        public void onClickItem(Admob admob) {
            Intent intent = new Intent(AdvertismentActivity.this, ProfileActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) admob.user);
            startActivity(intent);
        }

        @Override
        public void onClickDelete(final Admob admob) {
            DeleteDialog dialog = new DeleteDialog(AdvertismentActivity.this);
            dialog.setListener(() -> API.deleteAdvert(AdvertismentActivity.this, admob.id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    admobs.remove(admob);
                    adapter.setData(admobs);
                }
                @Override
                public void onFailure(String error) {
                    Toast.makeText(AdvertismentActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }));
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
        }
    };

    OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
            API.getAllAdmobs(AdvertismentActivity.this, new APICallback<List<Admob>>() {
                @Override
                public void onSuccess(List<Admob> response) {
                    refreshLayout.finishRefresh();
                    admobs = response;
                    adapter.setData(admobs);
                }
                @Override
                public void onFailure(String error) {
                    refreshLayout.finishRefresh();
                    showToast(error);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisment);

        ButterKnife.bind(this);
        setActivity();
    }

    void setActivity() {
        adapter = new AdvertismentAdapter(this, admobs, clubListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);

        refresh_layout.setOnRefreshListener(refreshListener);
        refresh_layout.autoRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh_layout.autoRefresh();
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
}
