package com.odelan.yang.aggone.Fragment.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.jwang123.flagkit.FlagKit;
import com.odelan.yang.aggone.Activity.Auth.ProfileSetupActivity;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Model.Description;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.MyApp;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DescriptionFragment extends Fragment {
    private Context context;
    private Listener mListener;
    private User user;

    @BindView(R.id.radar_skills) RadarChart radar_skills;
    @BindView(R.id.radar_individual_technique) RadarChart radar_individual_technique;
    @BindView(R.id.radar_physical_quantities) RadarChart radar_physical_quantities;
    @BindView(R.id.radar_tactics) RadarChart radar_tactics;

    @BindView(R.id.txt_skill1) TextView txt_skill1;
    @BindView(R.id.txt_skill2) TextView txt_skill2;
    @BindView(R.id.txt_skill3) TextView txt_skill3;
    @BindView(R.id.txt_skill4) TextView txt_skill4;

    @BindView(R.id.txt_height) TextView txt_height;
    @BindView(R.id.txt_weight) TextView txt_weight;
    @BindView(R.id.txt_age) TextView txt_age;
    @BindView(R.id.txt_position) TextView txt_position;
    @BindView(R.id.img_flag)    ImageView img_flag;

    @BindView(R.id.btn_access) Button btn_access;

    List<Description> listSkills = new ArrayList<>();
    List<Description> listIndividualTechnique = new ArrayList<>();
    List<Description> listPhysicalQuantities = new ArrayList<>();
    List<Description> listTactics = new ArrayList<>();

    public DescriptionFragment() {
    }

    @SuppressLint("ValidFragment")
    public DescriptionFragment(Context context, User user, Listener listener) {
        this.context = context;
        this.mListener = listener;
        this.user = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        ButterKnife.bind(this, view);

        setFragment();
        return view;
    }

    void setFragment() {
        if (user.id.equals(AppData.user.id)) {
            btn_access.setVisibility(View.GONE);
        } else {
            btn_access.setVisibility(View.VISIBLE);
        }
        txt_age.setText(String.valueOf(user.age));
        txt_weight.setText(String.valueOf(user.weight));
        txt_height.setText(String.valueOf(user.height));
        txt_position.setText(user.contract);
        if (user.country != null){
            img_flag.setImageDrawable(null);
            String c_code = MyApp.countries.get(user.country.trim());
            if (user.country.trim().equals("USA"))
                c_code = "US";
            if (c_code != null){
                Drawable flag = FlagKit.drawableWithFlag(context, c_code.toLowerCase());
                img_flag.setImageDrawable(flag);
            }
        }
        if (user.group_id == Constants.PLAYER) {
            txt_skill1.setText(getString(R.string.skill1));
            txt_skill2.setText(getString(R.string.skill2));
            txt_skill3.setText(getString(R.string.skill3));
            txt_skill4.setText(getString(R.string.skill4));
        } else {
            txt_skill1.setText(getString(R.string.cskill1));
            txt_skill2.setText(getString(R.string.cskill2));
            txt_skill3.setText(getString(R.string.cskill3));
            txt_skill4.setText(getString(R.string.cskill4));
        }
        loadDescription();
    }

    void loadDescription() {
        API.getAllDescriptionsByUserid(context, user.id, new APICallback<List<Description>>() {
            @Override
            public void onSuccess(List<Description> response) {
                if (user.group_id == Constants.PLAYER) {
                    int[] ids = new int[] {
                            Constants.DESCRIPTION_1,
                            Constants.DESCRIPTION_2,
                            Constants.DESCRIPTION_3,
                            Constants.DESCRIPTION_4,
                            Constants.DESCRIPTION_5,
                            Constants.DESCRIPTION_6  };
                    listSkills = BaseActivity.getDescriptions(user.id, ids, response);
                    ids = new int[] {
                            Constants.DESCRIPTION_7,
                            Constants.DESCRIPTION_8,
                            Constants.DESCRIPTION_9,
                            Constants.DESCRIPTION_10,
                            Constants.DESCRIPTION_11,
                            Constants.DESCRIPTION_12  };
                    listIndividualTechnique = BaseActivity.getDescriptions(user.id, ids, response);
                    ids = new int[] {
                            Constants.DESCRIPTION_13,
                            Constants.DESCRIPTION_14,
                            Constants.DESCRIPTION_15,
                            Constants.DESCRIPTION_16,
                            Constants.DESCRIPTION_17,
                            Constants.DESCRIPTION_18  };
                    listPhysicalQuantities = BaseActivity.getDescriptions(user.id, ids, response);
                    ids = new int[] {
                            Constants.DESCRIPTION_19,
                            Constants.DESCRIPTION_20,
                            Constants.DESCRIPTION_21,
                            Constants.DESCRIPTION_22,
                            Constants.DESCRIPTION_23,
                            Constants.DESCRIPTION_24  };
                    listTactics = BaseActivity.getDescriptions(user.id, ids, response);
                } else {
                    int[] ids = new int[] {
                            Constants.CDESCRIPTION_1,
                            Constants.CDESCRIPTION_2,
                            Constants.CDESCRIPTION_3,
                            Constants.CDESCRIPTION_4,
                            Constants.CDESCRIPTION_5,
                            Constants.CDESCRIPTION_6  };
                    listSkills = BaseActivity.getDescriptions(user.id, ids, response);
                    ids = new int[] {
                            Constants.CDESCRIPTION_7,
                            Constants.CDESCRIPTION_8,
                            Constants.CDESCRIPTION_9,
                            Constants.CDESCRIPTION_10,
                            Constants.CDESCRIPTION_11,
                            Constants.CDESCRIPTION_12  };
                    listIndividualTechnique = BaseActivity.getDescriptions(user.id, ids, response);
                    ids = new int[] {
                            Constants.CDESCRIPTION_13,
                            Constants.CDESCRIPTION_14,
                            Constants.CDESCRIPTION_15,
                            Constants.CDESCRIPTION_16,
                            Constants.CDESCRIPTION_17,
                            Constants.CDESCRIPTION_18  };
                    listPhysicalQuantities = BaseActivity.getDescriptions(user.id, ids, response);
                    ids = new int[] {
                            Constants.CDESCRIPTION_19,
                            Constants.CDESCRIPTION_20,
                            Constants.CDESCRIPTION_21,
                            Constants.CDESCRIPTION_22,
                            Constants.CDESCRIPTION_23,
                            Constants.CDESCRIPTION_24  };
                    listTactics = BaseActivity.getDescriptions(user.id, ids, response);
                }
                setChart();
            }
            @Override
            public void onFailure(String error) {
            }
        });
    }

    void setChart() {
        String[] skillLabels;
        String[] individualLabels;
        String[] physicalLabels;
        String[] tacticsLabels;
        if (user.group_id == Constants.PLAYER) {
            skillLabels = new String[] {
                    context.getResources().getString(R.string.description_1),
                    context.getResources().getString(R.string.description_2),
                    context.getResources().getString(R.string.description_3),
                    context.getResources().getString(R.string.description_4),
                    context.getResources().getString(R.string.description_5),
                    context.getResources().getString(R.string.description_6)};
            individualLabels = new String[] {
                    context.getResources().getString(R.string.description_7),
                    context.getResources().getString(R.string.description_8),
                    context.getResources().getString(R.string.description_9),
                    context.getResources().getString(R.string.description_10),
                    context.getResources().getString(R.string.description_11),
                    context.getResources().getString(R.string.description_12)};
            physicalLabels = new String[] {
                    context.getResources().getString(R.string.description_13),
                    context.getResources().getString(R.string.description_14),
                    context.getResources().getString(R.string.description_15),
                    context.getResources().getString(R.string.description_16),
                    context.getResources().getString(R.string.description_17),
                    context.getResources().getString(R.string.description_18)};
            tacticsLabels = new String[] {
                    context.getResources().getString(R.string.description_19),
                    context.getResources().getString(R.string.description_20),
                    context.getResources().getString(R.string.description_21),
                    context.getResources().getString(R.string.description_22),
                    context.getResources().getString(R.string.description_23),
                    context.getResources().getString(R.string.description_24)};
        } else {
            skillLabels = new String[] {
                    context.getResources().getString(R.string.cdescription_1),
                    context.getResources().getString(R.string.cdescription_2),
                    context.getResources().getString(R.string.cdescription_3),
                    context.getResources().getString(R.string.cdescription_4),
                    context.getResources().getString(R.string.cdescription_5),
                    context.getResources().getString(R.string.cdescription_6)};
            individualLabels = new String[] {
                    context.getResources().getString(R.string.cdescription_7),
                    context.getResources().getString(R.string.cdescription_8),
                    context.getResources().getString(R.string.cdescription_9),
                    context.getResources().getString(R.string.cdescription_10),
                    context.getResources().getString(R.string.cdescription_11),
                    context.getResources().getString(R.string.cdescription_12)};
            physicalLabels = new String[] {
                    context.getResources().getString(R.string.cdescription_13),
                    context.getResources().getString(R.string.cdescription_14),
                    context.getResources().getString(R.string.cdescription_15),
                    context.getResources().getString(R.string.cdescription_16),
                    context.getResources().getString(R.string.cdescription_17),
                    context.getResources().getString(R.string.cdescription_18)};
            tacticsLabels = new String[] {
                    context.getResources().getString(R.string.cdescription_19),
                    context.getResources().getString(R.string.cdescription_20),
                    context.getResources().getString(R.string.cdescription_21),
                    context.getResources().getString(R.string.cdescription_22),
                    context.getResources().getString(R.string.cdescription_23),
                    context.getResources().getString(R.string.cdescription_24)};
        }
        setRadarChart(radar_skills, skillLabels, listSkills);
        setRadarChart(radar_individual_technique, individualLabels, listIndividualTechnique);
        setRadarChart(radar_physical_quantities, physicalLabels, listPhysicalQuantities);
        setRadarChart(radar_tactics, tacticsLabels, listTactics);
    }

    void setRadarChart(RadarChart radar, final String[] labels, List<Description> descriptions) {
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

        setData(radar, descriptions);
    }

    private void setData(RadarChart radar, List<Description> descriptions) {
        ArrayList<RadarEntry> entries = new ArrayList<>();
        for (int i = 0; i < descriptions.size(); i++) {
            entries.add(new RadarEntry(descriptions.get(i).value));
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

    @OnClick(R.id.btn_access) void onClickAccess() {
        if (mListener != null) mListener.onClickAccess();
    }

    @OnClick(R.id.img_strength) void onClickStrength(){
        if (mListener != null) mListener.onClickStrength();
    }

    public interface Listener {
        void onClickAccess();
        void onClickStrength();
    }
}
