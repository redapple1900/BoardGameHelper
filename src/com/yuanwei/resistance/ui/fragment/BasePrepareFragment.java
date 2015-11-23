package com.yuanwei.resistance.ui.fragment;

/**
 * Created by chenyuanwei on 15/10/22.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanwei.resistance.GameActivity;
import com.yuanwei.resistance.R;
import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.fragment.DoubleButtonFragment;
import com.yuanwei.resistance.fragment.LongPressFragment;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.model.protocol.Role;
import com.yuanwei.resistance.moderator.Announcer;
import com.yuanwei.resistance.moderator.protocol.OnAnnounceListener;
import com.yuanwei.resistance.ui.grid.GridRecyclerViewAdapter;
import com.yuanwei.resistance.util.Roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenyuanwei on 15/10/21.
 */
public abstract class BasePrepareFragment extends BaseMultiSceneFragment
        implements OnAnnounceListener {

    public abstract int getGameId();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_prepare, container, false);

        initData();
        initUtility();
        initGridView(v); // Must be after initData();
        initTopView(v);

        return v;
    }

    /* Data */
    private static final String DOUBLE = "double";
    private static final String PRESS = "press";

    protected int mNumberOfPlayers;
    protected ArrayList<User> mUserList;
    protected SharedPreferences share;
    /*View */
    protected AlertDialog.Builder mShowRoleDialogBuilder;
    private AlertDialog mTransitDialog;
    private Dialog mInputNameDialog;

    /* Utility */
    protected Announcer mAnnouncer;
    protected GridRecyclerViewAdapter mGridRecycleViewAdapter;
    protected GridLayoutManager mGridLayoutManager;

    protected void initData() {

        share = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mUserList = getArguments().getParcelableArrayList("gamerList");

        mNumberOfPlayers = mUserList.size();

        // Shuffle identity
        Collections.shuffle(mUserList, new Random(System.currentTimeMillis()));

        // Shuffle pictures
        TypedArray pictures;

        if (share.getString(Constants.THEME, "").equals(Constants.THEME_MILITARY)) {
            pictures = getResources().obtainTypedArray(R.array.images);
        } else {
            pictures = getResources().obtainTypedArray(R.array.icon);
        }

        int i = 0;
        for (User user : mUserList) {
            user.setResId(pictures.getResourceId(i, -1));
            i++;
        }

        pictures.recycle();

        Collections.shuffle(mUserList, new Random(System.currentTimeMillis()));
    }

    protected void initUtility(){
        mAnnouncer = new Announcer(this);

        mAnnouncer.prepare(mNumberOfPlayers).initiate();
    }

    protected void initGridView(View view) {

        mGridRecycleViewAdapter = new GridRecyclerViewAdapter(getActivity());

        mGridRecycleViewAdapter.bindModels(mUserList, null);

        mGridLayoutManager = new GridLayoutManager(
                getActivity(),
                mUserList.size() < 7 ? 3 : 4);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setAdapter(mGridRecycleViewAdapter);

        recyclerView.setLayoutManager(mGridLayoutManager);

        recyclerView.setHasFixedSize(true);
    }

    protected void initTopView(View view) {
        TextView textview_gameSetting = (TextView) view.findViewById(R.id.prepare_top_textview);
        textview_gameSetting.setText(getString(
                R.string.string_gamesetting_main,
                Constants.getNormalPlayers(mNumberOfPlayers),
                Constants.getSpyPlayers(mNumberOfPlayers)));
    }

    protected void startNextActivity() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("gamerList", mUserList);
        bundle.putInt(Constants.GAME, getGameId());
        intent.putExtras(bundle);
        intent.setClass(getActivity(), GameActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    protected void showPlayerRoleDialog() {

        if (mShowRoleDialogBuilder == null) {
            mShowRoleDialogBuilder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
            mShowRoleDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton(
                            getString(R.string.confirmation),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAnnouncer.next();
                                }
                            });
        }

        Role role = Roles.findRole(
                        getGameId(),
                        mUserList.get(mAnnouncer.getCount()).getIdentity());

        mShowRoleDialogBuilder.setTitle(
                getString(R.string.show_role_game_prepare_activity)
                        + getString(role.getTitleResId()))
                .setIcon(role.getImgResId())
                .setMessage(role.getDescResId())
                .show();
    }

    @Override
    public void onEventStart(String type, int extra) {
        switch (type) {
            case LongPressFragment.TAG:
                mAnnouncer.next();
                break;
            case DOUBLE:
                if (extra == DoubleButtonFragment.PRIMARY) {
                    startNextActivity();
                } else if (extra == DoubleButtonFragment.SECONDARY) {
                    playSound();
                }
                break;
        }
    }

    @Override
    protected void onSpeakDone() {
        DoubleButtonFragment doubleButtonFragment =
                (DoubleButtonFragment) getFragmentManager().findFragmentByTag(DOUBLE);
        if (doubleButtonFragment != null)
            doubleButtonFragment.getDoubleButtonView().setButtonEnabled(true, true);
    }


    @Override
    protected void onSpeakStart() {
        DoubleButtonFragment doubleButtonFragment =
                (DoubleButtonFragment) getFragmentManager().findFragmentByTag(DOUBLE);
        if (doubleButtonFragment != null)
            doubleButtonFragment.getDoubleButtonView().setButtonEnabled(true, false);
    }

    @Override
    protected void onSpeakError() {
        Toast.makeText(
                getActivity(),
                getString(R.string.string_playsound_toast),
                Toast.LENGTH_LONG).show();
    }

    // Implementing Introducer.OnInteractionListener

    @Override
    public void onInitiate() {
        showFragment(PRESS);
    }
    
    @Override
    public void onIntroduceStart() {
        if (mUserList.get(mAnnouncer.getCount()).getName() == null)
            showInputNameDialog();
        else
            mAnnouncer.next(); // Skip step
    }

    @Override
    public void onIntroduce() {
        showPlayerRoleDialog();
    }

    @Override
    public void onIntroduceDone() {

        showTransitDialog(mAnnouncer.getCount() + 1 == mNumberOfPlayers);
    }

    @Override
    public void onComplete() {
        hideFragment(PRESS);
        showFragment(DOUBLE);
    }

    // End implementing Introducer.OnInteractionListener

    @Override
    protected Fragment createFragment(String tag) {
        Fragment fragment = null;
        switch (tag) {
            case PRESS:
                fragment = LongPressFragment.createInstance(tag, "");
                break;
            case DOUBLE:
                fragment = DoubleButtonFragment.createInstance(tag);
        }
        return fragment;
    }

    @Override
    protected int getContainer() {
        return R.id.layout_content_main;
    }

    private void showTransitDialog(boolean isLast) {
        
        if (mTransitDialog == null) {
            mTransitDialog = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK)
                    .setMessage(isLast ?
                            getString(R.string.last_transit_message) :
                            getString(R.string.transit_message))
                    .create();
        }

        if (isLast) {
            mTransitDialog.setMessage(getString(R.string.last_transit_message));
            mTransitDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    mAnnouncer.next();
                }
            });
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mTransitDialog.dismiss();
            }
        }, 1000);

        mTransitDialog.show();
    }

    private void showInputNameDialog() {
        if (mInputNameDialog == null) {
            mInputNameDialog = new Dialog(getActivity());

            mInputNameDialog.setContentView(R.layout.view_dialog_input_name);
            mInputNameDialog.setTitle(R.string.string_builder_inputname_title_main);
            mInputNameDialog.setCancelable(false);

            final AutoCompleteTextView ed = (AutoCompleteTextView) mInputNameDialog
                    .findViewById(R.id.autoCompleteTextView1);
            ed.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            final InputMethodManager inputMgr =
                    (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            // inputMgr.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);

            Button positive = (Button) mInputNameDialog
                    .findViewById(R.id.affirmative);

            Button negative = (Button) mInputNameDialog
                    .findViewById(R.id.negative);


            negative.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    inputMgr.hideSoftInputFromWindow(ed.getWindowToken(), 0);
                    updatePlayerName(null);
                    mInputNameDialog.dismiss();
                }
            });
            positive.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ed.getText().toString().isEmpty()
                            || ed.getText().toString().trim().contentEquals("")) {
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                        ed.startAnimation(shake);

                    } else {
                        inputMgr.hideSoftInputFromWindow(ed.getWindowToken(), 0);
                        updatePlayerName(ed.getText().toString().trim());
                        ed.getText().clear();
                        mInputNameDialog.dismiss();
                    }
                }
            });
        }

        mInputNameDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mAnnouncer.next();
            }
        });

        mInputNameDialog.show();
    }

    private void updatePlayerName(String name) {
        int current = mAnnouncer.getCount();

        if (name == null) {
            mUserList.get(current)
                    .setName(getString(R.string.string_item_game) + " " + (1 + current))
                    .setId(-1); // Important, -1 indicates this is an anonymous player

            Toast.makeText(getActivity(), getString(R.string.string_toast1_main), Toast.LENGTH_SHORT).show();
        } else {
            mUserList.get(current).setName(name);
        }

        mGridRecycleViewAdapter.notifyItemChanged(current);
    }

    private void showTerminationDialog() {
        AlertDialog.Builder mTerminationDialogBuilder =
                new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);

        mTerminationDialogBuilder
                .setTitle(getString(R.string.string_main_sound_title))
                .setCancelable(false)
                .setPositiveButton(
                        getString(R.string.string_main_sound_positive),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                    startNextActivity();
                            }})
                .setNeutralButton(
                        getString(R.string.string_main_sound_neutral),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                playSound();
                            }
                        });

        mTerminationDialogBuilder.setMessage(getString(R.string.string_main_sound_message)
                + "\n\n" + getString(R.string.script_close_eyes) + "\n\n"
                + getString(R.string.script_spies_find_each_other) + "\n\n"
                + getString(R.string.script_spies_close_eyes) + "\n\n"
                + getString(R.string.script_open_eyes));

        mTerminationDialogBuilder.show();
    }
}
