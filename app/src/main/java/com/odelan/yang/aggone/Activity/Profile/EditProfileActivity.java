package com.odelan.yang.aggone.Activity.Profile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.odelan.yang.aggone.Adapter.HeightWheelAdapter;
import com.odelan.yang.aggone.Adapter.SportWheelAdapter;
import com.odelan.yang.aggone.Dialog.ActivityDialog;
import com.odelan.yang.aggone.Dialog.TextInputDialog;
import com.odelan.yang.aggone.Model.Sport;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.odelan.yang.aggone.Activity.Auth.ProfileSetupActivity.AUTOCOMPLETE_REQUEST_CODE;

public class EditProfileActivity extends BaseActivity {

    @BindView(R.id.img_avata) CircleImageView img_avata;
    @BindView(R.id.txt_name) TextView txt_name;
    @BindView(R.id.txt_category) TextView txt_category;
    @BindView(R.id.txt_city) TextView txt_city;
    @BindView(R.id.txt_type) TextView txt_type;
    @BindView(R.id.txt_club) TextView txt_club;
    @BindView(R.id.txt_position) TextView txt_position;
    @BindView(R.id.txt_contract) TextView txt_contract;
    @BindView(R.id.txt_specialty) TextView txt_specialty;
    @BindView(R.id.txt_age) TextView txt_age;
    @BindView(R.id.txt_weight) TextView txt_weight;

    @BindView(R.id.btn_man)      TextView btn_man;
    @BindView(R.id.btn_woman)    TextView btn_woman;
    @BindView(R.id.btn_other)    TextView btn_other;
    @BindView(R.id.btn_private)    ImageView btn_private;
    @BindView(R.id.btn_available)    ImageView btn_available;
    @BindView(R.id.personal_panel)    LinearLayout personal_panel;

    private int selectedSport;
    private File photo;
    private int height;
    private int age;
    private int weight;
    private int gender = AppData.user.gender_id;
    private int group_id = AppData.user.group_id;
    String country = AppData.user.country;
    int nprivate = 0;
    int navailable = 0;

    @BindView(R.id.recycler_view_sports) RecyclerView recycler_view_sports;
    SportWheelAdapter adapter;
    SnapHelper snapHelper;
    LinearLayoutManager layoutManager;
    List<Sport> sports = new ArrayList<>();

    /** Sport Adapter Event */
    SportWheelAdapter.EventListener sportWheelListener = new SportWheelAdapter.EventListener() {
        @Override
        public void onSelectItem(int index) {
            selectedSport = sports.get(index).id;
        }
    };

    @BindView(R.id.recycler_view_height)    RecyclerView recycler_view_height;
    HeightWheelAdapter heightAdapter;
    SnapHelper heightSnapHelper;
    LinearLayoutManager heightLayoutManager;
    List<Pair<Integer, Boolean>> heights = new ArrayList<>();

