package com.odelan.yang.aggone.Fragment.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.odelan.yang.aggone.Adapter.CareerAdapter;
import com.odelan.yang.aggone.Model.Career;
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

public class CareerFragment extends Fragment {
    private Context context;
    private Listener mListener;
    private User user;

    @BindView(R.id.refresh_layout) SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    CareerAdapter adapter;
    List<Career> carriers = new ArrayList<>();

    OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
            API.getAllCareersByUserid(context, user.id, new APICallback<List<Career>>() {
                @Override
                public void onSuccess(List<Career> response) {
                    carriers.clear();
                    carriers.addAll(response);
                    adapter.notifyDataSetChanged();
                    refresh_layout.finishRefresh();
                }
                @Override
                public void onFailure(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    refresh_layout.finishRefresh();
                }
            });
        }
    };

    CareerAdapter.EventListener careerListener = new CareerAdapter.EventListener() {
        @Override
        public void onClickDelete(final int index) {
            API.deleteCareer(context, carriers.get(index).id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    carriers.remove(index);
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    public CareerFragment() {
    }

    @SuppressLint("ValidFragment")
    public CareerFragment(Context context, User user, Listener listener) {
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
        View view = inflater.inflate(R.layout.fragment_carrier, container, false);
        ButterKnife.bind(this, view);

        adapter = new CareerAdapter(context, carriers, careerListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(adapter);

        refresh_layout.setOnRefreshListener(refreshListener);
        refresh_layout.autoRefresh();
        return view;
    }

    public interface Listener {
    }
}
