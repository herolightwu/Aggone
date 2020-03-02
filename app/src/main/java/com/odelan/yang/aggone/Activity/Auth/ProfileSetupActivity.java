package com.odelan.yang.aggone.Activity.Auth;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.MyApp;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileSetupActivity extends BaseActivity {

    public static final int AUTOCOMPLETE_REQUEST_CODE = 9999;

    @BindView(R.id.edit_name)       EditText edit_name;
    @BindView(R.id.edit_year)       EditText edit_year;
    @BindView(R.id.edit_month)      EditText edit_month;
    @BindView(R.id.edit_day)        EditText edit_day;
    @BindView(R.id.edit_city)       EditText edit_city;
    @BindView(R.id.edit_nationality)       EditText edit_nation;
    @BindView(R.id.edit_gender)       EditText edit_gender;
    @BindView(R.id.edit_gender_other) EditText edit_gender_other;
    @BindView(R.id.edit_gender_else) EditText edit_gender_else;
    @BindView(R.id.img_flag)        ImageView img_flag;
    @BindView(R.id.choose_month)    RelativeLayout choose_month;

    @BindView(R.id.month_jan)       TextView month_jan;
    @BindView(R.id.month_feb)       TextView month_feb;
    @BindView(R.id.month_mar)       TextView month_mar;
    @BindView(R.id.month_apr)       TextView month_apr;
    @BindView(R.id.month_may)       TextView month_may;
    @BindView(R.id.month_jun)       TextView month_jun;
    @BindView(R.id.month_jul)       TextView month_jul;
    @BindView(R.id.month_aug)       TextView month_aug;
    @BindView(R.id.month_sep)       TextView month_sep;
    @BindView(R.id.month_oct)       TextView month_oct;
    @BindView(R.id.month_nov)       TextView month_nov;
    @BindView(R.id.month_dec)       TextView month_dec;

    private String email;
    private String password;
    private int gender = Constants.GENDER_MAN;
    private boolean bflag = false;
    private int month = 0;
    private int sign_mode = Constants.SIGNUP_EMAIL;

    public static List<String> MONTH_NAME = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        sign_mode = intent.getIntExtra(Constants.SIGNUP_MODE, Constants.SIGNUP_EMAIL);
        email = intent.getStringExtra(Constants.EMAIL);
        password = intent.getStringExtra(Constants.PASSWORD);

        setLayout();
    }

    private void setLayout(){
        MONTH_NAME = Arrays.asList(getResources().getStringArray(R.array.month_name_array));
        choose_month.setVisibility(View.GONE);
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
        edit_city.setText("Paris");
        edit_nation.setText("France");
        img_flag.setImageDrawable(FlagKit.drawableWithFlag(this, "fr"));
        bflag = true;

        edit_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_month.setVisibility(View.VISIBLE);
            }
        });

        choose_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_month.setVisibility(View.GONE);
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

    @OnClick(R.id.btn_go) void onClickGo() {
        String name = edit_name.getText().toString();

        String year_str = edit_year.getText().toString();
        String month_str = edit_month.getText().toString();
        String day_str = edit_day.getText().toString();
        String city = edit_city.getText().toString();
        String national = edit_nation.getText().toString();

        if (name.isEmpty()) {
            showToast("Please input name");
            return;
        }
        if (year_str.isEmpty()) {
            showToast("Please input year");
            return;
        }
        if (month_str.isEmpty() && month < 1) {
            showToast("Please choose month");
            return;
        }
        if (day_str.isEmpty()) {
            showToast("Please input day");
            return;
        }
        if (city.isEmpty() || national.isEmpty()){
            showToast("Please choose city");
            return;
        }

        int year = Integer.parseInt(year_str);
        //int month = Integer.parseInt(month_str);
        int day = Integer.parseInt(day_str);

        if (!isValidDate(year, month, day)) {
            showToast("Invalid Date");
            return;
        }

        Date now = new Date();
        Date birthday = new Date(year - 1900, month - 1, day);
        long age = (now.getTime() - birthday.getTime());
        if (age <= 0) {
            showToast("Invalid Date");
            return;
        }
        age = age / 1000 / 60 / 60 / 24 / 365;

        if(sign_mode == Constants.SIGNUP_EMAIL){
            User user = new User();
            user.username = name;
            user.year = year;
            user.month = month;
            user.day = day;
            user.age = (int)age;
            user.city = city;
            user.country = national;
            user.gender_id = gender;

            AppData.user = user;
        } else{
            AppData.user.username = name;
            AppData.user.year = year;
            AppData.user.month = month;
            AppData.user.day = day;
            AppData.user.age = (int)age;
            AppData.user.city = city;
            AppData.user.country = national;
            AppData.user.gender_id = gender;
        }

        Intent intent = new Intent(this, SelectGroupActivity.class);
        intent.putExtra(Constants.SIGNUP_MODE, sign_mode);
        intent.putExtra(Constants.EMAIL, email);
        intent.putExtra(Constants.PASSWORD, password);
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
                    bflag = false;
                    edit_city.setText(addr[0]);
                    int length = addr.length;
                    edit_nation.setText(addr[length - 1]);
                    img_flag.setImageDrawable(null);
                    String c_code = MyApp.countries.get(addr[length - 1].trim());
                    if (addr[length - 1].trim().equals("USA"))
                        c_code = "US";
                    if (c_code != null){
                        Drawable flag = FlagKit.drawableWithFlag(ProfileSetupActivity.this, c_code.toLowerCase());
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

    @OnClick(R.id.month_jan) void clickJan(){
        month = 1;
        edit_month.setText(MONTH_NAME.get(month));
        choose_month.setVisibility(View.GONE);
    }
    @OnClick(R.id.month_feb) void clickFeb(){
        month = 2;
        edit_month.setText(MONTH_NAME.get(month));
        choose_month.setVisibility(View.GONE);
    }
    @OnClick(R.id.month_mar) void clickMar(){
        month = 3;
        edit_month.setText(MONTH_NAME.get(month));
        choose_month.setVisibility(View.GONE);
    }
    @OnClick(R.id.month_apr) void clickApr(){
        month = 4;
        edit_month.setText(MONTH_NAME.get(month));
        choose_month.setVisibility(View.GONE);
    }
    @OnClick(R.id.month_may) void clickMay(){
        month = 5;
        edit_month.setText(MONTH_NAME.get(month));
        choose_month.setVisibility(View.GONE);
    }
    @OnClick(R.id.month_jun) void clickJun(){
        month = 6;
        edit_month.setText(MONTH_NAME.get(month));
        choose_month.setVisibility(View.GONE);
    }
    @OnClick(R.id.month_jul) void clickJul(){
        month = 7;
        edit_month.setText(MONTH_NAME.get(month));
        choose_month.setVisibility(View.GONE);
    }
    @OnClick(R.id.month_aug) void clickAug(){
        month = 8;
        edit_month.setText(MONTH_NAME.get(month));
        choose_month.setVisibility(View.GONE);
    }
    @OnClick(R.id.month_sep) void clickSep(){
        month = 9;
        edit_month.setText(MONTH_NAME.get(month));
        choose_month.setVisibility(View.GONE);
    }
    @OnClick(R.id.month_oct) void clickOct(){
        month = 10;
        edit_month.setText(MONTH_NAME.get(month));
        choose_month.setVisibility(View.GONE);
    }
    @OnClick(R.id.month_nov) void clickNov(){
        month = 11;
        edit_month.setText(MONTH_NAME.get(month));
        choose_month.setVisibility(View.GONE);
    }
    @OnClick(R.id.month_dec) void clickDec(){
        month = 12;
        edit_month.setText(MONTH_NAME.get(month));
        choose_month.setVisibility(View.GONE);
    }

}
