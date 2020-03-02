package com.odelan.yang.aggone.Fragment.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.yang.aggone.Activity.MainActivity;
import com.odelan.yang.aggone.Activity.Profile.ProfileActivity;
import com.odelan.yang.aggone.Adapter.TagUserAdapter;
import com.odelan.yang.aggone.Model.Career;
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


public class TeamFragment extends Fragment {

    private Context context;
    private Listener mListener;
    private User user;
    TagUserAdapter adapter;
    List<User> users = new ArrayList<>();

    @BindView(R.id.btn_join)        Button  btn_join;
    @BindView(R.id.recycler_view)    RecyclerView recycler_view;
    @BindView(R.id.refresh_layout)    SmartRefreshLayout refresh_layout;

    boolean bJoin = false;

    OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
            API.getTeamMembers(context, user.id, new APICallback<List<User>>() {
                @Override
                public void onSuccess(List<User> response) {
                    users.clear();
                    users.addAll(response);
                    adapter.setDataList(users);
                    refresh_layout.finishRefresh();
                    for(int i = 0; i < users.size(); i++ ){
                        if (users.get(i).id.equals(AppData.user.id)){
                            bJoin = true;
                            btn_join.setText("Leave");
                            return;
                        }
                    }
                    bJoin = false;
                    btn_join.setText("Join");
                }
                @Override
                public void onFailure(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    refresh_layout.finishRefresh();
                }
            });
        }
    };

    TagUserAdapter.EventListener userListener = new TagUserAdapter.EventListener() {
        @Override
        public void onClickItem(int index) {

        }

        @Override
        public void onClickProfile(User user) {
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra(Constants.USER, (Parcelable) user);
            startActivity(intent);
        }
    };

    public TeamFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public TeamFragment(Context context, User user, Listener listener) {
        this.context = context;
        this.mListener = listener;
        this.user = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        ButterKnife.bind(this, view);
        adapter = new TagUserAdapter(context, users, 1, null);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(adapter);

        refresh_layout.setOnRefreshListener(refreshListener);
        refresh_layout.autoRefresh();

        if (user.id.equals(AppData.user.id) || (user.group_id == Constants.PLAYER || user.group_id == Constants.COACH) ||
        AppData.user.group_id > Constants.COACH){
            btn_join.setVisibility(View.GONE);
        } else {
            btn_join.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @OnClick(R.id.btn_join) void onClickJoin(){
        if(bJoin){
            showProgress("Leaving...");
            API.leaveTeam(context, user.id, AppData.user.id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    dismissProgress();
                    refresh_layout.autoRefresh();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            });
        } else{
            showProgress("Joining...");
            API.joinTeam(context, user.id, AppData.user.id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    dismissProgress();
                    refresh_layout.autoRefresh();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    KProgressHUD hud;
    public void showProgress(String message) {
        hud = KProgressHUD.create(context).setLabel(message);
        hud.show();
    }

    public void dismissProgress() {
        if (hud != null) {
            hud.dismiss();
        }
    }

    public interface Listener {
    }
}
