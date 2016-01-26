package com.yuanwei.resistance.partygame.origin.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.util.Pair;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.model.Gamer;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.moderator.BaseSwitcher;
import com.yuanwei.resistance.partygame.avalon.model.Avalon;
import com.yuanwei.resistance.partygame.origin.model.Resistance.GameStatus;
import com.yuanwei.resistance.partygame.origin.model.Resistance.Role;
import com.yuanwei.resistance.partygame.origin.model.Resistance.Vote;

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

    // Results of missions in a game
    public List<Integer> mMissionResults;
    // Raw data
    private List<User> mUserList;
    private List<Gamer> mGamerList;
    private transient Map<User, Gamer> mGamerMap;
    private transient Map<User, Integer> mIndex;
    // Proposed team
    private List<List<Integer>> mProposedTeams;
    // Vote
    @SerializedName("votelist")
    private List<Vote> mVoteResults;
    // Team for the mission
    private transient List<List<Integer>> mTeam; /* Derived */
    // Results of mission execution
    private List<List<Integer>> mExecutionResults;
    // Success and Sabotages of a mission
    private transient List<Pair<Integer, Integer>> mMissionExecutionList;
    // Execution of each participant in the mission
    private transient List<List<Pair<Integer, Integer>>> mMissionExecutionDetails;
    private int mGame;
    private GameStatus mGameStatus;

    public Bookkeeper(int game, List<User> userList, List<Gamer> gamerList) {

        mGame = game;

        mUserList = userList;

        mGamerList = gamerList;

        mGamerMap = new HashMap<>();

        for (int i = 0; i < mUserList.size(); i++) {
            mGamerMap.put(mUserList.get(i), mGamerList.get(i));
        }

        mIndex = new HashMap<>();

        for (int i = 0; i < mUserList.size(); i++) {
            mIndex.put(mUserList.get(i), i);
        }

        mGameStatus = GameStatus.NOT_FINISHED;

        mProposedTeams = new ArrayList<>();

        mVoteResults = new ArrayList<>();

        mTeam = new ArrayList<>();

        mExecutionResults = new ArrayList<>();

        mMissionResults = new ArrayList<>();

        mMissionExecutionList = new ArrayList<>();

        mMissionExecutionDetails = new ArrayList<>();
    }

    private Bookkeeper() {
    }

    /**
     * This is a temporary way to initiate bookkeeper after GSON deserialization
     */
    public Bookkeeper initiate() {
        mGamerMap = new HashMap<>();

        for (int i = 0; i < mUserList.size(); i++){
            mGamerMap.put(mUserList.get(i), mGamerList.get(i));
        }

        mIndex = new HashMap<>();

        for (int i = 0; i < mUserList.size(); i++) {
            mIndex.put(mUserList.get(i), i);
        }

        if (mProposedTeams != null && mVoteResults != null) {
            mTeam = new ArrayList<>();
            for (int i = 0, j = mVoteResults.size(); i < j; i++) {
                if (mVoteResults.get(i) == Vote.APPROVAL)
                    mTeam.add(mProposedTeams.get(i));
            }
        }
        // TODO: temporary way of fix
        if (mProposedTeams.size() != mVoteResults.size()) {
            mProposedTeams.remove(mProposedTeams.size() - 1);
        }

        mMissionExecutionList = new ArrayList<>();

        if (mExecutionResults != null) {
            mMissionExecutionDetails = new ArrayList<>();
            for (int i = 0, j = mExecutionResults.size(); i < j; i++) {
                updateExecutionResults(i, mExecutionResults.get(i));
            }
        }
        return this;
    }

    public void clear(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(Constants.BOOKKEEPER_KEY, "").apply();
    }

    public void keepPropose(List<User> list) {
        mProposedTeams.add(getIndexList(list));
    }

    public void keepMissionExecution(List<Integer> list) {

        mExecutionResults.add(list);

        updateExecutionResults(mTeam.size() - 1, list);
    }

    private void updateExecutionResults(int round, List<Integer> list) {
        List<Integer> indices = mTeam.get(round);

        List<Pair<Integer, Integer>> executionResults = new ArrayList<>();
        int success = 0, sabotage = 0;

        for (int i = 0; i < indices.size(); i++) {
            int execution = list.get(i);
            executionResults.add(new Pair<>(indices.get(i), execution));
            if (execution == BaseSwitcher.PRIMARY)
                success++;
            else
                sabotage++;
        }
        mMissionExecutionList.add(new Pair<>(success, sabotage));
        mMissionExecutionDetails.add(executionResults);
    }

    public void keepMissionResult(List<Integer> list) {
        mMissionResults = list;
    }

    public void keepVote(Vote result) {
        mVoteResults.add(result);
        if (result == Vote.APPROVAL) {
            mTeam.add(mProposedTeams.get(mProposedTeams.size() - 1));
        }
    }

    public void updateMissionResult(int result) {
        // TODO:: Use some number more explictly.
        mMissionResults.add(result == Resistance.WIN ? BaseSwitcher.PRIMARY : BaseSwitcher.SECONDARY);

        for (Gamer gamer : mGamerList) {
            gamer.addMissionResult(Resistance.NEUTRAL);
        }

        List<Integer> team = mTeam.get(mTeam.size() - 1);

        for (Integer i : team) {
            if (result == Resistance.WIN || result == Resistance.LOSE)
                mGamerList.get(i).setCurrentMissionResult(result);
        }
    }

    /**
     * When game is over, reveal the 'fail's
     */
    public void updateGameResults(Context context, GameStatus status) {

        mGameStatus = status;

        int round = 0;

        for (List<Pair<Integer, Integer>> list : mMissionExecutionDetails) {
            for (Pair<Integer, Integer> pair : list) {
                // TODO: Create an enum for mission execution result
                if (pair.second == BaseSwitcher.SECONDARY)
                    mGamerList.get(pair.first).results.set(round, Resistance.SABOTAGE);
            }
            round ++;
        }

        if (mGame == Constants.ORIGIN && status != GameStatus.NOT_FINISHED) {
            for (Gamer gamer : mGamerList) {
                Role role = Resistance.getInstance().findRoleById(gamer.getRoleId());
                gamer.setRoleName(context.getString(role.getTitleResId()));
            }
            return;
        }

        if (status == GameStatus.ASSASSINATION) {
            for (Gamer gamer : mGamerList) {
                if (gamer.getRoleId() < 0) {
                    Avalon.Role role = Avalon.getInstance().findRoleById(gamer.getRoleId());
                    gamer.setRoleName(context.getString(role.getTitleResId()));
                }
            }
        } else if (status != GameStatus.NOT_FINISHED) {
            for (Gamer gamer : mGamerList) {
                Avalon.Role role = Avalon.getInstance().findRoleById(gamer.getRoleId());
                gamer.setRoleName(context.getString(role.getTitleResId()));
            }
        }
    }

    public List<User> getLastPropose() {
        return getUserList(mProposedTeams.get(mProposedTeams.size() - 1));
    }

    public List<Pair<Integer, Integer>> getMissionExecutionResult() {
        return mMissionExecutionList;
    }

    public List<Integer> getMissionResult() {
        return mMissionResults;
    }

    public int getIndex(User user) {
        return mIndex.get(user);
    }

    public int getGameId() {
        return mGame;
    }

    public Gamer getGamer(User user) {
        return mGamerMap.get(user);
    }

    public Gamer getGamer(int index) {
        return mGamerList.get(index);
    }

    public User getUser(int index) {
        return mUserList.get(index);
    }

    public GameStatus getGameStatus() {
        return mGameStatus;
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

    public ArrayList<User> getUserList() {
        return (ArrayList<User>) mUserList;
    }

    public ArrayList<Gamer> getGamerList() {
        return (ArrayList<Gamer>) mGamerList;
    }

    private List<User> getUserList(List<Integer> list) {
        List<User> result = new ArrayList<>();

        for (Integer i : list) {
            result.add(mUserList.get(i));
        }

        return result;
    }

    private List<Integer> getIndexList(List<User> list) {
        List<Integer> result = new ArrayList<>();

        for (User user : list) {
            result.add(mIndex.get(user));
        }

        return result;
    }

    public static class Agent {

        Gson gson;
        String PREF_KEY = Constants.BOOKKEEPER_KEY;

        //////////////////////////////////////////////////////////////////////////
        // Serialization

        public Agent() {
            gson = new Gson();
        }

        public void save(Context context, Bookkeeper bookkeeper) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String result = gson.toJson(bookkeeper);
            Log.d("Resistance: JSON", result);
            prefs.edit().putString(PREF_KEY, result).apply();
        }

        public Bookkeeper load(Context context) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            String json = prefs.getString(PREF_KEY, "");

            if ("".equals(json)) {
                return null;
            }
            return gson.fromJson(json, Bookkeeper.class);
        }
    }
}
