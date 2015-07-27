package com.yuanwei.resistance.moderator;

import com.yuanwei.resistance.DataSet;
import com.yuanwei.resistance.model.Gamer;
import com.yuanwei.resistance.model.ResistanceGameEvent;
import com.yuanwei.resistance.model.protocol.GamePresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by chenyuanwei on 15/7/9.
 */
public class Game {
    public Result result;
    public int maxRound;
    public int memberSelected;
    public int leader;
    public int currentMission;
    public int timeOfTeamPropose;
    public int victory;
    public int lose;
    public int round;
    public List<List<Integer>> missionResult;
    public LinkedHashSet<Integer> candidatesId;
    public Iterator<Integer> candidate_iterator;
    public ArrayList<Gamer> gamerList;
    public String date;

    private int focusedCandidatePosition;
    private GamePresenter presenter;

    public enum Result {
        WIN(1),
        NEUTRAL(0),
        LOSE(-1);

        private final int num;
        Result(int num) {this.num = num;}
        public int num() { return num;}
    }
    public Game(GamePresenter presenter){
        this.result = Result.NEUTRAL;
        this.maxRound = 5;
        this.memberSelected = 0;
        this.currentMission = 0;
        this.timeOfTeamPropose = 1;
        this.victory = 0;
        this.lose = 0;
        this.leader = 0;
        this.round = 0;
        this.candidatesId = new LinkedHashSet<>();
        this.missionResult = new ArrayList<>(this.maxRound);
        this.presenter = presenter;
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTimeInMillis(System.currentTimeMillis());
        final int year = myCalendar.get(Calendar.YEAR);
        final int month = myCalendar.get(Calendar.MONTH) + 1;
        final int day = myCalendar.get(Calendar.DAY_OF_MONTH);

        if (month < 10) {
            date = year + "-0" + month + "-" + day;
        } else {
            date = year + "-" + month + "-" + day;
        }
    }

    public void updateGameByEvent(ResistanceGameEvent event, int extra) {
        presenter.updateViewBeforeEvent(event, extra);
        switch (event.getType()) {
            case ResistanceGameEvent.PROPOSE:
                if (timeOfTeamPropose == 5) {
                    updateGameByEvent(
                            new ResistanceGameEvent(ResistanceGameEvent.PROPOSE_PASSED),
                            0);
                } else {
                    presenter.updateViewAfterEvent(event);
                }
                break;
            case ResistanceGameEvent.PROPOSE_PASSED:
                timeOfTeamPropose = 1;
                // Check if game is over
                if (victory == 2 || lose == 2) {
                    int fail = 0;
                    for (Integer i:candidatesId) {
                        if (gamerList.get(i).getIdentity() < 0)
                            fail++;
                    }
                    if (victory == 2 && checkMissionResult(fail)) {
                        updateGameByEvent(
                                new ResistanceGameEvent(ResistanceGameEvent.RESISTANCE_WIN),
                                0);
                    } else if (lose == 2 && !checkMissionResult(fail)) {
                        updateGameByEvent(new ResistanceGameEvent(ResistanceGameEvent.SPY_WIN), 0);
                    }
                }
                if (result == Result.NEUTRAL)
                    updateGameByEvent(
                            new ResistanceGameEvent(ResistanceGameEvent.MISSION_EXECUTION_START),
                            0);
                break;
            case ResistanceGameEvent.PROPOSE_VETO:
                timeOfTeamPropose++;
                round++;
                presenter.updateViewAfterEvent(event);
                break;
            case ResistanceGameEvent.MISSION_EXECUTION_START:
                prepareMissionExecution();
                presenter.updateViewAfterEvent(event);
                break;
            case ResistanceGameEvent.MISSION_EXECUTION_CONTINUE:
                getCurrentMissionResult().add((getFocusedCandidatePosition() + 1) * extra);
                changeFocusedCandidate();
                if (memberSelected == getCurrentMissionResult().size()) {
                    updateGameByEvent(
                            new ResistanceGameEvent(ResistanceGameEvent.MISSION_EXECUTION_COMPLETED),
                            0);
                } else {
                    presenter.updateViewAfterEvent(event);
                }
                break;
            case ResistanceGameEvent.MISSION_EXECUTION_COMPLETED:
                endMissionExcution();
                presenter.updateViewAfterEvent(event);
                break;
            case ResistanceGameEvent.MISSION_SUCCEED:
                victory++;
                break;
            case ResistanceGameEvent.MISSION_FAILED:
                lose++;
                break;
            case ResistanceGameEvent.SHOW_RESULTS:
                round++;
                int fail = 0;
                for (Integer i:getCurrentMissionResult()) {
                    if (i < 0) {
                        fail++;
                    }
                }
                if (checkMissionResult(fail))
                    updateGameByEvent(
                            new ResistanceGameEvent(ResistanceGameEvent.MISSION_SUCCEED),
                            fail);
                else
                    updateGameByEvent(
                            new ResistanceGameEvent(ResistanceGameEvent.MISSION_FAILED),
                            fail);

                if (currentMission < 4 && result == Result.NEUTRAL) {
                    currentMission++;
                    memberSelected = DataSet.NumOfMembersPerMission[getTotalPlayers()][currentMission];
                    missionResult.add(new ArrayList<Integer>(memberSelected));
                    presenter.updateViewAfterEvent(event);
                }
                break;
            case ResistanceGameEvent.RESISTANCE_WIN:
                endGame(Result.WIN);
                presenter.updateViewAfterEvent(event);
                break;
            case ResistanceGameEvent.SPY_WIN:
                endGame(Result.LOSE);
                presenter.updateViewAfterEvent(event);
                break;
        }

    }
    public int getFocusedCandidatePosition() {
        return focusedCandidatePosition;

    }
    public void prepareMissionExecution() {
        candidate_iterator = candidatesId.iterator();
        focusedCandidatePosition = candidate_iterator.next();
    }
    public Gamer getFocusedCandidate() {
        return gamerList.get(focusedCandidatePosition);
    }

    public void changeFocusedCandidate() {
        if (candidate_iterator.hasNext())
       focusedCandidatePosition = candidate_iterator.next();
    }
    
    public void endMissionExcution() {
        focusedCandidatePosition = -1;
    }
    public int getTotalPlayers() {
        if (gamerList == null) return 0;

        return gamerList.size();
    }

    public List<Integer> getCurrentMissionResult() {
        return missionResult.get(currentMission);
    }

    public void resetCandidates() {
        candidatesId.clear();
    }

    public boolean isPlayerSelected(int position) {
        return candidatesId.contains(position);
    }

    public void addCandidate (int position) {
        candidatesId.add(position);
    }

    public void removeCandidate( int position) {
        candidatesId.remove(position);
    }

    public int getLeaderPosition() {
        return round % getTotalPlayers();
    }

    public Gamer getLeader() {
        return gamerList.get(round % getTotalPlayers());
    }

    public void endGame(Result result ) {
        this.result = result;
        List<Integer> list = getCurrentMissionResult();
        for (Integer i:candidatesId) {
            list.add((i + 1) * (gamerList.get(i).getIdentity() / 100));
        }
    }

    public boolean checkMissionResult(int fail) {
        if (fail == 0)
            return true;
        else if (getTotalPlayers() > 6 && currentMission ==3 && fail ==1)
            return true;
        else
            return false;
    }

}
