package com.yuanwei.resistance;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.partygame.avalon.ui.AvalonSetupFragment;
import com.yuanwei.resistance.partygame.origin.ui.ResistanceSetupFragment;

public class SetupActivity extends BasePlotActivity {
    
    private boolean isGameNeeded;
    private int mGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_setup);

        isGameNeeded =
                getIntent().getExtras().getString("Cards").equals(Constants.GAME_WITHOUT_CARDS);

        mGame = getIntent().getExtras().getInt(Constants.GAME);

        if (savedInstanceState == null) {
            showFragment(mGame);
        }

    }
    
    private void showFragment(int game) {
        Fragment fragment = null;

        switch (game) {
            case Constants.ORIGIN:
                fragment = ResistanceSetupFragment.createInstance(isGameNeeded);
                break;
            case Constants.AVALON:
                fragment = AvalonSetupFragment.createInstance(isGameNeeded);
                break;
        }

        getSupportFragmentManager().beginTransaction().add(R.id.main, fragment).commit();
    }
}
