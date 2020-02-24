package com.odelan.yang.aggone.Activity.Stats;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.github.florent37.viewanimator.ViewAnimator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Activity.LookUp.LookUpActivity;
import com.odelan.yang.aggone.Activity.MainActivity;
import com.odelan.yang.aggone.Activity.Message.ContactActivity;
import com.odelan.yang.aggone.Model.Description;
import com.odelan.yang.aggone.Model.Skill;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.StrictMath.max;
import static java.lang.StrictMath.min;

public class StatsActivity extends BaseActivity {

    @BindView(R.id.txt_statistics) TextView txt_statistics;
    @BindView(R.id.txt_match) TextView txt_match;
    @BindView(R.id.txt_ratio) TextView txt_ratio;

    @BindView(R.id.btn_stats) ImageView btn_stats;
    @BindView(R.id.txt_stats) TextView txt_stats;
    @BindView(R.id.txt_pref) TextView txt_pref;

    @BindView(R.id.chart_cube) LineChart chart_cube;
    @BindView(R.id.chart_pie) PieChart chart_pie;

    @BindView(R.id.img_chart1) ImageView img_chart1;
    @BindView(R.id.img_chart2) ImageView img_chart2;
    @BindView(R.id.img_chart3) ImageView img_chart3;
    @BindView(R.id.img_chart4) ImageView img_chart4;

    @BindView(R.id.txt_percent1) TextView txt_percent1;
    @BindView(R.id.txt_percent2) TextView txt_percent2;
    @BindView(R.id.txt_percent3) TextView txt_percent3;
    @BindView(R.id.txt_percent4) TextView txt_percent4;

    @BindView(R.id.txt_skill1) TextView txt_skill1;
    @BindView(R.id.txt_skill2) TextView txt_skill2;
    @BindView(R.id.txt_skill3) TextView txt_skill3;
    @BindView(R.id.txt_skill4) TextView txt_skill4;

    @BindView(R.id.txt_skill_average) TextView txt_skill_average;

    @BindView(R.id.layout_disable)    RelativeLayout layout_disable;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        ButterKnife.bind(this);

