package com.odelan.yang.aggone.Model;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Summary {
    public String club_name;
    public List<Pair<String, Integer>> stats = new ArrayList<>();
    public List<Pair<String, List<String>>> years = new ArrayList<>();
}
