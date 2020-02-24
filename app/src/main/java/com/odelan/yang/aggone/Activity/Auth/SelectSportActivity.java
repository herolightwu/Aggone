package com.odelan.yang.aggone.Activity.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Activity.MainActivity;
import com.odelan.yang.aggone.Adapter.SportAdapter;
import com.odelan.yang.aggone.Model.Sport;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;
import com.odelan.yang.aggone.Utils.TinyDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectSportActivity extends BaseActivity implements SportAdapter.EventListener {

    @BindView(R.id.recycler_view)           RecyclerView recycler_view;
    SportAdapter adapter;
    List<Sport> sports = new ArrayList<>();

    private String email;
    private String password;
    private int sign_mode = Constants.SIGNUP_EMAIL;

    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sport);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        sign_mode = intent.getIntExtra(Constants.SIGNUP_MODE, Constants.SIGNUP_EMAIL);
        email = intent.getStringExtra(Constants.EMAIL);
        password = intent.getStringExtra(Constants.PASSWORD);

        tinyDB = new TinyDB(this);
        setActivity();
    }

    void setActivity() {
        sports = getAllSports();
        adapter = new SportAdapter(this, sports, this);
        recycler_view.setLayoutManager(new GridLayoutManager(this, 2));
        recycler_view.setAdapter(adapter);
    }

    @Override
    public void onClickItem(final int index) {
        showProgress();
        if (sign_mode == Constants.SIGNUP_EMAIL){
            API.signupWithEmailAndPassword(this, email, password, new APICallback<User>() {
                @Override
                public void onSuccess(User response) {
                    response.username = AppData.user.username;
                    response.year = AppData.user.year;
                    response.month = AppData.user.month;
                    response.day = AppData.user.day;
                    response.age = AppData.user.age;
                    response.group_id = AppData.user.group_id;
                    response.sport_id = sports.get(index).id;
                    response.city = AppData.user.city;
                    response.country = AppData.user.country;
                    response.gender_id = AppData.user.gender_id;
                    response.weight = Constants.WEIGHT_DEFAULT;
                    response.height = Constants.HEIGHT_DEFAULT;
                    API.updateUser(SelectSportActivity.this, response, new APICallback<User>() {
                        @Override
                        public void onSuccess(User user) {
                            dismissProgress();
                            AppData.user = user;
                            tinyDB.putBoolean(Constants.LOGIN_ISLOGGEDIN, true);
                            tinyDB.putString(Constants.LOGIN_EMAIL, email);
                            tinyDB.putString(Constants.LOGIN_PASSWORD, password);
                            Intent intent = new Intent(SelectSportActivity.this, MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(String error) {
                            dismissProgress();
                            showToast(error);
                        }
                    });
                }

                @Override
                public void onFailure(String error) {
                    dismissProgress();
                    showToast(error);
                }
            });
        } else{
            User response = new User();
            response.email = email;
            response.username = AppData.user.username;
            response.year = AppData.user.year;
            response.month = AppData.user.month;
            response.day = AppData.user.day;
            response.age = AppData.user.age;
            response.group_id = AppData.user.group_id;
            response.sport_id = sports.get(index).id;
            response.city = AppData.user.city;
            response.country = AppData.user.country;
            response.gender_id = AppData.user.gender_id;
            response.weight = Constants.WEIGHT_DEFAULT;
            response.height = Constants.HEIGHT_DEFAULT;
            API.updateUser(SelectSportActivity.this, response, new APICallback<User>() {
                @Override
                public void onSuccess(User user) {
                    dismissProgress();
                    if(user.photo_url == null || user.photo_url.length() == 0){
                        user.photo_url = AppData.user.photo_url;
                    }
                    AppData.user = user;

                    Intent intent = new Intent(SelectSportActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(String error) {
                    dismissProgress();
                    showToast(error);
                }
            });
        }

    }
}
