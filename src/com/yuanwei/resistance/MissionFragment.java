package com.yuanwei.resistance;

import java.util.Random;

import com.yuanwei.resistance.widget.ButtonOnTouchListener;

import android.widget.ImageView;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MissionFragment extends Fragment implements OnClickListener{
	OnMissionExcutionListener mCallback;
	
		public interface OnMissionExcutionListener{
			public void onMissionExcuted(int id);
		}
		@Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        
	        // This makes sure that the container activity has implemented
	        // the callback interface. If not, it throws an exception
	        try {
	            mCallback = (OnMissionExcutionListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement OnMissionExcutionListener");
	        }
	    }

		private View excuteLayout;
		//private View abortLayout;
		private View sabotageLayout;
		private ImageView image;
		private int identity;
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState) { 
		 	Random random =new Random(System.currentTimeMillis());
		 	View v;
		 	/*
		 	 * 
		 	 */
		 	int j=random.nextInt(2);
		 	switch(j){
		 	case 0:
		 		if (isTablet(getActivity())){
		 			v= inflater.inflate(R.layout.fragment_mission_large, container,false);
		 		}else
		 		 v = inflater.inflate(R.layout.fragment_mission, container, false);
		 		break;
		 	case 1:
		 		if (isTablet(getActivity())){
		 			v=inflater.inflate(R.layout.fragment_mission2_large,container,false);
		 		}else v = inflater.inflate(R.layout.fragment_mission2, container, false);
		 		break;
		 	default:
		 		v = inflater.inflate(R.layout.fragment_mission, container, false);
		 		break;
		 	}
		 	/*
		 	 * Old code: Determines the layout
		 	int j=random.nextInt(6);
		 	switch(j){
		 	case 0:
		 		 v = inflater.inflate(R.layout.fragment_mission, container, false);
		 		break;
		 	case 1:
		 		 v = inflater.inflate(R.layout.fragment_mission2, container, false);
		 		break;
		 	case 2:
		 		 v = inflater.inflate(R.layout.fragment_mission3, container, false);
		 		break;
		 	case 3:
		 		 v = inflater.inflate(R.layout.fragment_mission4, container, false);
		 		break;
		 	case 4:
		 		 v = inflater.inflate(R.layout.fragment_mission4, container, false);
		 		break;
		 	case 5:
		 		 v = inflater.inflate(R.layout.fragment_mission5, container, false);
		 		break;
		 	default:
		 		 v = inflater.inflate(R.layout.fragment_mission6, container, false);
		 		break;
		 		
		 	}
		 	*/
	        excuteLayout=v.findViewById(R.id.layout_excute_mission);
	        //abortLayout=v.findViewById(R.id.layout_abortion_mission);
	        sabotageLayout=v.findViewById(R.id.layout_sabotage_mission);
	        /*
	         * The following module determines the picture on mission execution.
	         */
	        image=(ImageView)v.findViewById(R.id.imageView_mission);
	        j=random.nextInt(4);
		 	switch(j){
		 	case 0:
		 		image.setImageResource(R.drawable.excution1);
		 		break;
		 	case 1:
		 		image.setImageResource(R.drawable.excution2);
		 		break;
		 	case 2:
		 		image.setImageResource(R.drawable.excution3);
		 		break;
		 	case 3:
		 		image.setImageResource(R.drawable.excution4);
		 		break;
		 	default:
		 		image.setImageResource(R.drawable.excution1);
		 		break;
		 	}	
	        identity=getArguments().getInt("identity");
	        excuteLayout.setOnClickListener(this);
	        excuteLayout.setOnTouchListener(new ButtonOnTouchListener(getActivity()));
	        //abortLayout.setOnClickListener(this);
	        sabotageLayout.setOnClickListener(this);
	        sabotageLayout.setOnTouchListener(new ButtonOnTouchListener(getActivity()));
	        if (identity==DataSet.SOILDER){
	        	sabotageLayout.setVisibility(View.INVISIBLE);
	        }
	        return v;  
	    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
			case R.id.layout_excute_mission:
				mCallback.onMissionExcuted(1);
				break;
			//case R.id.layout_abortion_mission:
				//Toast.makeText(getActivity(), getString(R.string.string_toast2_mission), Toast.LENGTH_SHORT).show();
				//mCallback.onMissionExcuted(0);
				//break;
			case R.id.layout_sabotage_mission:
				if (identity==DataSet.SPY)
					mCallback.onMissionExcuted(-1);								
				break;
			
				
		}
		
		
	}
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
