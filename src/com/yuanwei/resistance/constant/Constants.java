package com.yuanwei.resistance.constant;

public class Constants {
    public static final int ORIGIN = 0;
    public static final int AVALON = 1;

    /**
     * Roles in Original Resistance
     */
    public static final int RESISTANT = 100;
    public static final int SPY = -100;

    public static int[] NumOfMinPass = {0, 0, 0, 0, 0, 3, 4, 4, 5, 5, 6};
    public static int[][] NumOfMembersPerMission = {{}, {}, {}, {}, {},
            {2, 3, 2, 3, 3},
            {2, 3, 4, 3, 4},
            {2, 3, 3, 4, 4},
            {3, 4, 4, 5, 5},
            {3, 4, 4, 5, 5},
            {3, 4, 4, 5, 5}};
    /**
     * Some constants
     */
    public static final String LANGUAGE = "language";
    public static final String THEME = "theme";
    public static final String THEME_MILITARY = "military";
    public static final String TOTAL_PLAYERS_KEY = "total_players";
    public static final String GAME_WITH_CARDS = "game_with_cards";
    public static final String GAME_WITHOUT_CARDS = "game_without_cards";
    public static final String GAME = "game";
    public static final String USERLIST_KEY = "gamerList";

    public static int getNormalPlayers(int Total_players) {
        switch (Total_players) {
            case 5:
                return 3;
            case 6:
                return 4;
            case 7:
                return 4;
            case 8:
                return 5;
            case 9:
                return 6;
            case 10:
                return 6;
            default:
                return 0;
        }
    }

    public static int getSpyPlayers(int Total_players) {
        switch (Total_players) {
            case 5:
                return 2;
            case 6:
                return 2;
            case 7:
                return 3;
            case 8:
                return 3;
            case 9:
                return 3;
            case 10:
                return 4;
            default:
                return 0;
        }
    }
    //DataSet could be inputed from files.
}

