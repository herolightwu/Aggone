package com.odelan.yang.aggone.Activity.LookUp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.SnapHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.jwang123.flagkit.FlagKit;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Activity.Profile.ProfileActivity;
import com.odelan.yang.aggone.Adapter.HeightWheelAdapter;
import com.odelan.yang.aggone.Adapter.LookUp.LookingFilterAdapter;
import com.odelan.yang.aggone.Adapter.SportWheelAdapter;
import com.odelan.yang.aggone.Model.Sport;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.MyApp;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.odelan.yang.aggone.Activity.Auth.ProfileSetupActivity.AUTOCOMPLETE_REQUEST_CODE;
import static com.odelan.yang.aggone.Utils.Constants.AGE_DEFAULT;
import static com.odelan.yang.aggone.Utils.Constants.AGE_MAX;
import static com.odelan.yang.aggone.Utils.Constants.AGE_MIN;
import static com.odelan.yang.aggone.Utils.Constants.HEIGHT_DEFAULT;
import static com.odelan.yang.aggone.Utils.Constants.HEIGHT_MAX;
import static com.odelan.yang.aggone.Utils.Constants.HEIGHT_MIN;
import static com.odelan.yang.aggone.Utils.Constants.WEIGHT_DEFAULT;
import static com.odelan.yang.aggone.Utils.Constants.WEIGHT_MAX;
import static com.odelan.yang.aggone.Utils.Constants.WEIGHT_MIN;

public class FilterActivity extends BaseActivity {

    @BindView(R.id.edit_name) EditText edit_name;
    @BindView(R.id.edit_city) EditText edit_city;
    @BindView(R.id.edit_nationality)       EditText edit_nation;
    @BindView(R.id.layout_country)       LinearLayout ll_country;
    @BindView(R.id.layout_physical)      LinearLayout ll_physical;
    @BindView(R.id.img_flag)            ImageView img_flag;

    @BindView(R.id.txt_age) TextView txt_age;
    @BindView(R.id.txt_weight) TextView txt_weight;

    @BindView(R.id.recycler_view_sports)    RecyclerView recycler_view_sports;
    SportWheelAdapter adapter;
    SnapHelper snapHelper;
    LinearLayoutManager layoutManager;
    List<Sport> sports = new ArrayList<>();

    @BindView(R.id.recycler_view_height)    RecyclerView recycler_view_height;
    HeightWheelAdapter heightAdapter;
    SnapHelper heightSnapHelper;
    LinearLayoutManager heightLayoutManager;
    List<Pair<Integer, Boolean>> heights = new ArrayList<>();

    @BindView(R.id.rg_gender)    RadioGroup gendergroup;
    @BindView(R.id.rb_man)    RadioButton rb_man;
    @BindView(R.id.rb_woman)    RadioButton rb_woman;
    @BindView(R.id.rb_other)    RadioButton rb_other;

    @BindView(R.id.btn_player)              ImageView btn_player;
    @BindView(R.id.btn_coach)               ImageView btn_coach;
    @BindView(R.id.btn_club)                ImageView btn_club;
    @BindView(R.id.btn_agent)               ImageView btn_agent;
    @BindView(R.id.btn_staff)               ImageView btn_staff;
    @BindView(R.id.btn_company)             ImageView btn_company;

