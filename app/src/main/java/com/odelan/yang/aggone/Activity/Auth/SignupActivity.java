package com.odelan.yang.aggone.Activity.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends BaseActivity {

    @BindView(R.id.edit_email)              EditText edit_email;
    @BindView(R.id.edit_password)           EditText edit_password;
    @BindView(R.id.edit_confirm_password)   EditText edit_confirm_password;
    @BindView(R.id.chk_terms)               CheckBox chk_terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_go) void onClickGo() {
        final String email = edit_email.getText().toString();
        final String password = edit_password.getText().toString();
        String confirm_password = edit_confirm_password.getText().toString();
        if (email.isEmpty()) {
            showToast("Please input email");
            return;
        }
        if (isValidEmail(email) == false) {
            showToast("Invalid email");
            return;
        }
        if (password.isEmpty()) {
            showToast("Please input password");
            return;
        }
        if (confirm_password.isEmpty()) {
            showToast("Please input confirm password");
            return;
        }
        if (!password.equals(confirm_password)) {
            showToast("Invalid confirm password");
            return;
        }
        if (chk_terms.isChecked() == false) {
            showToast("Please read terms and conditions");
            return;
        }

        API.getUserByEmail(email, new APICallback<User>() {
            @Override
            public void onSuccess(User response) {
                showToast("This email address is already used.");
            }
            @Override
            public void onFailure(String error) {
                if (error.equals("Don't exist")) {
                    Intent intent = new Intent(SignupActivity.this, ProfileSetupActivity.class);
                    intent.putExtra(Constants.EMAIL, email);
                    intent.putExtra(Constants.PASSWORD, password);
                    intent.putExtra(Constants.SIGNUP_MODE, Constants.SIGNUP_EMAIL);
                    startActivity(intent);
                    finish();
                } else {
                    showToast(error);
                }
            }
        });
    }

    @OnClick(R.id.btn_login) void onClickLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.chk_terms) void onClickTemrs() {
        Intent intent = new Intent(this, TermsActivity.class);
        startActivity(intent);
    }
}
