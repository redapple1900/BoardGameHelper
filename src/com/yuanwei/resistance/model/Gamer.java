package com.yuanwei.resistance.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyuanwei on 15/11/9.
 */
public class Gamer implements Parcelable {

    // Resistance/Avalon specific identity;
    private int roleId;

    private boolean leader;

    public List<Integer> results;

    public Gamer(Parcel parcel) {

        results = new ArrayList<>();
        parcel.readList(results, null);

        roleId = parcel.readInt();

        boolean[] array = new boolean[1];
        parcel.readBooleanArray(array);
        leader = array[0];
    }

    public Gamer(int roleId, int round) {
        this.roleId = roleId;

        results = new ArrayList<>(round);
    }

    public int getRoleId() {
        return roleId;
    }
    public List<Integer> getResults () {
        return this.results;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setLeader (boolean leader) {
        this.leader = leader;
    }

    public boolean isLeader() {
        return this.leader;
    }

    public void addMissionResult(int result){
        results.add(result);
    }

    public void setCurrentMissionResult(int result) {
        results.set(results.size() - 1, result);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(roleId);
        parcel.writeList(results);
    }

    public static final Parcelable.Creator<Gamer> CREATOR = new Creator<Gamer>() {

        @Override
        public Gamer createFromParcel(Parcel parcel) {
            return new Gamer(parcel);
        }

        @Override
        public Gamer[] newArray(int i) {
            return new Gamer[i];
        }
    };
}
