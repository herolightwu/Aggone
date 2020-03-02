package com.odelan.yang.aggone.Activity.LookUp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.jwang123.flagkit.FlagKit;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Activity.MainActivity;
import com.odelan.yang.aggone.Activity.Message.ContactActivity;
import com.odelan.yang.aggone.Activity.Profile.ProfileActivity;
import com.odelan.yang.aggone.Activity.Stats.ClubChartActivity;
import com.odelan.yang.aggone.Activity.Stats.StatsActivity;
import com.odelan.yang.aggone.Adapter.LookUp.LookingPlayerAdapter;
import com.odelan.yang.aggone.Adapter.SportWheelAdapter;
import com.odelan.yang.aggone.Model.Admob;
import com.odelan.yang.aggone.Model.Feed;
import com.odelan.yang.aggone.Model.Sport;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.MyApp;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.odelan.yang.aggone.Activity.Auth.ProfileSetupActivity.AUTOCOMPLETE_REQUEST_CODE;

public class LookingPlayerActivity extends BaseActivity {

    @BindView(R.id.btn_lookup)    ImageView btn_lookup;
    @BindView(R.id.txt_lookup)    TextView txt_lookup;

    @BindView(R.id.edit_city)               EditText edit_city;
    @BindView(R.id.edit_nationality)        EditText edit_nation;
    @BindView(R.id.img_flag)                ImageView img_flag;
    @BindView(R.id.edit_gender)       EditText edit_gender;
    @BindView(R.id.edit_gender_other) EditText edit_gender_other;
    @BindView(R.id.edit_gender_else) EditText edit_gender_else;

