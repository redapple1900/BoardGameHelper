package com.yuanwei.resistance.model;

/**
 * Created by chenyuanwei on 15/7/13.
 */
public class ResistanceGameEvent extends BaseGameEvent {

    public static final int PROPOSE = 1;
    public static final int PROPOSE_VETO = 2;
    public static final int PROPOSE_PASSED = 3;
    public static final int MISSION_EXECUTION_START = 4;
    public static final int MISSION_EXECUTION_CONTINUE = 5;
    public static final int MISSION_EXECUTION_COMPLETED = 6;
    public static final int MISSION_SUCCEED = 7;
    public static final int MISSION_FAILED = 8;
    public static final int SHOW_RESULTS = 9;
    public static final int VOTE = 10;
    public static final int RESISTANCE_WIN = 11;
    public static final int SPY_WIN = 12;
    public static final int RESTART = 13;

    private int type;

    public ResistanceGameEvent(int type) {
        this.type = type;
    }


    @Override
    public int getType() {
        return type;
    }
}
