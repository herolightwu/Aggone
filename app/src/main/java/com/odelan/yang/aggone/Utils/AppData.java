package com.odelan.yang.aggone.Utils;

import com.odelan.yang.aggone.Model.Story;
import com.odelan.yang.aggone.Model.User;

import java.util.HashMap;
import java.util.Map;

public class AppData {
    public static User user = new User();
    public static Map<Integer, Story> last_stories = new HashMap<>();
}
