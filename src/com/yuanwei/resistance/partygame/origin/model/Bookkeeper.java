package com.yuanwei.resistance.partygame.origin.model;

import android.support.v4.util.Pair;

import com.yuanwei.resistance.model.Gamer;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.moderator.BaseSwitcher;

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

    private Resistance.GameEnd mGameEnd;

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

    public Bookkeeper(List<User> userList, List<Gamer> gamerList) {

        mUserList = userList;

        mGamerList = gamerList;

        mGamerMap = new HashMap<>();

        for (int i = 0; i < mUserList.size(); i++){
            mGamerMap.put(mUserList.get(i), mGamerList.get(i));
        }

        mGameEnd = Resistance.GameEnd.NOT_FINISHED;

        mTeamResults = new ArrayList<>();

        mProposeResults = new ArrayList<>();

        mProposeListResults = new ArrayList<>();

        mMissionExecutionResults = new ArrayList<>();
    }

    public void keepMissionResults(List<Integer> list) {

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

    public void updateMissionResults(int result) {

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
    public void updateGameResults(Resistance.GameEnd end) {

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
    }

    public Gamer getGamer(User user) {
        return mGamerMap.get(user);
    }

    public User getUser(int index) {
        return mUserList.get(index);
    }

    public Resistance.GameEnd getGameEnd() {
        return this.mGameEnd;
    }
}
