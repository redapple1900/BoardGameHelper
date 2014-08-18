package com.yuanwei.resistance;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

public class TimerFragment extends Fragment{
OnCheckResultListener mCallback;
	
	public interface OnCheckResultListener{
		public void onCheckResult(int id);
	}
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnCheckResultListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCheckResultListener");
        }
    }

	private Button button;
	private Chronometer chronometer;
	//private AlertDialog.Builder builder;
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState) {  
	        View v = inflater.inflate(R.layout.fragment_timer, container, false);  
	        button=(Button)v.findViewById(R.id.button_timer);
	        chronometer=(Chronometer)v.findViewById(R.id.chronometer);
	        /*
	        builder=new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
	        builder.setMessage(getString(R.string.string_message_timer)).setPositiveButton(getActivity().getString(R.string.string_builder_positive_game), 
	        		new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							chronometer.stop();
							mCallback.onCheckResult(0);
						}
					}).setNegativeButton(getActivity().getString(R.string.string_builder_negative_game),
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
								}
							});
							*/
	        button.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View v) {
					//missionFragment=new MissionFragment();
					// TODO Auto-generated method stub
					//getActivity().getFragmentManager().beginTransaction().replace(R.id.content_gameactivity, missionFragment).commit();
					//builder.show(); Remove one dialog
					//07.28.2014 @Yuanwei 
					chronometer.stop();
					mCallback.onCheckResult(0);
				}
			});
	        chronometer.start();
	        return v;  
	    } 
	 

}
