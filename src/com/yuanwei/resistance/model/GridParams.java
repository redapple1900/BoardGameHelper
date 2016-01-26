package com.yuanwei.resistance.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.yuanwei.resistance.partygame.origin.model.Resistance;
import com.yuanwei.resistance.partygame.origin.model.Resistance.GameStatus;

/**
 * Created by chenyuanwei on 15/11/15.
 */
public class GridParams implements Parcelable {

    public static final Parcelable.Creator<GridParams> CREATOR = new Creator<GridParams>() {

        @Override
        public GridParams createFromParcel(Parcel parcel) {
            return new GridParams(parcel);
        }

        @Override
        public GridParams[] newArray(int i) {
            return new GridParams[i];
        }
    };
    private int gameId;
    private int limit;
    private boolean allowPass;
    private String Tag;
    private GameStatus gameStatus;

    public GridParams(Parcel parcel) {
        Tag = parcel.readString();

        boolean[] booleans = new boolean[1];
        parcel.readBooleanArray(booleans);
        allowPass = booleans[0];

        int[] ints = new int[3];
        parcel.readIntArray(ints);
        gameId = ints[0];
        limit = ints[1];
        gameStatus = Resistance.Status[ints[2]];
    }

    public GridParams(Builder builder) {
        this.Tag = builder.Tag;
        this.gameId = builder.gameId;
        this.gameStatus = builder.stats;
        this.allowPass = builder.allowPass;
        this.limit = builder.limit;
    }

    private GridParams() {
    }

    public GameStatus getGameStatus() {
        return gameStatus;
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

    public int getGameId() {
        return gameId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Tag);
        parcel.writeBooleanArray(new boolean[]{allowPass});
        parcel.writeIntArray(new int[]{gameId, limit, gameStatus.ordinal()});
    }
    
    public static class Builder{

        private GameStatus stats;
        private boolean allowPass;
        private String Tag;
        private int limit;
        private int gameId;

        public Builder setStatus(GameStatus stats) {
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

        public Builder setGameId(int id) {
            this.gameId = id;
            return this;
        }

        public GridParams build() {
            return new GridParams(this);
        }
    }
}
