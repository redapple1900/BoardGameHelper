package com.yuanwei.resistance;

import game.redapple1900.resistance.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;



public class CustomGameActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		GeneralMethodSet gms=new GeneralMethodSet();
		gms.updateLanguage(this);
		gms.setActivityTheme(this);
		gms=null;
        setContentView(R.layout.activity_custom_game);


        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(CustomGameFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(CustomGameFragment.ARG_ITEM_ID));
            CustomGameFragment fragment = new CustomGameFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_custom_game_activity, fragment)
                    .commit();
        }
    }


}