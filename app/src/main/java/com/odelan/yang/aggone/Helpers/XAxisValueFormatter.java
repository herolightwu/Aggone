package com.odelan.yang.aggone.Helpers;

import android.content.Context;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.ValueFormatter;

import java.util.Arrays;

public class XAxisValueFormatter extends ValueFormatter
{

    private Context context;
    private String[] mMonths = new String[12];
    private String[] mWeekDays = new String[7];//{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}

    private String[] mWeeks = new String[4];//{"1st Week", "2nd Week", "3rd Week", "4th Week"};

    private final BarLineChartBase<?> chart;

    public XAxisValueFormatter(BarLineChartBase<?> chart, Context cxt) {
        this.chart = chart;
        this.context = cxt;
        Arrays.asList(context.getResources().getStringArray(R.array.week_day_array)).toArray(mWeekDays);
        Arrays.asList(context.getResources().getStringArray(R.array.weeks_array)).toArray(mWeeks);
        Arrays.asList(context.getResources().getStringArray(R.array.months_array)).toArray(mMonths);
    }

    @Override
    public String getFormattedValue(float value) {
        int days = (int) value;
        if(days > 5000) {
            String sVal = "";
            return mMonths[(days - 10000) % 12];
        } else if(days > 500) {
            return mWeekDays[(days - 1000) % 7];
        } else {
            return mWeeks[days%4];
        }
    }
}
