package com.odelan.yang.aggone.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Feed implements Serializable, Parcelable {
    public String id;
    public int type;//normal(video)=0, youtube=1, news=2
    public User user;
    public String title;
    public String video_url;
    public String thumbnail_url;
    public int sport_id;
    public int view_count;
    public int like_count;
    public long timestamp;
    public int shared;
    public boolean like;
    public boolean bookmark;
    public String tagged;
    public int mode;  //private, public
    public String desc_str;
    public String articles; //photo_url , max = 3

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(type);
        dest.writeParcelable((Parcelable) user, 1);
        dest.writeString(title);
        dest.writeString(video_url);
        dest.writeString(thumbnail_url);
        dest.writeInt(sport_id);
        dest.writeInt(view_count);
        dest.writeInt(like_count);
        dest.writeLong(timestamp);
        dest.writeInt(shared);
        dest.writeString(tagged);
        dest.writeInt(mode);
        dest.writeString(desc_str);
        dest.writeString(articles);
        if (like)
            dest.writeInt(1);
        else
            dest.writeInt(0);
        if (bookmark)
            dest.writeInt(1);
        else
            dest.writeInt(0);

    }
    public Feed(Parcel in) {
        id = in.readString();
        type = in.readInt();
        user = in.readParcelable(User.class.getClassLoader());
        title = in.readString();
        video_url = in.readString();
        thumbnail_url = in.readString();
        sport_id = in.readInt();
        view_count = in.readInt();
        like_count = in.readInt();
        timestamp = in.readLong();
        shared = in.readInt();
        tagged = in.readString();
        mode = in.readInt();
        desc_str = in.readString();
        articles = in.readString();
        like = in.readInt()==1;
        bookmark = in.readInt()==1;
    }

    public Feed() {
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
