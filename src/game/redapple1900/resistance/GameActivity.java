package game.redapple1900.resistance;

import game.redapple1900.gridviewtest.GridAdapter;
import game.redapple1900.gridviewtest.TableGridAdapter;
import game.redapple1900.gridviewtest.GridAdapter.Item;
import game.redapple1900.playerdatabase.Player;
import game.redapple1900.playerdatabase.PlayerDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import game.redapple1900.resistance.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends FragmentActivity 
implements VoteFragment.OnVoteResultListener,MissionFragment.OnMissionExcutionListener,
ExecutionFragment.OnPreMissionExcutionListener,TimerFragment.OnCheckResultListener{
	private VoteFragment voteFragment;
	private TimerFragment timerFragment;
	private ExecutionFragment excutionFragment;
	private MissionFragment missionFragment;
	private BlankFragment blankFragment;
	private android.support.v4.app.FragmentManager fragmentManager;
	private GridView view,view_top,view_bottom;
	private GridAdapter mGridAdapter,mGridAdapter_top;
	private TableGridAdapter mTableGridAdapter_bottom;
	private PlayerDataSource datasource;
	private static final int MaxRounds=5;
	private static TextView textview_topStatus;
	private static View layout;
	private static int identity[];
	private static long idlist[];
	private static int image[];
	private static String name[],date;
	private static final String status_top_Main[]=new String[3];
	private static int GameResult = 0;
	private static int TOTAL_PLAYERS;
	private static int MemberSelected=0;
	private static int isPlayerSelected[];
	private static int CandidatesId[];
	private static int MissionResult[][];
	private static int currentStatus=0;
	private static int currentMission=0;
	private static int round;
	private static int timeOfTeamPropose=1;
	private static int VictoryCount=0;
	private static int LoseCount=0;
	private ImageView mImageView;
	private static Button button;
	private static AlertDialog.Builder builder,builder_next,builder_leader,builder_result;
	private static AlertDialog mDialog;
	private Dialog dialog;

	
	 @Override  
	protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        this.setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);  
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
	        setContentView(R.layout.activity_game); 
	        // 初始化布局元素  
	    	fragmentManager=getSupportFragmentManager();
	    	initConstants();
	        initViews();
	        resetCandidates();
	        datasource= new PlayerDataSource(getApplicationContext());
	        datasource.open();
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
	public void onTerminate() {           
			 
		        GameActivity.this.finish();  
		                    
		        onDestroy();  
		          
		        System.exit(0); 
		 }
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    //your own stuff to handle orientation change, if needed
	}
	public void initConstants(){
		GameResult=0;
		MemberSelected=0;
		currentStatus=0;
		currentMission=0;
		timeOfTeamPropose=1;
		VictoryCount=0;
		 LoseCount=0;
		 round=0;
		 MissionResult=new int[MaxRounds][];
			status_top_Main[0]=getString(R.string.string_topstatus0_game);
			status_top_Main[1]=getString(R.string.string_topstatus1_game);
			status_top_Main[2]=getString(R.string.string_topstatus2_game);
			Calendar myCalendar;
			myCalendar = Calendar.getInstance();
			myCalendar.setTimeInMillis(System.currentTimeMillis());
			final int year = myCalendar.get(Calendar.YEAR);
			final int month = myCalendar.get(Calendar.MONTH)+1;
			final int day = myCalendar.get(Calendar.DAY_OF_MONTH);
			
			if (month<10){
				date = year + "-0" + month + "-" + day;
			}else {
				 date = year + "-" + month + "-" + day;
			}
			
	}
	public void initViews(){
		 	dialog = new Dialog(this);
			Bundle bundle=this.getIntent().getExtras();
			TOTAL_PLAYERS=bundle.getInt("TOTAL_PLAYERS");
			identity=bundle.getIntArray("identity");
			name=bundle.getStringArray("name");
			idlist=bundle.getLongArray("idlist");
			image=bundle.getIntArray("image");
			textview_topStatus=(TextView)findViewById(R.id.textview_topStatus_Game);
			layout=(View)findViewById(R.id.layout_middle_game);
			setTopStatus(currentStatus,TOTAL_PLAYERS);
		    mGridAdapter=new GridAdapter(getApplicationContext());
	        for (int i = 0; i <TOTAL_PLAYERS;i++) {
	        	GridAdapter.Item item = new GridAdapter.Item();
	            item.text = name[i];
	            item.resId = image[i];
				if (i==0)
				item.text_top=getString(R.string.string_leader);
	            mGridAdapter.addItem(item);
	        }
		    mGridAdapter_top=new GridAdapter(getApplicationContext());
		    mTableGridAdapter_bottom= new TableGridAdapter(getApplicationContext());
		    for	(int i=0;i<6*3;i++){
		    	
		    	if (i==0){
		    		TableGridAdapter.Item item =new TableGridAdapter.Item();
		    		item.text=getString(R.string.string_caption_bottom1_game);
		    		item.resId=R.drawable.blank;
		    		mTableGridAdapter_bottom.addItem(item);
		    	}else if(i>0&&i<6){
		    		TableGridAdapter.Item item =new TableGridAdapter.Item();
		    		item.text="";
		    		item.resId=R.drawable.waiting;
		    		mTableGridAdapter_bottom.addItem(item);
		    	}else if(i==6){
		    		TableGridAdapter.Item item =new TableGridAdapter.Item();
		    		item.text=getString(R.string.string_caption_bottom2_game);
		    		item.resId=R.drawable.blank;
		    		mTableGridAdapter_bottom.addItem(item);
		    	}else if(6<i&&i<12){
		    		TableGridAdapter.Item item =new TableGridAdapter.Item();
		    		item.text=""+DataSet.NumOfMembersPerMission[TOTAL_PLAYERS][i-7];
		    		item.resId=R.drawable.blank;
		    		mTableGridAdapter_bottom.addItem(item);
		    	}else if(i==12){
		    		TableGridAdapter.Item item =new TableGridAdapter.Item();
		    		item.text=getString(R.string.string_caption_bottom3_game);
		    		item.resId=R.drawable.blank;
		    		mTableGridAdapter_bottom.addItem(item);
		    	}else {
		    		TableGridAdapter.Item item =new TableGridAdapter.Item();
		    		item.text="";
		    		item.resId=R.drawable.waiting;
		    		mTableGridAdapter_bottom.addItem(item);
		    	}
		    }
		    /*
			if (i >= 0 && i < 5) {
				TableGridAdapter.Item item = new TableGridAdapter.Item();
				item.text = "";
				item.resId = R.drawable.waiting;
				mTableGridAdapter_bottom.addItem(item);
			} else if (i >= 5 && i < 10) {
				TableGridAdapter.Item item = new TableGridAdapter.Item();
				item.text = ""
						+ DataSet.NumOfMembersPerMission[TOTAL_PLAYERS][i - 5];
				item.resId = R.drawable.blank;
				mTableGridAdapter_bottom.addItem(item);

			} else {
				TableGridAdapter.Item item = new TableGridAdapter.Item();
				item.text = "";
				item.resId = R.drawable.waiting;
				mTableGridAdapter_bottom.addItem(item);
			}
			*/
		    			    	
	        view=(GridView)findViewById(R.id.grid);
	        view_top=(GridView)findViewById(R.id.grid_top);
	        view_bottom=(GridView)findViewById(R.id.grid_bottom);
	        view.setAdapter(mGridAdapter);
	        view_top.setAdapter(mGridAdapter_top);
	        view_bottom.setAdapter(mTableGridAdapter_bottom);
	        resetCandidates();
	        setLayoutOnClick();
			builder =new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_DARK);
			builder_next=new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
			//builder_vote= new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_DARK);
			builder_leader= new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_DARK);
			builder_result=new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_DARK);
			mImageView=(ImageView)findViewById(R.id.imageView_game);
			mImageView.setClickable(true);mImageView.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View arg0) {
					showRules();
				}
				});
			button=(Button)findViewById(R.id.button_showIndentity_Game);
			button.setVisibility(View.INVISIBLE);//Invisible after onCreate();
			button.setText(getString(R.string.string_button_propose_game));
			button.setOnClickListener(new Button.OnClickListener(){

				@Override
				public void onClick(View arg0) {
					
					if (currentStatus<2){
						builder.setTitle(status_top_Main[currentStatus]).setMessage(getString(R.string.string_builder_message_team_propose_game))
						.setPositiveButton(getString(R.string.string_builder_positive_game), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (timeOfTeamPropose<5){
									changeStatus_Positive(currentStatus,TOTAL_PLAYERS,currentMission);	
								}else {
									layout.setVisibility(View.INVISIBLE);
									setBottomStatus(currentMission,0);
									currentStatus++;
									changeStatus_Positive(currentStatus,TOTAL_PLAYERS,currentMission);									
								}
								
							}
						
						}).setNegativeButton(getString(R.string.string_builder_negative_game),new DialogInterface.OnClickListener() {
						
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
							}
						});
						if (mDialog==null||(!mDialog.isShowing())){
							mDialog=builder.create();
							mDialog.show();
						}
					}else changeStatus_Positive(currentStatus,TOTAL_PLAYERS,currentMission);				
				}
			});
	}	
	private void showRules(){
		//if (dialog == null&&(!dialog.isShowing())) {
			dialog.setContentView(R.layout.dialog_gamerules);
			Button negative = (Button) dialog
					.findViewById(R.id.button_dialog_rules);
			
			negative.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
			negative.setText(getString(R.string.string_builder_positive_game));
			negative.setTextColor(getResources().getColor(android.R.color.primary_text_light));
			negative.setPadding(1,1, 1, 1);
			

			negative.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
				
					dialog.dismiss();
				}
			});
		dialog.show();
		//}

	}
	
	private void resetCandidates(){
		isPlayerSelected= new int[TOTAL_PLAYERS];
		CandidatesId= new int[DataSet.NumOfMembersPerMission[TOTAL_PLAYERS][currentMission]];
		mGridAdapter_top.removeAllItems();
		for (int i=0;i<CandidatesId.length;i++){
			CandidatesId[i]=-1;
        	GridAdapter.Item item = new GridAdapter.Item();
            item.text = (1+i)+getString(R.string.string_item_game);
            item.resId = R.drawable.index;
            mGridAdapter_top.replaceItem(item,i);
            view_top.setColumnWidth(setColumnWidth(CandidatesId.length));
		}
		mGridAdapter_top.notifyDataSetChanged();
	}

	private int setColumnWidth(int length) {
		int i;
		switch(length){

		case 4:i=(int) getResources().getDimension(R.dimen.itemSize_medium); 
			break;
		case 5:i=(int) getResources().getDimension(R.dimen.itemSize); 
			break;
		default:
			i=(int) getResources().getDimension(R.dimen.itemSize_large);
			break;
		}
		return i;
	}
	private void setBottomStatus(int MissionNumber,int Status){
		TableGridAdapter.Item item = new TableGridAdapter.Item();
		item.text = "";
		if (Status==0){
			item.resId = R.drawable.execute;
		}else if (Status==1){
			item.resId=R.drawable.win;
		}else if (Status==-1){
			item.resId=R.drawable.lose;
		}
		mTableGridAdapter_bottom.replaceItem(item,MissionNumber+13);
		mTableGridAdapter_bottom.notifyDataSetChanged();
	}
	private void setTopStatus(int CurrentStatus,int Total_Players){
		textview_topStatus.setText(status_top_Main[CurrentStatus]);
	}
	private void setTeamProposedStatus(int TimeOfTeamPropose,boolean status){
		TableGridAdapter.Item item = new TableGridAdapter.Item();
		item.text = "";
		if (status==true){
			item.resId = R.drawable.approve;
		}else if (status==false){
			item.resId=R.drawable.veto;
		}	
		mTableGridAdapter_bottom.replaceItem(item,TimeOfTeamPropose);
		mTableGridAdapter_bottom.notifyDataSetChanged();
	}
	private void clearTeamProposedStatus(){
		 for	(int i=1;i<6;i++){
			 	
		    		TableGridAdapter.Item item =new TableGridAdapter.Item();
		    		item.text="";
		    		item.resId=R.drawable.waiting;
		    		mTableGridAdapter_bottom.replaceItem(item,i);
		    
		 }
	}

	private void setLayoutOnClick() {

		view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			
				if (isPlayerSelected(arg2)) {
					int i;
					for (i = 0; i < MemberSelected; i++) {
						if (CandidatesId[i] == arg2)
							break;
					}
					deselectPlayer(CandidatesId[i]);
					MemberSelected--;
					button.setVisibility(View.INVISIBLE);
					for (int j = i; j < MemberSelected; j++) {
						mGridAdapter_top.replaceItem(
								(Item) mGridAdapter_top.getItem(j + 1), j);
						CandidatesId[j] = CandidatesId[j + 1];
					}
					CandidatesId[MemberSelected] = -1;
					GridAdapter.Item item = new GridAdapter.Item();
					item.text = 
							 getString(R.string.string_item_game)+(MemberSelected + 1);
					item.resId = R.drawable.index;
					mGridAdapter_top.replaceItem(item, MemberSelected);
					mGridAdapter_top.notifyDataSetChanged();

				} else if (MemberSelected < CandidatesId.length) {
					mGridAdapter_top.replaceItem(
							(Item) mGridAdapter.getItem(arg2), MemberSelected);
					selectPlayer(arg2);
					CandidatesId[MemberSelected] = arg2;
					MemberSelected++;
					mGridAdapter_top.notifyDataSetChanged();
					if (MemberSelected == CandidatesId.length) {
						button.setVisibility(View.VISIBLE);
					}
				} else
					Toast.makeText(getApplicationContext(),
							getString(R.string.string_toast1_game),
							Toast.LENGTH_SHORT).show();
			}
		});
		view_top.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				if (CandidatesId[arg2] >= 0) {
					deselectPlayer(CandidatesId[arg2]);
					MemberSelected--;
					button.setVisibility(View.INVISIBLE);
					for (int i = arg2; i < MemberSelected; i++) {
						mGridAdapter_top.replaceItem(
								(Item) mGridAdapter_top.getItem(i + 1), i);
						CandidatesId[i] = CandidatesId[i + 1];
					}
					CandidatesId[MemberSelected] = -1;
					GridAdapter.Item item = new GridAdapter.Item();
					item.text = (MemberSelected + 1)
							+ getString(R.string.string_item_game);
					item.resId = R.drawable.index;
					mGridAdapter_top.replaceItem(item, MemberSelected);
					mGridAdapter_top.notifyDataSetChanged();
				}
			}
		});
	}
	private final boolean isPlayerSelected(int id){
		if (isPlayerSelected[id]==1){
			return true;
		}else if (isPlayerSelected[id]==0){
			return false;	
		}return false;
			
	}
	private final void selectPlayer(int id){
		isPlayerSelected[id]=1;
		mGridAdapter.changeSelection((GridAdapter.Item)mGridAdapter.getItem(id), id);
		mGridAdapter.notifyDataSetChanged();
	}
	private final void deselectPlayer(int id){
		isPlayerSelected[id]=0;
		mGridAdapter.deSelection((GridAdapter.Item)mGridAdapter.getItem(id), id);
		mGridAdapter.notifyDataSetChanged();
	}
	private final void winGame(){
		GameResult=1;
		endGame();
	}
	private final void loseGame(){
		GameResult=-1;
		endGame();
		/*
		Intent intent= new Intent();
		intent.setClass(GameActivity.this,WelcomeActivity.class);
		startActivity(intent);
		finish();
		*/
	}
	private final void endGame(){
		view_bottom.setVisibility(View.INVISIBLE);
		for (int i=0;i<CandidatesId.length;i++){		
			MissionResult[currentMission][i]=(CandidatesId[i]+1)*(int)(identity[CandidatesId[i]]/100);
		}
		if (GameResult==1){
			labelToken(GridAdapter.Token.WIN);	
		}else {
			labelToken(GridAdapter.Token.LOSE);
		}
		for (int i=0;i<CandidatesId.length;i++){
			Log.d(""+CandidatesId[i], "deselect");
			deselectPlayer(CandidatesId[i]);
		}
		currentStatus=3;
		button.setText(getString(R.string.string_topstatus3_game));
		View layout_top = (View)findViewById(R.id.linearLayout1);
		layout_top.setVisibility(View.INVISIBLE);
		layout.setVisibility(View.VISIBLE);
		hideFragments();
		showFragments(-1);
		showFragments(10);
		for (int i=0;i<=currentMission;i++){
			for (int j=0;j<MissionResult[i].length;j++){
				if (MissionResult[i][j]<0){
					int position=Math.abs(MissionResult[i][j])-1;
					GridAdapter.Item item = (GridAdapter.Item)mGridAdapter.getItem(position);
					mGridAdapter.setToken(item, position, i);
				}
			}
		}
		for (int i = 0; i <TOTAL_PLAYERS;i++) {
        	GridAdapter.Item item = (GridAdapter.Item)mGridAdapter.getItem(i);
            if (identity[i]<0){
               item.text_top=getString(R.string.string_identity_negative);	
            }else if (identity[i]>0){
            	item.text_top=getString(R.string.string_identity_positive);	
            }	            
            mGridAdapter.replaceItem(item,i);
        }
		for (int i=0;i<TOTAL_PLAYERS;i++){
			if (idlist[i]>0){
				Player player= datasource.selectPlayerById(idlist[i]);
				player.setLastDate(date);
				if (identity[i]*GameResult>0){
					player.setWin(player.getWin()+1);
				}else if (identity[i]*GameResult<0){
					player.setLose(player.getLose()+1);
				}
				datasource.updatePlayer(player);
			}else if (idlist[i]==0){
				if (identity[i]*GameResult>0){
					datasource.createPlayer(name[i], 1, 0, date);
				}else if (identity[i]*GameResult<0){
					datasource.createPlayer(name[i], 0, 1, date);
				}
			}
		}
        if (TOTAL_PLAYERS<7){
        	view.setColumnWidth((int) getResources().getDimension(R.dimen.itemSize_large));
        }else {
        	view.setColumnWidth((int) getResources().getDimension(R.dimen.itemSize_medium));
        }
		mGridAdapter.notifyDataSetChanged();
		
		View share_layout = findViewById(R.id.layout_share);
		share_layout.setVisibility(View.VISIBLE);
		Button button_twitter=(Button) findViewById(R.id.button_share_twitter);
		Button button_facebook=(Button) findViewById(R.id.button_share_facebook);
		Button button_weibo=(Button) findViewById(R.id.button_share_weibo);
		Button button_wechat=(Button) findViewById(R.id.button_share_wechat);
		
		button_twitter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	             Intent intent = new Intent();
	                ComponentName comp = new ComponentName("com.twitter.android",
	                                "com.twitter.android.PostActivity");
	                intent.setComponent(comp);
	                intent.setAction("android.intent.action.SEND");
	                intent.setType("text/plain");
	                //intent.setType("image/*");
	                intent.putExtra(Intent.EXTRA_TEXT,"This is a test");
	                //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
	                startActivity(intent);
				
			}
		});
		button_facebook.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	             Intent intent = new Intent();
	                ComponentName comp = new ComponentName("com.facebook.katana",
	                                "com.facebook.katana.ShareLinkActivity");
	                intent.setComponent(comp);
	                intent.setAction("android.intent.action.SEND");
	                intent.setType("text/plain");
	                //intent.setType("image/*");
	                intent.putExtra(Intent.EXTRA_TEXT,"This is a test");
	                //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
	                startActivity(intent);
				
			}
		});
		
		button_weibo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	             Intent intent = new Intent();
	                ComponentName comp = new ComponentName("com.tencent.mm",
	                                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
	                intent.setComponent(comp);
	                intent.setAction("android.intent.action.SEND");
	                intent.setType("text/plain");
	                //intent.setType("image/*");
	                intent.putExtra(Intent.EXTRA_TEXT,"This is a test");
	                //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
	                startActivity(intent);
				
			}
		});
		
		button_wechat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                Intent intent = new Intent();
                ComponentName comp = new ComponentName("com.tencent.mm",
                                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                intent.setComponent(comp);
                intent.setAction("android.intent.action.SEND");
                intent.setType("text/plain");
                //intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_TEXT,"This is a test");
                //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                startActivity(intent);
				
			}
		});
		
		//String mPath = Environment.getExternalStorageDirectory().toString() + "/";   
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraTest");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("CameraTest", "Required media storage does not exist");
            }
        }


       DateFormat timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.US);
       Date da = new Date();
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp.format(da)+ ".jpg");
		// create bitmap screen capture
		Bitmap bitmap;
		View v1 = getWindow().getDecorView().getRootView();
		v1.setDrawingCacheEnabled(true);
		bitmap = Bitmap.createBitmap(v1.getDrawingCache());
		v1.setDrawingCacheEnabled(false);

		OutputStream fout = null;
		//File imageFile = new File(mPath);

		try {
		    fout = new FileOutputStream(mediaFile);
		    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
		    fout.flush();
		    fout.close();

		} catch (FileNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
		//
	}
	private void hideFragments() { 
    	FragmentTransaction transaction = fragmentManager.beginTransaction(); 
        if (excutionFragment!=null){
        	transaction.hide(excutionFragment);
        	excutionFragment=null;
        }else if(missionFragment!=null){
        	transaction.remove(missionFragment);
        	missionFragment=null;
        }else if (blankFragment!=null){
        	transaction.remove(blankFragment);
        	blankFragment=null;
        }else if (voteFragment!=null){
        	transaction.remove(voteFragment);
        	voteFragment=null;
        }else if (timerFragment!=null){
        	transaction.remove(timerFragment);
        	timerFragment=null;
        }
        transaction.commit();
    } 
    private void showFragments(int id){
		 //hideFragments(transaction);
    	FragmentTransaction transaction = fragmentManager.beginTransaction(); 
		 switch (id){
		 case 0:{
			 if (missionFragment!=null){
				  transaction.remove(missionFragment);
			 }else if (voteFragment!=null){
				 transaction.remove(voteFragment);
			 }
			 if (excutionFragment == null) {  
	                excutionFragment = new ExecutionFragment();	            
	                transaction.add(R.id.content_gameactivity, excutionFragment);
	            } else { 
	            	transaction.replace(R.id.content_gameactivity,excutionFragment);
	            }
			 Bundle args= new Bundle();
			 args.putString("name",name[CandidatesId[CandidatesId.length-MemberSelected]]);			
			 excutionFragment.setArguments(args);
			 transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			 transaction.commit();  
			 break;
		 }                                                       
			 
		 case 1:{
			 if (excutionFragment!=null)transaction.remove(excutionFragment);
			 if ( missionFragment==null){
				 missionFragment= new MissionFragment();
				 transaction.add(R.id.content_gameactivity,missionFragment);  
			 }else{
				 transaction.replace(R.id.content_gameactivity, missionFragment); 
				// transaction.addToBackStack(null);
			 }
			 Bundle args= new Bundle();
			 args.putString("name",name[CandidatesId[CandidatesId.length-MemberSelected]]);
			 args.putInt("identity", identity[CandidatesId[CandidatesId.length-MemberSelected]]);
			 missionFragment.setArguments(args);
			 transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
			 break;
		 	}
		 case 2:
			 if (timerFragment!=null) transaction.remove(timerFragment);
			 if (voteFragment==null){
				 voteFragment= new VoteFragment();
				 transaction.add(R.id.content_gameactivity,voteFragment, "vote");  
			 }else{
				 transaction.replace(R.id.content_gameactivity, voteFragment, "vote"); 
				// transaction.addToBackStack(null);
			 }
			 Bundle args= new Bundle();
			 args.putInt("Votes Needed", DataSet.NumOfMinPass[TOTAL_PLAYERS]);
			 voteFragment.setArguments(args);
			 transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
			 break;
		 case -1:{
			 if(missionFragment!=null)transaction.remove(missionFragment);
			 if(voteFragment!=null)transaction.remove(voteFragment);
			 if (blankFragment==null){
				 blankFragment= new BlankFragment();
				 transaction.add(R.id.content_gameactivity,blankFragment);  
			 }else{
				 transaction.replace(R.id.content_gameactivity,blankFragment); 
			 }
			 transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			 transaction.commit(); 
			 break;	
		 }
		 case -2:
			 if (timerFragment==null){
				 timerFragment= new TimerFragment();
				 transaction.add(R.id.content_gameactivity,timerFragment);  
			 }else{
				 transaction.replace(R.id.content_gameactivity, timerFragment); 
				// transaction.addToBackStack(null);
			 }
			 transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
			 break;
		 case 10:
			 TopResultFragment topResultFragment =new TopResultFragment();
			 transaction.add(R.id.content_top_gameactivity,topResultFragment);
			
			 Bundle bundle=new Bundle();
			 bundle.putInt("GameResult", GameResult);	
			
			 topResultFragment.setArguments(bundle);
			
			 transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
			 break;
		/*	 
		 case 20:
			 ResultFragment resultFragment =new ResultFragment();
		     transaction.add(R.id.content_gameactivity,resultFragment);	 
		     Bundle bundle1 =new Bundle();  
		     resultFragment.setArguments(bundle1);
		     bundle1.putInt("TOTAL_PLAYERS", TOTAL_PLAYERS);
		     bundle1.putIntArray("identity", identity);
		     transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
		     */
			 default:
				 break;
		 }
		  
    }
    public void changeStatus_Positive(final int CurrentStatus,int Total_Players,int Mission_Number){
				if (CurrentStatus==0){//Propose the nomination
					layout.setVisibility(View.INVISIBLE);
					setBottomStatus(currentMission,0);
					setTopStatus(1,TOTAL_PLAYERS);
					/*
					builder_leader.setMessage(getString(R.string.string_builder_vote_message_before_Game)).setCancelable(false).setPositiveButton(getString(R.string.string_builder_positive_game),
							new DialogInterface.OnClickListener() {								
								@Override
								public void onClick(DialogInterface dialog, int which) {
															
								}
							});
					if (mDialog==null||(!mDialog.isShowing())){
						mDialog=builder_leader.create();
						mDialog.show();
					}*/
					builder_leader.setMessage(getString(R.string.string_builder_vote_message_before_Game)).setPositiveButton(getString(R.string.string_builder_positive_game),
							new DialogInterface.OnClickListener() {								
								@Override
								public void onClick(DialogInterface dialog, int which) {									
								}
							})
							.show();
					showFragments(-2);//show vote Fragment
					view_top.setEnabled(false);
					currentStatus++;
				} else if (CurrentStatus==1){//Nomination passed,ready to execute the mission.
					setTeamProposedStatus(timeOfTeamPropose,true);	
					timeOfTeamPropose=1;
					setTopStatus(2,TOTAL_PLAYERS);
					button.setText(getString(R.string.string_button_start_game));
					MissionResult[currentMission]= new int[DataSet.NumOfMembersPerMission[TOTAL_PLAYERS][currentMission]];
					currentStatus++;
					if (VictoryCount==2||LoseCount==2){
						int failurecount=0;
						for(int i=0;i<CandidatesId.length;i++){
							if (identity[CandidatesId[i]]<0)
								failurecount++;
						}
						if (VictoryCount==2&&checkMissionResult(failurecount,currentMission,TOTAL_PLAYERS)==true){
							winGame();
													
						}else if (LoseCount==2&&checkMissionResult(failurecount,currentMission,TOTAL_PLAYERS)==false) {
							loseGame();
						}
					} 
					if (GameResult==0){
						hideFragments();
						builder_next.setMessage(getString(R.string.string_builder_next_message_main)+":"+name[CandidatesId[CandidatesId.length-MemberSelected]]);
						
						final AlertDialog dlg =builder_next.create();dlg.show();
						Timer mTimer =new Timer();
						mTimer.schedule(new TimerTask(){
							@Override
							public void run() {
							
								dlg.dismiss();
							}}, 2000);
						showFragments(0);  
					}
				}else if(CurrentStatus==2){//Show the mission results
					setTopStatus(0,TOTAL_PLAYERS);
					view_top.setEnabled(true);
					clearTeamProposedStatus();
					int failurecount=0;
					for (int i=0;i<MissionResult[currentMission].length;i++){
						if (MissionResult[currentMission][i]<0){
							failurecount++;
						}
					}
					//Change the leader;
					GridAdapter.Item item =(GridAdapter.Item) mGridAdapter.getItem(round%TOTAL_PLAYERS);
					item.text_top="";
					mGridAdapter.replaceItem(item, round%TOTAL_PLAYERS);
					round++;
					item =(GridAdapter.Item) mGridAdapter.getItem(round%TOTAL_PLAYERS);
					item.text_top=getString(R.string.string_leader);
					mGridAdapter.replaceItem(item, round%TOTAL_PLAYERS);
					mGridAdapter.notifyDataSetChanged();
							if (checkMissionResult(failurecount,currentMission,TOTAL_PLAYERS)==true){
								if (failurecount==0){
									builder_result.setIcon(R.drawable.win).setTitle(getString(R.string.string_builder_title_winresult_game))
									.setMessage(getString(R.string.string_builder_message_winresult_game)+"\n"
									+"\n"+getString(R.string.string_builder_next_message_last_main)+":"+name[round%TOTAL_PLAYERS]).setPositiveButton(getString(R.string.string_builder_positive_game),null);
								}else if (failurecount==1){
									builder_result.setIcon(R.drawable.win).setTitle(getString(R.string.string_builder_title_winresult_game)+"\n"+getString(R.string.string_message_failvotes_game,failurecount))
									.setMessage(getString(R.string.string_builder_message_winresult_game)+"\n"
											+"\n"+getString(R.string.string_builder_next_message_last_main)+":"+name[round%TOTAL_PLAYERS]).setPositiveButton(getString(R.string.string_builder_positive_game),null);
								}
								
								if (mDialog==null||(!mDialog.isShowing())){
									mDialog=builder_result.create();
									mDialog.show();
								}
								setBottomStatus(currentMission,1);
								labelToken(GridAdapter.Token.WIN);
								VictoryCount++;	
							}else if (checkMissionResult(failurecount,currentMission,TOTAL_PLAYERS)==false){
								builder_result.setIcon(R.drawable.lose).setTitle(getString(R.string.string_builder_title_loseresult_game)+"\n"+getString(R.string.string_message_failvotes_game,failurecount))
								.setMessage(getString(R.string.string_builder_message_loseresult_game)+"\n"
								+"\n"
								+getString(R.string.string_builder_next_message_last_main)+":"+name[round%TOTAL_PLAYERS]).setPositiveButton(getString(R.string.string_builder_positive_game),null);
								if (mDialog==null||(!mDialog.isShowing())){
									mDialog=builder_result.create();
									mDialog.show();
								}
								setBottomStatus(currentMission,-1);
								labelToken(GridAdapter.Token.LOSE);
								LoseCount++;					
					}

					if (LoseCount==3){
						loseGame();
					}else if (VictoryCount==3){
						winGame();
					}
					if (currentMission<4&&GameResult==0){
						currentStatus=0;
						currentMission++;
						button.setText(getString(R.string.string_button_propose_game));
						button.setVisibility(View.INVISIBLE);
						resetCandidates();
						layout.setVisibility(View.VISIBLE);
					}				
		}else if (CurrentStatus==3){
			Intent intent= new Intent();
			intent.setClass(GameActivity.this,WelcomeActivity.class);
			startActivity(intent);
			finish();
		}
	}
	private final void labelToken(int status){
		for (int i=0;i<TOTAL_PLAYERS;i++){
			if (isPlayerSelected(i)){
				mGridAdapter.addToken((GridAdapter.Item)mGridAdapter.getItem(i), i, status);
			}else mGridAdapter.addToken((GridAdapter.Item)mGridAdapter.getItem(i), i, GridAdapter.Token.NEUTRAL);
		}
		mGridAdapter.notifyDataSetChanged();
	}
	private boolean checkMissionResult(int failurecount, int i,
			int tOTAL_PLAYERS2) {
		
		if (tOTAL_PLAYERS2>6&&i==3){
			if (failurecount>1) return false;
			else return true;
		}else if (failurecount>0) return false;
		else return true;
	}
	private void changeStatus_Negative(final int CurrentStatus,int Total_Players,int Mission_Number){
		//07.28.2014 @Yuanwei
		//Remove the dialog
		setTeamProposedStatus(timeOfTeamPropose,false);
		timeOfTeamPropose++;
		view_top.setEnabled(true);
		GridAdapter.Item item =(GridAdapter.Item) mGridAdapter.getItem(round%TOTAL_PLAYERS);
		item.text_top="";
		mGridAdapter.replaceItem(item, round%TOTAL_PLAYERS);
		round++;
		setTopStatus(0,TOTAL_PLAYERS);
		setBottomStatus(currentMission,0);
		layout.setVisibility(View.VISIBLE);
		button.setText(getString(R.string.string_button_propose_game));
		button.setVisibility(View.INVISIBLE);
		for (int i=0;i<CandidatesId.length;i++){
			deselectPlayer(CandidatesId[i]);
		}
		resetCandidates();
		MemberSelected=0;currentStatus=0;				
		hideFragments();
		showFragments(-1);
		builder_result.setIcon(R.drawable.veto).setTitle(getString(R.string.string_title_veto_game))
		.setMessage(getString(R.string.string_message_veto_game)+":"+name[round%TOTAL_PLAYERS]).setPositiveButton(getString(R.string.string_builder_positive_game),null).show();
		item =(GridAdapter.Item) mGridAdapter.getItem(round%TOTAL_PLAYERS);
		item.text_top=getString(R.string.string_leader);
		mGridAdapter.replaceItem(item, round%TOTAL_PLAYERS);
		mGridAdapter.notifyDataSetChanged();
		
		/*
		 * Construct a dialog box
		 */
		/*
		builder_vote.setCancelable(false).setMessage(getString(R.string.string_builder_message_team_veto_game,DataSet.NumOfMinPass[TOTAL_PLAYERS]))
		.setPositiveButton(getString(R.string.string_builder_positive_game), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				setTeamProposedStatus(timeOfTeamPropose,false);
				timeOfTeamPropose++;
				view_top.setEnabled(true);
				GridAdapter.Item item =(GridAdapter.Item) mGridAdapter.getItem(round%TOTAL_PLAYERS);
				item.text_top="";
				mGridAdapter.replaceItem(item, round%TOTAL_PLAYERS);
				round++;
				setTopStatus(0,TOTAL_PLAYERS);
				setBottomStatus(currentMission,0);
				layout.setVisibility(View.VISIBLE);
				button.setText(getString(R.string.string_button_propose_game));
				button.setVisibility(View.INVISIBLE);
				for (int i=0;i<CandidatesId.length;i++){
					deselectPlayer(CandidatesId[i]);
				}
				resetCandidates();
				MemberSelected=0;currentStatus=0;				
				hideFragments();
				showFragments(-1);
				builder_result.setIcon(R.drawable.veto).setTitle(getString(R.string.string_title_veto_game))
				.setMessage(getString(R.string.string_message_veto_game)+":"+name[round%TOTAL_PLAYERS]).setPositiveButton(getString(R.string.string_builder_positive_game),null).show();
				item =(GridAdapter.Item) mGridAdapter.getItem(round%TOTAL_PLAYERS);
				item.text_top=getString(R.string.string_leader);
				mGridAdapter.replaceItem(item, round%TOTAL_PLAYERS);
				mGridAdapter.notifyDataSetChanged();
			}
		}).setNegativeButton(getString(R.string.string_builder_negative_game),new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
		
			}
		});
		if (mDialog==null||(!mDialog.isShowing())){
			mDialog=builder_vote.create();
			mDialog.show();
		}
		*/
	}
	@Override
	public void onMissionExcuted(int id) {
			
		MissionResult[currentMission][CandidatesId.length-MemberSelected]=(CandidatesId[CandidatesId.length-MemberSelected]+1)*id;
		MemberSelected--;
		if (MemberSelected==0){
			builder_next.setMessage(getString(R.string.string_builder_next_message_last_main));
			layout.setVisibility(View.VISIBLE);	
			hideFragments();
			showFragments(-1);		
		}else {			
			builder_next.setMessage(getString(R.string.string_builder_next_message_main)+":"+name[CandidatesId[CandidatesId.length-MemberSelected]]);	
			showFragments(0);	
		}		
		final AlertDialog dlg =builder_next.create();
		dlg.show();
		TimerTask mTimerTask= new TimerTask(){
		@Override
		public void run() {
			
			dlg.dismiss();
		}	
	};
	Timer mTimer =new Timer();
	mTimer.schedule(mTimerTask, 2000);
};

	@Override
	public void onPreMissionExcution(int id) {
		
		 showFragments(1);
		
	}
	@Override
	public void onVoteResult(int id) {
	
		if (id==1){
			
			if (MissionResult[currentMission]==null)
				changeStatus_Positive(currentStatus,TOTAL_PLAYERS,currentMission);
				/*
			builder_vote.setCancelable(false).setMessage(getString(R.string.string_builder_message_team_pass_game,DataSet.NumOfMinPass[TOTAL_PLAYERS]))
			.setPositiveButton(getString(R.string.string_builder_positive_game), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					if (MissionResult[currentMission]==null){
						changeStatus_Positive(currentStatus,TOTAL_PLAYERS,currentMission);	
					}
					
				}
			
			}).setNegativeButton(getString(R.string.string_builder_negative_game),new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			if (mDialog==null||(!mDialog.isShowing())){
				mDialog=builder_vote.create();
				mDialog.show();
			}
			*/
			
		}else if (id==0){
			if (timeOfTeamPropose==5){
				Toast.makeText(getApplicationContext(), getString(R.string.string_toast_lastpropose_game), Toast.LENGTH_SHORT).show();
			}else 
			changeStatus_Negative(currentStatus,TOTAL_PLAYERS,currentMission);
		}		
	}
	@Override
	public void onCheckResult(int id) {//Interface for the timerFragment,which counts the time elapsed in discussion before vote
		
		hideFragments();
		showFragments(2);
	}
	  @Override
	public void onResume() {
	    datasource.open();
	    super.onResume();
	  }

	  @Override
	public void onPause() {
	    datasource.close();
	    super.onPause();
	  }
	  
}

/*
 * 提名组队 对话：
 * 投票指示对话：
 * 提名通过
 * 提名否决
 * 第五轮无讨论，直接跳到执行环节
 * 执行前提示：
 * 
 */


