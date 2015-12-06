package com.yuanwei.resistance.partygame.origin.model;

import android.content.Context;
import android.support.v4.util.Pair;

import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.model.Gamer;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.moderator.BaseSwitcher;
import com.yuanwei.resistance.partygame.avalon.model.Avalon;
import com.yuanwei.resistance.partygame.origin.model.Resistance.GameEnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenyuanwei on 15/11/3.
 *
 * A Class used to record all game related data.
 *
 */
public class Bookkeeper {
    // TODO: create state for what fragment should show and the GridFragment
    private int mGame;

    private GameEnd mGameEnd;

    // Raw data
    public List<User> mUserList;

    public List<Gamer> mGamerList;

    public Map<User, Gamer> mGamerMap;

    // Misssion Results
    public List<List<User>> mProposeListResults;

    public List<Resistance.Propose> mProposeResults;

    public List<List<User>> mTeamResults;

    public List<List<Pair<User, Integer>>> mMissionExecutionResults;

    // Game Results
    public List<Integer> mGameResults;

    public Bookkeeper(int game, List<User> userList, List<Gamer> gamerList) {

        mGame = game;

        mUserList = userList;

        mGamerList = gamerList;

        mGamerMap = new HashMap<>();

        for (int i = 0; i < mUserList.size(); i++){
            mGamerMap.put(mUserList.get(i), mGamerList.get(i));
        }

        mGameEnd = GameEnd.NOT_FINISHED;

        mTeamResults = new ArrayList<>();

        mProposeResults = new ArrayList<>();

        mProposeListResults = new ArrayList<>();

        mMissionExecutionResults = new ArrayList<>();
    }

    public void keepMissionList(List<Integer> list) {

        List<User> users = mTeamResults.get(mTeamResults.size() - 1);

        List<Pair<User, Integer>> executionResults = new ArrayList<>(mTeamResults.size());

        for (int i = 0; i < users.size(); i++) {
            executionResults.add(new Pair<>(users.get(i), list.get(i)));
        }

        mMissionExecutionResults.add(executionResults);
    }

    public void keepGameResults(List<Integer> list) {
        mGameResults = list;
    }

    public void keepProposeResults(List<User> list) {
        mProposeListResults.add(list);
    }

    public void keepTeamResults(List<User> list) {
        mTeamResults.add(list);
    }

    public List<User> getLastProposeList() {
        return mProposeListResults.get(mProposeListResults.size() - 1);
    }

    public Resistance.Propose getLastPropose() {
        return mProposeResults.get(mProposeResults.size() - 1);
    }

    public void updateProposeResults(Resistance.Propose result) {

        mProposeResults.add(result);
    }

    public void updateMissionResult(int result) {

        for (Gamer gamer : mGamerList) {
            gamer.addMissionResult(Resistance.NEUTRAL);
        }

        List<User> team = mTeamResults.get(mTeamResults.size() - 1);

        for (User user : team) {
            if (result == Resistance.WIN || result == Resistance.LOSE)
                mGamerMap.get(user).setCurrentMissionResult(result);
        }
    }

    /**
     * When game is over, reveal the 'fail's
     */
    public void updateGameResults(Context context, GameEnd end) {

        mGameEnd = end;

        int round = 0;

        for (List<Pair<User, Integer>> list : mMissionExecutionResults) {
            for (Pair<User, Integer> pair : list) {
                // TODO: Create an enum for mission execution result
                if (pair.second == BaseSwitcher.SECONDARY)
                mGamerMap.get(pair.first).results.set(round, Resistance.SABOTAGE);
            }
            round ++;
        }

        if (mGame == Constants.ORIGIN && end != GameEnd.NOT_FINISHED) {
            for (Gamer gamer : mGamerList) {
                gamer.setRoleName(
                        context.getString(
                                Resistance.getInstance().findRoleById(
                                        gamer.getRoleId()).getTitleResId()));
            }
            return;
        }

        if (end == GameEnd.ASSASSINATION) {
            for (Gamer gamer : mGamerList) {
                if (gamer.getRoleId() < 0) {
                    gamer.setRoleName(
                            context.getString(
                                    Avalon.getInstance().findRoleById(
                                            gamer.getRoleId()).getTitleResId()));
                }
            }
        } else if (end != GameEnd.NOT_FINISHED) {
            for (Gamer gamer : mGamerList) {
                gamer.setRoleName(
                        context.getString(
                                Avalon.getInstance().findRoleById(
                                        gamer.getRoleId()).getTitleResId()));
            }
        }
    }

    public Gamer getGamer(User user) {
        return mGamerMap.get(user);
    }

    public User getUser(int index) {
        return mUserList.get(index);
    }

    public GameEnd getGameEnd() {
        return this.mGameEnd;
    }

    public void swap(int roleId0, int roleId1) {
        for (Gamer gamer : mGamerList) {
            if (gamer.getRoleId() == roleId0) {
                gamer.setRoleId(roleId1);
                continue;
            }
            if (gamer.getRoleId() ==roleId1) {
                gamer.setRoleId(roleId0);
            }
        }
    }
}
