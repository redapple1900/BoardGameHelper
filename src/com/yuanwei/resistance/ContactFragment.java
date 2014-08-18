package com.yuanwei.resistance;




import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ContactFragment extends Fragment{
	private Button button;
	private EditText editText;
	
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState) {  
	        View Layout = inflater.inflate(R.layout.fragment_gamerules, container, false); 
	        Log.d("tag","allright");
	        button=(Button)Layout.findViewById(R.id.button_contact);
	        editText=(EditText)Layout.findViewById(R.id.editText_contact);
	        Log.d("tag","allright2");
	        button.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 Intent i = new Intent(Intent.ACTION_SEND);  
						//i.setType("text/plain"); //
						i.setType("message/rfc822"); // 
						i.putExtra(Intent.EXTRA_EMAIL, new String[]{"reclapple@gmail.com"});  
						i.putExtra(Intent.EXTRA_SUBJECT,"Test");  
						i.putExtra(Intent.EXTRA_TEXT,editText.getText().toString().trim());  
						startActivity(Intent.createChooser(i, "Send Email"));
						}
				});
	        
	        return Layout; 
	        
	    }
	 

}