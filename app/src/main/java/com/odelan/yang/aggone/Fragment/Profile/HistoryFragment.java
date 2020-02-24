package com.odelan.yang.aggone.Fragment.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.AppData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryFragment extends Fragment {

    private Context     context;
    private User        user;
    private View        rView;
    private Listener    mListener;

    @BindView(R.id.txt_city)    TextView txt_city;
    @BindView(R.id.txt_country)    TextView txt_country;
    @BindView(R.id.txt_email)    TextView txt_email;
    @BindView(R.id.txt_phone)    TextView txt_phone;
    @BindView(R.id.txt_website)    TextView txt_website;
    @BindView(R.id.txt_description) TextView txt_description;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public HistoryFragment(Context context, User user, Listener listener) {
        this.context = context;
        this.mListener = listener;
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rView = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, rView);

        txt_city.setText(user.city);
        txt_country.setText(user.country);
        txt_email.setText(user.email);
        txt_phone.setText(user.phone);
        txt_website.setText(user.web_url);
        if (user.desc_str != null && user.desc_str.length() > 0){
            txt_description.setText(user.desc_str);
        } else{
            txt_description.setText(R.string.history_desc);
        }
        return rView;
    }

    @OnClick(R.id.txt_description) void onEditDescription(){
        if (mListener!=null && user.id.equals(AppData.user.id))
            mListener.onClickText(txt_description.getText().toString());
    }

    @OnClick(R.id.img_edit_email) void onEditEmail(){
        if (mListener!=null && user.id.equals(AppData.user.id))
            mListener.onClickEmail(txt_email.getText().toString());
    }

    @OnClick(R.id.img_edit_phone) void onEditPhone(){
        if (mListener!=null && user.id.equals(AppData.user.id))
            mListener.onClickPhone(txt_phone.getText().toString());
    }

    @OnClick(R.id.img_edit_website) void onEditWebsite(){
        if(mListener != null && user.id.equals(AppData.user.id))
            mListener.onClickWeb(txt_website.getText().toString());
    }

    public void setPhoneNumber(String str){
        txt_phone.setText(str);
        user.phone = str;
    }
    public void setUrl(String str){
        txt_website.setText(str);
        user.web_url = str;
    }

    public void setDescription(String str){
        txt_description.setText(str);
        user.desc_str = str;
    }

    public void setEmailAddress(String str){
        txt_email.setText(str);
        user.email = str;
    }

    public interface Listener {
        void onClickPhone(String value);
        void onClickWeb(String value);
        void onClickText(String value);
        void onClickEmail(String value);
    }

}