    @BindView(R.id.result_layout)           RelativeLayout result_layout;
    @BindView(R.id.edit_search)             EditText       edit_search;
    @BindView(R.id.refresh_layout)          SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view)           RecyclerView    recycler_view;

    private int selectedSport = Constants.Football;
    private int type = Constants.PLAYER;
    private int weight = WEIGHT_DEFAULT;
    private int height = HEIGHT_DEFAULT;
    private int age = AGE_DEFAULT;
    private int gender = Constants.GENDER_MAN;

    LookingFilterAdapter resultAdapter;
    List<User> users = new ArrayList<>();

    /** Sport Adapter Event */
    SportWheelAdapter.EventListener sportWheelListener = new SportWheelAdapter.EventListener() {
        @Override
        public void onSelectItem(int index) {
            selectedSport = sports.get(index).id;
        }
    };

    OnScrollListener scrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            View centerView = snapHelper.findSnapView(layoutManager);
            int position = layoutManager.getPosition(centerView);
            for (int i = 0; i < sports.size(); i++) {
                sports.get(i).selected = false;
            }
            sports.get(position).selected = true;
            adapter.notifyDataSetChanged();
        }
    };

    /** Height Adapter Event */
    HeightWheelAdapter.EventListener heightWheelListener = new HeightWheelAdapter.EventListener() {
        @Override
        public void onSelectItem(int index) {
            height = heights.get(index).first;
        }
    };

    OnScrollListener heightScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            View centerView = heightSnapHelper.findSnapView(heightLayoutManager);
            int position = heightLayoutManager.getPosition(centerView);
            for (int i = 0; i < heights.size(); i++) {
                heights.set(i, Pair.create(heights.get(i).first, false));
            }
            heights.set(position, Pair.create(heights.get(position).first, true));
            heightAdapter.notifyDataSetChanged();
        }
    };

    LookingFilterAdapter.EventListener playerListener = user -> {
        Intent intent = new Intent(FilterActivity.this, ProfileActivity.class);
        intent.putExtra(Constants.USER, (Parcelable) user);
        startActivity(intent);
    };

    OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
            showProgress();
            API.getUsersByFilter(selectedSport, gender, type, edit_name.getText().toString(), edit_city.getText().toString(), edit_nation.getText().toString(), age,height,weight, new APICallback<List<User>>() {
                @Override
                public void onSuccess(List<User> response) {
                    dismissProgress();
                    refresh_layout.finishRefresh();
                    users.clear();
                    users.addAll(response);
                    resultAdapter.setDataList(users);
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
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);

        setActivity();
    }

    void setActivity() {
        txt_age.setText(String.valueOf(age));
        txt_weight.setText(String.valueOf(weight));

        /** Sport wheel Recycler view setting */
        sports = getAllSportsWithEdge();
        sports.get(1).selected = true;

        adapter = new SportWheelAdapter(this, sports, sportWheelListener);
        layoutManager = new LinearLayoutManager(this);
        recycler_view_sports.setLayoutManager(layoutManager);
        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recycler_view_sports);
        recycler_view_sports.setOnFlingListener(snapHelper);
        recycler_view_sports.setAdapter(adapter);
        recycler_view_sports.addOnScrollListener(scrollListener);

        /** Height Recycler view setting */
        for (int i = HEIGHT_MIN; i < HEIGHT_MAX; i++) {
            heights.add(Pair.create(i, i == HEIGHT_DEFAULT ? true : false));
        }
        heightAdapter = new HeightWheelAdapter(this, heights, heightWheelListener);
        heightLayoutManager = new LinearLayoutManager(this);
        recycler_view_height.setLayoutManager(heightLayoutManager);
        heightSnapHelper = new LinearSnapHelper();
        heightSnapHelper.attachToRecyclerView(recycler_view_height);
        recycler_view_height.setOnFlingListener(heightSnapHelper);
        recycler_view_height.setAdapter(heightAdapter);
        recycler_view_height.addOnScrollListener(heightScrollListener);
        recycler_view_height.scrollToPosition(76);

        gendergroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.rb_man) {
                    gender = Constants.GENDER_MAN;
                } else if(checkedId == R.id.rb_woman) {
                    gender = Constants.GENDER_WOMAN;
                } else {
                    gender = Constants.GENDER_OTHER;
                }
            }

        });

        ll_country.setVisibility(View.GONE);

        resultAdapter = new LookingFilterAdapter(this, users, playerListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(resultAdapter);

        refresh_layout.setOnRefreshListener(refreshListener);
        refresh_layout.autoRefresh();

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                resultAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick(R.id.btn_result_back) void onBackResult(){
        users.clear();
        result_layout.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_search_clear) void onSearchCancel(){
        edit_search.setText("");
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

    @OnClick(R.id.btn_player) void onClickPlayer() {
        type = Constants.PLAYER;
        refreshGroup();
    }
    @OnClick(R.id.btn_coach) void onClickCoach() {
        type = Constants.COACH;
        refreshGroup();
    }
    @OnClick(R.id.btn_club) void onClickClub() {
        type = Constants.TEAM_CLUB;
        refreshGroup();
    }
    @OnClick(R.id.btn_agent) void onClickAgent() {
        type = Constants.AGENT;
        refreshGroup();
    }
    @OnClick(R.id.btn_staff) void onClickStaff() {
        type = Constants.STAFF;
        refreshGroup();
    }
    @OnClick(R.id.btn_company) void onClickCompany() {
        type = Constants.COMPANY;
        refreshGroup();
    }

    private void refreshGroup(){
        btn_player.setImageResource(R.mipmap.auth_player_normal);
        btn_coach.setImageResource(R.mipmap.auth_coach_normal);
        btn_club.setImageResource(R.mipmap.auth_club_normal);
        btn_agent.setImageResource(R.mipmap.auth_agent_normal);
        btn_staff.setImageResource(R.mipmap.auth_staff_normal);
        btn_company.setImageResource(R.mipmap.auth_company_normal);
        switch (type){
            case Constants.PLAYER:
                btn_player.setImageResource(R.mipmap.auth_player_active);
                break;
            case Constants.COACH:
                btn_coach.setImageResource(R.mipmap.auth_coach_active);
                break;
            case Constants.TEAM_CLUB:
                btn_club.setImageResource(R.mipmap.auth_club_active);
                break;
            case Constants.AGENT:
                btn_agent.setImageResource(R.mipmap.auth_agent_active);
                break;
            case Constants.STAFF:
                btn_staff.setImageResource(R.mipmap.auth_staff_active);
                break;
            case Constants.COMPANY:
                btn_company.setImageResource(R.mipmap.auth_company_active);
                break;
        }

        if (type == Constants.PLAYER){
            ll_country.setVisibility(View.GONE);
            ll_physical.setVisibility(View.VISIBLE);
        } else{
            ll_country.setVisibility(View.VISIBLE);
            ll_physical.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
    @OnClick(R.id.btn_search) void onClickSearch() {
        showProgress();
        API.getUsersByFilter(
                selectedSport,
                gender,
                type,
                edit_name.getText().toString(),
                edit_city.getText().toString(),
                edit_nation.getText().toString(),
                age,
                height,
                weight,
                new APICallback<List<User>>() {
                    @Override
                    public void onSuccess(List<User> response) {
                        dismissProgress();
                        users.clear();
                        users.addAll(response);
                        resultAdapter.setDataList(users);
                        result_layout.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onFailure(String error) {
                        dismissProgress();
                        showToast(error);
                    }
                });
    }
    @OnClick(R.id.btn_reset) void onClickReset() {
        selectedSport = Constants.Football;
        onClickPlayer();
        type = Constants.PLAYER;

        age = AGE_DEFAULT;
        weight = WEIGHT_DEFAULT;
        height = HEIGHT_DEFAULT;
        edit_city.setText("");
        edit_name.setText("");
        txt_age.setText(String.valueOf(age));
        txt_weight.setText(String.valueOf(weight));

        sports.get(1).selected = true;
        recycler_view_sports.scrollToPosition(0);

        heights.clear();
        for (int i = HEIGHT_MIN; i < HEIGHT_MAX; i++) {
            heights.add(Pair.create(i, i == HEIGHT_DEFAULT ? true : false));
        }
        heightAdapter.notifyDataSetChanged();
        recycler_view_height.scrollToPosition(76);
        refreshGroup();
    }
    @OnClick(R.id.btn_age_minus) void onClickAgeMinus() {
        if (age > AGE_MIN) age--;
        txt_age.setText(String.valueOf(age));
    }
    @OnClick(R.id.btn_age_plus) void onClickAgePlus() {
        if (age < AGE_MAX) age++;
        txt_age.setText(String.valueOf(age));
    }
    @OnClick(R.id.btn_weight_minus) void onClickWeightMinus() {
        if (weight > WEIGHT_MIN) weight--;
        txt_weight.setText(String.valueOf(weight));
    }
    @OnClick(R.id.btn_weight_plus) void onClickWeightPlus() {
        if (weight < WEIGHT_MAX) weight++;
        txt_weight.setText(String.valueOf(weight));
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
                        Drawable flag = FlagKit.drawableWithFlag(FilterActivity.this, c_code.toLowerCase());
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
