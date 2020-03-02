package com.odelan.yang.aggone.Activity.Profile;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Helpers.XAxisValueFormatter;
import com.odelan.yang.aggone.Helpers.YValueFormatter;
import com.odelan.yang.aggone.Model.Audience;
import com.odelan.yang.aggone.Model.ViewStatic;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.Constants;
import com.odelan.yang.aggone.Utils.ValueFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AudienceActivity extends BaseActivity {

    @BindView(R.id.txt_storyview_count)     TextView    txt_storyview_count;
    @BindView(R.id.chart1)                  BarChart    bar_chart;
    @BindView(R.id.btn_today)               TextView    btn_today;
    @BindView(R.id.btn_week)                TextView    btn_week;
    @BindView(R.id.btn_month)               TextView    btn_month;
    @BindView(R.id.txt_total_profile)       TextView    txt_total_profile;
    @BindView(R.id.txt_club)                TextView    txt_club;
    @BindView(R.id.txt_agent)               TextView    txt_agent;
    @BindView(R.id.txt_coach)               TextView    txt_coach;
    @BindView(R.id.txt_player)              TextView    txt_player;
    @BindView(R.id.txt_company)             TextView    txt_company;
    @BindView(R.id.txt_staff)               TextView    txt_staff;
    @BindView(R.id.txt_view_video)          TextView    txt_view_video;
    @BindView(R.id.txt_star_video)          TextView    txt_star_video;
    @BindView(R.id.txt_views)               TextView    txt_views;
    @BindView(R.id.layout_total)               LinearLayout    layout_total;
    @BindView(R.id.layout_club)                LinearLayout    layout_club;
    @BindView(R.id.layout_agent)               LinearLayout    layout_agent;
    @BindView(R.id.layout_coach)               LinearLayout    layout_coach;
    @BindView(R.id.layout_player)              LinearLayout    layout_player;
    @BindView(R.id.layout_company)             LinearLayout    layout_company;
    @BindView(R.id.layout_staff)               LinearLayout    layout_staff;

    int mode = Constants.AUDIENCE_TODAY;
    long def_timestamp = 0;
    Audience data = new Audience();
    ViewStatic vStatic = new ViewStatic();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audience);

        ButterKnife.bind(this);
        setActivity();
    }

    private void setActivity(){
        //get basetime (today 00:00:00)
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String cur_full_date_time = currentDate + " 00:00:00";
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try{
            Date date = (Date)formatter.parse(cur_full_date_time);
            def_timestamp = date.getTime()/1000; //seconds unit
        } catch (ParseException e){
            e.printStackTrace();
        }

        //setting barchart
        bar_chart.setDrawBarShadow(false);
        bar_chart.setDrawValueAboveBar(true);
        bar_chart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        bar_chart.setMaxVisibleValueCount(10);
        // scaling can now only be done on x- and y-axis separately
        bar_chart.setPinchZoom(false);
        bar_chart.setDrawGridBackground(false);

        ValueFormatter xAxisFormatter = new XAxisValueFormatter(bar_chart, this);
        XAxis xAxis = bar_chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        ValueFormatter custom = new YValueFormatter("");

        YAxis leftAxis = bar_chart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        bar_chart.getAxisRight().setEnabled(false);
        bar_chart.getLegend().setEnabled(false);

        showProgress();
        API.getStoryViewStatics(this, def_timestamp, new APICallback<ViewStatic>() {
            @Override
            public void onSuccess(ViewStatic response) {
                dismissProgress();
                vStatic = response;
                refreshActivity();
            }

            @Override
            public void onFailure(String error) {
                dismissProgress();
            }
        });

        API.getAudienceStatics(this, new APICallback<Audience>() {
            @Override
            public void onSuccess(Audience response) {
                data = response;
                refreshActivity();
            }

            @Override
            public void onFailure(String error) {

            }
        });

        refreshActivity();
    }

    void setData(){
        ArrayList<BarEntry> values = new ArrayList<>();
//        vStatic.total = 100;
        Calendar calendar = Calendar.getInstance();

        switch (mode){
            case Constants.AUDIENCE_WEEK:
                int mm = calendar.get(Calendar.WEEK_OF_MONTH);
                for (int i = 0; i < 4; i++) {
                    float val = (float) (Math.random() * vStatic.total / 2);
                    if (i == 2) {
                        values.add(new BarEntry(mm + i + 1, vStatic.nweek, getResources().getDrawable(R.mipmap.main_like_active)));
                    } else {
                        values.add(new BarEntry(mm + i + 1, val));
                    }
                }
                if(vStatic.nweek >= 0)
                    txt_views.setText(prettyCount(vStatic.nweek));
                break;
            case Constants.AUDIENCE_MONTH:
                int mmm = calendar.get(Calendar.MONTH);
                for (int i = 0; i < 7; i++) {
                    float val = (float) (Math.random() * vStatic.total / 2);
                    if (i == 3) {
                        values.add(new BarEntry(mmm + i + 10009, vStatic.nmonth, getResources().getDrawable(R.mipmap.main_like_active)));
                    } else {
                        values.add(new BarEntry(mmm + i + 10009, val));
                    }
                }
                if(vStatic.nmonth >= 0)
                    txt_views.setText(prettyCount(vStatic.nmonth));
                break;
            default:
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                for (int i = 0; i < 7; i++) {
                    float val = (float) (Math.random() * vStatic.total / 2);
                    if (i == 3) {
                        values.add(new BarEntry(i + day + 1003 , vStatic.ntoday, getResources().getDrawable(R.mipmap.main_like_active)));
                    } else {
                        values.add(new BarEntry(i + day + 1003, val));
                    }
                }
                if(vStatic.ntoday >= 0)
                    txt_views.setText(prettyCount(vStatic.ntoday));
                break;
        }
        BarDataSet set1;

//        if (bar_chart.getData() != null &&
//                bar_chart.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) bar_chart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            bar_chart.getData().notifyDataChanged();
//            bar_chart.notifyDataSetChanged();
//
//        } else
            {
            set1 = new BarDataSet(values, "");

            set1.setDrawIcons(false);

            int startColor = ContextCompat.getColor(this, R.color.active_color);
            int startColor_sel = ContextCompat.getColor(this, R.color.active_color);
            int endColor = ContextCompat.getColor(this, android.R.color.white);
            int endColor_sel = ContextCompat.getColor(this, R.color.active_color);

            List<GradientColor> gradientFills = new ArrayList<>();
            if (mode == Constants.AUDIENCE_WEEK){
                gradientFills.add(new GradientColor(startColor, endColor));
                gradientFills.add(new GradientColor(startColor, endColor));
                gradientFills.add(new GradientColor(startColor_sel, endColor_sel));
                gradientFills.add(new GradientColor(startColor, endColor));
            } else{
                gradientFills.add(new GradientColor(startColor, endColor));
                gradientFills.add(new GradientColor(startColor, endColor));
                gradientFills.add(new GradientColor(startColor, endColor));
                gradientFills.add(new GradientColor(startColor_sel, endColor_sel));
                gradientFills.add(new GradientColor(startColor, endColor));
                gradientFills.add(new GradientColor(startColor, endColor));
                gradientFills.add(new GradientColor(startColor, endColor));
            }

            set1.setGradientColors(gradientFills);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTextColor(ContextCompat.getColor(this, android.R.color.white));
            data.setBarWidth(0.9f);

            bar_chart.setData(data);
        }
        bar_chart.invalidate();
    }

    void refreshActivity(){
        switch (mode){
            case Constants.AUDIENCE_WEEK:
                btn_today.setBackgroundResource(R.drawable.gender_choose_inactive_male);
                btn_today.setTextColor(Color.BLACK);
                btn_week.setBackgroundResource(R.drawable.gender_choose_active_female);
                btn_week.setTextColor(Color.WHITE);
                btn_month.setBackgroundResource(R.drawable.gender_choose_inactive_other);
                btn_month.setTextColor(Color.BLACK);
                break;
            case Constants.AUDIENCE_MONTH:
                btn_today.setBackgroundResource(R.drawable.gender_choose_inactive_male);
                btn_today.setTextColor(Color.BLACK);
                btn_week.setBackgroundResource(R.drawable.gender_choose_inactive_female);
                btn_week.setTextColor(Color.BLACK);
                btn_month.setBackgroundResource(R.drawable.gender_choose_active_other);
                btn_month.setTextColor(Color.WHITE);
                break;
            default:
                btn_today.setBackgroundResource(R.drawable.gender_choose_active_male);
                btn_today.setTextColor(Color.WHITE);
                btn_week.setBackgroundResource(R.drawable.gender_choose_inactive_female);
                btn_week.setTextColor(Color.BLACK);
                btn_month.setBackgroundResource(R.drawable.gender_choose_inactive_other);
                btn_month.setTextColor(Color.BLACK);
                break;
        }

        txt_total_profile.setText(prettyCount(data.total_profile));
        layout_club.setVisibility(View.VISIBLE);
        txt_club.setText(prettyCount(data.club));

        layout_agent.setVisibility(View.VISIBLE);
        txt_agent.setText(prettyCount(data.agent));

        layout_coach.setVisibility(View.VISIBLE);
        txt_coach.setText(prettyCount(data.coach));

        layout_player.setVisibility(View.VISIBLE);
        txt_player.setText(prettyCount(data.player));

        layout_company.setVisibility(View.VISIBLE);
        txt_company.setText(prettyCount(data.company));

        layout_staff.setVisibility(View.VISIBLE);
        txt_staff.setText(prettyCount(data.staff));

        txt_view_video.setText(prettyCount(data.view_video));
        txt_star_video.setText(prettyCount(data.star_video));

        txt_storyview_count.setText(typicalCount(vStatic.total));
        setData();
    }

    @OnClick(R.id.btn_today) void onClickToday(){
        mode = Constants.AUDIENCE_TODAY;
        refreshActivity();
    }

    @OnClick(R.id.btn_week) void onClickWeek(){
        mode = Constants.AUDIENCE_WEEK;
        refreshActivity();
    }

    @OnClick(R.id.btn_month) void onClickMonth(){
        mode = Constants.AUDIENCE_MONTH;
        refreshActivity();
    }

    @OnClick(R.id.btn_back) void onClickBack(){
        finish();
    }

    @Override
    public void onBackPressed() {
    }
}
