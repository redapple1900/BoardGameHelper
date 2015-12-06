package com.yuanwei.resistance.partygame.avalon.model;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.model.protocol.Game;
import com.yuanwei.resistance.partygame.origin.model.Resistance.Option;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenyuanwei on 15/10/15.
 */
public class Avalon implements Game {

    private static Avalon mGame;

    private Map<Integer, Role> mRoleMap;

    private Role[] mDefaultRoles = {
            Role.LOYALIST, Role.MINION
    };

    private Role[] mSpecialRoles = {
            Role.MERLIN, Role.ASSASSIN, Role.PERCIVAL, Role.MORDRED,
            Role.OBERON, Role.MORGANA, Role.LANCELOT_ARTHUR, Role.LANCELOT_MORDRED
    };

    private Role[] mRoles = {
            Role.LOYALIST, Role.MINION,
            Role.MERLIN, Role.ASSASSIN, Role.PERCIVAL, Role.MORDRED,
            Role.OBERON, Role.MORGANA, Role.LANCELOT_ARTHUR, Role.LANCELOT_MORDRED
    };

    private Option[] mOptions = {Option.ALLOW_PASS, Option.MANUEL_GAME_OVER};

    private Avalon() {
        mRoleMap = new HashMap<>(mRoles.length * 2);

        for (Role role : mRoles) {
            mRoleMap.put(role.getRoleId(), role);
        }
    }


    public static Avalon getInstance() {
        if (mGame == null) {
            mGame = new Avalon();
        }
        return mGame;
    }

    public Option[] getOptions() {
        return mOptions;
    }

    @Override
    public String getName() {
        return "Avalon";
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

    @Override
    public Role findRoleById(int id) {
        return mRoleMap.get(id);
    }

    public enum Role implements com.yuanwei.resistance.model.protocol.Role {
        LOYALIST(100, R.string.role_loyalist, R.string.role_resistant_short_desc, R.drawable.role_loyalist),
        MINION(-100, R.string.role_minion, R.string.role_spy_short_desc, R.drawable.role_minion),
        MERLIN(101, R.string.option_merlin, R.string.option_merlin_short_desc, R.drawable.role_merlin),
        ASSASSIN(-101, R.string.option_assassin, R.string.option_assassin_short_desc, R.drawable.role_assassin),

        PERCIVAL(102, R.string.option_percival, R.string.option_percival_short_desc, R.drawable.role_percival),
        MORDRED(-102, R.string.option_mordred, R.string.option_mordred_short_desc, R.drawable.role_mordred),

        OBERON(-104, R.string.option_oberon, R.string.option_oberon_short_desc, R.drawable.role_oberon),
        MORGANA(-105, R.string.option_morgana, R.string.option_morgana_short_desc, R.drawable.role_morgana),
        LANCELOT_ARTHUR(103, R.string.option_lancelot_arthur, R.string.option_lancelot_v3_short_desc, R.drawable.role_lancelot_good),
        LANCELOT_MORDRED(-103, R.string.option_lancelot_mordred, R.string.option_lancelot_v3_short_desc, R.drawable.role_lancelot_evil);

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
            return "Avalon";
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
}
