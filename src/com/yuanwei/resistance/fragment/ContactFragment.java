package com.yuanwei.resistance.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yuanwei.resistance.R;

public class ContactFragment extends Fragment{
	private Button button;
	private EditText editText;
	
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState) {  
	        View Layout = inflater.inflate(R.layout.fragment_gamerules, container, false);
	        button=(Button)Layout.findViewById(R.id.button_contact);
	        editText=(EditText)Layout.findViewById(R.id.editText_contact);

	        button.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 Intent i = new Intent(Intent.ACTION_SEND);
						i.setType("message/rfc822");
						i.putExtra(Intent.EXTRA_EMAIL, new String[]{"reclapple@gmail.com"});  
						i.putExtra(Intent.EXTRA_SUBJECT,"Test");  
						i.putExtra(Intent.EXTRA_TEXT,editText.getText().toString().trim());  
						startActivity(Intent.createChooser(i, "Send Email"));
						}
				});
	        
	        return Layout; 
	        
	    }
	 

}