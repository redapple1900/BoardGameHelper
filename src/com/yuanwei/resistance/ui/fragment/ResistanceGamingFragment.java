package com.yuanwei.resistance.ui.fragment;

import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.fragment.MissionFragment;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.moderator.Announcer;
import com.yuanwei.resistance.moderator.BaseSwitcher;
import com.yuanwei.resistance.moderator.Elector;
import com.yuanwei.resistance.moderator.Rotater;
import com.yuanwei.resistance.moderator.Scorer;
import com.yuanwei.resistance.moderator.Selector;
import com.yuanwei.resistance.moderator.Staff;
import com.yuanwei.resistance.moderator.Switcher;
import com.yuanwei.resistance.moderator.protocol.OnActListener;
import com.yuanwei.resistance.moderator.protocol.OnAnnounceListener;
import com.yuanwei.resistance.moderator.protocol.OnCountListener;
import com.yuanwei.resistance.moderator.protocol.OnSwitchListener;
import com.yuanwei.resistance.partygame.avalon.model.Avalon;
import com.yuanwei.resistance.partygame.origin.model.Resistance;
import com.yuanwei.resistance.partygame.origin.model.Resistance.GameStatus;
import com.yuanwei.resistance.partygame.origin.model.StateKeeper;
import com.yuanwei.resistance.partygame.origin.ui.BaseResistanceGamingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyuanwei on 15/10/25.
 */
