package com.yuanwei.resistance.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

import com.yuanwei.resistance.constant.Constants;

import java.util.Locale;

public class GeneralMethodSet {

    public static Locale getTextToSpeechLocale(Activity activity) {
        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(activity);
        String language = share.getString(Constants.LANGUAGE, Locale.getDefault().getLanguage());

        if (language.equals("default")){
            return Locale.getDefault();
        }

        switch (language) {
            case "zh_CN":
            case "zh_TW":
            case "zh_HK":
            case "zh":
                return Locale.CHINESE;
            default:
                return Locale.ENGLISH;
        }
    }

    public void updateLanguage(Context context) {
        /*
        SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(context);
        String language = share.getString(Constants.LANGUAGE, Locale.getDefault().getLanguage());
        Log.e("GeneralMethod", language);
        if (!language.equals("default")) {
            Locale locale;
            if (language.equals("zh_CN"))
                locale = Locale.SIMPLIFIED_CHINESE;
            else if (language.equals("zh_TW") || language.equals("zh_HK") || language.equals("zh"))
                locale = Locale.TRADITIONAL_CHINESE;
            else
                locale = new Locale(language);

            Configuration config = new Configuration();
            config.locale = locale;
            context.getResources().updateConfiguration(config, null);
        }
        */
    }

    public void setActivityTheme(Activity context) {
        context.setTheme(android.R.style.Theme_Wallpaper_NoTitleBar_Fullscreen);
        context.requestWindowFeature(Window.FEATURE_NO_TITLE);
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
