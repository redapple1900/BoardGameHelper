package com.yuanwei.resistance.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenyuanwei on 15/11/15.
 */
public  class Params implements Parcelable {

    // TODO:: This is very similar to Resistance#GameEnd. Better to find a way to unify
    // Also states in BaseGamingResistantFragment
    public static final int GAME_NOT_OVER = 0;

    public static final int RESISTANCE_WIN = 1;

    public static final int SPY_WIN = 2;

    public static final int ASSASINATION = 3;

    public static final int LOYALIST_WIN = 4;

    public static final int MINION_WIN = 5;

    public static final int REMOVE_USER = 6;

    public int getStats() {
        return stats;
    }

    public boolean isPassAllowed() {
        return allowPass;
    }

    public String getTag() {
        return Tag;
    }

    public int getLimit() {
        return limit;
    }

    private int stats;

    private boolean allowPass;

    private String Tag;

    private int limit;

    public Params(Parcel parcel) {
        Tag = parcel.readString();
        
        boolean[] booleans = new boolean[1];
        parcel.readBooleanArray(booleans);
        allowPass = booleans[0];
        
        int[] ints = new int[2];
        parcel.readIntArray(ints);
        stats = ints[0];
        limit = ints[1];
    }

    public Params(Builder builder) {
        this.Tag = builder.Tag;
        this.stats = builder.stats;
        this.allowPass = builder.allowPass;
        this.limit = builder.limit;
    }

    private Params() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Tag);
        parcel.writeBooleanArray(new boolean[]{allowPass});
        parcel.writeIntArray(new int[]{stats, limit});
    }

    public static final Parcelable.Creator<Params> CREATOR = new Creator<Params>() {

        @Override
        public Params createFromParcel(Parcel parcel) {
            return new Params(parcel);
        }

        @Override
        public Params[] newArray(int i) {
            return new Params[i];
        }
    };
    
    public static class Builder{

        public Builder setStatus(int stats) {
            this.stats = stats;
            return this;
        }

        public Builder setAllowPass(boolean allowPass) {
            this.allowPass = allowPass;
            return this;
        }

        public Builder setTag(String tag) {
            Tag = tag;
            return this;
        }

        public Builder setLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public Params build() {
            return new Params(this);
        }

        private int stats;

        private boolean allowPass;

        private String Tag;

        private int limit;

        private int count;
    }
}
