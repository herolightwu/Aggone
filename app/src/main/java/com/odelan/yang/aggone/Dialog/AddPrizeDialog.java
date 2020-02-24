package com.odelan.yang.aggone.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.Constants;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPrizeDialog extends Dialog {
    Listener mListener;
    Context context;

    @BindView(R.id.edit_title) EditText edit_title;
    @BindView(R.id.edit_club) EditText edit_club;
    @BindView(R.id.edit_year) EditText edit_year;
    private int icon;

    @BindView(R.id.layout_prize1) RelativeLayout layout_prize1;
    @BindView(R.id.layout_prize2) RelativeLayout layout_prize2;
    @BindView(R.id.layout_prize3) RelativeLayout layout_prize3;
    @BindView(R.id.layout_prize4) RelativeLayout layout_prize4;
    @BindView(R.id.layout_prize5) RelativeLayout layout_prize5;

    public AddPrizeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        icon = -1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_prize);
        ButterKnife.bind(this);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @OnClick(R.id.btn_close) void onClickClose() {
        dismiss();
    }
    @OnClick(R.id.btn_prize1) void onClickPrize1() {
        icon = Constants.PRIZE1;
        clearIcons();
        layout_prize1.setAlpha(1.0f);
    }
    @OnClick(R.id.btn_prize2) void onClickPrize2() {
        icon = Constants.PRIZE2;
        clearIcons();
        layout_prize2.setAlpha(1.0f);
    }
    @OnClick(R.id.btn_prize3) void onClickPrize3() {
        icon = Constants.PRIZE3;
        clearIcons();
        layout_prize3.setAlpha(1.0f);
    }
    @OnClick(R.id.btn_prize4) void onClickPrize4() {
        icon = Constants.PRIZE4;
        clearIcons();
        layout_prize4.setAlpha(1.0f);
    }
    @OnClick(R.id.btn_prize5) void onClickPrize5() {
        icon = Constants.PRIZE5;
        clearIcons();
        layout_prize5.setAlpha(1.0f);
    }
    @OnClick(R.id.btn_submit) void onClickSubmit() {
        if (edit_title.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edit_club.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input club", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edit_year.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input year", Toast.LENGTH_SHORT).show();
            return;
        }
        if (icon == -1) {
            Toast.makeText(context, "Please select icon", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mListener != null) mListener.onClickSubmit(edit_title.getText().toString(), edit_club.getText().toString(), edit_year.getText().toString(), icon);
        edit_title.setText("");
        edit_club.setText("");
        edit_year.setText("");
        icon = -1;
        dismiss();
    }

    void clearIcons() {
        layout_prize1.setAlpha(0.5f);
        layout_prize2.setAlpha(0.5f);
        layout_prize3.setAlpha(0.5f);
        layout_prize4.setAlpha(0.5f);
        layout_prize5.setAlpha(0.5f);
    }

    public interface Listener {
        void onClickSubmit(String title, String club, String year, int icon);
    }
}
