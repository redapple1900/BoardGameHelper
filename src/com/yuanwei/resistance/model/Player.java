package com.yuanwei.resistance.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {
    private long id;
    private String name;
    private int win;
    private int lose;
    private String last_date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public String getLastDate() {
        return last_date;
    }

    public void setLastDate(String date) {
        this.last_date = date;
    }

    public Player (Parcel parcel) {
        this.id = parcel.readLong();
        String[] strings = new String[2];
        parcel.readStringArray(strings);
        this.name = strings[0];
        this.last_date = strings[1];
        int[] nums = new int[2];
        parcel.readIntArray(nums);
        this.win = nums[0];
        this.lose = nums[1];
    }

    public Player (String name, long id) {
        this.name = name;
        this.id = id;
    }

    public Player () {}

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeStringArray(new String[]{name, last_date});
        parcel.writeIntArray(new int[]{win, lose});
    }

    public static final Creator<Player> creator = new Creator<Player>() {

        @Override
        public Player createFromParcel(Parcel parcel) {
            return new Player(parcel);
        }

        @Override
        public Player[] newArray(int i) {
            return new Player[i];
        }
    };
}