package com.odelan.yang.aggone.Activity.Stats;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evrencoskun.tableview.TableView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Adapter.Stats.ResultAdapter;
import com.odelan.yang.aggone.Model.Cell;
import com.odelan.yang.aggone.Model.Skill;
import com.odelan.yang.aggone.Model.Summary;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;
import com.odelan.yang.aggone.Utils.PercentFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultActivity extends BaseActivity {

    @BindView(R.id.txt_statistics) TextView txt_statistics;
    @BindView(R.id.txt_match) TextView txt_match;
    @BindView(R.id.txt_ratio) TextView txt_ratio;
    @BindView(R.id.btn_edit) ImageView btn_edit;
    @BindView(R.id.chart_victory)    PieChart victory_chart;
    @BindView(R.id.chart_draw)       PieChart draw_chart;
    @BindView(R.id.chart_defeat)     PieChart defeat_chart;
    @BindView(R.id.txt_victory)      TextView txt_victory;
    @BindView(R.id.txt_draw)         TextView txt_draw;
    @BindView(R.id.txt_defeat)       TextView txt_defeat;

    List<String> mRowData = new ArrayList<>();
    List<String> mColumnData = new ArrayList<>();
    List<List<Cell>> mCellData = new ArrayList<>();
    @BindView(R.id.table_view) TableView table_view;
    ResultAdapter adapter;

    @BindView(R.id.chart_stats) LineChart chart_stats;
    List<List<Skill>> monthResult = new ArrayList<>();

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        user = getIntent().getExtras().getParcelable(Constants.USER);
        setActivity();
    }

    void setActivity() {
        adapter = new ResultAdapter(this);
        table_view.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshActivity();
    }

    private void refreshActivity(){
        if (!user.id.equals(AppData.user.id)) {
            btn_edit.setVisibility(View.INVISIBLE);
        }
        API.getUserResultSummary(this, user, new APICallback<List<Summary>>() {
            @Override
            public void onSuccess(List<Summary> response) {
                mColumnData.clear();
                mRowData.clear();
                mCellData.clear();
                List<String> sum = new ArrayList<>();
                List<Skill> skills = getSportSkills(user.group_id == Constants.PLAYER ? user.sport_id : Constants.STATS_COACH, new ArrayList<>());
                for (Skill skill : skills) {
                    mColumnData.add(skill.description);//skill.summary
                    sum.add("0");
                }
                int row = 0, column;
                for (Summary club : response) {
                    mRowData.add(club.club_name);
                    skills = getSportSkills(user.group_id == Constants.PLAYER ? user.sport_id : Constants.STATS_COACH, club.stats);
                    column = 0;
                    List<Cell> rowCell = new ArrayList<>();
                    for (Skill skill : skills) {
                        Cell cell = new Cell(row, column, String.valueOf(skill.value));
                        if (skill.key.equals(Constants.PERFORMANCE)) {
                            cell.value = String.format("%.1f", (float)skill.value / 10);
                            sum.set(column, String.format("%.1f", Float.valueOf(sum.get(column)) + (float)skill.value / 10));
                        } else {
                            sum.set(column, String.valueOf(Integer.valueOf(sum.get(column)) + skill.value));
                        }
                        if (skill.key.equals(Constants.PERFORMANCE) || skill.key.equals(Constants.RANKING)) {
                            if (row == response.size() - 1) {
                                sum.set(column, String.format("%.1f", Float.valueOf(sum.get(column)) / response.size()));
                            }
                        }
                        rowCell.add(cell);
                        column++;
                    }
                    mCellData.add(rowCell);
                    row++;
                }
                column = 0;
                List<Cell> rowCell = new ArrayList<>();
                for (String one : sum) {
                    Cell cell = new Cell(-1, column, one);
                    rowCell.add(cell);
                    column++;
                }
                mCellData.add(rowCell);
                mRowData.add(getString(R.string.total));
                if(response.size() > 0){
                    ViewGroup.LayoutParams params = table_view.getLayoutParams();
                    params.height = convertDpToPixelInt(response.size() * 40 + 90, ResultActivity.this);
                    table_view.setLayoutParams(params);
                    table_view.setVisibility(View.VISIBLE);
                } else {
                    table_view.setVisibility(View.GONE);
                }
                adapter.setAllItems(mColumnData, mRowData, mCellData);
            }

            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });

        int nYear = Calendar.getInstance().get(Calendar.YEAR);
        API.getYearResultSummary(ResultActivity.this, user, nYear, new APICallback<List<List<Pair<String, Integer>>>>() {
            @Override
            public void onSuccess(List<List<Pair<String, Integer>>> response) {
                monthResult.clear();
                for (List<Pair<String, Integer>> one : response) {
                    monthResult.add(getSportSkills(user.group_id == Constants.PLAYER ? user.sport_id : Constants.STATS_COACH, one));
                }
                setChart();
                drawCircleChart();
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
                user.sport_id == Constants.WinterSports || user.sport_id == Constants.Air_Sports) {
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
        chart_stats.setData(data);
    }

    void setChart() {
        chart_stats.getDescription().setEnabled(false);
        chart_stats.setTouchEnabled(false);
        chart_stats.setDragEnabled(false);
        chart_stats.setScaleEnabled(false);
        chart_stats.setPinchZoom(false);
        chart_stats.setDrawGridBackground(false);

        setStatisticsData();

        XAxis x = chart_stats.getXAxis();
        x.setLabelCount(12, true);

        final String[] labels = new String[12];
        Arrays.asList(getResources().getStringArray(R.array.months_array)).toArray(labels);
        x.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels[(int) value % labels.length];
            }
        });
        x.setTextColor(Color.parseColor("#404040"));
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setAxisLineColor(Color.parseColor("#707070"));

        YAxis y = chart_stats.getAxisLeft();
        y.setLabelCount(10, false);
        y.setStartAtZero(true);
        y.setTextColor(Color.parseColor("#404040"));
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.parseColor("#707070"));

        chart_stats.getAxisRight().setEnabled(false);

        Legend legend = chart_stats.getLegend();
        legend.setEnabled(false);

        String[] legendLabels = getLabels();
        txt_statistics.setText(legendLabels[0]);
        txt_match.setText(legendLabels[1]);
        txt_ratio.setText(legendLabels[2]);

        chart_stats.animateXY(500, 500);
        chart_stats.invalidate();
    }

    private void drawCircleChart(){
        drawVictoryChart();
        drawDrawChart();
        drawDefeatChart();
    }

    private void drawDefeatChart(){
        defeat_chart.setUsePercentValues(true);
        defeat_chart.getDescription().setEnabled(false);
        defeat_chart.getLegend().setEnabled(false);
        defeat_chart.setDragDecelerationFrictionCoef(0.95f);
        defeat_chart.setCenterText(generateCenterSpannableText("D"));

        defeat_chart.setDrawHoleEnabled(true);
        defeat_chart.setHoleColor(Color.rgb(246, 159, 84));
        defeat_chart.setTransparentCircleColor(Color.WHITE);
        defeat_chart.setTransparentCircleAlpha(255);
        defeat_chart.setHoleRadius(40f);
        defeat_chart.setTransparentCircleRadius(92f);
        defeat_chart.setDrawCenterText(true);
        defeat_chart.setRotationAngle(-90);

        defeat_chart.animateY(200, Easing.EaseInOutQuad);

        ArrayList<PieEntry> entries = new ArrayList<>();

        float value = 0;
        value = getDefeatsRate(monthResult);
        String temp = String.format("%s %d%%", getString(R.string.txt_defeat), (int)value);
        txt_defeat.setText(temp);
        entries.add(new PieEntry((float) value, "", null));
        entries.add(new PieEntry((float) 100 - value, "", null));

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(246, 159, 84));
        colors.add(Color.WHITE);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(defeat_chart));
        data.setValueTextColor(Color.WHITE);
        defeat_chart.setData(data);

        defeat_chart.invalidate();
    }

    private void drawDrawChart(){
        draw_chart.setUsePercentValues(true);
        draw_chart.getDescription().setEnabled(false);
        draw_chart.getLegend().setEnabled(false);
        draw_chart.setDragDecelerationFrictionCoef(0.95f);
        draw_chart.setCenterText(generateCenterSpannableText("D"));

        draw_chart.setDrawHoleEnabled(true);
        draw_chart.setHoleColor(Color.rgb(152, 153, 156));
        draw_chart.setTransparentCircleColor(Color.WHITE);
        draw_chart.setTransparentCircleAlpha(255);
        draw_chart.setHoleRadius(40f);
        draw_chart.setTransparentCircleRadius(92f);
        draw_chart.setDrawCenterText(true);
        draw_chart.setRotationAngle(-90);

        draw_chart.animateY(200, Easing.EaseInOutQuad);

        ArrayList<PieEntry> entries = new ArrayList<>();

        float value = 0;
        value = getDrawsRate(monthResult);
        String temp = String.format("%s %d%%", getString(R.string.txt_draw), (int)value);
        txt_draw.setText(temp);
        entries.add(new PieEntry((float) value, "", null));
        entries.add(new PieEntry((float) 100 - value, "", null));

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(152, 153, 156));
        colors.add(Color.WHITE);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(draw_chart));
        data.setValueTextColor(Color.WHITE);
        draw_chart.setData(data);

        draw_chart.invalidate();
    }

    private void drawVictoryChart(){
        victory_chart.setUsePercentValues(true);
        victory_chart.getDescription().setEnabled(false);
        victory_chart.getLegend().setEnabled(false);
        victory_chart.setDragDecelerationFrictionCoef(0.95f);
        victory_chart.setCenterText(generateCenterSpannableText("V"));

        victory_chart.setDrawHoleEnabled(true);
        victory_chart.setHoleColor(Color.rgb(184, 220, 104));
        victory_chart.setTransparentCircleColor(Color.WHITE);
        victory_chart.setTransparentCircleAlpha(255);
        victory_chart.setHoleRadius(40f);
        victory_chart.setTransparentCircleRadius(92f);
        victory_chart.setDrawCenterText(true);
        victory_chart.setRotationAngle(-90);

        victory_chart.animateY(200, Easing.EaseInOutQuad);

        ArrayList<PieEntry> entries = new ArrayList<>();

        float value = 0;
        value = getVictoryRate(monthResult);
        String temp = String.format("%s %d%%", getString(R.string.txt_victory), (int)value);
        txt_victory.setText(temp);
        entries.add(new PieEntry((float) value, "", null));
        entries.add(new PieEntry((float) 100 - value, "", null));

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(184, 220, 104));
        colors.add(Color.WHITE);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(victory_chart));
        data.setValueTextColor(Color.WHITE);
        victory_chart.setData(data);

        victory_chart.invalidate();
    }

    private SpannableString generateCenterSpannableText(String s) {

        SpannableString ss = new SpannableString(s);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        return ss;
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
    @OnClick(R.id.btn_edit) void onClickEdit() {
        if(user.id.equals(AppData.user.id)){
            startActivity(new Intent(this, ClubResultActivity.class));
        }
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
        } else if (user.sport_id == Constants.AutoRacing || user.sport_id == Constants.MotoRacing ||
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

    private float getVictoryRate(List<List<Skill>> skill_list){
        float result = 0;
        float fVictory = 0;
        float fSum = 0;
        for (int i = 0; i < 12; i++) {
            List<Skill> skills = skill_list.get(i);
            if (user.group_id == Constants.COACH || user.group_id == Constants.TEAM_CLUB || user.group_id == Constants.STAFF) {
                fVictory += (float)getSkillValue(Constants.STATS_VICTORIES, skills);
                fSum += (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
            } else if (user.sport_id == Constants.AutoRacing || user.sport_id == Constants.MotoRacing ||
                    user.sport_id == Constants.Skiing ||user.sport_id == Constants.Snowboarding || user.sport_id == Constants.Surf ||
                    user.sport_id == Constants.IceSkating || user.sport_id == Constants.Sailing || user.sport_id == Constants.Canoeing ||
                    user.sport_id == Constants.SkateBoard){
                fVictory += (float)getSkillValue(Constants.STATS_VICTORIES, skills);
                fSum += (float)getSkillValue(Constants.STATS_RACES, skills);
            } else {
                fVictory += (float)getSkillValue(Constants.STATS_VICTORIES, skills);
                fSum += (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
            }
        }
        result = fSum == 0 ? 0 : fVictory * 100 / fSum;
        if(result > 100) result = 100;
        return  result;
    }

    private float getDefeatsRate(List<List<Skill>> skill_list){
        float result = 0;
        float fDefeat = 0;
        float fSum = 0;
        for (int i = 0; i < 12; i++){
            List<Skill> skills = skill_list.get(i);
            if (user.group_id == Constants.COACH || user.group_id == Constants.TEAM_CLUB || user.group_id == Constants.STAFF) {
                fDefeat += (float)getSkillValue(Constants.STATS_DEFEATS, skills);
                fSum += (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
            } else if (user.sport_id == Constants.AutoRacing || user.sport_id == Constants.MotoRacing ||
                    user.sport_id == Constants.Skiing ||user.sport_id == Constants.Snowboarding || user.sport_id == Constants.Surf ||
                    user.sport_id == Constants.IceSkating || user.sport_id == Constants.Sailing || user.sport_id == Constants.Canoeing ||
                    user.sport_id == Constants.SkateBoard){
                fDefeat += (float)getSkillValue(Constants.STATS_DEFEATS, skills);
                fSum += (float)getSkillValue(Constants.STATS_RACES, skills);
            } else {
                fDefeat += (float)getSkillValue(Constants.STATS_DEFEATS, skills);
                fSum += (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
            }
        }
        result = fSum == 0 ? 0 : fDefeat * 100 / fSum;
        if(result > 100) result = 100;
        return  result;
    }

    private float getDrawsRate(List<List<Skill>> skill_list){
        float result = 0;
        float fDraw = 0;
        float fSum = 0;
        for (int i = 0; i < 12; i++){
            List<Skill> skills = skill_list.get(i);
            if (user.group_id == Constants.COACH || user.group_id == Constants.TEAM_CLUB || user.group_id == Constants.STAFF) {
                fDraw += (float)getSkillValue(Constants.STATS_DRAWS, skills);
                fSum += (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
            } else if (user.sport_id == Constants.AutoRacing || user.sport_id == Constants.MotoRacing ||
                    user.sport_id == Constants.Skiing ||user.sport_id == Constants.Snowboarding || user.sport_id == Constants.Surf ||
                    user.sport_id == Constants.IceSkating || user.sport_id == Constants.Sailing || user.sport_id == Constants.Canoeing ||
                    user.sport_id == Constants.SkateBoard){
                fDraw += (float)getSkillValue(Constants.STATS_DRAWS, skills);
                fSum += (float)getSkillValue(Constants.STATS_RACES, skills);
            } else {
                fDraw += (float)getSkillValue(Constants.STATS_DRAWS, skills);
                fSum += (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
            }
        }
        result = fSum == 0 ? 0 : fDraw * 100 / fSum;
        if(result > 100) result = 100;
        return  result;
    }
}
