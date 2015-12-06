package com.yuanwei.resistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.partygame.avalon.ui.AvalonSetupFragment;
import com.yuanwei.resistance.partygame.origin.ui.GeneralSetupFragment;

import java.util.ArrayList;

public class SetupActivity extends BasePlotActivity {
    
    private boolean isGameNeeded;

    private ArrayList<User> mUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_setup);

        isGameNeeded =
                getIntent().getExtras().getString("Cards").equals(Constants.GAME_WITHOUT_CARDS);

        int game = getIntent().getExtras().getInt(Constants.GAME);

        mUserList = getIntent().getExtras().getParcelableArrayList(Constants.USERLIST_KEY);

        if (savedInstanceState == null) {
            showFragment(game);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            Intent intent = new Intent();
            intent.setClass(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private void showFragment(int game) {
        Fragment fragment = null;

        switch (game) {
            case Constants.ORIGIN:
                fragment = GeneralSetupFragment.createInstance(isGameNeeded, mUserList);
                break;
            case Constants.AVALON:
                fragment = AvalonSetupFragment.createInstance(isGameNeeded, mUserList);
                break;
        }

        getSupportFragmentManager().beginTransaction().add(R.id.main, fragment).commit();
    }
}
