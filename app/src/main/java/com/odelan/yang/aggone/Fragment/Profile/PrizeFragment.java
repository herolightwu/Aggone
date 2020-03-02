package com.odelan.yang.aggone.Fragment.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.odelan.yang.aggone.Adapter.PrizeAdapter;
import com.odelan.yang.aggone.Dialog.DeleteDialog;
import com.odelan.yang.aggone.Model.Prize;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrizeFragment extends Fragment {
    private Context context;
    private Listener mListener;
    private User user;

    @BindView(R.id.refresh_layout) SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    PrizeAdapter adapter;
    List<Prize> prizes = new ArrayList<>();

    PrizeAdapter.EventListener prizeListener = new PrizeAdapter.EventListener() {
        @Override
        public void onClickItem(int index) {
        }
        @Override
        public void onLongClickItem(final int index) {
            DeleteDialog dialog = new DeleteDialog(context);
            dialog.setListener(() -> API.deletePrize(context, prizes.get(index), new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    prizes.remove(index);
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT);
                }
            }));
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
        }
    };

    public PrizeFragment() {
    }

    @SuppressLint("ValidFragment")
    public PrizeFragment(Context context, User user, Listener listener) {
        this.context = context;
        this.mListener = listener;
        this.user = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prize, container, false);
        ButterKnife.bind(this, view);

        adapter = new PrizeAdapter(context, prizes, prizeListener);
        recycler_view.setLayoutManager(new GridLayoutManager(context, 3));
        recycler_view.setAdapter(adapter);

        refresh_layout.setOnRefreshListener(refreshListener);
        refresh_layout.autoRefresh();
        return view;
    }

    OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
            API.getAllPrizesByUserid(context, user.id, new APICallback<List<Prize>>() {
                @Override
                public void onSuccess(List<Prize> response) {
                    prizes.clear();
                    prizes.addAll(response);
                    adapter.notifyDataSetChanged();
                    refresh_layout.finishRefresh();
                }
                @Override
                public void onFailure(String error) {
                    refresh_layout.finishRefresh();
                }
            });
        }
    };

    public interface Listener {
    }
}
