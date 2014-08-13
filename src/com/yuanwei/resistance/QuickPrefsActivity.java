package com.yuanwei.resistance;

import game.redapple1900.resistance.R;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;

public class QuickPrefsActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {

  super.onCreate(savedInstanceState);
	GeneralMethodSet gms=new GeneralMethodSet();
	gms.updateLanguage(this);	
	gms=null;

  addPreferencesFromResource(R.xml.preferences);
  
    }
    @Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK )
              startActivity(new Intent(this,WelcomeActivity.class));
 
		return super.onKeyDown(keyCode, event);
	}


}
