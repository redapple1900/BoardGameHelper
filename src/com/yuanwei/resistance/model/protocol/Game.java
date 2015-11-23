package com.yuanwei.resistance.model.protocol;

/**
 * Created by chenyuanwei on 15/10/15.
 */
public interface Game {

    String getName();

    GameType getType();

    int getMaxParties();

    int getMinParties();

    int getMaxPlayers();

    int getMinPlayers();

    <T extends Role> T[] getGameRoles();

    <T extends Role> T[] getDefaultRoles();

    <T extends Role> T[] getSpecialRoles();

    <T extends Role> T findRoleById(int id);

   enum GameType {
        SINGLE_OFFLINE,
        MULTIPLE_OFFLINE,
        SINGLE_ONLINE,
        MULTIPLE_ONLINE
    }
}
