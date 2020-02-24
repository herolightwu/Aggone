package com.odelan.yang.aggone.Adapter.Stats;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.odelan.yang.aggone.Model.Skill;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClubChartAdapter extends RecyclerView.Adapter<ClubChartAdapter.ViewHolder> {
    Context context;
    List<Pair<String, List<List<Pair<String, Integer>>>>> stats;
    EventListener listener;
    public User user;

    public ClubChartAdapter(Context context, List<Pair<String, List<List<Pair<String, Integer>>>>> data, EventListener listener) {
        this.context = context;
        this.stats = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_club_chart, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Pair<String, List<List<Pair<String, Integer>>>> one = stats.get(position);
        viewHolder.txt_year.setText(one.first);

        setCubeChart(viewHolder, one.second);
        viewHolder.btn_advanced_stats.setOnClickListener(v -> {
            if (listener != null) listener.onClickItem(position);
        });
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    void setCubeChart(ViewHolder viewHolder, List<List<Pair<String, Integer>>> month_data) {
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

    public void setDataList(List<Pair<String, List<List<Pair<String, Integer>>>>> data){
        this.stats.clear();
        this.stats.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView    txt_year;
        LineChart   chart_cube;
        Button      btn_advanced_stats;
        TextView    txt_statistics;
        TextView    txt_match;
        TextView    txt_ratio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_year              =     itemView.findViewById(R.id.txt_year);
            chart_cube            =     itemView.findViewById(R.id.chart_cube);
            btn_advanced_stats    =     itemView.findViewById(R.id.btn_advanced_stats);
            txt_statistics        =     itemView.findViewById(R.id.txt_statistics);
            txt_match             =     itemView.findViewById(R.id.txt_match);
            txt_ratio             =     itemView.findViewById(R.id.txt_ratio);
        }
    }

    public interface EventListener {
        void onClickItem(int index);
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
}
