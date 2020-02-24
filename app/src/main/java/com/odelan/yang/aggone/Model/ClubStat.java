package com.odelan.yang.aggone.Model;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ClubStat {
    public String name = "";
    public List<List<Pair<String, Integer>>> stats = new ArrayList<>();
    public List<Pair<String, List<Pair<String, Integer>>>> years_stat = new ArrayList<>();
}
