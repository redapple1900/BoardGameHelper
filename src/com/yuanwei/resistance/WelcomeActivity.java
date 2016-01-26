package com.yuanwei.resistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.fragment.ContactFragment;
import com.yuanwei.resistance.fragment.QuickStartFragment;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.model.protocol.PlotHost;
import com.yuanwei.resistance.model.protocol.PlotListener;
import com.yuanwei.resistance.util.GeneralMethodSet;

import java.util.ArrayList;
import java.util.Locale;

/**
 * In this activity, the type of game is determined.
 */
public class WelcomeActivity extends FragmentActivity implements PlotHost, PlotListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keepPropose every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private QuickStartFragment quickstartFragment;
    private ContactFragment contactFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GeneralMethodSet gms = new GeneralMethodSet();
        gms.updateLanguage(this);
        gms.setActivityTheme(this);

        setContentView(R.layout.newactivity_welcome);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0, true);

    }

    @Override
    public void onEventStart(String type, int extra) {
        startActivity((new Intent()
                .putExtra("Cards", type)
                .putExtra(Constants.GAME, extra)
                .putExtra(Constants.USERLIST_KEY, new ArrayList<User>())
                .setClass(this, SetupActivity.class)));
        finish();
    }

    @Override
    public PlotListener getPlotListener() {
        return this;
    }

    @Override
    public  void setPlotListener(PlotListener listener) {
        // Do nothing;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            switch (position) {
                //Move CustomGameFragment to another Activity 07.31.2014 @Yuanwei
                case 0:
                    if (quickstartFragment == null) {
                        quickstartFragment = new QuickStartFragment();
                    }
                    return quickstartFragment;
                case 1:
                    if (contactFragment == null) {
                        contactFragment = new ContactFragment();
                    }
                    return contactFragment;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_quick_game).toUpperCase(l);
                case 1:
                    return getString(R.string.title_rules).toUpperCase(l);
            }
            return null;
        }

    }
}



