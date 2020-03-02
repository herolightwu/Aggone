package com.odelan.yang.aggone.Activity.Auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.odelan.yang.aggone.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermsActivity extends AppCompatActivity {

    @BindView(R.id.txt_content) TextView txt_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        ButterKnife.bind(this);
        txt_content.setText(Html.fromHtml(getResources().getString(R.string.terms_content)));
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
}
