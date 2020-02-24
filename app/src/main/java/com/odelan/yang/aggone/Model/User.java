package com.odelan.yang.aggone.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.odelan.yang.aggone.Utils.Constants;

import java.io.Serializable;

public class User implements Serializable, Parcelable {
    public String id;
    public String username;
    public String email;
    public String photo_url = "";
    public int group_id;                 /** coach or player */
    public int sport_id = 500;
    public String city = "";
    public String category = "";
    public String position = "";
    public String country = "";
    public int gender_id = 0;
    public String club = "";
    public int age;
    public int height = Constants.HEIGHT_DEFAULT;
    public int weight = Constants.WEIGHT_DEFAULT;
    public int year = 1970;
    public int month = 1;
    public int day = 1;
    public String contract = "";
    public String phone = "";
    public String web_url = "";
    public String desc_str = "";
    public int available_club = 0;
    public int recommends = 0;

    public User(Parcel in) {
        id = in.readString();
        username = in.readString();
        email = in.readString();
        photo_url = in.readString();
        group_id = in.readInt();
        sport_id = in.readInt();
        city = in.readString();
        category = in.readString();
        position = in.readString();
        country = in.readString();
        club = in.readString();
        age = in.readInt();
        height = in.readInt();
        weight = in.readInt();
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        contract = in.readString();
        phone = in.readString();
        web_url = in.readString();
        desc_str = in.readString();
        available_club = in.readInt();
        recommends = in.readInt();
    }

    public User() {
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(photo_url);
        dest.writeInt(group_id);
        dest.writeInt(sport_id);
        dest.writeString(city);
        dest.writeString(category);
        dest.writeString(position);
        dest.writeString(country);
        dest.writeString(club);
        dest.writeInt(age);
        dest.writeInt(height);
        dest.writeInt(weight);
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeString(contract);
        dest.writeString(phone);
        dest.writeString(web_url);
        dest.writeString(desc_str);
        dest.writeInt(available_club);
        dest.writeInt(recommends);
    }
}
