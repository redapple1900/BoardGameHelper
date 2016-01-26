package com.yuanwei.resistance.partygame.origin.model;

import com.google.gson.annotations.SerializedName;
import com.yuanwei.resistance.R;
import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.model.protocol.Game;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenyuanwei on 15/10/15.
 */
public class Resistance implements Game {

    public static final int WIN = 1;

    public static final int LOSE = -1;

    public static final int SABOTAGE = -10;

    public static final int NEUTRAL = 0;

    public static GameStatus[] Status = {
            GameStatus.NOT_FINISHED,
            GameStatus.RESISTANCE_WIN,
            GameStatus.SPY_WIN,
            GameStatus.ASSASSINATION};

    private static Resistance mGame;

    private Map<Integer, Role> mRoleMap;

    private Role[] mDefaultRoles = {Role.RESISTANT, Role.SPY};

    private Role[] mSpecialRoles = {};

    private Role[] mRoles = {Role.RESISTANT, Role.SPY};

    private Option[] mOptions = {Option.BLIND_SPY, /*Option.ALLOW_PASS,*/ Option.MANUEL_GAME_OVER};

    private Resistance() {
        mRoleMap = new HashMap<>(mRoles.length * 2);
        for (Role role : mRoles) {
            mRoleMap.put(role.getRoleId(), role);
        }
    }

    public static Resistance getInstance() {
        if (mGame == null) {
            mGame = new Resistance();
        }
        return mGame;
    }

    public static int getNumberForMission(int total, int mission) {
        if (Constants.NumOfMembersPerMission[total].length > mission)
            return Constants.NumOfMembersPerMission[total][mission];
        else
            return 0;
    }
    // How many success are needed for a mission
    public static int getSuccess(int total, int mission) {
        return getNumberForMission(total, mission) - getFailure(total, mission) + 1;
    }

    public static int getFailure(int total, int mission) {
        if (total >= 7 && mission == 3 /* First  mission is 0 */) {
            return 2;
        }
        return 1;
    }

    @Override
    public String getName() {
        return "Resistance";
    }

    @Override
    public GameType getType() {
        return GameType.MULTIPLE_OFFLINE;
    }

    @Override
    public int getMaxParties() {
        return 2;
    }

    @Override
    public int getMinParties() {
        return 2;
    }

    @Override
    public int getMaxPlayers() {
        return 10;
    }

    @Override
    public int getMinPlayers() {
        return 5;
    }

    @Override
    public Role[] getGameRoles() {
        return mRoles;
    }

    @Override
    public Role[] getDefaultRoles() {
        return mDefaultRoles;
    }

    @Override
    public Role[] getSpecialRoles() {
        return mSpecialRoles;
    }

    public Option[] getOptions() { return mOptions;}

    @Override
    public Role findRoleById(int id) {
        return mRoleMap.get(id);
    }

    public enum Role implements com.yuanwei.resistance.model.protocol.Role {
        RESISTANT(100, R.string.role_resistant, R.string.role_resistant_short_desc, R.drawable.thief),
        SPY(-100, R.string.role_spy, R.string.role_spy_short_desc, R.drawable.spy);

        private int mRoleId;
        private int mTitleResId;
        private int mDescResId;
        private int mImgResId;

        Role(int roleId, int titleResId, int descResId, int imgResId) {
            mRoleId = roleId;
            mTitleResId = titleResId;
            mDescResId = descResId;
            mImgResId = imgResId;
        }

        public String getGameName() {
            return "Resistance";
        }

        public String getName() {
            return this.name();
        }

        public int getRoleId() {
            return mRoleId;
        }

        public int getTitleResId() {
            return mTitleResId;
        }

        public int getDescResId() {
            return mDescResId;
        }

        public int getImgResId() {
            return mImgResId;
        }
    }

    public enum Option {
        BLIND_SPY(R.string.option_blind_spy, R.string.option_blind_spy_short_desc),
        ALLOW_PASS(R.string.option_allow_pass, R.string.option_allow_pass_short_desc),
        MANUEL_GAME_OVER(R.string.option_manuel_game_over, R.string.option_manuel_game_over_short_desc);

        private int mTitleResId;
        private int mDescResId;

        Option(int titleResId, int descResId) {
            mTitleResId = titleResId;
            mDescResId = descResId;
        }

        public int getTitleResId() {
            return mTitleResId;
        }

        public int getDescResId() {
            return mDescResId;
        }
    }

    public enum Vote {
        @SerializedName("0")
        UNKNOWN,
        @SerializedName("1")
        APPROVAL,
        @SerializedName("2")
        VETO,
        @SerializedName("3")
        PASS
    }

    public enum Execution {
        EXECUTION,
        SABOTAGE
    }

    public enum MissionResult {
        SUCCESS,
        FAIL
    }

    public enum GameStatus {
        @SerializedName("0")
        NOT_FINISHED,
        @SerializedName("1")
        RESISTANCE_WIN,
        @SerializedName("2")
        SPY_WIN,
        @SerializedName("3")
        ASSASSINATION,
        @SerializedName("4")
        MODIFYING_USERS;
    }
}
