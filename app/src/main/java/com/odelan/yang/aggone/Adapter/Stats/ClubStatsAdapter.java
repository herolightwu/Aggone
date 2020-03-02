package com.odelan.yang.aggone.Adapter.Stats;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.evrencoskun.tableview.TableView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.odelan.yang.aggone.Activity.Stats.ClubDetailActivity;
import com.odelan.yang.aggone.Model.Cell;
import com.odelan.yang.aggone.Model.ClubStat;
import com.odelan.yang.aggone.Model.Skill;
import com.odelan.yang.aggone.Model.Summary;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.Constants;
import com.odelan.yang.aggone.Utils.PercentFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClubStatsAdapter extends RecyclerView.Adapter<ClubStatsAdapter.ViewHolder> {
    Context context;
    List<ClubStat> stats = new ArrayList<>();
    EventListener listener;
    public User user;

    public ClubStatsAdapter(Context context, List<ClubStat> data, EventListener listener) {
        this.context = context;
        this.stats = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_stat_club, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        ClubStat one = stats.get(position);
        viewHolder.txt_clubname.setText(one.name);
        setCubeChart(viewHolder, one.stats);
        drawDataTable(viewHolder, one.years_stat);
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    public void setDataList(List<ClubStat> data){
        this.stats.clear();
        this.stats.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_clubname;
        LineChart chart_cube;
        TextView    txt_statistics;
        TextView    txt_match;
        TextView    txt_ratio;
        PieChart victory_chart;
        PieChart draw_chart;
        PieChart defeat_chart;
        TextView txt_victory;
        TextView txt_draw;
        TextView txt_defeat;
        TableView table_view;

        List<String> mRowData = new ArrayList<>();
        List<String> mColumnData = new ArrayList<>();
        List<List<Cell>> mCellData = new ArrayList<>();
        ClubAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_clubname          =     itemView.findViewById(R.id.txt_year);
            chart_cube            =     itemView.findViewById(R.id.chart_stats);
            txt_statistics        =     itemView.findViewById(R.id.txt_statistics);
            txt_match             =     itemView.findViewById(R.id.txt_match);
            txt_ratio             =     itemView.findViewById(R.id.txt_ratio);
            victory_chart             =     itemView.findViewById(R.id.chart_victory);
            draw_chart             =     itemView.findViewById(R.id.chart_draw);
            defeat_chart             =     itemView.findViewById(R.id.chart_defeat);
            txt_draw             =     itemView.findViewById(R.id.txt_draw);
            txt_defeat             =     itemView.findViewById(R.id.txt_defeat);
            txt_victory             =     itemView.findViewById(R.id.txt_victory);
            table_view             =     itemView.findViewById(R.id.table_view);
            adapter = new ClubAdapter(context);
            table_view.setAdapter(adapter);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
    }

    private void drawDataTable(ViewHolder vh, List<Pair<String, List<Pair<String, Integer>>>> years_data){
        vh.mColumnData.clear();
        vh.mRowData.clear();
        vh.mCellData.clear();
        List<String> sum = new ArrayList<>();
        List<Skill> skills = getSportSkills(Constants.STATS_COACH, new ArrayList<>());
        for (Skill skill : skills) {
            vh.mColumnData.add(skill.description);//skill.summary
            sum.add("0");
        }

        int row = 0, column;
        for (Pair<String, List<Pair<String, Integer>>> yr_data : years_data) {
            vh.mRowData.add(yr_data.first);
            skills = getSportSkills(Constants.STATS_COACH, yr_data.second);
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
                    if (row == years_data.size() - 1) {
                        sum.set(column, String.format("%.1f", Float.valueOf(sum.get(column)) / years_data.size()));
                    }
                }
                rowCell.add(cell);
                column++;
            }
            vh.mCellData.add(rowCell);
            row++;
        }
        column = 0;
        List<Cell> rowCell = new ArrayList<>();
        for (String one : sum) {
            Cell cell = new Cell(-1, column, one);
            rowCell.add(cell);
            column++;
        }
        vh.mCellData.add(rowCell);
        if(years_data.size() > 0){
            vh.mRowData.add(context.getString(R.string.total));
            ViewGroup.LayoutParams params = vh.table_view.getLayoutParams();
            params.height = convertDpToPixelInt(years_data.size() * 40 + 90, context);
            vh.table_view.setLayoutParams(params);
            vh.table_view.setVisibility(View.VISIBLE);
        } else{
            vh.table_view.setVisibility(View.GONE);
        }
        vh.adapter.setAllItems(vh.mColumnData, vh.mRowData, vh.mCellData);
    }

    private void setCubeChart(ViewHolder viewHolder, List<List<Pair<String, Integer>>> month_data) {
        viewHolder.chart_cube.getDescription().setEnabled(false);
        viewHolder.chart_cube.setTouchEnabled(false);
        viewHolder.chart_cube.setDragEnabled(false);
        viewHolder.chart_cube.setScaleEnabled(false);
        viewHolder.chart_cube.setPinchZoom(false);
        viewHolder.chart_cube.setDrawGridBackground(false);

        setStatisticsData(viewHolder,month_data);

        XAxis x = viewHolder.chart_cube.getXAxis();
        x.setLabelCount(12, true);
        final String[] labels = new String[12];
        Arrays.asList(context.getResources().getStringArray(R.array.months_array)).toArray(labels);
        x.setValueFormatter((value, axis) -> labels[(int) value % labels.length]);
        x.setTextColor(Color.parseColor("#404040"));
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawGridLines(false);
        x.setAxisLineColor(Color.parseColor("#707070"));

        YAxis y = viewHolder.chart_cube.getAxisLeft();
        y.setLabelCount(10, false);
        y.setStartAtZero(true);
        y.setTextColor(Color.parseColor("#404040"));
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.parseColor("#707070"));

        viewHolder.chart_cube.getAxisRight().setEnabled(false);

        Legend legend = viewHolder.chart_cube.getLegend();
        legend.setEnabled(false);

        String[] legendLabels = new String[] {
                context.getResources().getString(R.string.statistics),
                context.getResources().getString(R.string.match_win_percent),
                context.getResources().getString(R.string.ratio),
        };
        viewHolder.txt_statistics.setText(legendLabels[0]);
        viewHolder.txt_match.setText(legendLabels[1]);
        viewHolder.txt_ratio.setText(legendLabels[2]);

        viewHolder.chart_cube.animateXY(500, 500);
        viewHolder.chart_cube.invalidate();
    }

    private void setStatisticsData(ViewHolder vh, List<List<Pair<String, Integer>>> months) {
        List<List<Skill>> monthResult = new ArrayList<>();
        monthResult.clear();
        for (List<Pair<String, Integer>> one : months) {
            monthResult.add(getSportSkills(Constants.STATS_COACH, one));
        }
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

        set1 = new LineDataSet(values1, context.getResources().getString(R.string.statistics));
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);
        set1.setLineWidth(0.0f);
        set1.setColor(Color.parseColor("#F7A055"));
        set1.setFillColor(Color.parseColor("#F7A055"));
        set1.setFillAlpha(150);

        set2 = new LineDataSet(values2, context.getResources().getString(R.string.match));
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setCubicIntensity(0.2f);
        set2.setDrawFilled(true);
        set2.setDrawCircles(false);
        set2.setLineWidth(0.0f);
        set2.setColor(Color.parseColor("#F76055"));
        set2.setFillColor(Color.parseColor("#F76055"));
        set2.setFillAlpha(150);

        set3 = new LineDataSet(values3, context.getResources().getString(R.string.ratio));
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
        vh.chart_cube.setData(data);

        drawCircleChart(vh, monthResult);
    }

    private float getStatistics_New(List<Skill> skills) {
        float result = 0;
        int i = 0;
        List<String> stat_keys = new ArrayList<>();
        if (user.group_id == Constants.COACH || user.group_id == Constants.TEAM_CLUB || user.group_id == Constants.STAFF) {
            stat_keys = Arrays.asList(context.getResources().getStringArray(R.array.stat_coach_key));
            result =getSkillValue(stat_keys.get(1), skills);//victories
        }
        return  result;
    }

    private float getMatch_New(List<Skill> skills) {
        float result = 0;
        if (user.group_id == Constants.COACH || user.group_id == Constants.TEAM_CLUB || user.group_id == Constants.STAFF) {
            result = getSkillValue(Constants.STATS_GAMES_PLAYED, skills) == 0 ? 0 :
                    (float)getSkillValue(Constants.STATS_VICTORIES, skills) / (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
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
        } else {
            result = getSkillValue(Constants.STATS_GAMES_PLAYED, skills) == 0 ? 0 :
                    getStatistics_New(skills) / getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
        }
        return  result;
    }

    private int getSkillValue(String key, List<Skill> skills) {
        for (Skill one : skills) {
            if (one.key.equals(key)) return one.value;
        }
        return 0;
    }

    private List<Skill> getSportSkills(int sport) {
        List<Skill> skills = getAllStats();
        List<Skill> result = new ArrayList<>();
        for (Skill skill : skills) {
            if (skill.sport == sport) {
                result.add(skill);
            }
        }
        return result;
    }

    private List<Skill> getSportSkills(int sport, List<Pair<String, Integer>> apiResult) {
        List<Skill> result = getSportSkills(sport);
        for (Skill skill : result) {
            for (Pair<String, Integer> one : apiResult) {
                if (one.first.equals(skill.key)) {
                    skill.value += Integer.valueOf(one.second);
                }
            }
        }
        return result;
    }
    public List<Skill> getAllStats() {
        List<Skill> result = new ArrayList<>();

        /** coach */
        List<String> stat_keys = Arrays.asList(context.getResources().getStringArray(R.array.stat_coach_key));
        List<String> stat_names = Arrays.asList(context.getResources().getStringArray(R.array.stat_coach));
        List<String> stat_names_s = Arrays.asList(context.getResources().getStringArray(R.array.stat_coach_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.STATS_COACH, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        return result;
    }

    private void drawCircleChart(ViewHolder vh, List<List<Skill>> mResult){
        drawVictoryChart(vh, mResult);
        drawDrawChart(vh, mResult);
        drawDefeatChart(vh, mResult);
    }

    private void drawDefeatChart(ViewHolder vh, List<List<Skill>> monthResult){
        vh.defeat_chart.setUsePercentValues(true);
        vh.defeat_chart.getDescription().setEnabled(false);
        vh.defeat_chart.getLegend().setEnabled(false);
        vh.defeat_chart.setDragDecelerationFrictionCoef(0.95f);
        vh.defeat_chart.setCenterText(generateCenterSpannableText("D"));

        vh.defeat_chart.setDrawHoleEnabled(true);
        vh.defeat_chart.setHoleColor(Color.rgb(246, 159, 84));
        vh.defeat_chart.setTransparentCircleColor(Color.WHITE);
        vh.defeat_chart.setTransparentCircleAlpha(255);
        vh.defeat_chart.setHoleRadius(40f);
        vh.defeat_chart.setTransparentCircleRadius(92f);
        vh.defeat_chart.setDrawCenterText(true);
        vh.defeat_chart.setRotationAngle(-90);

        vh.defeat_chart.animateY(200, Easing.EaseInOutQuad);

        ArrayList<PieEntry> entries = new ArrayList<>();

        float value = 0;
        value = getDefeatsRate(monthResult);
        String temp = String.format("%s %d%%", context.getResources().getString(R.string.txt_defeat), (int)value);
        vh.txt_defeat.setText(temp);
        entries.add(new PieEntry((float) value, "", null));
        entries.add(new PieEntry((float) 100 - value, "", null));

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(246, 159, 84));
        colors.add(Color.WHITE);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(vh.defeat_chart));
        data.setValueTextColor(Color.WHITE);
        vh.defeat_chart.setData(data);

        vh.defeat_chart.invalidate();
    }

    private void drawDrawChart(ViewHolder vh, List<List<Skill>> monthResult){
        vh.draw_chart.setUsePercentValues(true);
        vh.draw_chart.getDescription().setEnabled(false);
        vh.draw_chart.getLegend().setEnabled(false);
        vh.draw_chart.setDragDecelerationFrictionCoef(0.95f);
        vh.draw_chart.setCenterText(generateCenterSpannableText("D"));

        vh.draw_chart.setDrawHoleEnabled(true);
        vh.draw_chart.setHoleColor(Color.rgb(152, 153, 156));
        vh.draw_chart.setTransparentCircleColor(Color.WHITE);
        vh.draw_chart.setTransparentCircleAlpha(255);
        vh.draw_chart.setHoleRadius(40f);
        vh.draw_chart.setTransparentCircleRadius(92f);
        vh.draw_chart.setDrawCenterText(true);
        vh.draw_chart.setRotationAngle(-90);

        vh.draw_chart.animateY(200, Easing.EaseInOutQuad);

        ArrayList<PieEntry> entries = new ArrayList<>();

        float value = 0;
        value = getDrawsRate(monthResult);
        String temp = String.format("%s %d%%", context.getResources().getString(R.string.txt_draw), (int)value);
        vh.txt_draw.setText(temp);
        entries.add(new PieEntry((float) value, "", null));
        entries.add(new PieEntry((float) 100 - value, "", null));

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(152, 153, 156));
        colors.add(Color.WHITE);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(vh.draw_chart));
        data.setValueTextColor(Color.WHITE);
        vh.draw_chart.setData(data);

        vh.draw_chart.invalidate();
    }

    private void drawVictoryChart(ViewHolder vh, List<List<Skill>> monthResult){
        vh.victory_chart.setUsePercentValues(true);
        vh.victory_chart.getDescription().setEnabled(false);
        vh.victory_chart.getLegend().setEnabled(false);
        vh.victory_chart.setDragDecelerationFrictionCoef(0.95f);
        vh.victory_chart.setCenterText(generateCenterSpannableText("V"));

        vh.victory_chart.setDrawHoleEnabled(true);
        vh.victory_chart.setHoleColor(Color.rgb(184, 220, 104));
        vh.victory_chart.setTransparentCircleColor(Color.WHITE);
        vh.victory_chart.setTransparentCircleAlpha(255);
        vh.victory_chart.setHoleRadius(40f);
        vh.victory_chart.setTransparentCircleRadius(92f);
        vh.victory_chart.setDrawCenterText(true);
        vh.victory_chart.setRotationAngle(-90);

        vh.victory_chart.animateY(200, Easing.EaseInOutQuad);

        ArrayList<PieEntry> entries = new ArrayList<>();

        float value = 0;
        value = getVictoryRate(monthResult);
        String temp = String.format("%s %d%%", context.getResources().getString(R.string.txt_victory), (int)value);
        vh.txt_victory.setText(temp);
        entries.add(new PieEntry((float) value, "", null));
        entries.add(new PieEntry((float) 100 - value, "", null));

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(184, 220, 104));
        colors.add(Color.WHITE);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(vh.victory_chart));
        data.setValueTextColor(Color.WHITE);
        vh.victory_chart.setData(data);

        vh.victory_chart.invalidate();
    }

    private SpannableString generateCenterSpannableText(String s) {

        SpannableString ss = new SpannableString(s);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        return ss;
    }

    private float getVictoryRate(List<List<Skill>> skill_list){
        float result = 0;
        float fVictory = 0;
        float fSum = 0;
        for (int i = 0; i < 12; i++) {
            List<Skill> skills = skill_list.get(i);
            fVictory += (float)getSkillValue(Constants.STATS_VICTORIES, skills);
            fSum += (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
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
            fDefeat += (float)getSkillValue(Constants.STATS_DEFEATS, skills);
            fSum += (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
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
            fDraw += (float)getSkillValue(Constants.STATS_DRAWS, skills);
            fSum += (float)getSkillValue(Constants.STATS_GAMES_PLAYED, skills);
        }
        result = fSum == 0 ? 0 : fDraw * 100 / fSum;
        if(result > 100) result = 100;
        return  result;
    }

    private int convertDpToPixelInt(float dp, Context context) {
        return (int) (dp * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f));
    }
}