public class ResistanceGamingFragment extends BaseResistanceGamingFragment
        implements OnAnnounceListener {
    // TODO: too hacky
    private int SUCCEED = Resistance.WIN;

    private int FAILED = Resistance.LOSE;

    private Staff mDiscussionStaff;

    private Scorer mProposeScorer;

    private Switcher mVoteSwitcher;

    private Selector<User> mTeamMemberSelector;

    private Elector mGameElector, mMissionElector;

    private Announcer mMissionAnnouncer;

    private Rotater mLeaderRotater;

    protected void initStaff() {

        mDiscussionStaff = new Staff(new OnActListener() {
            @Override
            public void onInitiate() {
                showFragment(DISCUSSION);
            }

            @Override
            public void onComplete() {
                hideFragment(DISCUSSION);

                mVoteSwitcher.prepare(1, 1, 1).initiate();
            }
        });

        mProposeScorer = new Scorer(new OnCountListener() {

            @Override
            public void onCountChange(int before, int after) {
                // TODO:: no hard coded number
                if (after == 5) {
                    mBookKeeper.updateGameResults(
                            getActivity(),
                            GameStatus.SPY_WIN);
                    mStateKeeper.keep(StateKeeper.State.OVER);
                    mLeaderRotater.complete();
                    mGameElector.complete();
                }
                mStateKeeper.keep(StateKeeper.PROPOSE_KEY, after);
                updateStatusView(PROPOSE, after);
            }

            @Override
            public void onInitiate() {
                updateStatusView(PROPOSE, mProposeScorer.getCount());
            }

            @Override
            public void onComplete() {
                mStateKeeper.keep(StateKeeper.PROPOSE_KEY, 0);
            }
        });

        mVoteSwitcher = new Switcher(new OnSwitchListener() {
            @Override
            public void onSwitch(int flag) {

                switch (flag) {
                    case BaseSwitcher.PRIMARY:
                        mBookKeeper.keepVote(Resistance.Vote.APPROVAL);
                        mStateKeeper.keep(StateKeeper.State.MISSION);
                        mProposeScorer.complete();
                        break;
                    case BaseSwitcher.SECONDARY:
                        showTransitDialog();
                        mBookKeeper.keepVote(Resistance.Vote.VETO);
                        mLeaderRotater.rotate();
                        mProposeScorer.increment();
                        break;
                }

                mVoteSwitcher.complete();
            }

            @Override
            public void onInitiate() {
                showFragment(VOTE);
            }

            @Override
            public void onComplete() {
                hideFragment(VOTE);
                step(mStateKeeper.getState());
            }
        });

        mGameElector = new Elector(new OnSwitchListener() {

            @Override
            public void onSwitch(int flag) {
                mLeaderRotater.complete();
                mBookKeeper.keepMissionResult(mGameElector.provide());

                GameStatus status = GameStatus.SPY_WIN;
                StateKeeper.State state = StateKeeper.State.OVER;

                if (flag == BaseSwitcher.PRIMARY) {
                    if (getGameId() == Constants.AVALON
                            && mConfig.isRoleEnabled(Avalon.Role.ASSASSIN)) {
                        status = GameStatus.ASSASSINATION;
                        state = StateKeeper.State.ASSASSINATION;
                    } else {
                        status = GameStatus.RESISTANCE_WIN;
                    }
                }

                mStateKeeper.keep(state);
                mBookKeeper.updateGameResults(getActivity(), status);
            }

            @Override
            public void onInitiate() {
                updateStatusView(WIN, mGameElector.getPrimaryCount());
                updateStatusView(LOSE, mGameElector.getSecondaryCount());
                updateStatusView(MISSION, Resistance.getNumberForMission(
                        mConfig.getNumberOfPlayers(),
                        mGameElector.getCount()));

                mProposeScorer.prepare(5).load(mStateKeeper.getPropose()).initiate();
                mLeaderRotater.prepare(mUserList).load(mStateKeeper.getLeader()).initiate();
            }

            @Override
            public void onComplete() {
                if (mBookKeeper.getGameStatus() == GameStatus.ASSASSINATION) {
                    String name = "";
                    for (User user : mUserList) {
                        if (mBookKeeper.getGamer(user).getRoleId()
                                == Avalon.Role.ASSASSIN.getRoleId()) {
                            name = user.getName();
                            break;
                        }
                    }
                    showTransitDialog(name);
                }
            }
        });

        mMissionElector = new Elector(new OnSwitchListener() {
            @Override
            public void onSwitch(int flag) {
                switch (flag) {
                    case BaseSwitcher.PRIMARY:
                        mBookKeeper.updateMissionResult(SUCCEED);
                        break;
                    case BaseSwitcher.SECONDARY:
                        mBookKeeper.updateMissionResult(FAILED);
                        break;
                }
            }

            @Override
            public void onInitiate() {
            }

            @Override
            public void onComplete() {
                mBookKeeper.keepMissionExecution(mMissionElector.provide());

                mGameElector.vote(mMissionElector.isPrimaryReached() ?
                        BaseSwitcher.PRIMARY :
                        BaseSwitcher.SECONDARY);

                StateKeeper.State state = StateKeeper.State.PROPOSE;
                if (mGameElector.isPrimaryReached()) {
                    state = mConfig.isRoleEnabled(Avalon.Role.ASSASSIN) ?
                            StateKeeper.State.ASSASSINATION :
                            StateKeeper.State.OVER;
                }

                if (mGameElector.isSecondaryReached()) {
                    state = StateKeeper.State.OVER;
                }

                mStateKeeper.keep(state);
                mStateKeeper.keep(StateKeeper.ROUND_KEY, mGameElector.getCount());
                mStateKeeper.keep(StateKeeper.WIN_KEY, mGameElector.getPrimaryCount());
                mStateKeeper.keep(StateKeeper.LOSE_KEY, mGameElector.getSecondaryCount());

                updateStatusView(WIN, mGameElector.getPrimaryCount());
                updateStatusView(LOSE, mGameElector.getSecondaryCount());

                mLeaderRotater.rotate();

                showDialog(MISSION);

                step(mStateKeeper.getState());
            }
        });

        mTeamMemberSelector = new Selector<>(new OnCountListener() {

            @Override
            public void onCountChange(int before, int after) {
            }

            @Override
            public void onInitiate() {
                updateStatusView(MISSION, Resistance.getNumberForMission(
                        mConfig.getNumberOfPlayers(),
                        mGameElector.getCount()));
                showFragment(PROPOSE);
            }

            @Override
            public void onComplete() {
                mBookKeeper.keepPropose(mTeamMemberSelector.provide());

                hideFragment(PROPOSE);

                if (!mConfig.isOptionEnabled(Resistance.Option.MANUEL_GAME_OVER) &&
                        mProposeScorer.getCount() == 4) {
                    mBookKeeper.keepVote(Resistance.Vote.APPROVAL);
                    mStateKeeper.keep(StateKeeper.State.MISSION);

                    mProposeScorer.complete();

                    step(mStateKeeper.getState());

                    return;
                }

                mDiscussionStaff.initiate();
            }
        });

        mMissionAnnouncer = new Announcer(this);

        mLeaderRotater = new Rotater(new Rotater.OnRotateListener() {

            @Override
            public void onInitiate() {
                mBookKeeper.getGamer(mLeaderRotater.getCount()).setLeader(true);
            }

            @Override
            public void onRoundStart() {
                mBookKeeper.getGamer(mLeaderRotater.getCount()).setLeader(false);
            }

            @Override
            public void onRoundComplete() {
                mBookKeeper.getGamer(mLeaderRotater.getCount()).setLeader(true);
                mStateKeeper.keep(StateKeeper.LEADER_KEY, mLeaderRotater.getCount());
            }

            @Override
            public void onComplete() {
                mBookKeeper.getGamer(mLeaderRotater.getCount()).setLeader(false);
            }
        });

        mTeamMemberSelector.setTag(SELECTION);

        mDiscussionStaff.setTag(DISCUSSION);

        mVoteSwitcher.setTag(VOTE);

        mMissionAnnouncer.setTag("announcer");

        mMissionElector.setTag(MISSION);

        mGameElector.setTag("game");

        mProposeScorer.setTag(PROPOSE);

        mLeaderRotater.setTag("leader");
    }

    @Override
    public void onInitiate() {
        mMissionAnnouncer.next();
    }

    @Override
    public void onIntroduceStart() {
        mStateKeeper.keep(
                StateKeeper.GAMER_KEY, mBookKeeper.getIndex(mMissionAnnouncer.getCurrentItem()));
        showTransitDialog();
        showFragment(PRESS);
    }

    @Override
    public void onIntroduce() {
        hideFragment(PRESS);
        showFragment(MISSION);
    }

    @Override
    public void onIntroduceDone() {
        hideFragment(MISSION);
        mMissionAnnouncer.next();
    }

    @Override
    public void onComplete() {
        mMissionElector.complete();
    }

    @Override
    public void onEventStart(String type, int extra) {

        switch (type) {
            case DISCUSSION:
                mDiscussionStaff.complete();
                break;
            case MISSION:
                // TODO: Create an enum for mission execution result
                if (extra == MissionFragment.SUCCEED)
                    mMissionElector.vote(BaseSwitcher.PRIMARY);
                else
                    mMissionElector.vote(BaseSwitcher.SECONDARY);

                mMissionAnnouncer.next();
                break;
            case PROPOSE:
                if (extra == BaseSwitcher.PRIMARY)
                    mTeamMemberSelector.complete();
                    // TODO: NO Writes in these methods
                else {
                    mBookKeeper.keepPropose(new ArrayList<User>());
                    mBookKeeper.keepVote(Resistance.Vote.PASS);

                    hideFragment(PROPOSE);

                    mTeamMemberSelector.terminate();

                    mProposeScorer.increment();
                    mLeaderRotater.rotate();

                    startPropose();
                }
                break;
            case PRESS:
                mMissionAnnouncer.next();
                break;
            case VOTE:
                if (extra == BaseSwitcher.PRIMARY)
                    mVoteSwitcher.increment(BaseSwitcher.PRIMARY);
                else
                    mVoteSwitcher.increment(BaseSwitcher.SECONDARY);
                break;
            case SELECTION:
                mTeamMemberSelector.select(mBookKeeper.getUser(extra));
                break;
            case OVER:
                if (extra == BaseSwitcher.PRIMARY) {
                    clear();
                    showDialog(type);
                } else
                    rateAppOnGooglePlay();
                break;
            case ASSASSINATION:
                // TODO::make gamer a wrapper of user
                int roleId = mBookKeeper.getGamer(mBookKeeper.getUser(extra)).getRoleId();

                if (roleId < 0) return;

                mStateKeeper.keep(StateKeeper.State.OVER);

                mBookKeeper.updateGameResults(
                        getActivity(),
                        roleId == Avalon.Role.MERLIN.getRoleId() ?
                                GameStatus.SPY_WIN :
                                GameStatus.RESISTANCE_WIN);

                hideFragment(ASSASSINATION);
                step(mStateKeeper.getState());
                break;
            case REPLAY: // Event comes from a dialog
                if (extra == BaseSwitcher.PRIMARY) {
                    startSetupActivity();
                } else {
                    hideFragment(ASSASSINATION);
                    hideFragment(OVER);
                    showFragment(REMOVE);
                }
                break;
            case REMOVE:
                if (extra == BaseSwitcher.PRIMARY) {
                    startSetupActivity();
                } else if (extra == BaseSwitcher.SECONDARY) {
                    rateAppOnGooglePlay();
                } else {
                    mUserList.remove(extra);
                }
        }
    }

    @Override
    protected void start() {
        mGameElector.prepare(5, 3, 3)
                .load(mStateKeeper.getRound(), mStateKeeper.getWin(), mStateKeeper.getLose())
                .initiate();
        step(mStateKeeper.getState());
    }

    private void step(StateKeeper.State state) {
        switch (state) {
            case PROPOSE:
                startPropose();
                break;
            case MISSION:
                startMission();
                break;
            case OVER:
                mGameElector.complete();
                showFragment(OVER);
                break;
            case ASSASSINATION:
                showFragment(ASSASSINATION);
                break;
        }
    }

    /**
     * Tip: Never keep state/log in following action methods.
     */
    private void startPropose() {

        if (!mProposeScorer.isInitiated()) {
            mProposeScorer.prepare(5).initiate();
        }

        int total = mConfig.getNumberOfPlayers();
        int round = mGameElector.getCount();
        mTeamMemberSelector
                .prepare(Resistance.getNumberForMission(total, round))
                .initiate();

        mStateKeeper.keep(StateKeeper.State.PROPOSE);
    }

    private void startMission() {
        if (mGameElector.getCount() > 0 && getGameId() == Constants.AVALON &&
                mConfig.isRoleEnabled(Avalon.Role.LANCELOT_ARTHUR)) {
            mBookKeeper.swap(
                    Avalon.Role.LANCELOT_ARTHUR.getRoleId(),
                    Avalon.Role.LANCELOT_MORDRED.getRoleId());
            playSound();
        }

        int total = mConfig.getNumberOfPlayers();
        int round = mGameElector.getCount();
        int num = Resistance.getNumberForMission(total, round);

        mMissionElector.prepare(
                num,
                Resistance.getSuccess(total, round),
                Resistance.getFailure(total, round))
                .initiate();

        if (maybeEndGame()) {
            return;
        }

        mMissionAnnouncer.prepare(mBookKeeper.getLastPropose()).initiate();
    }

    private boolean maybeEndGame() {
        int total = mConfig.getNumberOfPlayers();
        int mission = mGameElector.getCount();

        if (mGameElector.getPrimaryCount() == 3 || mGameElector.getSecondaryCount() == 3) {
            return true;
        }

        if (mConfig.isOptionEnabled(Resistance.Option.MANUEL_GAME_OVER)) {
            return false;
        }

        if (mGameElector.getPrimaryCount() == 2) {
            int possibleSuccess = 0;
            List<User> list = mBookKeeper.getLastPropose();
            for (User user : list) {
                if (mBookKeeper.getGamer(user).getRoleId() > 0) { // TODO: make it human readable
                    possibleSuccess++;
                }
            }
            if (possibleSuccess >= Resistance.getSuccess(total, mission)) {
                executeMissionAutomatically(list);
                return true;
            }
        }

        if (mGameElector.getSecondaryCount() == 2) {
            int possibleFail = 0;
            List<User> list = mBookKeeper.getLastPropose();
            for (User user : list) {
                if (mBookKeeper.getGamer(user).getRoleId() < 0) { // TODO: make it human readable
                    possibleFail++;
                }
            }
            if (possibleFail >= Resistance.getFailure(total, mission)) {
                executeMissionAutomatically(list);
                return true;
            }
        }

        return false;
    }

    private void executeMissionAutomatically(List<User> list) {
        for (User user : list) {
            if (mBookKeeper.getGamer(user).getRoleId() > 0) { // TODO: make it human readable
                mMissionElector.vote(BaseSwitcher.PRIMARY);
            } else {
                mMissionElector.vote(BaseSwitcher.SECONDARY);
            }
        }
        mMissionElector.complete();
    }
}
