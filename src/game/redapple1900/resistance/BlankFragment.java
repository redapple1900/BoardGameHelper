package game.redapple1900.resistance;


import game.redapple1900.resistance.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BlankFragment extends Fragment{
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState) {  
	        View v = inflater.inflate(R.layout.fragment_blank, container, false);  
	        return v;  
	    }  

}