        user = getIntent().getExtras().getParcelable(Constants.USER);
        setActivity();
    }

    List<List<Skill>> monthResult = new ArrayList<>();
    List<Description> skills = new ArrayList<>();

    void setActivity() {
        btn_stats.setImageResource(R.mipmap.tab_stats_active);
        txt_stats.setTextColor(getResources().getColor(R.color.tab_active));
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshLayout();
    }

    private void refreshLayout(){
        if(user.group_id == Constants.AGENT || user.group_id == Constants.COMPANY){
            layout_disable.setVisibility(View.VISIBLE);
        } else{
            layout_disable.setVisibility(View.GONE);
            showProgress();
            int nYear = Calendar.getInstance().get(Calendar.YEAR);
            API.getYearResultSummary(this, user, nYear, new APICallback<List<List<Pair<String, Integer>>>>() {
                @Override
                public void onSuccess(List<List<Pair<String, Integer>>> response) {
                    dismissProgress();
                    monthResult.clear();
                    for (List<Pair<String, Integer>> one : response) {
                        monthResult.add(getSportSkills(user.group_id == Constants.PLAYER ? user.sport_id : Constants.STATS_COACH, one));
                    }
                    setCubeChart();
                    getSkills();
                }
                @Override
                public void onFailure(String error) {
                    dismissProgress();
                    showToast(error);
                }
            });
        }
    }

    void getSkills() {
        API.getAllDescriptionsByUserid(this, user.id, new APICallback<List<Description>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(List<Description> response) {
                int[] ids;
                if (user.group_id == Constants.PLAYER) {
                    ids = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };
                } else {
                    ids = new int[] { 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47 };
                }
                skills.clear();
                skills.addAll(BaseActivity.getDescriptions(user.id, ids, response));
                skills.sort((o1, o2) -> {
                    if (o1.value < o2.value) return 1;
                    if (o1.value == o2.value) return 0;
                    return -1;
                });
                setPieChart2();
            }
            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });
    }

    String[] getLabels() {
        String[] labels = new String[] {
                getResources().getString(R.string.statistics),
                getResources().getString(R.string.match),
                getResources().getString(R.string.ratio)
        };
        if (user.group_id == Constants.COACH) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.American_Football) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Basketball || user.sport_id == Constants.Korfball) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Football) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Rugby) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Cricket) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.BaseBall || user.sport_id == Constants.Softball) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Ice_Hockey || user.sport_id == Constants.Field_Hockey ||
                user.sport_id == Constants.Floorball || user.sport_id == Constants.WaterPolo) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Handball) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.VolleyBall || user.sport_id == Constants.SepakTakraw) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Tennis) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Badminton) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Squash || user.sport_id == Constants.Bowling ||
                user.sport_id == Constants.Crossfit_Fitness || user.sport_id == Constants.Weightlifting) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.TableTennis) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Golf) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Athletics) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Swimming || user.sport_id == Constants.KickBoxing ||
                user.sport_id == Constants.MMA || user.sport_id == Constants.Trail ||
                user.sport_id == Constants.Rowing || user.sport_id == Constants.Mountaineering ||
                user.sport_id == Constants.WinterSports || user.sport_id == Constants.Air_Sports ) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.WaterSports) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Gymnastic) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Judo) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Boxing) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.MartialArt) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Wrestling) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Fencing) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Cycling) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Equestrianism) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.AutoRacing) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.MotoRacing) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Skiing) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        }  else if (user.sport_id == Constants.Snowboarding || user.sport_id == Constants.Surf ||
                user.sport_id == Constants.IceSkating || user.sport_id == Constants.Sailing || user.sport_id == Constants.Canoeing) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.SkateBoard) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Climbing) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        } else if (user.sport_id == Constants.Dance) {
            labels = new String[] {
                    getResources().getString(R.string.statistics),
                    getResources().getString(R.string.match_win_percent),
                    getResources().getString(R.string.ratio),
            };
        }

        return labels;
    }

    private void setStatisticsData() {
        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();
        ArrayList<Entry> values3 = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            float val = getStatistics_New(monthResult.get(i));
            values1.add(new Entry(i, val));
        }
        for (int i = 0; i < 12; i++) {
            float val = getMatch_New(monthResult.get(i));
            values2.add(new Entry(i, val));
        }
        for (int i = 0; i < 12; i++) {
            float val = getRatio_New(monthResult.get(i));
            values3.add(new Entry(i, val));
        }

        LineDataSet set1, set2, set3, set4;

        set1 = new LineDataSet(values1, getResources().getString(R.string.statistics));
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setLineWidth(0.0f);
        set1.setColor(Color.parseColor("#F7A055"));
        set1.setFillColor(Color.parseColor("#F7A055"));
        set1.setFillAlpha(150);

        set2 = new LineDataSet(values2, getResources().getString(R.string.match));
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setCubicIntensity(0.2f);
        set2.setDrawFilled(true);
        set2.setDrawCircles(false);
        set2.setLineWidth(0.0f);
        set2.setColor(Color.parseColor("#F76055"));
        set2.setFillColor(Color.parseColor("#F76055"));
        set2.setFillAlpha(150);

        set3 = new LineDataSet(values3, getResources().getString(R.string.ratio));
        set3.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set3.setCubicIntensity(0.2f);
        set3.setDrawFilled(true);
        set3.setDrawCircles(false);
        set3.setLineWidth(0.0f);
        set3.setColor(Color.parseColor("#9B3655"));
        set3.setFillColor(Color.parseColor("#9B3655"));
        set3.setFillAlpha(150);

        set4 = new LineDataSet(values1, "");
        set4.setMode(LineDataSet.Mode.LINEAR);
        set4.setDrawCircles(true);
        set4.setCircleColor(Color.parseColor("#F7A055"));
        set4.setCircleRadius(4.0f);
        set4.setLineWidth(2.0f);
        set4.setColor(Color.BLACK);

        // create a data object with the data sets
        LineData data = new LineData(set1, set2, set3, set4);
        data.setDrawValues(false);

        // set data
        chart_cube.setData(data);
    }

    void setCubeChart() {
        chart_cube.getDescription().setEnabled(false);
        chart_cube.setTouchEnabled(false);
        chart_cube.setDragEnabled(false);
        chart_cube.setScaleEnabled(false);
        chart_cube.setPinchZoom(false);
        chart_cube.setDrawGridBackground(false);

        setStatisticsData();

        XAxis x = chart_cube.getXAxis();
        x.setLabelCount(12, true);

        final String[] labels = new String[12];
        Arrays.asList(getResources().getStringArray(R.array.months_array)).toArray(labels);
        x.setValueFormatter((value, axis) -> labels[(int) value % labels.length]);
        x.setTextColor(Color.parseColor("#404040"));
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setAxisLineColor(Color.parseColor("#707070"));

        YAxis y = chart_cube.getAxisLeft();
        y.setLabelCount(10, false);
        y.setStartAtZero(true);
        y.setTextColor(Color.parseColor("#404040"));
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.parseColor("#707070"));

        chart_cube.getAxisRight().setEnabled(false);

        Legend legend = chart_cube.getLegend();
        legend.setEnabled(false);

        String[] legendLabels = getLabels();
        txt_statistics.setText(legendLabels[0]);
        txt_match.setText(legendLabels[1]);
        txt_ratio.setText(legendLabels[2]);

        chart_cube.animateXY(500, 500);
        chart_cube.invalidate();
    }

    void setPieChart2() {
        int count = 0;
        int sum = 0;
        for (Description skill : skills) {
            if (skill.value > 0) {
                count++;
                sum += skill.value;
            }
        }
        int percent1 = skills.get(0).value * 100 / max(sum, 1);
        int percent2 = skills.get(1).value * 100 / max(sum, 1);
        int percent3 = skills.get(2).value * 100 / max(sum, 1);
        int percent4 = skills.get(3).value * 100 / max(sum, 1);

        txt_percent1.setText(String.format("%d%%", percent1));
        txt_percent2.setText(String.format("%d%%", percent2));
        txt_percent3.setText(String.format("%d%%", percent3));
        txt_percent4.setText(String.format("%d%%", percent4));

        txt_skill1.setText(getDescriptionName(this, skills.get(0).type));
        txt_skill2.setText(getDescriptionName(this, skills.get(1).type));
        txt_skill3.setText(getDescriptionName(this, skills.get(2).type));
        txt_skill4.setText(getDescriptionName(this, skills.get(3).type));

        float average = (float)sum / max(count, 1);
        txt_skill_average.setText(String.format("%.0f", average));

        int ratio_count = 0;
        float ratio_sum = 0;

        for (int i = 0; i < 12; i++) {
            float val = getStatistics_New(monthResult.get(i));
            if (val > 0) {
                ratio_count++;
                ratio_sum += val;
            }
        }
        float ratio_average = ratio_sum / max(ratio_count, 1) / getSkillCount();
        float perf = ratio_average / max(average, 1);
        txt_pref.setText(String.format("%.1f", min(perf, 10)));

        ViewAnimator
                .animate(img_chart1)
                .dp().height(0, percent1 * 1.4f / 3 + 70.0f / 3)
                .dp().width(0, percent1 * 1.4f / 3 + 70.0f / 3)

                .andAnimate(img_chart2)
                .dp().height(0, percent2 * 1.4f / 3 + 70.0f / 3)
                .dp().width(0, percent2 * 1.4f / 3 + 70.0f / 3)


                .andAnimate(img_chart3)
                .dp().height(0, percent3 * 1.4f / 3 + 70.0f / 3)
                .dp().width(0, percent3 * 1.4f / 3 + 70.0f / 3)

                .andAnimate(img_chart4)
                .dp().height(0, percent4 * 1.4f / 3 + 70.0f / 3)
                .dp().width(0, percent4 * 1.4f / 3 + 70.0f / 3)

                .duration(700)
                .start();
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
    /**
     * Tab bar Event
     * */
    @OnClick(R.id.btn_home) void onClickHome() {
        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @OnClick(R.id.btn_lookup) void onClickLookup() {
        Intent intent = new Intent(this, LookUpActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btn_chats) void onClickChats() {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btn_advanced_stats) void onClickStats() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(Constants.USER, (Parcelable) user);
        startActivity(intent);
    }

    private float getStatistics_New(List<Skill> skills) {
        float result = 0;
        int i = 0;
        List<String> stat_keys = new ArrayList<>();
        if (user.group_id == Constants.COACH || user.group_id == Constants.TEAM_CLUB || user.group_id == Constants.STAFF) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_coach_key));
            result =getSkillValue(stat_keys.get(1), skills);//victories
        } else if (user.sport_id == Constants.American_Football) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_american_football_key));
            for(i = 4 ; i < stat_keys.size(); i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.Basketball) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_basketball_key));
            for(i = 4 ; i < stat_keys.size(); i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.Korfball){
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_korfball_key));
            for(i = 4 ; i < stat_keys.size(); i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        }else if (user.sport_id == Constants.Football) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_football_key));
            for(i = 4 ; i < stat_keys.size()-1; i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.Rugby) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_rugby_key));
            for(i = 4 ; i < stat_keys.size(); i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.Cricket) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_cricket_key));
            for(i = 4 ; i < stat_keys.size(); i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.BaseBall) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_baseball_key));
            for(i = 4 ; i < stat_keys.size(); i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.Softball) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_softball_key));
            for(i = 4 ; i < stat_keys.size(); i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.Ice_Hockey || user.sport_id == Constants.Field_Hockey) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_hockey_key));
            for(i = 4 ; i < stat_keys.size(); i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.Handball) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_handball_key));
            for(i = 4 ; i < stat_keys.size()-1; i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.VolleyBall) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_volleyball_key));
            for(i = 4 ; i < stat_keys.size(); i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.SepakTakraw) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_sepak_takraw_key));
            for(i = 4 ; i < stat_keys.size(); i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        }else if (user.sport_id == Constants.Tennis) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_tennis_key));
            for(i = 4 ; i < 6; i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.Badminton || user.sport_id == Constants.Squash || user.sport_id == Constants.TableTennis) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_badminton_key));
            for(i = 4 ; i < 6; i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.Golf) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_golf_key));
            for(i = 4 ; i < stat_keys.size()-2; i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.Athletics || user.sport_id == Constants.Swimming || user.sport_id == Constants.WaterSports ||
                user.sport_id == Constants.Gymnastic || user.sport_id == Constants.Boxing || user.sport_id == Constants.MartialArt ||
                user.sport_id == Constants.Wrestling || user.sport_id == Constants.Fencing || user.sport_id == Constants.Cycling ||
                user.sport_id == Constants.Equestrianism || user.sport_id == Constants.Dance || user.sport_id == Constants.Climbing ||
                user.sport_id == Constants.Bowling || user.sport_id == Constants.Crossfit_Fitness || user.sport_id == Constants.Weightlifting ||
                user.sport_id == Constants.KickBoxing || user.sport_id == Constants.MMA || user.sport_id == Constants.Trail ||
                user.sport_id == Constants.Rowing || user.sport_id == Constants.Mountaineering || user.sport_id == Constants.WinterSports ||
                user.sport_id == Constants.Air_Sports) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_athletics_key));
            for(i = 4 ; i < 5; i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.Judo) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_judo_key));
            for(i = 4 ; i < 8; i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.AutoRacing || user.sport_id == Constants.MotoRacing ||
                user.sport_id == Constants.Skiing ||user.sport_id == Constants.Snowboarding || user.sport_id == Constants.Surf ||
                user.sport_id == Constants.IceSkating || user.sport_id == Constants.Sailing || user.sport_id == Constants.Canoeing ||
                user.sport_id == Constants.SkateBoard) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_racing_key));
            for(i = 4 ; i < 5; i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.Floorball) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_floorball_key));
            for(i = 4 ; i < stat_keys.size(); i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        } else if (user.sport_id == Constants.WaterPolo) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_water_polo_key));
            for(i = 4 ; i < stat_keys.size(); i++){
                result += getSkillValue(stat_keys.get(i), skills);
            }
        }
        return  result;
    }

    private float getMatch_New(List<Skill> skills) {
        float result = 0;
        if (user.group_id == Constants.COACH || user.group_id == Constants.TEAM_CLUB || user.group_id == Constants.STAFF) {
            result = getSkillValue(Constants.STATS_GAMES_PLAYED, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_VICTORIES, skills) / (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
        } else if (user.sport_id == Constants.American_Football || user.sport_id == Constants.Basketball ||
                user.sport_id == Constants.Korfball || user.sport_id == Constants.Football || user.sport_id == Constants.Rugby ||
                user.sport_id == Constants.Cricket || user.sport_id == Constants.BaseBall || user.sport_id == Constants.Softball ||
                user.sport_id == Constants.Ice_Hockey || user.sport_id == Constants.Field_Hockey || user.sport_id == Constants.VolleyBall ||
                user.sport_id == Constants.SepakTakraw || user.sport_id == Constants.Handball) {
            result = getSkillValue(Constants.STATS_GAMES_PLAYED, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_VICTORIES, skills) / (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
//            result =getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
        } else if (user.sport_id == Constants.Tennis || user.sport_id == Constants.Badminton || user.sport_id == Constants.Squash ||
                user.sport_id == Constants.Bowling || user.sport_id == Constants.Crossfit_Fitness || user.sport_id == Constants.Weightlifting ||
                user.sport_id == Constants.TableTennis || user.sport_id == Constants.Golf || user.sport_id == Constants.Athletics ||
                user.sport_id == Constants.Swimming || user.sport_id == Constants.KickBoxing || user.sport_id == Constants.WaterSports ||
                user.sport_id == Constants.MMA || user.sport_id == Constants.Trail || user.sport_id == Constants.Gymnastic ||
                user.sport_id == Constants.Rowing || user.sport_id == Constants.Mountaineering || user.sport_id == Constants.Judo ||
                user.sport_id == Constants.WinterSports || user.sport_id == Constants.Air_Sports || user.sport_id == Constants.Boxing ||
                user.sport_id == Constants.MartialArt || user.sport_id == Constants.Wrestling || user.sport_id == Constants.Fencing ||
                user.sport_id == Constants.Cycling || user.sport_id == Constants.Equestrianism || user.sport_id == Constants.Climbing ||
                user.sport_id == Constants.Dance) {
            result = getSkillValue(Constants.STATS_GAMES_PLAYED, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_VICTORIES, skills) / (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
        } else if (user.sport_id == Constants.AutoRacing || user.sport_id == Constants.MotoRacing ||
                user.sport_id == Constants.Skiing ||user.sport_id == Constants.Snowboarding || user.sport_id == Constants.Surf ||
                user.sport_id == Constants.IceSkating || user.sport_id == Constants.Sailing || user.sport_id == Constants.Canoeing ||
                user.sport_id == Constants.SkateBoard){
            result = getSkillValue(Constants.STATS_RACES, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_VICTORIES, skills) / (float)getSkillValue(Constants.STATS_RACES, skills);
        } else {
            result = getSkillValue(Constants.STATS_GAMES_PLAYED, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_VICTORIES, skills) / (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
        }
        return  result;
    }

    private float getRatio_New(List<Skill> skills) {
        float result = 0;
        if (user.group_id == Constants.COACH || user.group_id == Constants.TEAM_CLUB || user.group_id == Constants.STAFF) {
            result = getSkillValue(Constants.STATS_DEFEATS, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_VICTORIES, skills) / (float)getSkillValue(Constants.STATS_DEFEATS, skills);
        }

        else if (user.sport_id == Constants.AutoRacing || user.sport_id == Constants.MotoRacing ||
                user.sport_id == Constants.Skiing ||user.sport_id == Constants.Snowboarding || user.sport_id == Constants.Surf ||
                user.sport_id == Constants.IceSkating || user.sport_id == Constants.Sailing || user.sport_id == Constants.Canoeing ||
                user.sport_id == Constants.SkateBoard){
            result = getSkillValue(Constants.STATS_RACES, skills) == 0 ? 0 :
                    getStatistics_New(skills) / (float)getSkillValue(Constants.STATS_RACES, skills);
        } else {
            result = getSkillValue(Constants.STATS_GAMES_PLAYED, skills) == 0 ? 0 :
                    getStatistics_New(skills) / getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
        }
        return  result;
    }

    private float getVictoryRate(List<Skill> skills){
        float result = 0;
        if (user.group_id == Constants.COACH || user.group_id == Constants.TEAM_CLUB || user.group_id == Constants.STAFF) {
            result = getSkillValue(Constants.STATS_GAMES_PLAYED, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_VICTORIES, skills) / (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
        } else if (user.sport_id == Constants.AutoRacing || user.sport_id == Constants.MotoRacing ||
                user.sport_id == Constants.Skiing ||user.sport_id == Constants.Snowboarding || user.sport_id == Constants.Surf ||
                user.sport_id == Constants.IceSkating || user.sport_id == Constants.Sailing || user.sport_id == Constants.Canoeing ||
                user.sport_id == Constants.SkateBoard){
            result = getSkillValue(Constants.STATS_RACES, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_VICTORIES, skills) / (float)getSkillValue(Constants.STATS_RACES, skills);
        } else {
            result = getSkillValue(Constants.STATS_GAMES_PLAYED, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_VICTORIES, skills) / (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
        }
        return  result;
    }

    private float getDefeatsRate(List<Skill> skills){
        float result = 0;
        if (user.group_id == Constants.COACH || user.group_id == Constants.TEAM_CLUB || user.group_id == Constants.STAFF) {
            result = getSkillValue(Constants.STATS_GAMES_PLAYED, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_DEFEATS, skills) / (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
        } else if (user.sport_id == Constants.AutoRacing || user.sport_id == Constants.MotoRacing ||
                user.sport_id == Constants.Skiing ||user.sport_id == Constants.Snowboarding || user.sport_id == Constants.Surf ||
                user.sport_id == Constants.IceSkating || user.sport_id == Constants.Sailing || user.sport_id == Constants.Canoeing ||
                user.sport_id == Constants.SkateBoard){
            result = getSkillValue(Constants.STATS_RACES, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_DEFEATS, skills) / (float)getSkillValue(Constants.STATS_RACES, skills);
        } else {
            result = getSkillValue(Constants.STATS_GAMES_PLAYED, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_DEFEATS, skills) / (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
        }
        return  result;
    }

    private float getDrawsRate(List<Skill> skills){
        float result = 0;
        if (user.group_id == Constants.COACH || user.group_id == Constants.TEAM_CLUB || user.group_id == Constants.STAFF) {
            result = getSkillValue(Constants.STATS_GAMES_PLAYED, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_DRAWS, skills) / (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
        } else if (user.sport_id == Constants.AutoRacing || user.sport_id == Constants.MotoRacing ||
                user.sport_id == Constants.Skiing ||user.sport_id == Constants.Snowboarding || user.sport_id == Constants.Surf ||
                user.sport_id == Constants.IceSkating || user.sport_id == Constants.Sailing || user.sport_id == Constants.Canoeing ||
                user.sport_id == Constants.SkateBoard){
            result = getSkillValue(Constants.STATS_RACES, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_DRAWS, skills) / (float)getSkillValue(Constants.STATS_RACES, skills);
        } else {
            result = getSkillValue(Constants.STATS_GAMES_PLAYED, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_DRAWS, skills) / getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
        }
        return  result;
    }

    private float getSkillCount() {
        int result = 1;
        List<String> stat_keys = new ArrayList<>();
        if (user.group_id == Constants.COACH || user.group_id == Constants.TEAM_CLUB || user.group_id == Constants.STAFF) {
            result = 1;//victories
        } else if (user.sport_id == Constants.American_Football) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_american_football_key));
            result = stat_keys.size() - 4;
        } else if (user.sport_id == Constants.Basketball) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_basketball_key));
            result = stat_keys.size() - 4;
        } else if (user.sport_id == Constants.Korfball){
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_korfball_key));
            result = stat_keys.size() - 4;
        }else if (user.sport_id == Constants.Football) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_football_key));
            result = stat_keys.size() - 5;
        } else if (user.sport_id == Constants.Rugby) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_rugby_key));
            result = stat_keys.size() - 4;
        } else if (user.sport_id == Constants.Cricket) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_cricket_key));
            result = stat_keys.size() - 4;
        } else if (user.sport_id == Constants.BaseBall) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_baseball_key));
            result = stat_keys.size() - 4;
        } else if (user.sport_id == Constants.Softball) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_softball_key));
            result = stat_keys.size() - 4;
        } else if (user.sport_id == Constants.Ice_Hockey || user.sport_id == Constants.Field_Hockey) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_hockey_key));
            result = stat_keys.size() - 4;
        } else if (user.sport_id == Constants.Handball) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_handball_key));
            result = stat_keys.size() - 5;
        } else if (user.sport_id == Constants.VolleyBall) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_volleyball_key));
            result = stat_keys.size() - 4;
        } else if (user.sport_id == Constants.SepakTakraw) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_sepak_takraw_key));
            result = stat_keys.size() - 4;
        }else if (user.sport_id == Constants.Tennis) {
            result = 2;
        } else if (user.sport_id == Constants.Badminton || user.sport_id == Constants.Squash || user.sport_id == Constants.TableTennis) {
            result = 2;
        } else if (user.sport_id == Constants.Golf) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_golf_key));
            result = stat_keys.size() - 6;
        } else if (user.sport_id == Constants.Athletics || user.sport_id == Constants.Swimming || user.sport_id == Constants.WaterSports ||
                user.sport_id == Constants.Gymnastic || user.sport_id == Constants.Boxing || user.sport_id == Constants.MartialArt ||
                user.sport_id == Constants.Wrestling || user.sport_id == Constants.Fencing || user.sport_id == Constants.Cycling ||
                user.sport_id == Constants.Equestrianism || user.sport_id == Constants.Dance || user.sport_id == Constants.Climbing ||
                user.sport_id == Constants.Bowling || user.sport_id == Constants.Crossfit_Fitness || user.sport_id == Constants.Weightlifting ||
                user.sport_id == Constants.KickBoxing || user.sport_id == Constants.MMA || user.sport_id == Constants.Trail ||
                user.sport_id == Constants.Rowing || user.sport_id == Constants.Mountaineering || user.sport_id == Constants.WinterSports ||
                user.sport_id == Constants.Air_Sports) {
            result = 1;
        } else if (user.sport_id == Constants.Judo) {
            result = 4;
        } else if (user.sport_id == Constants.AutoRacing || user.sport_id == Constants.MotoRacing ||
                user.sport_id == Constants.Skiing ||user.sport_id == Constants.Snowboarding || user.sport_id == Constants.Surf ||
                user.sport_id == Constants.IceSkating || user.sport_id == Constants.Sailing || user.sport_id == Constants.Canoeing ||
                user.sport_id == Constants.SkateBoard) {
            result = 1;
        } else if (user.sport_id == Constants.Floorball) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_floorball_key));
            result = stat_keys.size() - 4;
        } else if (user.sport_id == Constants.WaterPolo) {
            stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_water_polo_key));
            result = stat_keys.size() - 4;
        }
        if(result == 0) return 1;
        return  result;
    }
}
