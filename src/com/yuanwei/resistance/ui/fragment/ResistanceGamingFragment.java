package com.yuanwei.resistance.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.SetupActivity;
import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.fragment.MissionFragment;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.moderator.Announcer;
import com.yuanwei.resistance.moderator.BaseSwitcher;
import com.yuanwei.resistance.moderator.Director;
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
import com.yuanwei.resistance.partygame.origin.model.Resistance.GameEnd;
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

    private Director mDirector;

    private Staff mDiscussionStaff;

    private Scorer mProposeScorer;

    private Switcher mVoteSwitcher;

    private Selector<User> mTeamMemberSelector;

    private Elector mGameElector, mMissionElector;

    private Announcer mMissionAnnouncer;

    private Rotater mLeaderRotater;

    protected void initStaff() {
        mDirector = new Director(getActivity());

        mDiscussionStaff = new Staff(new OnActListener() {
            @Override
            public void onInitiate() {
                showFragment(DISCUSSION);
            }

            @Override
            public void onComplete() {
                hideFragment(DISCUSSION);

                mVoteSwitcher.prepare(1, 1, 1).report(mDirector).initiate();
            }
        });

        mProposeScorer = new Scorer(new OnCountListener() {

            @Override
            public void onCountChange(int before, int after) {
                if (after == 5) {
                    mBookkeeper.updateGameResults(
                            getActivity(),
                            Resistance.GameEnd.SPY_WIN_PROPOSE);
                    mLeaderRotater.complete();
                    mGameElector.complete();
                }
                updateStatusView(PROPOSE, after);
            }

            @Override
            public void onInitiate() {
                updateStatusView(PROPOSE, 0);
            }

            @Override
            public void onComplete() {
            }
        });

        // TODO: This is indeed a terrible use logic handler
        mVoteSwitcher = new Switcher(new OnSwitchListener() {
            @Override
            public void onSwitch(int flag) {

                switch (flag) {
                    case BaseSwitcher.SECONDARY:
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

                if (mVoteSwitcher.isPrimaryReached()) {
                    mBookkeeper.updateProposeResults(Resistance.Propose.APPROVAL);
                    startMission();
                } else {
                    mBookkeeper.updateProposeResults(Resistance.Propose.VETO);
                    mLeaderRotater.rotate();
                    startPropose();
                }
            }
        });

        mGameElector = new Elector(new OnSwitchListener() {

            @Override
            public void onSwitch(int flag) {
                mBookkeeper.keepGameResults(mGameElector.provide());

                if (flag == BaseSwitcher.PRIMARY) {
                    if (getGameId() == Constants.AVALON
                            && mConfig.isRoleEnabled(Avalon.Role.ASSASSIN)) {
                        mBookkeeper.updateGameResults(getActivity(), GameEnd.ASSASSINATION);
                    } else
                        mBookkeeper.updateGameResults(getActivity(), GameEnd.RESISTANCE_WIN);
                } else {
                    mBookkeeper.updateGameResults(getActivity(), GameEnd.SPY_WIN_MISSION);
                }
            }

            @Override
            public void onInitiate() {
                updateStatusView(WIN, 0);
                updateStatusView(LOSE, 0);
            }

            @Override
            public void onComplete() {}
        });

        mMissionElector = new Elector(new OnSwitchListener() {
            @Override
            public void onSwitch(int flag) {
                switch (flag) {
                    case BaseSwitcher.PRIMARY:
                        mBookkeeper.updateMissionResult(SUCCEED);
                        break;
                    case BaseSwitcher.SECONDARY:
                        mBookkeeper.updateMissionResult(FAILED);
                        break;
                }
            }
            @Override
            public void onInitiate() {
            }

            @Override
            public void onComplete() {
                mBookkeeper.keepMissionList(mMissionElector.provide());

                mGameElector.vote(
                        mMissionElector.isPrimaryReached() ?
                                BaseSwitcher.PRIMARY :
                                BaseSwitcher.SECONDARY);

                mLeaderRotater.rotate();

                if (mGameElector.isPrimaryReached() || mGameElector.isSecondaryReached()){
                    mLeaderRotater.complete();
                }

                mProposeScorer.prepare(5).report(mDirector).initiate();
                startPropose();

                updateStatusView(WIN, mGameElector.getPrimaryCount());
                updateStatusView(LOSE, mGameElector.getSecondaryCount());
            }
        });

        mTeamMemberSelector = new Selector<>(new OnCountListener() {

            @Override
            public void onCountChange(int before, int after) {
            }

            @Override
            public void onInitiate() {
                updateStatusView(MISSION,
                        Resistance.getNumberForMission(
                                mConfig.getNumberOfPlayers(),
                                mGameElector.getCount()));
                showFragment(PROPOSE);
            }

            @Override
            public void onComplete() {
                mBookkeeper.keepProposeResults(mTeamMemberSelector.provide());

                hideFragment(PROPOSE);

                if (!mConfig.isOptionEnabled(Resistance.Option.MANUEL_GAME_OVER) &&
                        mProposeScorer.getCount() == 4) {
                    mBookkeeper.updateProposeResults(Resistance.Propose.APPROVAL);
                    startMission();
                    return;
                }

                mDiscussionStaff.report(mDirector).initiate();
            }
        });

        mMissionAnnouncer = new Announcer(this);

        mLeaderRotater = new Rotater(new Rotater.OnRotateListener() {

            @Override
            public void onInitiate() {
                mBookkeeper.getGamer(mLeaderRotater.getItem(0)).setLeader(true);
            }

            @Override
            public void onRoundStart() {
                mBookkeeper.getGamer(
                        mLeaderRotater.getItem(mLeaderRotater.getCount())).setLeader(false);
            }

            @Override
            public void onRoundComplete() {
                mBookkeeper.getGamer(
                        mLeaderRotater.getItem(mLeaderRotater.getCount())).setLeader(true);

                if (mBookkeeper.getGameEnd() == GameEnd.ASSASSINATION) {
                    String name = "";
                    for (User user : mUserList) {
                        if (mBookkeeper.getGamer(user).getRoleId()
                                == Avalon.Role.ASSASSIN.getRoleId()) {
                            name = user.getName();
                            break;
                        }
                    }
                    showTransitDialog(name);
                } else if (mBookkeeper.getLastPropose() == Resistance.Propose.APPROVAL) {
                    showMissionResultDialog();
                } else {
                    showTransitDialog();
                }
            }

            @Override
            public void onComplete() {
                mBookkeeper.getGamer(
                        mLeaderRotater.getItem(mLeaderRotater.getCount())).setLeader(false);
            }
        });

        // TODO: DO not hard code constants
        mGameElector.prepare(5, 3, 3).report(mDirector).initiate();

        mProposeScorer.prepare(5).report(mDirector).initiate();

        mLeaderRotater.prepare(mUserList).report(mDirector).initiate();
    }

    @Override
    public void onInitiate() {
        mMissionAnnouncer.next();
    }

    @Override
    public void onIntroduceStart() {
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
                else {
                    mBookkeeper.keepProposeResults(new ArrayList<User>());
                    mBookkeeper.updateProposeResults(Resistance.Propose.PASS);

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
                // TODO: use book keeper for all
                mTeamMemberSelector.select(mLeaderRotater.getItem(extra));
                break;
            case OVER:
                if (extra == BaseSwitcher.PRIMARY) {
                    showReplayRemoveUserDialog();
                } else
                    rateAppOnGooglePlay();
                break;
            case ASSASSINATION:
                // TODO::make gamer a wrapper of user
                int roleId = mBookkeeper.getGamer(mBookkeeper.getUser(extra)).getRoleId();

                if (roleId < 0) return;

                if (roleId == Avalon.Role.MERLIN.getRoleId()) {
                    mBookkeeper.updateGameResults(getActivity(), GameEnd.SPY_WIN_MISSION);
                } else {
                    mBookkeeper.updateGameResults(getActivity(), GameEnd.RESISTANCE_WIN);
                }
                hideFragment(PROPOSE);
                showFragment(OVER);
                break;
            case REPLAY: // Event comes from a dialog
                if (extra == BaseSwitcher.PRIMARY) {
                    myStartActivity(new Intent()
                            .putExtra("Cards", Constants.GAME_WITHOUT_CARDS)
                            .putExtra(Constants.GAME, getGameId())
                            .putExtra(Constants.USERLIST_KEY, mUserList)
                            .setClass(getActivity(), SetupActivity.class));
                    getActivity().finish();
                } else {
                    hideFragment(PROPOSE);
                    hideFragment(OVER);
                    showFragment(REMOVE);
                }
                break;
            case REMOVE:
                if (extra == BaseSwitcher.PRIMARY) {
                    myStartActivity(new Intent()
                            .putExtra("Cards", Constants.GAME_WITHOUT_CARDS)
                            .putExtra(Constants.GAME, getGameId())
                            .putExtra(Constants.USERLIST_KEY, mUserList)
                            .setClass(getActivity(), SetupActivity.class));
                    getActivity().finish();
                } else if (extra == BaseSwitcher.SECONDARY) {
                    rateAppOnGooglePlay();
                } else {
                    mUserList.remove(extra);
                }
        }
    }

    @Override
    public int getGameId() {
        return mConfig.getGame();
    }

    @Override
    protected int getCurrentPlayerIdentity() {
        return mBookkeeper.getGamer(mMissionAnnouncer.getCurrentItem()).getRoleId();
    }

    @Override
    protected  User getLeader() {
        return mLeaderRotater.getItem(mLeaderRotater.getCount());
    }

    @Override
    protected  User getCurrentGamer() {
        return mMissionAnnouncer.getCurrentItem();
    }

    @Override
    protected boolean getMissionResult() {
        return mGameElector.getLastItem() == BaseSwitcher.PRIMARY;
    }

    @Override
    protected int getMissionLimit() {
        return Resistance.getNumberForMission(mConfig.getNumberOfPlayers(), mGameElector.getCount());
    }

    @Override
    protected int getFailCount() {
        return mMissionElector.getSecondaryCount();
    }

    @Override
    protected void start() {
        startPropose();
    }

    private void startPropose() {

        int total = mConfig.getNumberOfPlayers();
        int round = mGameElector.getCount();
        mTeamMemberSelector
                .prepare(Resistance.getNumberForMission(total, round))
                .report(mDirector)
                .initiate();
    }

    private void startMission() {
        mProposeScorer.complete();

        List<User> list = mBookkeeper.getLastProposeList();

        mBookkeeper.keepTeamResults(list);

        if (mGameElector.getCount() > 0 && getGameId() == Constants.AVALON &&
                mConfig.isRoleEnabled(Avalon.Role.LANCELOT_ARTHUR)) {
            mBookkeeper.swap(
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
                .report(mDirector)
                .initiate();

        if (maybeEndGame()) {
            return;
        }

        mMissionAnnouncer.prepare(list).report(mDirector).initiate();
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
            List<User> list = mBookkeeper.getLastProposeList();
            for (User user : list) {
                if (mBookkeeper.getGamer(user).getRoleId() > 0) { // TODO: make it human readable
                    possibleSuccess ++;
                }
            }
            if (possibleSuccess >= Resistance.getSuccess(total, mission)) {
                executeMissionAutomatically(list);
                return true;
            }
        }

        if (mGameElector.getSecondaryCount() == 2) {
            int possibleFail = 0;
            List<User> list = mBookkeeper.getLastProposeList();
            for (User user : list) {
                if (mBookkeeper.getGamer(user).getRoleId() < 0){ // TODO: make it human readable
                    possibleFail ++;
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
            if (mBookkeeper.getGamer(user).getRoleId() > 0) { // TODO: make it human readable
                mMissionElector.vote(BaseSwitcher.PRIMARY);
            } else {
                mMissionElector.vote(BaseSwitcher.SECONDARY);
            }
        }
        mMissionElector.complete();
    }

    // TODO:: make it as a utility
    //On click event for rate this app button
    private void rateAppOnGooglePlay() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Try Google play
        intent.setData(Uri.parse("market://details?id=" + "com.yuanwei.resistance"));
        if (!myStartActivity(intent)) {
            //Market (Google play) app seems not installed, let's try to open a webbrowser
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?" + "com.yuanwei.resistance"));
            if (!myStartActivity(intent)) {
                //Well if this also fails, we have run out of options, inform the user.
                Toast.makeText(getActivity(), "Could not open Google Play, please install the market app.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean myStartActivity(Intent intent) {
        try {
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), getString(R.string.string_toast2_game), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
