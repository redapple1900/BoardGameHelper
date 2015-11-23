package com.yuanwei.resistance.fragment;




import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanwei.resistance.R;

public class BlankFragment extends Fragment{
	
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState) {  
	        View v = inflater.inflate(R.layout.fragment_blank, container, false);
	        return v;  
	    }  

}
