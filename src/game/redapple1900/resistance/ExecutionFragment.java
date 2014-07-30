package game.redapple1900.resistance;


import game.redapple1900.resistance.R;

import android.app.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ExecutionFragment extends Fragment{
	OnPreMissionExcutionListener mCallback;
	
	public interface OnPreMissionExcutionListener{
		public void onPreMissionExcution(int id);
	}
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnPreMissionExcutionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPreMissionExcutionListener");
        }
    }

	private ImageButton button;
	private TextView textview;
	//private MissionFragment missionFragment; 
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState) {  
	        View excutionLayout = inflater.inflate(R.layout.fragment_excution, container, false);
	        
	        textview=(TextView)excutionLayout.findViewById(R.id.textview_Execution);
	        textview.setText(getArguments().getString("name"));
	        textview.setTextColor(Color.WHITE);
	        button=(ImageButton)excutionLayout.findViewById(R.id.imageButton_Execution);
	        button.setLongClickable(true);
	        button.setOnLongClickListener(new Button.OnLongClickListener(){
				@Override
				public boolean onLongClick(View v) {
					//missionFragment=new MissionFragment();
					// TODO Auto-generated method stub
					//getActivity().getFragmentManager().beginTransaction().replace(R.id.content_gameactivity, missionFragment).commit();
					mCallback.onPreMissionExcution(0);
					return true;
				}
			});
	        return excutionLayout;  
	    }  
}

