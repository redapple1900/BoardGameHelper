package com.yuanwei.resistance;


import game.redapple1900.resistance.R;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TopResultFragment extends Fragment{
	private TextView text;

	 public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState) {  
	        View v = inflater.inflate(R.layout.fragment_topresult, container, false);
	        text=(TextView)v.findViewById(R.id.textView_topresult);
	        
	        //Difficult to handle the picture size. 07.23.2014  @Yuanwei
	        //Delete the figure 07.28.2014 @Yuanwei
	      
	        if (getArguments().getInt("GameResult")==1){
	        	if (isTablet(getActivity())){
	        		text.setText(getString(R.string.string_thievies_win_large));
	        	}else {
	        		text.setText(getString(R.string.string_thievies_win));
	        	}
	        	//image.setImageResource(R.drawable.gamewin);
	        }else if (getArguments().getInt("GameResult")==-1){
	        	if (isTablet(getActivity())){
	        		text.setText(getString(R.string.string_spies_win_large));
	        	}else{
	        		text.setText(getString(R.string.string_spies_win));
	        	}	        	
	        	//image.setImageResource(R.drawable.gamelose);
	        	}
	        return v;  
	    } 
	    public static boolean isTablet(Context context) {
	        return (context.getResources().getConfiguration().screenLayout
	                & Configuration.SCREENLAYOUT_SIZE_MASK)
	                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	    }

}
