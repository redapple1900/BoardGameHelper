package game.redapple1900.resistance;

import android.media.MediaPlayer;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
//import android.graphics.Color;
//import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
//import android.widget.ImageView;
import android.widget.TextView;

import game.redapple1900.gridviewtest.GridAdapter;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import game.redapple1900.resistance.R;



public class MainActivity extends FragmentActivity implements ExecutionFragment.OnPreMissionExcutionListener{
	private ExecutionFragment excutionFragment=null;
	private BlankFragment blankFragment=null;
	private FragmentManager fragmentManager;
	private GridView view;
	private GridAdapter mGridAdapter;
	private long idlist[];
	private static int identity[];
	private static String name[];
	private static int image[];

	private int pictures[]={R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4,R.drawable.pic5,R.drawable.pic6,R.drawable.pic7,R.drawable.pic8,R.drawable.pic9,R.drawable.pic10};
	private int TOTAL_PLAYERS;
	private int NORMAL_PLAYERS;
	private int SHUFFLE_COUNT;
	private  EditText ed;
	private  Button button,button_playsound;
	private  TextView textview_gameSetting;
	private  AlertDialog.Builder builder,builder_inputName,builder_next,builder_last;
	private Dialog mDialog;
	private  MediaPlayer mediaPlayer;

    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        this.setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        setContentView(R.layout.activity_main); 
        fragmentManager=getSupportFragmentManager();
        initViews();
        shuffle(TOTAL_PLAYERS,NORMAL_PLAYERS);
        shufflePicture(TOTAL_PLAYERS,NORMAL_PLAYERS);
        showFragments(0);       
    } 
    @Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME){
                   showExitGameAlert();
		}
 
		return super.onKeyDown(keyCode, event);
	}
 
	private void showExitGameAlert() {
		final AlertDialog.Builder dlg = new AlertDialog.Builder(this);
		dlg.setTitle(getString(R.string.string_exit_title));
		dlg.setMessage(getString(R.string.string_exit_message));
		
		//Window window = dlg.getWindow();
		//window.setContentView(R.layout.shrew_exit_dialog);
		//ImageButton ok = (ImageButton) window.findViewById(R.id.btn_ok);
		dlg.setPositiveButton(getString(R.string.string_exit_positive), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			onTerminate();	
			}
		});
 
		dlg.setNegativeButton(getString(R.string.string_exit_negative), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.dismiss();
			}
		});
		dlg.show();
	}
	public void initViews(){
		
		SHUFFLE_COUNT=0;
		Bundle bundle=this.getIntent().getExtras();
		TOTAL_PLAYERS=bundle.getInt("TOTAL_PLAYERS");
		NORMAL_PLAYERS=bundle.getInt("NORMAL_PLAYERS");
		identity =new int[TOTAL_PLAYERS];
		name=bundle.getStringArray("name");
		if (name==null){name = new String[TOTAL_PLAYERS];}
		idlist=bundle.getLongArray("idlist");
		if (idlist==null){idlist =new long[TOTAL_PLAYERS];}
		image= new int[TOTAL_PLAYERS];
		mediaPlayer=MediaPlayer.create(getApplicationContext(), R.raw.sound_male);
	    mGridAdapter=new GridAdapter(getApplicationContext());
        for (int i = 0; i <TOTAL_PLAYERS;i++) {
        	GridAdapter.Item item = new GridAdapter.Item();
        	if (name!=null&&name[i]!=null&&(!name[i].equals(" "))){
        		item.text=name[i];
        	}else{
        		item.text = (1+i)+getString(R.string.string_item_game);
        	}
            
            item.resId = R.drawable.index;    
            mGridAdapter.addItem(item);
        }
        mDialog = new Dialog(this);
        view=(GridView)findViewById(R.id.grid);
        view.setAdapter(mGridAdapter);
        if (TOTAL_PLAYERS<7){
        	view.setColumnWidth((int) getResources().getDimension(R.dimen.itemSize_large));
        }else view.setColumnWidth((int) getResources().getDimension(R.dimen.itemSize_medium));
		textview_gameSetting=(TextView)findViewById(R.id.textView_gamesetting_main);
		textview_gameSetting.setText(getString(R.string.string_gamesetting_main,NORMAL_PLAYERS,TOTAL_PLAYERS-NORMAL_PLAYERS));
		builder =new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_DARK);
		builder.setTitle(getString(R.string.string_buildertitle_main)).setCancelable(false)
		.setPositiveButton(getString(R.string.string_builder_positivebutton_main), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				SHUFFLE_COUNT++;
				if (SHUFFLE_COUNT<TOTAL_PLAYERS){
				builder_next.setMessage(getString(R.string.string_builder_next_message_main));
				}
				
				else if (SHUFFLE_COUNT==TOTAL_PLAYERS){
					builder_next.setMessage(getString(R.string.string_builder_next_message_last_main));
					showFragments(-1); 
					button.setVisibility(View.VISIBLE);
					button_playsound.setVisibility(View.VISIBLE);
					Toast.makeText(getApplicationContext(), getString(R.string.string_toast2_main), Toast.LENGTH_LONG).show();
					
				}	
				final AlertDialog dlg =builder_next.create();
				if (!dlg.isShowing()) dlg.show();
				TimerTask mTimerTask= new TimerTask(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						dlg.dismiss();
					}	
				};
				Timer mTimer =new Timer();
				mTimer.schedule(mTimerTask, 1000);
			dialog.dismiss();
			}
		});
		builder_next=new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
		
		builder_inputName=new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_DARK);
		builder_inputName.setTitle(getString(R.string.string_builder_inputname_title_main)).setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if ((name[SHUFFLE_COUNT]=ed.getText().toString().trim()).contentEquals("")){
					Animation shake = AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.shake);
					ed.startAnimation(shake);
					/*
					name[SHUFFLE_COUNT]=defaultName[SHUFFLE_COUNT];
					Toast.makeText(getApplicationContext(),getString(R.string.string_toast1_main)+name[SHUFFLE_COUNT], Toast.LENGTH_SHORT).show();*/
				}
				GridAdapter.Item item= new GridAdapter.Item();
				item.text=name[SHUFFLE_COUNT];item.resId=image[SHUFFLE_COUNT];
				mGridAdapter.replaceItem(item, SHUFFLE_COUNT);
				mGridAdapter.notifyDataSetChanged();			
				if(identity[SHUFFLE_COUNT]==DataSet.SOILDER){
					builder.setIcon(R.drawable.thief).setMessage(name[SHUFFLE_COUNT]+getString(R.string.string_builder_message_positive_main));
				}else if(identity[SHUFFLE_COUNT]==DataSet.SPY) {
					builder.setIcon(R.drawable.spy).setMessage(name[SHUFFLE_COUNT]+getString(R.string.string_builder_message_negative_main));
				}
				if (mDialog.isShowing()){
					mDialog=builder.create();
					mDialog.show();
				}
			}
		});
		builder_last=new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
		
		builder_last.setTitle("正式开始").setCancelable(false).setPositiveButton("准备完毕", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				if (SHUFFLE_COUNT==TOTAL_PLAYERS){
					Intent intent = new Intent();
					Bundle bundle =new Bundle();
					bundle.putIntArray("identity",identity);
					bundle.putStringArray("name",name);
					bundle.putInt("TOTAL_PLAYERS", TOTAL_PLAYERS);
					bundle.putIntArray("image",image);
					bundle.putLongArray("idlist",idlist);
					intent.putExtras(bundle);
					intent.setClass(getApplicationContext(), GameActivity.class);
					startActivity(intent);
					finish();
				}
			}
			//TODO Auto-generated method stub
		}).setNeutralButton("语音提示", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				if (getVolume()<6){
					Toast.makeText(getApplicationContext(), getString(R.string.string_playsound_toast),Toast.LENGTH_LONG).show();
				}else
				playSound();
			}
		});
		//TODO
		builder_last.setMessage("间谍知道自己同伴了么?请播放“语音提示”\n\n或者诵读以下提示语：\n大家请闭眼。\n间谍请睁眼。\n间谍相互认识。\n间谍请闭眼。\n请大家都睁眼。\n\n两句之间留出几秒时间. ");
		button=(Button)findViewById(R.id.button_showIndentity_Main);
		button.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				
				builder_last.show();
				/*
				if (!sound_flag){
					Toast.makeText(getApplicationContext(), "间谍相互认识了么?可以播放语音", Toast.LENGTH_LONG).show();
					sound_flag=true;
				}else if (SHUFFLE_COUNT==TOTAL_PLAYERS){
						Intent intent = new Intent();
						Bundle bundle =new Bundle();
						bundle.putIntArray("identity",identity);
						bundle.putStringArray("name",name);
						bundle.putInt("TOTAL_PLAYERS", TOTAL_PLAYERS);
						bundle.putIntArray("image",image);
						bundle.putLongArray("idlist",idlist);
						intent.putExtras(bundle);
						intent.setClass(getApplicationContext(), GameActivity.class);
						startActivity(intent);
						finish();
					}*/
			}
			
		});
		button_playsound=(Button)findViewById(R.id.button_playsound_Main);
		button_playsound.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				if (getVolume()<6){
					Toast.makeText(getApplicationContext(), getString(R.string.string_playsound_toast),Toast.LENGTH_LONG).show();
				}else
				playSound();
			}});
		button.setVisibility(View.GONE);
		//button.setEnabled(false);
		button_playsound.setVisibility(View.GONE);
		/*
		button.setOnLongClickListener(new Button.OnLongClickListener(){

		
			@Override
			public boolean onLongClick(View v) {
				
				if (SHUFFLE_COUNT==TOTAL_PLAYERS){
					Intent intent = new Intent();
					Bundle bundle =new Bundle();
					bundle.putIntArray("image",image);
					bundle.putIntArray("identity",identity);
					bundle.putStringArray("name",name);
					bundle.putInt("TOTAL_PLAYERS", TOTAL_PLAYERS);
					intent.putExtras(bundle);
					intent.setClass(getApplicationContext(), GameActivity.class);
					startActivity(intent);
					MainActivity.this.finish();
					return false;
					
				}else{
					
					if (name[SHUFFLE_COUNT]==null){
						ed= new EditText(getApplicationContext());
						builder_inputName.setView(ed);
						builder_inputName.show();
						return true;
					}
					else{
					STUPID_CLICK=0;
					textViewStupid.setText("");
					if (SHUFFLE_COUNT!=(TOTAL_PLAYERS-1)){
						builder.setMessage(name[SHUFFLE_COUNT]+"同志:"+"\n你的真实身份是："+identity[SHUFFLE_COUNT]+"\n请把平板交给下一位同志。");
					}
					else{ 
						builder.setMessage(name[SHUFFLE_COUNT]+"同志:"+"\n你的真实身份是："+identity[SHUFFLE_COUNT]+"\n请把平板交给队长。");
					}
					builder.show();
					
					return false;
					}
			}
		}
	});	
		*/
	}
	public void shuffle(int TotalPlayers,int NormalPlayers){
		Random random =new Random(System.currentTimeMillis());
		int i,j;
			for(i=0;i<TOTAL_PLAYERS;i++){
				j=random.nextInt(TotalPlayers);
				if (j<NormalPlayers){
					identity[i]=DataSet.SOILDER;
					TotalPlayers--;
					NormalPlayers--;
					
				}else {
					identity[i]=DataSet.SPY;
					TotalPlayers--;					
				}
			}
	}
	private final void shufflePicture(int TotalPlayers,int NormalPlayers){
		Random random =new Random(System.currentTimeMillis());
		int temp[]= new int[10];
		int temp2[] = new int[10];
			for(int i=0;i<10;i++){
				temp[i]=pictures[i];
			}
			int count=10;
			for (int i=0;i<10;i++){
				int j=random.nextInt(count);
				temp2[i]=temp[j];
				count--;
				for (int k=j;k<count;k++){
					temp[k]=temp[k+1];
				}
				
				
			}
			for (int i=0;i<TotalPlayers;i++){
				image[i]=temp2[i];
			}
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    //your own stuff to handle orientation change, if needed
	}
	public void playSound(){
		 Log.d("sound","Sound1");
		 
		 try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		 mediaPlayer.start();
		 //button_playsound.setEnabled(false);

		 mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
			
				if (SHUFFLE_COUNT==TOTAL_PLAYERS){
					//button.setEnabled(true);
					builder_last.show();
				}
			}
		});
	}
	public void showDialog() {
		if (mDialog != null) {
			mDialog.setContentView(R.layout.view_dialog_input_name);
			mDialog.setTitle(R.string.string_builder_inputname_title_main);
			mDialog.setCancelable(false);

			final AutoCompleteTextView ed = (AutoCompleteTextView) mDialog
					.findViewById(R.id.autoCompleteTextView1);
			ed.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
			final InputMethodManager inputMgr = (InputMethodManager) getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			// inputMgr.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);

			

			Button positive = (Button) mDialog
					.findViewById(R.id.Button_OK_input);
			positive.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
			positive.setTextColor(getResources().getColor(android.R.color.primary_text_light));
			positive.setOnTouchListener(mOnTouchListener);
			positive.setPadding(1,1, 1, 1);
			Button negative = (Button) mDialog
					.findViewById(R.id.button_Cancel_input);
			negative.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
			negative.setTextColor(getResources().getColor(android.R.color.primary_text_light));
			negative.setPadding(1,1, 1, 1);
			negative.setVisibility(View.GONE);
			positive.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					if (ed.getText().toString().isEmpty()
							|| ed.getText().toString().trim().contentEquals("")) {
						Animation shake = AnimationUtils.loadAnimation(
								getApplicationContext(), R.anim.shake);
						ed.startAnimation(shake);
						// Toast.makeText(Information.this,
						// "Please enter an age", Toast.LENGTH_SHORT).show();
					} else {
						inputMgr.hideSoftInputFromWindow(ed.getWindowToken(), 0);
						name[SHUFFLE_COUNT]= ed.getText().toString().trim();
						GridAdapter.Item item= new GridAdapter.Item();
						
						item.text=name[SHUFFLE_COUNT];item.resId=image[SHUFFLE_COUNT];
						mGridAdapter.replaceItem(item, SHUFFLE_COUNT);
						mGridAdapter.notifyDataSetChanged();			
						if(identity[SHUFFLE_COUNT]==DataSet.SOILDER){
							builder.setTitle(getString(R.string.string_buildertitle_main)+getString(R.string.string_identity_positive)).setIcon(R.drawable.thief).setMessage(name[SHUFFLE_COUNT]+getString(R.string.string_builder_message_positive_main)).show();
						}else if(identity[SHUFFLE_COUNT]==DataSet.SPY) {
							builder.setTitle(getString(R.string.string_buildertitle_main)+getString(R.string.string_identity_negative)).setIcon(R.drawable.spy).setMessage(name[SHUFFLE_COUNT]+getString(R.string.string_builder_message_negative_main)).show();
							
						}
						mDialog.dismiss();
					}
				}
			});
		mDialog.show();
		}

	}
    private int getVolume() {
        int volume = -1;
        
        getApplicationContext();
		AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.i("STREAM_RING", "" + volume);
        
        return volume;
    }
	
	 public void onTerminate() {           
		 
	        MainActivity.this.finish();  
	                    
	        onDestroy();  
	          
	        System.exit(0); 
	 }
	  private void showFragments(int id){
			 //hideFragments(transaction);
	    	FragmentTransaction transaction = fragmentManager.beginTransaction(); 
			 switch (id){
			 case 0:{
				 if (excutionFragment == null) {  
		                excutionFragment = new ExecutionFragment();	            
		                transaction.add(R.id.layout_content_main, excutionFragment);
		           	 Log.d("new","1123");
		            } else { 
		            	transaction.replace(R.id.layout_content_main,excutionFragment);
		           	 Log.d("old","1123");
		            }
				 Bundle args= new Bundle();
				 args.putString("name","");			
				 excutionFragment.setArguments(args);
				 transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				 transaction.commit();  
				 break;
			 }
				 

			 case -1:{
				 if(excutionFragment!=null)transaction.remove(excutionFragment);
				 if (blankFragment==null){
					 blankFragment= new BlankFragment();
					 transaction.add(R.id.layout_content_main,blankFragment);  
				 }else{
					 transaction.replace(R.id.layout_content_main,blankFragment); 
				 }
				 transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				 transaction.commit(); 
				 break;	
			 }
			
				 default:
					 break;
			 }			  
	    }

	@Override
	public void onPreMissionExcution(int id) {
	
		if (name==null||name[SHUFFLE_COUNT]==null){
			showDialog();
		}else {
			GridAdapter.Item item= new GridAdapter.Item();
			item.text=name[SHUFFLE_COUNT];item.resId=image[SHUFFLE_COUNT];
			mGridAdapter.replaceItem(item, SHUFFLE_COUNT);
			mGridAdapter.notifyDataSetChanged();			
			if(identity[SHUFFLE_COUNT]==DataSet.SOILDER){
				builder.setTitle(getString(R.string.string_buildertitle_main)+getString(R.string.string_identity_positive)).setIcon(R.drawable.thief).setMessage(name[SHUFFLE_COUNT]+getString(R.string.string_builder_message_positive_main)).show();
			}else if(identity[SHUFFLE_COUNT]==DataSet.SPY) {
				builder.setTitle(getString(R.string.string_buildertitle_main)+getString(R.string.string_identity_negative)).setIcon(R.drawable.spy).setMessage(name[SHUFFLE_COUNT]+getString(R.string.string_builder_message_negative_main)).show();
			}
			
		}
	}
	private OnTouchListener mOnTouchListener = new OnTouchListener(){


	    @Override
	    public boolean onTouch(View v, MotionEvent event) {

	        switch (event.getAction()) {
	            case MotionEvent.ACTION_DOWN: {

	    			((Button)v).setBackgroundColor(getResources().getColor(android.R.color.primary_text_light_nodisable));
	    			((Button)v).setTextColor(getResources().getColor(android.R.color.darker_gray));
	    			v.invalidate();

	                break;
	            }
	            case MotionEvent.ACTION_UP:{

	    			((Button)v).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
	    			((Button)v).setTextColor(getResources().getColor(android.R.color.primary_text_light));
	    			v.invalidate();

	            	v.performClick();
	            	break;
	            }
	            case MotionEvent.ACTION_CANCEL: {

	    			((Button)v).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
	    			((Button)v).setTextColor(getResources().getColor(android.R.color.primary_text_light));
	    			v.invalidate();

	                break;
	            }
	        }
	        return true;
	    }
	};

}
