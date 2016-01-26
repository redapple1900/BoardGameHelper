package com.yuanwei.resistance;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.model.protocol.PlotListener;
import com.yuanwei.resistance.partygame.origin.model.Resistance;
import com.yuanwei.resistance.partygame.origin.ui.BaseResistanceGamingFragment;
import com.yuanwei.resistance.ui.fragment.ResistanceGamingFragment;
import com.yuanwei.resistance.util.playerdatabase.PlayerDataSource;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends BasePlotActivity {
    private boolean qe = false;
    private String tag = "tag";
    private PlayerDataSource datasource;
    private PlotListener plotListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ArrayList<User> list = new ArrayList<>();

        int game;

        if (BuildConfig.DEBUG && qe) {

            for (int i = 0; i < 5; i++) {
                User user = new User();
                user.setIdentity(Resistance.Role.RESISTANT.getRoleId());
                user.setName("Resistant" + i);
                user.setResId(R.drawable.index);
                list.add(user);
            }

            for (int i = 0; i < 3; i++) {
                User user = new User();
                user.setIdentity(Resistance.Role.SPY.getRoleId());
                user.setName("SPY" + i);
                user.setResId(R.drawable.index);
                list.add(user);
            }

            Collections.shuffle(list);
            game = 1;
        } else {
            list = getIntent().getExtras().getParcelableArrayList(Constants.USERLIST_KEY);
            game = getIntent().getExtras().getInt(Constants.GAME);
        }


        Fragment fragment = new ResistanceGamingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.USERLIST_KEY, list);
        bundle.putInt(Constants.GAME, game);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.content, fragment, tag).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (fragment != null && fragment instanceof BaseResistanceGamingFragment) {
                ((BaseResistanceGamingFragment) fragment)
                        .showDialog(BaseResistanceGamingFragment.EXIT);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showRules() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_gamerules);
        Button negative = (Button) dialog
                .findViewById(R.id.button_dialog_rules);

        negative.setBackgroundColor(getResources().getColor(
                android.R.color.darker_gray));
        negative.setText(getString(R.string.string_builder_positive_game));
        negative.setTextColor(getResources().getColor(
                android.R.color.primary_text_light));
        negative.setPadding(1, 1, 1, 1);

        negative.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public PlotListener getPlotListener() {
        return this.plotListener;
    }

    @Override
    public void setPlotListener(PlotListener listener) {
        plotListener = listener;
    }
}