    @BindView(R.id.refresh_layout) SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view) RecyclerView recycler_view;

    @BindView(R.id.recycler_view_sports) RecyclerView recycler_sport;
    SportWheelAdapter sportadapter;
    SnapHelper snapHelper;
    LinearLayoutManager layoutManager;
    List<Sport> sports = new ArrayList<>();

    LookingPlayerAdapter adapter;
    List<User> users = new ArrayList<>();

    private int selectedSport = Constants.Football;
    private int gender = Constants.GENDER_MAN;

    /** Sport Adapter Event */
    SportWheelAdapter.EventListener sportWheelListener = new SportWheelAdapter.EventListener() {
        @Override
        public void onSelectItem(int index) {
            selectedSport = sports.get(index).id;
        }
    };

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            View centerView = snapHelper.findSnapView(layoutManager);
            int position = layoutManager.getPosition(centerView);
            for (int i = 0; i < sports.size(); i++) {
                sports.get(i).selected = false;
            }
            sports.get(position).selected = true;
            sportadapter.notifyDataSetChanged();
        }
    };

    LookingPlayerAdapter.EventListener playerListener = user -> {
        Intent intent = new Intent(LookingPlayerActivity.this, ProfileActivity.class);
        intent.putExtra(Constants.USER, (Parcelable) user);
        startActivity(intent);
    };

    OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
            showProgress();
            API.getUsersByFilter(selectedSport, gender, Constants.PLAYER, "", edit_city.getText().toString(), edit_nation.getText().toString(), 0,0,0, new APICallback<List<User>>() {
                @Override
                public void onSuccess(List<User> response) {
                    dismissProgress();
                    refresh_layout.finishRefresh();
                    users.clear();
                    users.addAll(response);
                    adapter.setData(users);
                }
                @Override
                public void onFailure(String error) {
                    dismissProgress();
                    refresh_layout.finishRefresh();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looking_player);
        ButterKnife.bind(this);

        setActivity();
    }

    void setActivity() {
        btn_lookup.setImageResource(R.mipmap.tab_lookup_active);
        txt_lookup.setTextColor(getResources().getColor(R.color.tab_active));

        adapter = new LookingPlayerAdapter(this, users, playerListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);

        refresh_layout.setOnRefreshListener(refreshListener);
        refresh_layout.autoRefresh();

        /** Sport wheel Recycler view setting */
        sports = getAllSportsWithEdge();
        sports.get(1).selected = true;

        sportadapter = new SportWheelAdapter(this, sports, sportWheelListener);
        layoutManager = new LinearLayoutManager(this);
        recycler_sport.setLayoutManager(layoutManager);
        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recycler_sport);
        recycler_sport.setOnFlingListener(snapHelper);
        recycler_sport.setAdapter(sportadapter);
        recycler_sport.addOnScrollListener(scrollListener);

        edit_gender_other.setVisibility(View.GONE);
        edit_gender_else.setVisibility(View.GONE);
        edit_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gender == Constants.GENDER_MAN){
                    edit_gender.setText(getString(R.string.gender_man));
                    edit_gender_other.setText(getString(R.string.gender_woman));
                    edit_gender_else.setText(getString(R.string.gender_other));
                } else if (gender == Constants.GENDER_WOMAN){
                    edit_gender.setText(getString(R.string.gender_woman));
                    edit_gender_other.setText(getString(R.string.gender_man));
                    edit_gender_else.setText(getString(R.string.gender_other));
                } else {
                    edit_gender.setText(getString(R.string.gender_other));
                    edit_gender_other.setText(getString(R.string.gender_man));
                    edit_gender_else.setText(getString(R.string.gender_woman));
                }
                if (edit_gender_other.getVisibility() == View.VISIBLE){
                    edit_gender_other.setVisibility(View.GONE);
                    edit_gender_else.setVisibility(View.GONE);
                } else{
                    edit_gender_other.setVisibility(View.VISIBLE);
                    edit_gender_else.setVisibility(View.VISIBLE);
                }
            }
        });
        edit_gender_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gender == Constants.GENDER_MAN){
                    edit_gender.setText(getString(R.string.gender_woman));
                    gender = Constants.GENDER_WOMAN;
                } else if (gender == Constants.GENDER_WOMAN){
                    edit_gender.setText(getString(R.string.gender_man));
                    gender = Constants.GENDER_MAN;
                } else{
                    edit_gender.setText(getString(R.string.gender_man));
                    gender = Constants.GENDER_MAN;
                }
                edit_gender_other.setVisibility(View.GONE);
                edit_gender_else.setVisibility(View.GONE);
            }
        });
        edit_gender_else.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gender == Constants.GENDER_OTHER){
                    edit_gender.setText(getString(R.string.gender_woman));
                    gender = Constants.GENDER_WOMAN;
                } else{
                    edit_gender.setText(getString(R.string.gender_other));
                    gender = Constants.GENDER_OTHER;
                }
                edit_gender_other.setVisibility(View.GONE);
                edit_gender_else.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.edit_city) void onClickCity() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setTypeFilter(TypeFilter.REGIONS)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @OnClick(R.id.btn_reset) void onClickReset() {
        selectedSport = Constants.Football;

        edit_city.setText("");
        edit_nation.setText("");
        img_flag.setImageDrawable(null);

        sports.get(1).selected = true;
        recycler_sport.scrollToPosition(0);
        users.clear();
        adapter.setData(users);
    }

    @OnClick(R.id.btn_apply) void onClickApply() {
        showProgress();
        API.getUsersByFilter(selectedSport, gender, Constants.PLAYER, "", edit_city.getText().toString(), edit_nation.getText().toString(), 0,0,0, new APICallback<List<User>>() {
            @Override
            public void onSuccess(List<User> response) {
                dismissProgress();
                refresh_layout.finishRefresh();
                users.clear();
                users.addAll(response);
                adapter.setData(users);
            }
            @Override
            public void onFailure(String error) {
                dismissProgress();
                showToast(error);
            }
        });
    }

    /**
     * Tab bar Event
     * */
    @OnClick(R.id.btn_lookup) void onClickLookup() {
        finish();
    }

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
    @OnClick(R.id.btn_chats) void onClickChats() {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                //Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                String addr_str = place.getAddress();
                String[] addr = addr_str.split(",");
                if (addr.length > 1){
                    edit_city.setText(addr[0]);
                    int length = addr.length;
                    edit_nation.setText(addr[length - 1]);
                    img_flag.setImageDrawable(null);
                    String c_code = MyApp.countries.get(addr[length - 1].trim());
                    if (addr[length - 1].trim().equals("USA"))
                        c_code = "US";
                    if (c_code != null){
                        Drawable flag = FlagKit.drawableWithFlag(LookingPlayerActivity.this, c_code.toLowerCase());
                        img_flag.setImageDrawable(flag);
                    }
                    return;
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                //Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            Toast.makeText(this, "Please try to search city again", Toast.LENGTH_SHORT).show();
        }
    }
}
