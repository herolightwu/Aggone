package com.odelan.yang.aggone.Model;

public class Skill {
    public int sport;
    public String key;
    public String description;
    public String summary;
    public int value = 0;
    public String string_value;

    public Skill(int sport, String key, String description, String summary) {
        this.sport = sport;
        this.key = key;
        this.description = description;
        this.summary = summary;
        this.value = 0;
    }
}