    /** Height Adapter Event */
    HeightWheelAdapter.EventListener heightWheelListener = new HeightWheelAdapter.EventListener() {
        @Override
        public void onSelectItem(int index) {
            height = heights.get(index).first;
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

    RecyclerView.OnScrollListener heightScrollListener = new RecyclerView.OnScrollListener() {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ButterKnife.bind(this);
        setActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);
            photo = new File(mPaths.get(0));
            Glide.with(this).load(Uri.fromFile(photo)).into(img_avata);
        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                //Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                String addr_str = place.getAddress();
                String[] addr = addr_str.split(",");
                if (addr.length > 1){
                    txt_city.setText(addr[0]);
                    int length = addr.length;
                    country = addr[length - 1].trim();
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
    void setActivity() {
        /** Sport wheel Recycler view setting */
        sports = getAllSportsWithEdge();
        //sports.get(1).selected = true;

        for(int i = 0; i < sports.size(); i++){
            if(sports.get(i).id == AppData.user.sport_id){
                sports.get(i).selected = true;
                break;
            }
        }

        adapter = new SportWheelAdapter(this, sports, sportWheelListener);
        layoutManager = new LinearLayoutManager(this);
        recycler_view_sports.setLayoutManager(layoutManager);
        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recycler_view_sports);
        recycler_view_sports.setOnFlingListener(snapHelper);
        recycler_view_sports.setAdapter(adapter);
        recycler_view_sports.addOnScrollListener(scrollListener);
        recycler_view_sports.scrollToPosition(AppData.user.sport_id-1000);


        /**Profile setting*/
        txt_name.setText(AppData.user.username);
        txt_city.setText(AppData.user.city);
        //txt_type.setText(AppData.user.group_id == Constants.PLAYER ? getString(R.string.player) : getString(R.string.coach));
        switch (AppData.user.group_id){
            case Constants.PLAYER:
                txt_type.setText(getString(R.string.player));
                break;
            case Constants.COACH:
                txt_type.setText(getString(R.string.coach));
                break;
            case Constants.TEAM_CLUB:
                txt_type.setText(getString(R.string.club));
                break;
            case Constants.AGENT:
                txt_type.setText(getString(R.string.agent));
                break;
            case Constants.STAFF:
                txt_type.setText(getString(R.string.staff));
                break;
            case Constants.COMPANY:
                txt_type.setText(getString(R.string.company));
                break;
            default:
                txt_type.setText("");
                break;
        }
        txt_club.setText(AppData.user.club);
        txt_position.setText(AppData.user.position);
        txt_specialty.setText(AppData.user.contract);
        txt_category.setText(AppData.user.category);
        txt_age.setText(String.valueOf(AppData.user.age));
        txt_weight.setText(String.valueOf(AppData.user.weight));

        age = AppData.user.age;
        weight = AppData.user.weight;
        height = Math.max(AppData.user.height, 120);

        if (AppData.user.photo_url != null && !AppData.user.photo_url.isEmpty()) {
            if(AppData.user.photo_url.contains("http")){
                Glide.with(this).load(AppData.user.photo_url).into(img_avata);
            } else {
                Glide.with(this).load(API.baseUrl + API.imgDirUrl + AppData.user.photo_url).into(img_avata);
            }
        } else {
            img_avata.setImageResource(R.mipmap.default_avata);
        }

        selectedSport = AppData.user.sport_id;

        /** Height Recycler view setting */
        for (int i = 100; i < 300; i++) {
            heights.add(Pair.create(i, i == height ? true : false));
        }
        heightAdapter = new HeightWheelAdapter(this, heights, heightWheelListener);
        heightLayoutManager = new LinearLayoutManager(this);
        recycler_view_height.setLayoutManager(heightLayoutManager);
        heightSnapHelper = new LinearSnapHelper();
        heightSnapHelper.attachToRecyclerView(recycler_view_height);
        recycler_view_height.setOnFlingListener(heightSnapHelper);
        recycler_view_height.setAdapter(heightAdapter);
        recycler_view_height.addOnScrollListener(heightScrollListener);
        recycler_view_height.scrollToPosition(height - 104);

        navailable = AppData.user.available_club;

        refreshActivity();
    }

    void refreshActivity(){
        if (group_id > Constants.COACH){
            personal_panel.setVisibility(View.GONE);
        } else{
            personal_panel.setVisibility(View.VISIBLE);
        }

        switch (gender){
            case Constants.GENDER_WOMAN:
                btn_man.setBackgroundResource(R.drawable.gender_choose_inactive_male);
                btn_man.setTextColor(Color.BLACK);
                btn_woman.setBackgroundResource(R.drawable.gender_choose_active_female);
                btn_woman.setTextColor(Color.WHITE);
                btn_other.setBackgroundResource(R.drawable.gender_choose_inactive_other);
                btn_other.setTextColor(Color.BLACK);
                break;
            case Constants.GENDER_OTHER:
                btn_man.setBackgroundResource(R.drawable.gender_choose_inactive_male);
                btn_man.setTextColor(Color.BLACK);
                btn_woman.setBackgroundResource(R.drawable.gender_choose_inactive_female);
                btn_woman.setTextColor(Color.BLACK);
                btn_other.setBackgroundResource(R.drawable.gender_choose_active_other);
                btn_other.setTextColor(Color.WHITE);
                break;
            default:
                btn_man.setBackgroundResource(R.drawable.gender_choose_active_male);
                btn_man.setTextColor(Color.WHITE);
                btn_woman.setBackgroundResource(R.drawable.gender_choose_inactive_female);
                btn_woman.setTextColor(Color.BLACK);
                btn_other.setBackgroundResource(R.drawable.gender_choose_inactive_other);
                btn_other.setTextColor(Color.BLACK);
                break;
        }
        if(nprivate == 1){
            btn_private.setImageResource(R.mipmap.ic_switch_on);
        } else{
            btn_private.setImageResource(R.mipmap.ic_switch_off);
        }
        if(navailable == 1){
            btn_available.setImageResource(R.mipmap.ic_switch_on);
        } else{
            btn_available.setImageResource(R.mipmap.ic_switch_off);
        }
    }

    private void updateUserField(final String field, final String val){
        API.updateUserFields(this, field, val, new APICallback<User>() {
            @Override
            public void onSuccess(User response) {
                //success
                AppData.user = response;
                navailable = AppData.user.available_club;
                refreshActivity();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(EditProfileActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_private) void onClickPrivate(){
        nprivate = nprivate == 0? 1:0;
        refreshActivity();
    }

    @OnClick(R.id.btn_available) void onClickAvailable(){
        navailable = navailable == 0? 1:0;
        updateUserField("available_club", "" + navailable);
    }

    @OnClick(R.id.btn_man) void onClickMan(){
        gender= Constants.GENDER_MAN;
        refreshActivity();
    }

    @OnClick(R.id.btn_woman) void onClickWoman(){
        gender= Constants.GENDER_WOMAN;
        refreshActivity();
    }

    @OnClick(R.id.btn_other) void onClickOther(){
        gender= Constants.GENDER_OTHER;
        refreshActivity();
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(Constants.USER, (Parcelable)AppData.user);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.btn_apply) void onClickApply() {
        if (txt_name.getText().toString().isEmpty()) {
            showToast("Please input name");
            return;
        }
        final User user = AppData.user;
        user.username = txt_name.getText().toString();
        user.city = txt_city.getText().toString();
        user.club = txt_club.getText().toString();
        user.group_id = group_id;
        user.sport_id = selectedSport;
        user.position = txt_position.getText().toString();
        user.contract = txt_specialty.getText().toString();
        user.category = txt_category.getText().toString();
        user.age = age;
        user.weight = weight;
        user.height = height;
        if (group_id > Constants.COACH){
            user.age = Constants.AGE_DEFAULT;
            user.weight = Constants.WEIGHT_DEFAULT;
            user.height = Constants.HEIGHT_DEFAULT;
        }
        user.gender_id = gender;
        user.country = country;

        if (photo != null) {
            showProgress();
            String name = "photo_" + user.id + "_" + String.valueOf(System.currentTimeMillis()/1000);
            API.uploadImage(name, photo, new APICallback<String>() {
                @Override
                public void onSuccess(String response) {
                    user.photo_url = response;
                    updateProfile(user);
                    dismissProgress();
                }
                @Override
                public void onFailure(String error) {
                    dismissProgress();
                    showToast(error);
                }
            });
        } else {
            updateProfile(user);
        }
    }
    void updateProfile(User user) {
        showProgress();
        API.updateUser(this, user, new APICallback<User>() {
            @Override
            public void onSuccess(User response) {
                AppData.user = response;
                dismissProgress();
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
                intent.putExtra(Constants.USER, (Parcelable)AppData.user);
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
    @OnClick(R.id.btn_discard) void onClickDiscard() {
        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
        intent.putExtra(Constants.USER, (Parcelable) AppData.user);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_camera) void onClickCamera() {
        new ImagePicker.Builder(this)
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.HARD)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.JPG)
                .scale(500, 500)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build();
    }
    @OnClick(R.id.btn_name) void onClickName() {
        TextInputDialog dialog = new TextInputDialog(this, getString(R.string.name), txt_name.getText().toString());
        dialog.setListener(new TextInputDialog.Listener() {
            @Override
            public void onClickOK(String value) {
                txt_name.setText(value);
            }
        });
        View decorView = dialog.getWindow().getDecorView();
        decorView.setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }
    @OnClick(R.id.btn_category) void onClickCategory() {
        TextInputDialog dialog = new TextInputDialog(this, getString(R.string.category), txt_category.getText().toString());
        dialog.setListener(new TextInputDialog.Listener() {
            @Override
            public void onClickOK(String value) {
                txt_category.setText(value);
            }
        });
        View decorView = dialog.getWindow().getDecorView();
        decorView.setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

    @OnClick(R.id.btn_specialty) void onClickSpecialty() {
        TextInputDialog dialog = new TextInputDialog(this, getString(R.string.specialty), txt_specialty.getText().toString());
        dialog.setListener(new TextInputDialog.Listener() {
            @Override
            public void onClickOK(String value) {
                txt_specialty.setText(value);
            }
        });
        View decorView = dialog.getWindow().getDecorView();
        decorView.setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }
    @OnClick(R.id.btn_city) void onClickCity() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setTypeFilter(TypeFilter.REGIONS)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }
    @OnClick(R.id.btn_type) void onClickType() {
        ActivityDialog dialog = new ActivityDialog(this, group_id);
        dialog.setListener(new ActivityDialog.Listener() {
            @Override
            public void onSelectType(int type) {
                group_id = type;
                switch (group_id){
                    case Constants.PLAYER:
                        txt_type.setText(getString(R.string.player));
                        break;
                    case Constants.COACH:
                        txt_type.setText(getString(R.string.coach));
                        break;
                    case Constants.TEAM_CLUB:
                        txt_type.setText(getString(R.string.club));
                        break;
                    case Constants.AGENT:
                        txt_type.setText(getString(R.string.agent));
                        break;
                    case Constants.STAFF:
                        txt_type.setText(getString(R.string.staff));
                        break;
                    case Constants.COMPANY:
                        txt_type.setText(getString(R.string.company));
                        break;
                    default:
                        txt_type.setText("");
                        break;
                }
                refreshActivity();
            }
        });
        View decorView = dialog.getWindow().getDecorView();
        decorView.setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }
    @OnClick(R.id.btn_club) void onClickClub() {
        TextInputDialog dialog = new TextInputDialog(this, getString(R.string.club), txt_club.getText().toString());
        dialog.setListener(new TextInputDialog.Listener() {
            @Override
            public void onClickOK(String value) {
                txt_club.setText(value);
            }
        });
        View decorView = dialog.getWindow().getDecorView();
        decorView.setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }
    @OnClick(R.id.btn_position) void onClickPosition() {
        TextInputDialog dialog = new TextInputDialog(this, getString(R.string.position), txt_position.getText().toString());
        dialog.setListener(new TextInputDialog.Listener() {
            @Override
            public void onClickOK(String value) {
                txt_position.setText(value);
            }
        });
        View decorView = dialog.getWindow().getDecorView();
        decorView.setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }
    @OnClick(R.id.btn_contract) void onClickContract() {
        TextInputDialog dialog = new TextInputDialog(this, getString(R.string.contract), txt_contract.getText().toString());
        dialog.setListener(new TextInputDialog.Listener() {
            @Override
            public void onClickOK(String value) {
                txt_contract.setText(value);
            }
        });
        View decorView = dialog.getWindow().getDecorView();
        decorView.setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }
    @OnClick(R.id.btn_age_minus) void onClickAgeMinus() {
        if (age > 0) age--;
        txt_age.setText(String.valueOf(age));
    }
    @OnClick(R.id.btn_age_plus) void onClickAgePlus() {
        age++;
        txt_age.setText(String.valueOf(age));
    }
    @OnClick(R.id.btn_weight_minus) void onClickWeightMinus() {
        if (weight > 0) weight--;
        txt_weight.setText(String.valueOf(weight));
    }
    @OnClick(R.id.btn_weight_plus) void onClickWeightPlus() {
        weight++;
        txt_weight.setText(String.valueOf(weight));
    }

}
