package com.odelan.yang.aggone.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class VideoItem implements Serializable,  Parcelable {
    public String title;
    public String description;
    public String thumbnailURL;
    public String id;

    public VideoItem() {}
    public VideoItem(Parcel in) {
        title = in.readString();
        description = in.readString();
        thumbnailURL = in.readString();
        id = in.readString();
    }

    public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel in) {
            return new VideoItem(in);
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(thumbnailURL);
        dest.writeString(id);
    }
}