package com.yuanwei.resistance;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.yuanwei.resistance.fragment.CustomGameFragment;
import com.yuanwei.resistance.util.GeneralMethodSet;


public class CustomGameActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		GeneralMethodSet gms=new GeneralMethodSet();
		gms.updateLanguage(this);
		gms.setActivityTheme(this);

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