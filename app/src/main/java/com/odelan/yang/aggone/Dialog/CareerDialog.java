package com.odelan.yang.aggone.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.R;

import java.util.Date;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CareerDialog extends Dialog {
    Listener mListener;
    Context context;
    boolean isUploadedLogo;

    @BindView(R.id.edit_club) EditText edit_club;
    @BindView(R.id.edit_sport) EditText edit_sport;
    @BindView(R.id.edit_day) EditText edit_day;
    @BindView(R.id.edit_month) EditText edit_month;
    @BindView(R.id.edit_year) EditText edit_year;
    @BindView(R.id.edit_tday) EditText edit_tday;
    @BindView(R.id.edit_tmonth) EditText edit_tmonth;
    @BindView(R.id.edit_tyear) EditText edit_tyear;
    @BindView(R.id.edit_position) EditText edit_position;
    @BindView(R.id.edit_location) EditText edit_location;

    public CareerDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        isUploadedLogo = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_career);
        ButterKnife.bind(this);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }
    public void setUploadedLogo(boolean value) { isUploadedLogo = value; }

    @OnClick(R.id.btn_add_logo) void onClickLogo() {
        if (mListener != null) mListener.onClickLogo();
    }
    @OnClick(R.id.btn_close) void onClickClose() {
        dismiss();
    }
    @OnClick(R.id.btn_submit) void onClickSubmit() {
        if (edit_club.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input club", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edit_sport.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input sport", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edit_day.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input day", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edit_month.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input month", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edit_year.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input year", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edit_tday.getText().toString().isEmpty()) {
            edit_tmonth.setText("");
            edit_tyear.setText("");
        }
        if (edit_tmonth.getText().toString().isEmpty()) {
            edit_tday.setText("");
            edit_tyear.setText("");
        }
        if (edit_tyear.getText().toString().isEmpty()) {
            edit_tday.setText("");
            edit_tmonth.setText("");
        }
        if (edit_position.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input position", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edit_location.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please input location", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (isUploadedLogo == false) {
//            Toast.makeText(context, "Please add image", Toast.LENGTH_SHORT).show();
//            return;
//        }

        String year_str = edit_year.getText().toString();
        String month_str = edit_month.getText().toString();
        String day_str = edit_day.getText().toString();
        int year = Integer.parseInt(year_str);
        int month = Integer.parseInt(month_str);
        int day = Integer.parseInt(day_str);

        if (BaseActivity.isValidDate(year, month, day) == false) {
            Toast.makeText(context, "Invalid Date", Toast.LENGTH_SHORT).show();
            return;
        }

        Date now = new Date();
        Date birthday = new Date(year - 1900, month - 1, day);
        long age = (now.getTime() - birthday.getTime());
        if (age <= 0) {
            Toast.makeText(context, "Invalid Date", Toast.LENGTH_SHORT).show();
            return;
        }

        year_str = edit_tyear.getText().toString();
        month_str = edit_tmonth.getText().toString();
        day_str = edit_tday.getText().toString();
        int tyear = 0;
        int tmonth = 0;
        int tday = 0;
        if (year_str.isEmpty() == false) {
            tyear = Integer.parseInt(year_str);
            tmonth = Integer.parseInt(month_str);
            tday = Integer.parseInt(day_str);
            if (BaseActivity.isValidDate(tyear, tmonth, tday) == false) {
                Toast.makeText(context, "Invalid Date", Toast.LENGTH_SHORT).show();
                return;
            }
            now = new Date();
            birthday = new Date(tyear - 1900, tmonth - 1, tday);
            age = (now.getTime() - birthday.getTime());
            if (age <= 0) {
                Toast.makeText(context, "Invalid Date", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (mListener != null) {
            mListener.onClickSubmit(
                    edit_club.getText().toString().trim(),
                    edit_sport.getText().toString(),
                    day,
                    month,
                    year,
                    tday,
                    tmonth,
                    tyear,
                    edit_position.getText().toString().trim(),
                    edit_location.getText().toString().trim(),
                    isUploadedLogo);
        }
        edit_club.setText("");
        edit_sport.setText("");
        edit_day.setText("");
        edit_month.setText("");
        edit_year.setText("");
        edit_tday.setText("");
        edit_tmonth.setText("");
        edit_tyear.setText("");
        edit_position.setText("");
        edit_location.setText("");
        isUploadedLogo = false;
        dismiss();
    }

    public interface Listener {
        void onClickLogo();
        void onClickSubmit(String club,
                           String sport,
                           int day,
                           int month,
                           int year,
                           int tday,
                           int tmonth,
                           int tyear,
                           String position,
                           String location,
                           boolean has_image);
    }
}
