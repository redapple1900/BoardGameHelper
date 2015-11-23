package com.yuanwei.resistance.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A data object records general info of a role in a game. It is independent of a real person
 * Created by chenyuanwei on 15/7/5.
 */
public class User implements Parcelable {

    private long id;

    private String name;

    // Game specific identity;
    private int identity;

    private int resId;

    private String pic_url;

    public User(Parcel parcel) {
        this.id = parcel.readLong();
        String[] strings = new String[2];
        parcel.readStringArray(strings);
        this.name = strings[0];
        this.pic_url = strings[1];
        int[] nums = new int[2];
        parcel.readIntArray(nums);
        this.resId = nums[0];
        this.identity = nums[1];
    }

    public User(long id, String name, int resId) {
        this.id = id;
        this.name = name;
        this.resId = resId;
    }

    public User() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeStringArray(new String[]{name, pic_url});
        parcel.writeIntArray(new int[]{resId, identity});

    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {

        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[i];
        }
    };

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public int getIdentity() {
        return identity;
    }

    public User setIdentity(int identity) {
        this.identity = identity;
        return this;
    }

    public int getResId() {
        return resId;
    }

    public User setResId(int resId) {
        this.resId = resId;
        return this;
    }

    public String getPicUrl() {
        return pic_url;
    }

    public User setPicUrl(String pic_url) {
        this.pic_url = pic_url;
        return this;
    }
}
