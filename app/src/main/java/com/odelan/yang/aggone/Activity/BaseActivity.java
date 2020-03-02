package com.odelan.yang.aggone.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.yang.aggone.Model.Description;
import com.odelan.yang.aggone.Model.Skill;
import com.odelan.yang.aggone.Model.Sport;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    private KProgressHUD hud;

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showProgress() {
        showProgress("Please wait");
    }

    public void showProgress(String message) {
        hud = KProgressHUD.create(this).setLabel(message);
        hud.show();
    }

    public void dismissProgress() {
        if (hud != null) {
            hud.dismiss();
        }
    }

    public String typicalCount(int i){
        String sss = NumberFormat.getIntegerInstance().format(i);
        return sss;
    }

    public String prettyCount(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.00").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat().format(numValue);
        }
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean isValidPhone(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches());
    }

    public boolean isValidUrl(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.WEB_URL.matcher(target).matches());
    }

    public int convertDpToPixelInt(float dp, Context context) {
        return (int) (dp * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f));
    }

    public List<Sport> getAllSports() {
        List<Sport> sports = new ArrayList<>();
        sports.add(new Sport(Constants.Football, getString(R.string.football), R.mipmap.icon_football));
        sports.add(new Sport(Constants.Basketball, getString(R.string.basketball), R.mipmap.icon_basketball));
        sports.add(new Sport(Constants.American_Football, getString(R.string.american_football), R.mipmap.icon_american_football));
        sports.add(new Sport(Constants.Cricket, getString(R.string.cricket), R.mipmap.icon_cricket));
        sports.add(new Sport(Constants.Tennis, getString(R.string.tennis), R.mipmap.icon_tennis));
        sports.add(new Sport(Constants.Rugby, getString(R.string.rugby), R.mipmap.icon_rugby));
        sports.add(new Sport(Constants.Golf, getString(R.string.golf), R.mipmap.icon_golf));
        sports.add(new Sport(Constants.BaseBall, getString(R.string.baseball), R.mipmap.icon_baseball));
        sports.add(new Sport(Constants.Ice_Hockey, getString(R.string.ice_hockey), R.mipmap.icon_ice_hockey));
        sports.add(new Sport(Constants.Field_Hockey, getString(R.string.field_hockey), R.mipmap.icon_field_hockey));
        sports.add(new Sport(Constants.VolleyBall, getString(R.string.volleyball), R.mipmap.icon_volleyball));
        sports.add(new Sport(Constants.Handball, getString(R.string.handball), R.mipmap.icon_handball));
        sports.add(new Sport(Constants.Badminton, getString(R.string.badminton), R.mipmap.icon_badminton));
        sports.add(new Sport(Constants.Squash, getString(R.string.squash), R.mipmap.icon_squash));
        sports.add(new Sport(Constants.TableTennis, getString(R.string.table_tennis), R.mipmap.icon_table_tennis));
        sports.add(new Sport(Constants.Boxing, getString(R.string.boxing), R.mipmap.icon_boxing));
        sports.add(new Sport(Constants.MartialArt, getString(R.string.martial_art), R.mipmap.icon_material_art));
        sports.add(new Sport(Constants.MMA, getString(R.string.mma), R.mipmap.icon_mma));
        sports.add(new Sport(Constants.KickBoxing, getString(R.string.kick_boxing), R.mipmap.icon_kick_boxing));
        sports.add(new Sport(Constants.Wrestling, getString(R.string.wrestling), R.mipmap.icon_wrestling));
        sports.add(new Sport(Constants.Gymnastic, getString(R.string.gymnastic), R.mipmap.icon_gymnastic));
        sports.add(new Sport(Constants.Athletics, getString(R.string.athletics), R.mipmap.icon_athletics));
        sports.add(new Sport(Constants.Fencing, getString(R.string.fencing), R.mipmap.icon_fencing));
        sports.add(new Sport(Constants.Judo, getString(R.string.judo), R.mipmap.icon_judo));
        sports.add(new Sport(Constants.Swimming, getString(R.string.swimming), R.mipmap.icon_swimming));
        sports.add(new Sport(Constants.WaterSports, getString(R.string.water_sports), R.mipmap.icon_water_sports));
        sports.add(new Sport(Constants.WaterPolo, getString(R.string.water_polo), R.mipmap.icon_water_polo));
        sports.add(new Sport(Constants.AutoRacing, getString(R.string.auto_racing), R.mipmap.icon_autoracing));
        sports.add(new Sport(Constants.MotoRacing, getString(R.string.moto_racing), R.mipmap.icon_motoracing));
        sports.add(new Sport(Constants.Cycling, getString(R.string.cycling), R.mipmap.icon_cycling));
        sports.add(new Sport(Constants.Equestrianism, getString(R.string.equestrianism), R.mipmap.icon_equestrianism));
        sports.add(new Sport(Constants.PoloSport, getString(R.string.polo_sport), R.mipmap.icon_polo_sport));
        sports.add(new Sport(Constants.Dance, getString(R.string.dance), R.mipmap.icon_dance));
        sports.add(new Sport(Constants.Softball, getString(R.string.softball), R.mipmap.icon_softball));
        sports.add(new Sport(Constants.SepakTakraw, getString(R.string.sepak_takraw), R.mipmap.icon_sepak_takraw));
        sports.add(new Sport(Constants.Korfball, getString(R.string.korfball), R.mipmap.icon_korfball));
        sports.add(new Sport(Constants.Floorball, getString(R.string.floorball), R.mipmap.icon_floorball));
        sports.add(new Sport(Constants.Climbing, getString(R.string.climbing), R.mipmap.icon_climbing));
        sports.add(new Sport(Constants.Mountaineering, getString(R.string.mountaineering), R.mipmap.icon_mountaineering));
        sports.add(new Sport(Constants.Canyoning, getString(R.string.canyoning), R.mipmap.icon_canyoning));
        sports.add(new Sport(Constants.Trail, getString(R.string.trail), R.mipmap.icon_trail));
        sports.add(new Sport(Constants.Triathlon, getString(R.string.triathlon), R.mipmap.icon_triathlon));
        sports.add(new Sport(Constants.Archery, getString(R.string.archery), R.mipmap.icon_archery));
        sports.add(new Sport(Constants.Snowboarding, getString(R.string.snowboarding), R.mipmap.icon_snowboarding));
        sports.add(new Sport(Constants.Skiing, getString(R.string.skiing), R.mipmap.icon_skiing));
        sports.add(new Sport(Constants.IceSkating, getString(R.string.ice_skating), R.mipmap.icon_ice_skating));
        sports.add(new Sport(Constants.Petanque, getString(R.string.petanque), R.mipmap.icon_petanque));
        sports.add(new Sport(Constants.SkateBoard, getString(R.string.skate_board), R.mipmap.icon_skate_board));
        sports.add(new Sport(Constants.Weightlifting, getString(R.string.weightlifting), R.mipmap.icon_weightlifting));
        sports.add(new Sport(Constants.Crossfit_Fitness, getString(R.string.crossfit_fitness), R.mipmap.icon_crossfit_fitness));
        sports.add(new Sport(Constants.Bowling, getString(R.string.bowling), R.mipmap.icon_bowling));
        sports.add(new Sport(Constants.Surf, getString(R.string.surf), R.mipmap.icon_surf));
        sports.add(new Sport(Constants.Sailing, getString(R.string.sailing), R.mipmap.icon_sailing));
        sports.add(new Sport(Constants.Canoeing, getString(R.string.canoeing), R.mipmap.icon_canoeing));
        sports.add(new Sport(Constants.Rowing, getString(R.string.rowing), R.mipmap.icon_rowing));
        sports.add(new Sport(Constants.Air_Sports, getString(R.string.air_sports), R.mipmap.icon_air_sports));
        sports.add(new Sport(Constants.WinterSports, getString(R.string.winter_sports), R.mipmap.icon_winter_sports));
        return sports;
    }

    public List<Sport> getAllSportsWithEdge() {
        List<Sport> sports = new ArrayList<>();
        sports.add(new Sport(-1, "", -1));
        sports.add(new Sport(Constants.Football, getString(R.string.football), R.mipmap.icon_football));
        sports.add(new Sport(Constants.Basketball, getString(R.string.basketball), R.mipmap.icon_basketball));
        sports.add(new Sport(Constants.American_Football, getString(R.string.american_football), R.mipmap.icon_american_football));
        sports.add(new Sport(Constants.Cricket, getString(R.string.cricket), R.mipmap.icon_cricket));
        sports.add(new Sport(Constants.Tennis, getString(R.string.tennis), R.mipmap.icon_tennis));
        sports.add(new Sport(Constants.Rugby, getString(R.string.rugby), R.mipmap.icon_rugby));
        sports.add(new Sport(Constants.Golf, getString(R.string.golf), R.mipmap.icon_golf));
        sports.add(new Sport(Constants.Basketball, getString(R.string.baseball), R.mipmap.icon_baseball));
        sports.add(new Sport(Constants.Ice_Hockey, getString(R.string.ice_hockey), R.mipmap.icon_ice_hockey));
        sports.add(new Sport(Constants.Field_Hockey, getString(R.string.field_hockey), R.mipmap.icon_field_hockey));
        sports.add(new Sport(Constants.VolleyBall, getString(R.string.volleyball), R.mipmap.icon_volleyball));
        sports.add(new Sport(Constants.Handball, getString(R.string.handball), R.mipmap.icon_handball));
        sports.add(new Sport(Constants.Badminton, getString(R.string.badminton), R.mipmap.icon_badminton));
        sports.add(new Sport(Constants.Squash, getString(R.string.squash), R.mipmap.icon_squash));
        sports.add(new Sport(Constants.TableTennis, getString(R.string.table_tennis), R.mipmap.icon_table_tennis));
        sports.add(new Sport(Constants.Boxing, getString(R.string.boxing), R.mipmap.icon_boxing));
        sports.add(new Sport(Constants.MartialArt, getString(R.string.martial_art), R.mipmap.icon_material_art));
        sports.add(new Sport(Constants.MMA, getString(R.string.mma), R.mipmap.icon_mma));
        sports.add(new Sport(Constants.KickBoxing, getString(R.string.kick_boxing), R.mipmap.icon_kick_boxing));
        sports.add(new Sport(Constants.Wrestling, getString(R.string.wrestling), R.mipmap.icon_wrestling));
        sports.add(new Sport(Constants.Gymnastic, getString(R.string.gymnastic), R.mipmap.icon_gymnastic));
        sports.add(new Sport(Constants.Athletics, getString(R.string.athletics), R.mipmap.icon_athletics));
        sports.add(new Sport(Constants.Fencing, getString(R.string.fencing), R.mipmap.icon_fencing));
        sports.add(new Sport(Constants.Judo, getString(R.string.judo), R.mipmap.icon_judo));
        sports.add(new Sport(Constants.Swimming, getString(R.string.swimming), R.mipmap.icon_swimming));
        sports.add(new Sport(Constants.WaterSports, getString(R.string.water_sports), R.mipmap.icon_water_sports));
        sports.add(new Sport(Constants.WaterPolo, getString(R.string.water_polo), R.mipmap.icon_water_polo));
        sports.add(new Sport(Constants.AutoRacing, getString(R.string.auto_racing), R.mipmap.icon_autoracing));
        sports.add(new Sport(Constants.MotoRacing, getString(R.string.moto_racing), R.mipmap.icon_motoracing));
        sports.add(new Sport(Constants.Cycling, getString(R.string.cycling), R.mipmap.icon_cycling));
        sports.add(new Sport(Constants.Equestrianism, getString(R.string.equestrianism), R.mipmap.icon_equestrianism));
        sports.add(new Sport(Constants.PoloSport, getString(R.string.polo_sport), R.mipmap.icon_polo_sport));
        sports.add(new Sport(Constants.Dance, getString(R.string.dance), R.mipmap.icon_dance));
        sports.add(new Sport(Constants.Softball, getString(R.string.softball), R.mipmap.icon_softball));
        sports.add(new Sport(Constants.SepakTakraw, getString(R.string.sepak_takraw), R.mipmap.icon_sepak_takraw));
        sports.add(new Sport(Constants.Korfball, getString(R.string.korfball), R.mipmap.icon_korfball));
        sports.add(new Sport(Constants.Floorball, getString(R.string.floorball), R.mipmap.icon_floorball));
        sports.add(new Sport(Constants.Climbing, getString(R.string.climbing), R.mipmap.icon_climbing));
        sports.add(new Sport(Constants.Mountaineering, getString(R.string.mountaineering), R.mipmap.icon_mountaineering));
        sports.add(new Sport(Constants.Canyoning, getString(R.string.canyoning), R.mipmap.icon_canyoning));
        sports.add(new Sport(Constants.Trail, getString(R.string.trail), R.mipmap.icon_trail));
        sports.add(new Sport(Constants.Triathlon, getString(R.string.triathlon), R.mipmap.icon_triathlon));
        sports.add(new Sport(Constants.Archery, getString(R.string.archery), R.mipmap.icon_archery));
        sports.add(new Sport(Constants.Snowboarding, getString(R.string.snowboarding), R.mipmap.icon_snowboarding));
        sports.add(new Sport(Constants.Skiing, getString(R.string.skiing), R.mipmap.icon_skiing));
        sports.add(new Sport(Constants.IceSkating, getString(R.string.ice_skating), R.mipmap.icon_ice_skating));
        sports.add(new Sport(Constants.Petanque, getString(R.string.petanque), R.mipmap.icon_petanque));
        sports.add(new Sport(Constants.SkateBoard, getString(R.string.skate_board), R.mipmap.icon_skate_board));
        sports.add(new Sport(Constants.Weightlifting, getString(R.string.weightlifting), R.mipmap.icon_weightlifting));
        sports.add(new Sport(Constants.Crossfit_Fitness, getString(R.string.crossfit_fitness), R.mipmap.icon_crossfit_fitness));
        sports.add(new Sport(Constants.Bowling, getString(R.string.bowling), R.mipmap.icon_bowling));
        sports.add(new Sport(Constants.Surf, getString(R.string.surf), R.mipmap.icon_surf));
        sports.add(new Sport(Constants.Sailing, getString(R.string.sailing), R.mipmap.icon_sailing));
        sports.add(new Sport(Constants.Canoeing, getString(R.string.canoeing), R.mipmap.icon_canoeing));
        sports.add(new Sport(Constants.Rowing, getString(R.string.rowing), R.mipmap.icon_rowing));
        sports.add(new Sport(Constants.Air_Sports, getString(R.string.air_sports), R.mipmap.icon_air_sports));
        sports.add(new Sport(Constants.WinterSports, getString(R.string.winter_sports), R.mipmap.icon_winter_sports));
        sports.add(new Sport(-1, "", -1));
        return sports;
    }

    public String getSportName(int id) {
        switch (id) {
            case Constants.Football:
                return getString(R.string.football);
            case Constants.Basketball:
                return getString(R.string.basketball);
            case Constants.American_Football:
                return getString(R.string.american_football);
            case Constants.Cricket:
                return getString(R.string.cricket);
            case Constants.Tennis:
                return getString(R.string.tennis);
            case Constants.Rugby:
                return getString(R.string.rugby);
            case Constants.Golf:
                return getString(R.string.golf);
            case Constants.BaseBall:
                return getString(R.string.baseball);
            case Constants.Ice_Hockey:
                return getString(R.string.ice_hockey);
            case Constants.Field_Hockey:
                return getString(R.string.field_hockey);
            case Constants.VolleyBall:
                return getString(R.string.volleyball);
            case Constants.Handball:
                return getString(R.string.handball);
            case Constants.Badminton:
                return getString(R.string.badminton);
            case Constants.Squash:
                return getString(R.string.squash);
            case Constants.TableTennis:
                return getString(R.string.table_tennis);
            case Constants.Boxing:
                return getString(R.string.boxing);
            case Constants.MartialArt:
                return getString(R.string.martial_art);
            case Constants.MMA:
                return getString(R.string.mma);
            case Constants.KickBoxing:
                return getString(R.string.kick_boxing);
            case Constants.Wrestling:
                return getString(R.string.wrestling);
            case Constants.Gymnastic:
                return getString(R.string.gymnastic);
            case Constants.Athletics:
                return getString(R.string.athletics);
            case Constants.Fencing:
                return getString(R.string.fencing);
            case Constants.Judo:
                return getString(R.string.judo);
            case Constants.Swimming:
                return getString(R.string.swimming);
            case Constants.WaterSports:
                return getString(R.string.water_sports);
            case Constants.WaterPolo:
                return getString(R.string.water_polo);
            case Constants.AutoRacing:
                return getString(R.string.auto_racing);
            case Constants.MotoRacing:
                return getString(R.string.moto_racing);
            case Constants.Cycling:
                return getString(R.string.cycling);
            case Constants.Equestrianism:
                return getString(R.string.equestrianism);
            case Constants.PoloSport:
                return getString(R.string.polo_sport);
            case Constants.Dance:
                return getString(R.string.dance);
            case Constants.Softball:
                return getString(R.string.softball);
            case Constants.SepakTakraw:
                return getString(R.string.sepak_takraw);
            case Constants.Korfball:
                return getString(R.string.korfball);
            case Constants.Floorball:
                return getString(R.string.floorball);
            case Constants.Climbing:
                return getString(R.string.climbing);
            case Constants.Mountaineering:
                return getString(R.string.mountaineering);
            case Constants.Canyoning:
                return getString(R.string.canyoning);
            case Constants.Trail:
                return getString(R.string.trail);
            case Constants.Triathlon:
                return getString(R.string.triathlon);
            case Constants.Archery:
                return getString(R.string.archery);
            case Constants.Snowboarding:
                return getString(R.string.snowboarding);
            case Constants.Skiing:
                return getString(R.string.skiing);
            case Constants.IceSkating:
                return getString(R.string.ice_skating);
            case Constants.Petanque:
                return getString(R.string.petanque);
            case Constants.SkateBoard:
                return getString(R.string.skate_board);
            case Constants.Weightlifting:
                return getString(R.string.weightlifting);
            case Constants.Crossfit_Fitness:
                return getString(R.string.crossfit_fitness);
            case Constants.Surf:
                return getString(R.string.surf);
            case Constants.Sailing:
                return getString(R.string.sailing);
            case Constants.Canoeing:
                return getString(R.string.canoeing);
            case Constants.Rowing:
                return getString(R.string.rowing);
            case Constants.Air_Sports:
                return getString(R.string.air_sports);
            case Constants.WinterSports:
                return getString(R.string.winter_sports);
            case Constants.Bowling:
                return getString(R.string.bowling);
            default:
                return "";
        }
    }

    public int getSkillValue(String key, List<Skill> skills) {
        for (Skill one : skills) {
            if (one.key.equals(key)) return one.value;
        }
        return 0;
    }

    public List<Skill> getSportSkills(int sport) {
        List<Skill> skills = getAllStats();
        List<Skill> result = new ArrayList<>();
        for (Skill skill : skills) {
            if (skill.sport == sport) {
                result.add(skill);
            }
        }
        return result;
    }

    public List<Skill> getSportSkills(int sport, List<Pair<String, Integer>> apiResult) {
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

    public static String getDescriptionName(Context context, int id) {
        if (id < 24) {
            String[] names = new String[]{
                    context.getString(R.string.description_1),
                    context.getString(R.string.description_2),
                    context.getString(R.string.description_3),
                    context.getString(R.string.description_4),
                    context.getString(R.string.description_5),
                    context.getString(R.string.description_6),
                    context.getString(R.string.description_7),
                    context.getString(R.string.description_8),
                    context.getString(R.string.description_9),
                    context.getString(R.string.description_10),
                    context.getString(R.string.description_11),
                    context.getString(R.string.description_12),
                    context.getString(R.string.description_13),
                    context.getString(R.string.description_14),
                    context.getString(R.string.description_15),
                    context.getString(R.string.description_16),
                    context.getString(R.string.description_17),
                    context.getString(R.string.description_18),
                    context.getString(R.string.description_19),
                    context.getString(R.string.description_20),
                    context.getString(R.string.description_21),
                    context.getString(R.string.description_22),
                    context.getString(R.string.description_23),
                    context.getString(R.string.description_24),
            };
            return names[id];
        } else {
            String[] names = new String[]{
                    context.getString(R.string.cdescription_1),
                    context.getString(R.string.cdescription_2),
                    context.getString(R.string.cdescription_3),
                    context.getString(R.string.cdescription_4),
                    context.getString(R.string.cdescription_5),
                    context.getString(R.string.cdescription_6),
                    context.getString(R.string.cdescription_7),
                    context.getString(R.string.cdescription_8),
                    context.getString(R.string.cdescription_9),
                    context.getString(R.string.cdescription_10),
                    context.getString(R.string.cdescription_11),
                    context.getString(R.string.cdescription_12),
                    context.getString(R.string.cdescription_13),
                    context.getString(R.string.cdescription_14),
                    context.getString(R.string.cdescription_15),
                    context.getString(R.string.cdescription_16),
                    context.getString(R.string.cdescription_17),
                    context.getString(R.string.cdescription_18),
                    context.getString(R.string.cdescription_19),
                    context.getString(R.string.cdescription_20),
                    context.getString(R.string.cdescription_21),
                    context.getString(R.string.cdescription_22),
                    context.getString(R.string.cdescription_23),
                    context.getString(R.string.cdescription_24),
            };
            return names[id - 24];
        }
    }

    public static List<Description> getDescriptions(String user_id, int[] ids, List<Description> descriptions) {
        List<Description> result = new ArrayList<>();
        for (int id : ids) {
            Description description = new Description();
            description.type = id;
            description.value = 0;
            description.user_id = user_id;
            for (Description one : descriptions) {
                if (one.type == id) {
                    description.id = one.id;
                    description.value = one.value;
                }
            }
            result.add(description);
        }
        return result;
    }

    public String getTypeName(int type) {
        switch (type){
            case Constants.PLAYER:
                return getString(R.string.player);
            case Constants.COACH:
                return getString(R.string.coach);
            case Constants.TEAM_CLUB:
                return getString(R.string.team_club);
            case Constants.AGENT:
                return getString(R.string.agent);
            case Constants.STAFF:
                return getString(R.string.staff);
            default:
                return getString(R.string.company);
        }
    }

    public static boolean isValidDate(int year, int month, int day) {
        if (year < 1900 || year >= 2100) return false;
        if (month < 1 || month > 12) return false;
        if (day < 1 || day > 31) return false;
        if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
            return false;
        }
        if (month == 2) {
            if (year%400 == 0 || (year%100 != 0 && year%4 == 0)) {
                if(day > 29) {
                    return false;
                } else {
                    return true;
                }
            } else{
                if(day > 28) {
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    public static String deltaTimeString(long delta) {
        long min = delta / 60;
        long hour = min / 60;
        long day = hour / 24;
        if (day > 30) {
            return "";
        }
        if (day > 0) {
            return String.format("%d days ago", day);
        }
        if (hour > 0) {
            return String.format("%d hours ago", hour);
        }
        if (min == 0) {
            return "Now";
        }
        return String.format("%d mins ago", min);
    }

    public static String getChatId(User a, User b) {
        if (a.id.compareTo(b.id) < 0) {
            return "room" + a.id + "_" + b.id;
        } else {
            return "room" + b.id + "_" + a.id;
        }
    }

    public List<String> configureStrength(int sid){
        switch(sid){
            case Constants.Football:
                return Arrays.asList(getResources().getStringArray(R.array.strength_football));
            case Constants.Basketball:
                return Arrays.asList(getResources().getStringArray(R.array.strength_basketball));
            case Constants.American_Football:
                return Arrays.asList(getResources().getStringArray(R.array.strength_american_football));
            case Constants.Cricket:
                return Arrays.asList(getResources().getStringArray(R.array.strength_cricket));
            case Constants.Tennis:
                return Arrays.asList(getResources().getStringArray(R.array.strength_tennis));
            case Constants.Rugby:
                return Arrays.asList(getResources().getStringArray(R.array.strength_rugby));
            case Constants.Golf:
                return Arrays.asList(getResources().getStringArray(R.array.strength_golf));
            case Constants.BaseBall:
                return Arrays.asList(getResources().getStringArray(R.array.strength_baseball));
            case Constants.Ice_Hockey:
                return Arrays.asList(getResources().getStringArray(R.array.strength_ice_hockey));
            case Constants.Field_Hockey:
                return Arrays.asList(getResources().getStringArray(R.array.strength_field_hockey));
            case Constants.VolleyBall:
                return Arrays.asList(getResources().getStringArray(R.array.strength_volley));
            case Constants.Handball:
                return Arrays.asList(getResources().getStringArray(R.array.strength_handball));
            case Constants.Badminton:
                return Arrays.asList(getResources().getStringArray(R.array.strength_badminton));
            case Constants.Squash:
                return Arrays.asList(getResources().getStringArray(R.array.strength_squash));
            case Constants.TableTennis:
                return Arrays.asList(getResources().getStringArray(R.array.strength_table_tennis));
            case Constants.Boxing:
                return Arrays.asList(getResources().getStringArray(R.array.strength_boxing));
            case Constants.MartialArt:
                return Arrays.asList(getResources().getStringArray(R.array.strength_martial_art));
            case Constants.MMA:
                return Arrays.asList(getResources().getStringArray(R.array.strength_mma));
            case Constants.KickBoxing:
                return Arrays.asList(getResources().getStringArray(R.array.strength_kick_boxing));
            case Constants.Wrestling:
                return Arrays.asList(getResources().getStringArray(R.array.strength_wrestling));
            case Constants.Gymnastic:
                return Arrays.asList(getResources().getStringArray(R.array.strength_gymnastic));
            case Constants.Athletics:
                return Arrays.asList(getResources().getStringArray(R.array.strength_athletics));
            case Constants.Fencing:
                return Arrays.asList(getResources().getStringArray(R.array.strength_fencing));
            case Constants.Judo:
                return Arrays.asList(getResources().getStringArray(R.array.strength_judo));
            case Constants.Swimming:
                return Arrays.asList(getResources().getStringArray(R.array.strength_swimming));
            case Constants.WaterSports:
                return Arrays.asList(getResources().getStringArray(R.array.strength_water_sports));
            case Constants.WaterPolo:
                return Arrays.asList(getResources().getStringArray(R.array.strength_water_polo));
            case Constants.AutoRacing:
                return Arrays.asList(getResources().getStringArray(R.array.strength_autoracing));
            case Constants.MotoRacing:
                return Arrays.asList(getResources().getStringArray(R.array.strength_motoracing));
            case Constants.Cycling:
                return Arrays.asList(getResources().getStringArray(R.array.strength_cycling));
            case Constants.Equestrianism:
                return Arrays.asList(getResources().getStringArray(R.array.strength_equestrian));
            case Constants.PoloSport:
                return Arrays.asList(getResources().getStringArray(R.array.strength_polo));
            case Constants.Dance:
                return Arrays.asList(getResources().getStringArray(R.array.strength_dance));
            case Constants.Softball:
                return Arrays.asList(getResources().getStringArray(R.array.strength_softball));
            case Constants.SepakTakraw:
                return Arrays.asList(getResources().getStringArray(R.array.strength_sepak_takraw));
            case Constants.Korfball:
                return Arrays.asList(getResources().getStringArray(R.array.strength_korfball));
            case Constants.Floorball:
                return Arrays.asList(getResources().getStringArray(R.array.strength_floorball));
            case Constants.Climbing:
                return Arrays.asList(getResources().getStringArray(R.array.strength_climbing));
            case Constants.Mountaineering:
                return Arrays.asList(getResources().getStringArray(R.array.strength_mountaineering));
            case Constants.Canyoning:
                return Arrays.asList(getResources().getStringArray(R.array.strength_canyoning));
            case Constants.Trail:
                return Arrays.asList(getResources().getStringArray(R.array.strength_trail));
            case Constants.Triathlon:
                return Arrays.asList(getResources().getStringArray(R.array.strength_triathlon));
            case Constants.Archery:
                return Arrays.asList(getResources().getStringArray(R.array.strength_archery));
            case Constants.Snowboarding:
                return Arrays.asList(getResources().getStringArray(R.array.strength_snowboarding));
            case Constants.Skiing:
                return Arrays.asList(getResources().getStringArray(R.array.strength_skiing));
            case Constants.IceSkating:
                return Arrays.asList(getResources().getStringArray(R.array.strength_ice_skating));
//            case Constants.Petanque:
//                return Arrays.asList(getResources().getStringArray(R.array.strength_pet));
            case Constants.SkateBoard:
                return Arrays.asList(getResources().getStringArray(R.array.strength_skateborard));
            case Constants.Weightlifting:
                return Arrays.asList(getResources().getStringArray(R.array.strength_weightlifting));
            case Constants.Crossfit_Fitness:
                return Arrays.asList(getResources().getStringArray(R.array.strength_crossfit_fitness));
            case Constants.Surf:
                return Arrays.asList(getResources().getStringArray(R.array.strength_surf));
            case Constants.Sailing:
                return Arrays.asList(getResources().getStringArray(R.array.strength_sailing));
            case Constants.Canoeing:
                return Arrays.asList(getResources().getStringArray(R.array.strength_canoeing));
            case Constants.Rowing:
                return Arrays.asList(getResources().getStringArray(R.array.strength_rowing));
            case Constants.Air_Sports:
                return Arrays.asList(getResources().getStringArray(R.array.strength_air_sport));
            case Constants.WinterSports:
                return Arrays.asList(getResources().getStringArray(R.array.strength_winter_sport));
            case Constants.Bowling:
                return Arrays.asList(getResources().getStringArray(R.array.strength_bowls));
            default:
                return new ArrayList<>();
        }
    }

    public String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public List<Skill> getAllStats() {
        List<Skill> result = new ArrayList<>();
        /** american football */
        List<String> stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_american_football_key));
        List<String> stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_american_football));
        List<String> stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_american_football_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.American_Football, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** basketball */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_basketball_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_basketball));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_basketball_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.Basketball, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** football */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_football_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_football));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_football_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.Football, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** rugby */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_rugby_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_rugby));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_rugby_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.Rugby, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** cricket */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_cricket_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_cricket));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_cricket_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.Cricket, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** baseball */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_baseball_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_baseball));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_baseball_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.BaseBall, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** hockey */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_hockey_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_hockey));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_hockey_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.Ice_Hockey, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Field_Hockey, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** handball */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_handball_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_handball));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_handball_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.Handball, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** volleyball */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_volleyball_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_volleyball));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_volleyball_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.VolleyBall, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** tennis */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_tennis_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_tennis));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_tennis_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.Tennis, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** badminton * squash * table tennis */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_badminton_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_badminton));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_badminton_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.Badminton, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Squash, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.TableTennis, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** golf */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_golf_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_golf));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_golf_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.Golf, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** athletics * swimming * water sports * gymnastic * boxing * martial art *
         * wrestling * fencing * cycling * equestrianism * dance * climbing * Bowling *
         * Crossfit_Fitness * Weightlifting * KickBoxing * mma * Trail * Rowing *
         * Mountaineering * WinterSports * Air_Sports **/
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_athletics_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_athletics));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_athletics_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.Athletics, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Swimming, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.WaterSports, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Gymnastic, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Boxing, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.MartialArt, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Wrestling, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Fencing, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Cycling, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Equestrianism, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Dance, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Climbing, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Bowling, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Crossfit_Fitness, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Weightlifting, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.KickBoxing, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.MMA, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Trail, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Rowing, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Mountaineering, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.WinterSports, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Air_Sports, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** judo */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_judo_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_judo));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_judo_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.Judo, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** sport car * sport moto * skiing * snowboarding * skateboard * surf *
         *  IceSkating * Sailing * Canoeing * */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_racing_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_racing));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_racing_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.AutoRacing, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.MotoRacing, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Skiing, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Snowboarding, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.SkateBoard, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Surf, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.IceSkating, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Sailing, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            result.add(new Skill(Constants.Canoeing, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /*2020-3-9 add skills start*/
        /** korfball */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_korfball_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_korfball));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_korfball_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.Korfball, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** softball */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_softball_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_softball));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_softball_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.Softball, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** floorball */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_floorball_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_floorball));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_floorball_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.Floorball, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** waterpolo */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_water_polo_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_water_polo));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_water_polo_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.WaterPolo, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        /** SepakTakraw */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_sepak_takraw_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_sepak_takraw));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_sepak_takraw_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.SepakTakraw, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }
        /**Add End*/

        /** coach */
        stat_keys = Arrays.asList(getResources().getStringArray(R.array.stat_coach_key));
        stat_names = Arrays.asList(getResources().getStringArray(R.array.stat_coach));
        stat_names_s = Arrays.asList(getResources().getStringArray(R.array.stat_coach_s));
        for(int i = 0 ; i < stat_keys.size(); i++){
            result.add(new Skill(Constants.STATS_COACH, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            //result.add(new Skill(Constants.STAFF, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
            //result.add(new Skill(Constants.TEAM_CLUB, stat_keys.get(i), stat_names.get(i), stat_names_s.get(i)));
        }

        return result;
    }
}