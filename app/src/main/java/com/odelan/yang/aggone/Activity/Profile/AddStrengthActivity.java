package com.odelan.yang.aggone.Activity.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Activity.MainActivity;
import com.odelan.yang.aggone.Activity.Message.ContactActivity;
import com.odelan.yang.aggone.Activity.Stats.StatsActivity;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;
import com.odelan.yang.aggone.Utils.TinyDB;

import net.alhazmy13.mediapicker.Image.ImagePicker;
import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddStrengthActivity extends BaseActivity {

    final static int EDIT_STRENGTH = 731;
    private User user;

//    @BindView(R.id.btn_lookup) ImageView btn_lookup;
//    @BindView(R.id.txt_lookup) TextView txt_lookup;

    @BindView(R.id.img_avatar)    CircleImageView img_photo;
    @BindView(R.id.img_first)    CircleImageView img_first;
    @BindView(R.id.img_second)    CircleImageView img_second;
    @BindView(R.id.img_third)    CircleImageView img_third;
    @BindView(R.id.btn_edit)         ImageView btn_edit;
    @BindView(R.id.txt_strength1)    TextView txt_strength1;
    @BindView(R.id.txt_strength2)    TextView txt_strength2;
    @BindView(R.id.txt_strength3)    TextView txt_strength3;

    List<String> strengths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strength);

        ButterKnife.bind(this);

        user = getIntent().getExtras().getParcelable(Constants.USER);
        setActivity();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    void getAllStrengths(){
        showProgress();
        API.getStrengths(this, user.id, new APICallback<List<String>>() {
            @Override
            public void onSuccess(List<String> list) {
                dismissProgress();
                txt_strength1.setText("");
                txt_strength2.setText("");
                txt_strength3.setText("");
                img_first.setVisibility(View.GONE);
                img_second.setVisibility(View.GONE);
                img_third.setVisibility(View.GONE);

                if (list.size() > 0){
                    int strength1 = Integer.valueOf(list.get(0));
                    int ssid = strength1/100;
                    int stid = strength1%100;
                    strengths = configureStrength(ssid);
                    if(stid >= 0 && stid < strengths.size()){
                        txt_strength1.setText(strengths.get(stid));
                        img_first.setVisibility(View.VISIBLE);
                    }
                    if (list.size() > 1){
                        int strength2 = Integer.valueOf(list.get(1));
                        ssid = strength2/100;
                        stid = strength2%100;
                        strengths = configureStrength(ssid);
                        if(stid >= 0 && stid < strengths.size()){
                            txt_strength2.setText(strengths.get(stid));
                            img_second.setVisibility(View.VISIBLE);
                        }
                        if (list.size() > 2){
                            int strength3 = Integer.valueOf(list.get(2));
                            ssid = strength3/100;
                            stid = strength3%100;
                            strengths = configureStrength(ssid);
                            if(stid >= 0 && stid < strengths.size()){
                                txt_strength3.setText(strengths.get(stid));
                                img_third.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                dismissProgress();
                showToast(error);
            }
        });
    }

    private void setActivity(){
        /** Tab bar setting */
//        btn_lookup.setImageResource(R.mipmap.tab_lookup_active);
//        txt_lookup.setTextColor(getResources().getColor(R.color.tab_active));

        if (user.photo_url != null && !user.photo_url.isEmpty()) {
            if (user.photo_url.contains("http")){
                Glide.with(this).load(user.photo_url).into(img_photo);
            } else{
                Glide.with(this).load(API.baseUrl + API.imgDirUrl + user.photo_url).into(img_photo);
            }
        } else {
            img_photo.setImageResource(R.mipmap.default_avata);
        }
        if(user.id.equals(AppData.user.id)){
            btn_edit.setVisibility(View.VISIBLE);
        } else{
            btn_edit.setVisibility(View.GONE);
        }
        txt_strength1.setText("");
        txt_strength2.setText("");
        txt_strength3.setText("");
        img_first.setVisibility(View.GONE);
        img_second.setVisibility(View.GONE);
        img_third.setVisibility(View.GONE);

        getAllStrengths();
    }

    @OnClick(R.id.btn_back) void onClickBack(){
        finish();
    }

    @OnClick(R.id.btn_edit) void onClickEdit(){
        startActivityForResult(new Intent(this, EditStrengthActivity.class), EDIT_STRENGTH);
    }

    /**
     * Tab bar Event
     * */
//    @OnClick(R.id.btn_home) void onClickHome() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }
//    @OnClick(R.id.btn_stats) void onClickStats() {
//        Intent intent = new Intent(this, StatsActivity.class);
//        intent.putExtra(Constants.USER, (Parcelable) AppData.user);
//        startActivity(intent);
//    }
//    @OnClick(R.id.btn_chats) void onClickChats() {
//        Intent intent = new Intent(this, ContactActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_STRENGTH && resultCode == RESULT_OK) {
            user = AppData.user;
            setActivity();
        }
    }
}
