package com.yuanwei.resistance;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;

import com.yuanwei.resistance.util.GeneralMethodSet;

public class QuickPrefsActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        GeneralMethodSet gms = new GeneralMethodSet();
        gms.updateLanguage(this);

        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }
}
