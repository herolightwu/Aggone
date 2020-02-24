package com.odelan.yang.aggone.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 3/5/2018.
 */

public class Result implements Serializable {
    public String id;
    public int value_type; /*** 0 - integer, 1 - float, 2 - string */
    public String user_id;
    public String club;
    public int sport_id;
    public String type;
    public int value;
    public int year;
    public int month;
}

