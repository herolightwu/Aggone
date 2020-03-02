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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.odelan.yang.aggone.Activity.Auth.ProfileSetupActivity;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Activity.MainActivity;
import com.odelan.yang.aggone.Activity.Message.ContactActivity;
import com.odelan.yang.aggone.Activity.Stats.ClubChartActivity;
import com.odelan.yang.aggone.Activity.Stats.StatsActivity;
import com.odelan.yang.aggone.Adapter.SportWheelAdapter;
import com.odelan.yang.aggone.Model.Admob;
import com.odelan.yang.aggone.Model.Sport;
import com.odelan.yang.aggone.MyApp;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

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

public class CreateAdActivity extends BaseActivity {

    @BindView(R.id.btn_lookup) ImageView btn_lookup;
    @BindView(R.id.txt_lookup) TextView txt_lookup;

    @BindView(R.id.edit_position) EditText edit_position;
    @BindView(R.id.edit_city) EditText edit_city;
    @BindView(R.id.edit_description) EditText edit_description;
    @BindView(R.id.img_flag)    ImageView img_flag;

    @BindView(R.id.recycler_view_sports) RecyclerView recycler_view_sports;
    SportWheelAdapter adapter;
    SnapHelper snapHelper;
    LinearLayoutManager layoutManager;
    List<Sport> sports = new ArrayList<>();

    boolean bflag = false;

    private int selectedSports = -1;

    /** Sport Adapter Event */
    SportWheelAdapter.EventListener sportWheelListener = new SportWheelAdapter.EventListener() {
        @Override
        public void onSelectItem(int index) {
            selectedSports = sports.get(index).id;
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
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad);
        ButterKnife.bind(this);

        setActivity();
    }

    void setActivity() {
        /** Tab bar setting */
        btn_lookup.setImageResource(R.mipmap.tab_lookup_active);
        txt_lookup.setTextColor(getResources().getColor(R.color.tab_active));

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
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
    @OnClick(R.id.btn_send) void onClickSend() {
        if (selectedSports == -1) {
            showToast("Please select sports");
            return;
        }

        if (edit_city.getText().toString().isEmpty()) {
            showToast("Please choose city");
            return;
        }
        if (edit_position.getText().toString().isEmpty()) {
            showToast("Please choose position");
            return;
        }
        if (edit_description.getText().toString().isEmpty()) {
            showToast("Please input description");
            return;
        }

        Admob admob = new Admob();
        admob.user = AppData.user;
        admob.city = edit_city.getText().toString();
        admob.position = edit_position.getText().toString();
        admob.description = edit_description.getText().toString();
        admob.sport_id = selectedSports;

        showProgress();
        API.saveAdmob(this, admob, new APICallback<Admob>() {
            @Override
            public void onSuccess(Admob response) {
                dismissProgress();
                finish();
            }
            @Override
            public void onFailure(String error) {
                dismissProgress();
                showToast(error);
            }
        });
    }
    @OnClick(R.id.btn_reset) void onClickReset() {
        edit_city.setText("");
        edit_description.setText("");
        edit_position.setText("");
        bflag = false;
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
                if (addr.length > 1) {
                    bflag = false;
                    edit_city.setText(addr[0]);
                    int length = addr.length;
                    edit_position.setText(addr[length - 1]);
                    img_flag.setImageDrawable(null);
                    String c_code = MyApp.countries.get(addr[length - 1].trim());
                    if (addr[length - 1].trim().equals("USA"))
                        c_code = "US";
                    if (c_code != null) {
                        Drawable flag = FlagKit.drawableWithFlag(CreateAdActivity.this, c_code.toLowerCase());
                        img_flag.setImageDrawable(flag);
                        bflag = true;
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
