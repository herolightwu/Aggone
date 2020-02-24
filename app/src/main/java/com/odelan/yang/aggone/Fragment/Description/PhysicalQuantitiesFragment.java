package com.odelan.yang.aggone.Fragment.Description;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Adapter.SkillAdapter;
import com.odelan.yang.aggone.Model.Description;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhysicalQuantitiesFragment extends Fragment {
    private Context context;
    private Listener mListener;

    User user;

    @BindView(R.id.radar) RadarChart radar;

    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    SkillAdapter adapter;
    List<Description> skills = new ArrayList<>();

    SkillAdapter.EventListener skillListener = new SkillAdapter.EventListener() {
        @Override
        public void onClickMinus(final int index) {
            Description description = skills.get(index);
            if (description.value == 0) return;
            description.value--;

            API.updateDescription(context, description, new APICallback<Description>() {
                @Override
                public void onSuccess(Description response) {
                    skills.set(index, response);
                    adapter.notifyDataSetChanged();
                    setData();
                }
                @Override
                public void onFailure(String error) {
                }
            });
        }
        @Override
        public void onClickPlus(final int index) {
            Description description = skills.get(index);
            if (description.value == Constants.MAX_DESCRIPTION) return;
            description.value++;

            API.updateDescription(context, description, new APICallback<Description>() {
                @Override
                public void onSuccess(Description response) {
                    skills.set(index, response);
                    adapter.notifyDataSetChanged();
                    setData();
                }
                @Override
                public void onFailure(String error) {
                }
            });
        }
    };

    public PhysicalQuantitiesFragment() {
    }

    @SuppressLint("ValidFragment")
    public PhysicalQuantitiesFragment(Context context, User user, Listener listener) {
        this.context = context;
        this.user = user;
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_physical_quantities, container, false);
        ButterKnife.bind(this, view);

        setFragment();
        return view;
    }

    void loadDescription() {
        API.getAllDescriptionsByUserid(context, user.id, new APICallback<List<Description>>() {
            @Override
            public void onSuccess(List<Description> response) {
                int[] ids;
                if (user.group_id == Constants.PLAYER) {
                    ids = new int[] {
                            Constants.DESCRIPTION_13,
                            Constants.DESCRIPTION_14,
                            Constants.DESCRIPTION_15,
                            Constants.DESCRIPTION_16,
                            Constants.DESCRIPTION_17,
                            Constants.DESCRIPTION_18  };
                } else {
                    ids = new int[] {
                            Constants.CDESCRIPTION_13,
                            Constants.CDESCRIPTION_14,
                            Constants.CDESCRIPTION_15,
                            Constants.CDESCRIPTION_16,
                            Constants.CDESCRIPTION_17,
                            Constants.CDESCRIPTION_18  };
                }
                skills.clear();
                skills.addAll(BaseActivity.getDescriptions(user.id, ids, response));
                adapter.notifyDataSetChanged();
                setRadarChart();
            }
            @Override
            public void onFailure(String error) {
            }
        });
    }

    void setFragment() {
        adapter = new SkillAdapter(context, skills, skillListener);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(adapter);

        loadDescription();
    }

    void setRadarChart() {
        final String[] labels;
        if (user.group_id == Constants.PLAYER) {
            labels = new String[] {
                    context.getResources().getString(R.string.description_13),
                    context.getResources().getString(R.string.description_14),
                    context.getResources().getString(R.string.description_15),
                    context.getResources().getString(R.string.description_16),
                    context.getResources().getString(R.string.description_17),
                    context.getResources().getString(R.string.description_18)};
        } else {
            labels = new String[] {
                    context.getResources().getString(R.string.cdescription_13),
                    context.getResources().getString(R.string.cdescription_14),
                    context.getResources().getString(R.string.cdescription_15),
                    context.getResources().getString(R.string.cdescription_16),
                    context.getResources().getString(R.string.cdescription_17),
                    context.getResources().getString(R.string.cdescription_18)};
        }
        radar.getDescription().setEnabled(false);
        radar.getLegend().setEnabled(false);
        radar.setRotationEnabled(false);
        radar.setWebLineWidth(1f);
        radar.setWebColor(Color.parseColor("#707070"));
        radar.setWebLineWidthInner(1f);
        radar.setWebColorInner(Color.parseColor("#707070"));
        radar.setWebAlpha(100);

        XAxis xAxis = radar.getXAxis();
        xAxis.setTextSize(7f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels[(int) value % labels.length];
            }
        });
        xAxis.setTextColor(Color.parseColor("#707070"));

        YAxis yAxis = radar.getYAxis();
        yAxis.setLabelCount(5, true);
        yAxis.setAxisMaximum(Constants.MAX_DESCRIPTION + 2);
        yAxis.setDrawLabels(false);

        setData();
    }

    private void setData() {
        ArrayList<RadarEntry> entries = new ArrayList<>();
        for (int i = 0; i < skills.size(); i++) {
            entries.add(new RadarEntry(skills.get(i).value + 1));
        }
        RadarDataSet set = new RadarDataSet(entries, "My");
        set.setColor(Color.parseColor("#B4D969"));
        set.setFillColor(Color.TRANSPARENT);
        set.setLineWidth(2f);
        set.setDrawHighlightCircleEnabled(false);
        set.setHighlightCircleFillColor(Color.parseColor("#048797"));
        set.setHighlightCircleInnerRadius(3f);
        set.setHighlightCircleStrokeColor(Color.parseColor("#048797"));
        set.setHighlightCircleOuterRadius(3f);
        set.setDrawHighlightIndicators(false);

        RadarData data = new RadarData(set);
        data.setDrawValues(false);
        data.setHighlightEnabled(true);

        radar.setData(data);
        radar.invalidate();
    }

    public interface Listener {
    }
}
