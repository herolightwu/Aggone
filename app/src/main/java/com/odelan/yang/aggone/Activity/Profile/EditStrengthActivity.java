package com.odelan.yang.aggone.Activity.Profile;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Activity.MainActivity;
import com.odelan.yang.aggone.Activity.Message.ContactActivity;
import com.odelan.yang.aggone.Activity.Stats.ClubChartActivity;
import com.odelan.yang.aggone.Activity.Stats.StatsActivity;
import com.odelan.yang.aggone.Adapter.Profile.EditStrengthAdapter;
import com.odelan.yang.aggone.Model.Sport;
import com.odelan.yang.aggone.MyApp;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditStrengthActivity extends BaseActivity {

    @BindView(R.id.btn_lookup)    ImageView btn_lookup;
    @BindView(R.id.txt_lookup)    TextView txt_lookup;

    @BindView(R.id.edit_search)    EditText edit_search;
    @BindView(R.id.txt_sport)       TextView txt_sport;
    @BindView(R.id.recycler_view)    RecyclerView recycler_view;

    List<String> strengths;
    List<Integer> sel_indexes = new ArrayList<>();
    EditStrengthAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_strength);

        ButterKnife.bind(this);
        setActivity();
    }

    void getAllStrengths(){
        API.getStrengths(this, AppData.user.id, new APICallback<List<String>>() {
            @Override
            public void onSuccess(List<String> response) {
                sel_indexes.clear();
                for (int i = 0; i < response.size(); i++){
                    int iStrength = Integer.valueOf(response.get(i));
                    int ind = iStrength - AppData.user.sport_id * 100;
                    if (ind >= 0 && ind < strengths.size()){
                        sel_indexes.add(ind);
                    }
                }
                adapter.sel_indexes = sel_indexes;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(EditStrengthActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setActivity(){
        /** Tab bar setting */
        btn_lookup.setImageResource(R.mipmap.tab_lookup_active);
        txt_lookup.setTextColor(getResources().getColor(R.color.tab_active));

        strengths = configureStrength(AppData.user.sport_id);
        adapter = new EditStrengthAdapter(this, strengths, sel_indexes, null);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setAdapter(adapter);

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        MyApp.hideKeyboard(this);
        String sport_name = getSportName(AppData.user.sport_id);
        txt_sport.setText(sport_name);
        getAllStrengths();
    }

    @OnClick(R.id.btn_back) void onClickBack(){
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @OnClick(R.id.btn_search_clear) void onSearchClear(){
        edit_search.setText("");
    }

    @OnClick(R.id.btn_cancel) void onSearchCancel(){
        edit_search.setText("");
    }

    @OnClick(R.id.btn_edit) void onClickEdit(){
        //
        String mystrength = "";
        for (int i = 0; i < adapter.sel_indexes.size(); i++){
            int istrength = AppData.user.sport_id * 100 + adapter.sel_indexes.get(i);
            if(i == 0){
                mystrength = "" + istrength;
            } else{
                mystrength = mystrength + "," + istrength;
            }
        }

        if (mystrength.length() > 0)
            API.saveStrengths(this, mystrength, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(EditStrengthActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });

    }

    /**
     * Tab bar Event
     * */
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
}
