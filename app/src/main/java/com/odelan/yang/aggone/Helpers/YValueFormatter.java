package com.odelan.yang.aggone.Helpers;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.odelan.yang.aggone.Utils.ValueFormatter;

import java.text.DecimalFormat;

public class YValueFormatter extends ValueFormatter
{

    private final DecimalFormat mFormat;
    private String suffix;

    public YValueFormatter(String suffix) {
        mFormat = new DecimalFormat("###,###,###,###");
        this.suffix = suffix;
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value) + suffix;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        if (axis instanceof XAxis) {
            return mFormat.format(value);
        } else if (value > 0) {
            return mFormat.format(value) + suffix;
        } else {
            return mFormat.format(value);
        }
    }
}
