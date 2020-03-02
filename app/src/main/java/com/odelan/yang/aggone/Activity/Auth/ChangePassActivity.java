package com.odelan.yang.aggone.Activity.Auth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.Constants;
import com.odelan.yang.aggone.Utils.TinyDB;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePassActivity extends BaseActivity {

    @BindView(R.id.edit_curpass)    EditText cur_pass;
    @BindView(R.id.edit_newpass)    EditText new_pass;
    @BindView(R.id.edit_retypepass)    EditText confirm_pass;
    @BindView(R.id.btn_confirm)     Button btn_confirm;

    String curpass = "";
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        ButterKnife.bind(this);

        tinyDB = new TinyDB(this);
        if (tinyDB.getBoolean(Constants.LOGIN_ISLOGGEDIN)) {
            curpass = tinyDB.getString(Constants.LOGIN_PASSWORD);
        }
    }

    @OnClick(R.id.btn_confirm) void onClickConfirm(){
        final String password = cur_pass.getText().toString();
        final String newpass = new_pass.getText().toString();
        String confirm_password = confirm_pass.getText().toString();
        if (password.isEmpty()) {
            showToast("Please input password");
            return;
        }
        if (newpass.isEmpty()) {
            showToast("Please input new password");
            return;
        }
        if (!newpass.equals(confirm_password)) {
            showToast("Invalid confirm password");
            return;
        }

        if (!curpass.equals(password)){
            showToast("Current password is wrong");
            return;
        }

        showProgress();
        API.changePassword(this, newpass, new APICallback<Boolean>() {
            @Override
            public void onSuccess(Boolean response) {
                dismissProgress();
                tinyDB.putString(Constants.LOGIN_PASSWORD, newpass);
                finish();
            }

            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });
    }

    @OnClick(R.id.btn_back) void onClickBack(){
        finish();
    }
}
