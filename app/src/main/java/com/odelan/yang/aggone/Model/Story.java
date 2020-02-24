package com.odelan.yang.aggone.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Story implements Serializable, Parcelable {
    public String id;
    public User user;
    public String image;
    public long timebeg;
    public long timeend;
    public String tags = "";

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable((Parcelable) user, 1);
        dest.writeString(image);
        dest.writeLong(timebeg);
        dest.writeLong(timeend);
        dest.writeString(tags);
    }
    public Story(Parcel in) {
        id = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        image = in.readString();
        timebeg = in.readLong();
        timeend = in.readLong();
        tags = in.readString();
    }

    public Story() {
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
