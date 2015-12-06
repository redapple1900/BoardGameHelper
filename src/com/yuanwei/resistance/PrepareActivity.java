package com.yuanwei.resistance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.partygame.avalon.ui.AvalonPrepareFragment;
import com.yuanwei.resistance.partygame.origin.ui.ResistancePrepareFragment;

import java.util.ArrayList;

public class PrepareActivity extends BasePlotActivity {

    private int mGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mGame = getIntent().getExtras().getInt(Constants.GAME);

        if (savedInstanceState == null) {
            showFragment(mGame);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            showExitGameAlert();
        }
        return super.onKeyDown(keyCode, event);
    }


    private void showExitGameAlert() {
        final AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle(getString(R.string.string_exit_title));
        dlg.setMessage(getString(R.string.string_exit_message));
        dlg.setPositiveButton(getString(R.string.string_exit_positive),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent();
                        intent.setClass(getParent(), WelcomeActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });

        dlg.setNegativeButton(getString(R.string.string_exit_negative),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
        dlg.show();
    }

    private void showFragment(int game) {

        Fragment fragment = null;

        ArrayList<User> users = getIntent().getExtras().getParcelableArrayList("gamerList");

        switch (game) {
            case Constants.ORIGIN:
                fragment = ResistancePrepareFragment.createInstance(users);
                break;
            case Constants.AVALON:
                fragment = AvalonPrepareFragment.createInstance(users);
                break;
        }

        getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
    }
}
