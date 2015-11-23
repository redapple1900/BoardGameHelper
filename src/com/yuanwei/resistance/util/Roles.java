package com.yuanwei.resistance.util;

import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.model.protocol.Game;
import com.yuanwei.resistance.model.protocol.Role;
import com.yuanwei.resistance.partygame.avalon.model.Avalon;
import com.yuanwei.resistance.partygame.origin.model.Resistance;

/**
 * Created by chenyuanwei on 15/10/23.
 * Utility class
 */
public class Roles {

    public static Role findRole(int game, int id){
        return findGame(game).findRoleById(id);
    }

    private static Game findGame(int game) {
        switch (game) {
            case Constants.ORIGIN:
                return Resistance.getInstance();
            case Constants.AVALON:
                return Avalon.getInstance();
        }
        return null;
    }
}
