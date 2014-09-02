package com.yuanwei.resistance;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class GeneralMethodSet {
	public  void updateLanguage(Context context) {
		SharedPreferences share = PreferenceManager.getDefaultSharedPreferences(context);
		String language=share.getString(DataSet.LANGUAGE, Locale.getDefault().getLanguage()); 
		Log.e("GeneralMethod", language);
		if (language!="default"){
		Locale locale;
		if(language.equals("zh_CN"))
		    locale = Locale.SIMPLIFIED_CHINESE;
		else if(language.equals("zh_TW")||language.equals("zh_HK")||language.equals("zh"))
		   locale = Locale.TRADITIONAL_CHINESE;	
		else
		 locale = new Locale(language);
	        
	        Locale.setDefault(locale);
	        Configuration config = new Configuration();
	        config.locale = locale;
	        context.getResources().updateConfiguration(config, null);
		}
	}
	
	public void setActivityTheme(Context context){
		context.setTheme(android.R.style.Theme_Wallpaper_NoTitleBar_Fullscreen);  
		((Activity) context).requestWindowFeature(Window.FEATURE_NO_TITLE);  
	    ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	}

}
