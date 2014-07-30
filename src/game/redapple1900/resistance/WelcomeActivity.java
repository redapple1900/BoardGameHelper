package game.redapple1900.resistance;

import game.redapple1900.gridviewtest.GridAdapter;
import game.redapple1900.gridviewtest.GridAdapter.Item;
import game.redapple1900.listviewtest.ListAdapter;
import game.redapple1900.playerdatabase.Player;
import game.redapple1900.playerdatabase.PlayerDataSource;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.InputFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import java.util.Calendar;
import java.util.List;
import game.redapple1900.resistance.R;

import android.widget.AutoCompleteTextView;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class WelcomeActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	private QuickStartFragment quickstartFragment;
	private ContactFragment contactFragment;
	private CustomGameFragment customGameFragment;
	private static ListView listView;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		this.setTheme(android.R.style.Theme_Wallpaper_NoTitleBar_Fullscreen);  
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
	    setContentView(R.layout.newactivity_welcome);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setCurrentItem(1, true);
		

	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	*/

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			switch(position){
			case 0:
				if (customGameFragment==null){
					customGameFragment=new CustomGameFragment();
				}
				return customGameFragment;
			case 1:
				if (quickstartFragment==null){
					quickstartFragment=new QuickStartFragment();
				}
				return  quickstartFragment;
			case 2:
				if (contactFragment==null){
					contactFragment =new ContactFragment();
				}
				return  contactFragment;
			default:
				return null;
			}		
			/*
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
			*/
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
		
	}


	public static class CustomGameFragment extends Fragment{
		 private PlayerDataSource datasource;
		 private ImageView mImageView;
		 private GridView grid;
		 private int TotalPlayers;
		 private static  String[] name;
		 private AlertDialog.Builder builder;
		 private static long idlist[];
		 private GridAdapter mGridAdapter;
		 private ListAdapter mListAdapter;
		 private Dialog dialog;
		 //private Spinner spinner;
		 private Button bn_stranger,bn_friend,bn_clear,bn_start;
		 public View onCreateView(LayoutInflater inflater, ViewGroup container,  
		            Bundle savedInstanceState) {  
		        View v = inflater.inflate(R.layout.fragment_management, container, false); 
		        if (savedInstanceState==null){
		        	 TotalPlayers=0;
		 	        name = new String[10];
		 	        idlist= new long[10];
		 	        dialog = new Dialog(getActivity());
		 	        datasource = new PlayerDataSource(getActivity());
			        mGridAdapter=new GridAdapter(getActivity());
			        for (int i = 0; i <1;i++) {
		        	GridAdapter.Item item = new GridAdapter.Item();
		            item.text = "";
		            item.resId = R.drawable.index;    
		            mGridAdapter.addItem(item);
			        }
		 	        
		        }
		       builder=new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
		        datasource.open();
		        mListAdapter=new ListAdapter(getActivity(),(List<Player>)datasource.getAllPlayersByDate());
		        datasource.close();

		        listView=(ListView)v.findViewById(R.id.list_management);
		        listView.setAdapter(mListAdapter);
		        registerForContextMenu(listView);
		       
		        mImageView=(ImageView)v.findViewById(R.id.imageView_game);
				mImageView.setClickable(true);mImageView.setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						builder.setMessage(getResources().getString(R.string.string_manu)).setPositiveButton(getString(R.string.string_exit_negative),
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										
									}
								}).show();
					}

					});
		        /*
		        bn_manu=(Button)v.findViewById(R.id.button_manule);
		        bn_manu.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						
					}});
					*/
		        bn_stranger=(Button) v.findViewById(R.id.button_management1);
		        bn_stranger.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (TotalPlayers<10&&TotalPlayers>0){
							GridAdapter.Item item = new GridAdapter.Item();
				            item.text =getString(R.string.string_newplayer_customgame);
				            item.resId = R.drawable.index;    
				            mGridAdapter.addItem(item);
				            mGridAdapter.notifyDataSetChanged();
				            TotalPlayers++;	
						}else if (TotalPlayers==0){
							GridAdapter.Item item = new GridAdapter.Item();
				            item.text =getString(R.string.string_newplayer_customgame);
				            item.resId = R.drawable.index;    
				            mGridAdapter.replaceItem(item,0);
				            mGridAdapter.notifyDataSetChanged();
				            TotalPlayers++;	
						}else Toast.makeText(getActivity(), getActivity().getString(R.string.string_toast1_game),Toast.LENGTH_SHORT).show();
						
					}});
		        
		        bn_friend=(Button) v.findViewById(R.id.button_management2);
		        bn_friend.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						/*
						ed= new EditText(getActivity());
						ed.setFilters(new InputFilter[] {new InputFilter.LengthFilter(15)});
						builder.setView(ed);
						builder.show();
						*/
						showDialog();
						//showCusPopUp(arg0);
					}});
		        bn_clear=(Button) v.findViewById(R.id.button_clear_management);
		        bn_clear.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

							mGridAdapter.removeAllItems();
							GridAdapter.Item item = new GridAdapter.Item();
				            item.text = "";
				            item.resId = R.drawable.index;    
				            mGridAdapter.addItem(item);
				            mGridAdapter.notifyDataSetChanged();
				            idlist= new long[10];
				            name= new String[10];
				            TotalPlayers=0;
						
					}});
		        bn_start=(Button) v.findViewById(R.id.button_startgame_management2);
		        bn_start.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View arg0) {
					
						if (TotalPlayers>4){
							Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putLongArray("idlist", idlist);
						bundle.putStringArray("name", name);
						bundle.putInt("TOTAL_PLAYERS",TotalPlayers);
						bundle.putInt("NORMAL_PLAYERS",getNormalPlayers(TotalPlayers));
						
						intent.setClass(getActivity(),MainActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
						getActivity().finish();
						}else Toast.makeText(getActivity(), getString(R.string.toast_customgame), Toast.LENGTH_SHORT).show();
						
					}});
		        //
		        /*
		        builder= new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_DARK).setTitle(getString(R.string.string_builder_inputname_title_main)).setCancelable(false)
		        		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        			
		        			@Override
		        			public void onClick(DialogInterface dialog, int which) {
		        				// TODO Auto-generated method stub
		        				if ((name[TotalPlayers]=ed.getText().toString().trim()).contentEquals("")){
		        					name[TotalPlayers]=defaultName[TotalPlayers];
		        					Toast.makeText(getActivity(),getActivity().getString(R.string.string_toast1_main)+name[TotalPlayers], Toast.LENGTH_SHORT).show();
		        				}
		        				GridAdapter.Item item= new GridAdapter.Item();
		        				item.text=name[TotalPlayers];item.resId=R.drawable.index;
		        				mGridAdapter.replaceItem(item, TotalPlayers);
		        				mGridAdapter.notifyDataSetChanged();			
		        				//Datebase 
		        			}
		        		});
		        //
		         * */
		         

		        grid=(GridView)(v.findViewById(R.id.grid_management));
		        grid.setAdapter(mGridAdapter);
		        setLayoutOnClick();
		        //Field of Spinner and its adpater
		        /*
		        spinner=(Spinner)v.findViewById(R.id.spinner_management);
		        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,NumOfGamers);
		        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        spinner.setAdapter(adapter);
		        spinner.setBackgroundColor(Color.WHITE);
		        spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						TotalPlayers=Integer.parseInt(NumOfGamers[arg2]);
						arg0.setVisibility(View.VISIBLE);
						tv.setText(getString(R.string.string_eachsides_quickstart,getNormalPlayers(TotalPlayers),getSpyPlayers(TotalPlayers)));
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						TotalPlayers=5;
					}});
					*/
		       
		        return v;  
		    } 
			@Override
			public void onConfigurationChanged(Configuration newConfig) {
			    super.onConfigurationChanged(newConfig);
			    //your own stuff to handle orientation change, if needed
			} 
			       
		 public int getNormalPlayers(int TotalPlayers){
				switch(TotalPlayers){
				case 5: return 3;
				case 6:	return 4;
				case 7: return 4;
				case 8:	return 5;
				case 9: return 6;
				case 10:return 6;
				default: return 0;
				}
			}
		 public int getSpyPlayers(int TotalPlayers){
					switch(TotalPlayers){
					case 5: return 2;
					case 6:	return 2;
					case 7: return 3;
					case 8:	return 3;
					case 9: return 3;
					case 10:return 4;
					default: return 0;
					}				
			}
		 /*
		 private void showCusPopUp(View parent)  
		    {  
		        //if(window == null)  
		        //{  
		            View popupView=LayoutInflater.from(getActivity()).inflate(R.layout.view_dialog_input_name, null);  
		            parent.findViewById(R.id.autoCompleteTextView1);

		            PopupWindow window =new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
		        //}  
		       // window.setAnimationStyle(R.style.PopupAnimation);  
		        /*�������setBackgroundDrawable�� ��Ϊpopupwindow�ڳ�ʼʱ������background�Ƿ�Ϊnull,�����onTouch or onKey events�Ͳ�����Ӧ�����Ա�������background*/  
		        /*����Ҳ�кܶ���˵������pop֮�󣬲���Ӧ�����¼��ˣ������ʵ�ǽ�����pop�����viewȥ�ˡ�*/  
		 /*
		        window.setFocusable(true);  
		        window.setBackgroundDrawable(new ColorDrawable());   
		        window.update();  
		        window.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);
		        /*
		        ed.setOnFocusChangeListener(new OnFocusChangeListener(){

					@Override
					public void onFocusChange(View v, boolean isFocused) {
						// TODO Auto-generated method stub
						if (isFocused){
							   InputMethodManager inputMgr = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				                inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				                inputMgr.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
						}
					}});
	            ed.requestFocus();
	            */
		        /*
		        cusPopupBtn1.setOnClickListener(new OnClickListener() {  
		            @Override  
		            public void onClick(View v) {  
		                // TODO Auto-generated method stub  
		                showClickMessage("popup window��ȷ��");  
		            }  
		            
		        }); 
		    } 
		    */
		 @Override
			public void onCreateContextMenu(ContextMenu arg0, View arg1,
					ContextMenuInfo arg2) {
				// TODO Auto-generated method stub
				super.onCreateContextMenu(arg0, arg1, arg2);
			arg0.add(Menu.NONE,Menu.FIRST,Menu.NONE,"Delete");
			}
		 //Super Important, Sounds like the reason is that do not return super.OnContextItemSelected.
		 public boolean onContextItemSelected(MenuItem menuItem){
			 AdapterContextMenuInfo info = (AdapterContextMenuInfo) (menuItem)
			          .getMenuInfo();
			 Log.d(((menuItem).getItemId()+""), "2234");
			switch((menuItem).getItemId()){
			case Menu.FIRST:
				final long id =((Player)(mListAdapter.getItem(info.position))).getId();
				datasource.deletePlayer(id);
				Log.d(id+"", "database delete");
				mListAdapter.removeItem(info.position);
				Log.d(info.position+"", "check");
				mListAdapter.notifyDataSetChanged();
				deselectPlayer(id);			
				break;
			}
			return true;
		}
		private void deselectPlayer(long id) {
			// TODO Auto-generated method stub
			int i;
			boolean flag=false;
			for (i = 0; i < TotalPlayers; i++) {
				if (idlist[i]==id) {
					flag = true;
					break;
				}
			}
			if (flag == true) {
				if (TotalPlayers > 0) {
					TotalPlayers--;
					for (int j = i; j < TotalPlayers; j++) {
						mGridAdapter.replaceItem(
								(Item) mGridAdapter.getItem(j + 1), j);
						idlist[j] = idlist[j + 1];
						name[j] = name[j + 1];
					}
					mGridAdapter.removeItem(TotalPlayers);
					name[TotalPlayers]="";
					idlist[TotalPlayers]=0;
					if (TotalPlayers==0){
						GridAdapter.Item item = new GridAdapter.Item("", R.drawable.index);
						mGridAdapter.replaceItem(item, 0);
					}
				} 
			}
			mGridAdapter.notifyDataSetChanged();
		}


		public void showDialog() {
			if (dialog != null) {
				dialog.setContentView(R.layout.view_dialog_input_name);
				dialog.setTitle(R.string.string_builder_inputname_title_main);
				dialog.setCancelable(false);

				final AutoCompleteTextView ed = (AutoCompleteTextView) dialog
						.findViewById(R.id.autoCompleteTextView1);
				ed.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
				final InputMethodManager inputMgr = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				// inputMgr.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);

				Button positive = (Button) dialog
						.findViewById(R.id.Button_OK_input);
				positive.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
				positive.setTextColor(getResources().getColor(android.R.color.primary_text_light));
				positive.setPadding(1,1, 1, 1);
				Button negative = (Button) dialog
						.findViewById(R.id.button_Cancel_input);
				negative.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
				negative.setTextColor(getResources().getColor(android.R.color.primary_text_light));
				negative.setPadding(1,1, 1, 1);
				positive.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						if (ed.getText().toString().isEmpty()
								|| ed.getText().toString().trim().contentEquals("")) {
							Animation shake = AnimationUtils.loadAnimation(
									getActivity(), R.anim.shake);
							ed.startAnimation(shake);
							// Toast.makeText(Information.this,
							// "Please enter an age", Toast.LENGTH_SHORT).show();
						} else {
							// database.updateAge(Integer.parseInt(ageEditText.getText().toString()));
							inputMgr.hideSoftInputFromWindow(ed.getWindowToken(), 0);
							// Todo:Select player with the same name in the
							// database--
							final Calendar myCalendar;
							myCalendar = Calendar.getInstance();
							myCalendar.setTimeInMillis(System.currentTimeMillis());
							final int year = myCalendar.get(Calendar.YEAR);
							final int month = myCalendar.get(Calendar.MONTH)+1;
							final int day = myCalendar.get(Calendar.DAY_OF_MONTH);
							final String date;
							if (month<10){
								date = year + "-0" + month + "-" + day;
							}else {
								 date = year + "-" + month + "-" + day;
							}
							
							final Player player = datasource.createPlayer(ed
									.getText().toString().trim(), 0, 0, date);
							mListAdapter.addPlayer(player);
							mListAdapter.notifyDataSetChanged();
							name[TotalPlayers] = player.getName();
							idlist[TotalPlayers]=player.getId();
							Log.d(player.getId()+"", "add player");
							if (TotalPlayers==0){
								mGridAdapter.replaceItem(new GridAdapter.Item(player
									.getName(), R.drawable.index), 0);
							}else{
								mGridAdapter.addItem(new GridAdapter.Item(player
										.getName(), R.drawable.index));
							}
							
							mGridAdapter.notifyDataSetChanged();
							TotalPlayers++;
							dialog.dismiss();
						}
					}
				});

				negative.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						inputMgr.hideSoftInputFromWindow(ed.getWindowToken(), 0);
						dialog.dismiss();
					}
				});
			dialog.show();
			}

		}

		private void setLayoutOnClick() {

			grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					if (TotalPlayers > 0) {
						TotalPlayers--;
						for (int j = arg2; j < TotalPlayers; j++) {
							mGridAdapter.replaceItem(
									(Item) mGridAdapter.getItem(j + 1), j);
							idlist[j] = idlist[j + 1];
							name[j] = name[j + 1];
						}
						mGridAdapter.removeItem(TotalPlayers);
						name[TotalPlayers]="";
						idlist[TotalPlayers]=0;
						if (TotalPlayers==0){
							GridAdapter.Item item = new GridAdapter.Item("", R.drawable.index);
							mGridAdapter.replaceItem(item, 0);
						}
					} 
					mGridAdapter.notifyDataSetChanged();
				}
			});
			/*
			listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

				@Override
				public void onCreateContextMenu(ContextMenu arg0, View arg1,
						ContextMenuInfo arg2) {
					// TODO Auto-generated method stub
					//super.onCreateContextMenu(arg0, arg1, arg2);
					Log.d("123", "123");
				arg0.add(Menu.NONE,Menu.FIRST,Menu.NONE,"Delete");
				}});
			*/
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					int i;
					boolean flag = false;
					final Player player = (Player) arg0.getItemAtPosition(arg2);
					final long id = player.getId();
					for (i = 0; i < TotalPlayers; i++) {
						if (id == idlist[i]) {
							flag = true;
							break;
						}
					}
					if (flag == true) {
						Toast.makeText(getActivity(), "Already in team",
								Toast.LENGTH_LONG).show();
						Animation shake = AnimationUtils.loadAnimation(
								getActivity(), R.anim.shake);
						arg1.setAnimation(shake);
						
					} else if (TotalPlayers < 10) {
						idlist[TotalPlayers] = id;
						name[TotalPlayers] = player.getName();
						GridAdapter.Item item = new GridAdapter.Item(player
								.getName(), R.drawable.index);
						mGridAdapter.replaceItem(item, TotalPlayers);
						TotalPlayers++;

					} else {
						Toast.makeText(getActivity(), "Too many members",
								Toast.LENGTH_LONG).show();
						Animation shake = AnimationUtils.loadAnimation(
								getActivity(), R.anim.shake);
						arg1.setAnimation(shake);
						
					}
					mGridAdapter.notifyDataSetChanged();
				}

			});
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
}

//private class OldWelcomeActivity extends Activity implements OnClickListener {  
	  
	    /** 
	     * ����չʾ��Ϣ��Fragment 
	     */  
	   // private RulesFragment rulesFragment;  
	  
	    /** 
	     * ����չʾ��ϵ�˵�Fragment 
	     */  
	  //  private ContactFragment contactFragment;  
	  
	    /** 
	     * ����չʾ��̬��Fragment 
	     */  
	    //private ManagementFragment managementFragment;
	   // private QuickStartFragment quickstartFragment;  
	  
	    /** 
	     * ����չʾ���õ�Fragment 
	     */  
	   // private SettingFragment settingFragment;  
	  
	    /** 
	     * ��Ϣ���沼�� 
	     */  
	   // private View rulesLayout;  
	  
	    /** 
	     * ��ϵ�˽��沼�� 
	     */  
	   // private View contactsLayout;  
	  
	    /** 
	     * ��̬���沼�� 
	     */  
	    //private View managementLayout;  
	  
	    /** 
	     * ���ý��沼�� 
	     */  
	  //  private View settingLayout;  
	  
	    /** 
	     * ��Tab��������ʾ��Ϣͼ��Ŀؼ� 
	      
	    private ImageView rulesImage;  
	  
	   
	     * ��Tab��������ʾ��ϵ��ͼ��Ŀؼ� 
	    
	    private ImageView managementImage;  
	  
	    
	     * ��Tab��������ʾ��̬ͼ��Ŀؼ� 
	     
	    private ImageView settingImage;  
	  
	    /** 
	     * ��Tab��������ʾ����ͼ��Ŀؼ� 
	     
	    private ImageView contactsImage;  
	  
	    /** 
	     * ��Tab��������ʾ��Ϣ����Ŀؼ� 
	     */  
	    //private TextView rulesText;  
	  
	    /** 
	     * ��Tab��������ʾ��ϵ�˱���Ŀؼ� 
	     */  
	    //private TextView managementText;  
	  
	    /** 
	     * ��Tab��������ʾ��̬����Ŀؼ� 
	     */  
	   // private TextView settingText;  
	  
	    /** 
	     * ��Tab��������ʾ���ñ���Ŀؼ� 
	     */  
	    //private TextView contactText;  
	  
	    /** 
	     * ���ڶ�Fragment���й��� 
	     
	    private FragmentManager fragmentManager;  
	  
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        this.setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);  
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
	        setContentView(R.layout.activity_welcome);  
	        // ��ʼ������Ԫ��  
	        initViews();  
	       // fragmentManager = getFragmentManager();  
	        // ��һ������ʱѡ�е�1��tab  
	        setTabSelection(1);  
	    }  
	  
	       
	    private void initViews() {  
	        rulesLayout = findViewById(R.id.rules_layout);  
	        managementLayout = findViewById(R.id.management_layout);  
	        contactsLayout = findViewById(R.id.contact_layout);  
	        settingLayout = findViewById(R.id.setting_layout);  
	        /*
	        rulesImage = (ImageView) findViewById(R.id.rules_image);  
	        managementImage = (ImageView) findViewById(R.id.management_image);  
	        settingImage = (ImageView) findViewById(R.id.setting_image);  
	        contactsImage = (ImageView) findViewById(R.id.contact_image);\
	         
	        rulesText = (TextView) findViewById(R.id.rules_text);  
	        managementText = (TextView) findViewById(R.id.management_text);  
	        settingText = (TextView) findViewById(R.id.setting_text);  
	        contactText = (TextView) findViewById(R.id.contact_text);  
	        rulesLayout.setOnClickListener(this);  
	        contactsLayout.setOnClickListener(this);  
	        managementLayout.setOnClickListener(this);  
	        settingLayout.setOnClickListener(this);  
	    }  
	  /*
	    @Override  
	    public void onClick(View v) {  
	        switch (v.getId()) {  
	        case R.id.rules_layout:  
	            // ���������Ϣtabʱ��ѡ�е�1��tab 
	        	Log.d("Clicked","rules");
	            setTabSelection(0);  
	            break;  
	        case R.id.management_layout:  
	            // ���������ϵ��tabʱ��ѡ�е�2��tab  
	        	Log.d("Clicked","management");
	            setTabSelection(1);  
	            break;  
	        case R.id.setting_layout:  
	            // ������˶�̬tabʱ��ѡ�е�3��tab 
	        	Log.d("Clicked","setting");
	            setTabSelection(2);  
	            break;  
	        case R.id.contact_layout:  
	            // �����������tabʱ��ѡ�е�4��tab  
	        	Log.d("Clicked","contact");
	            setTabSelection(3);  
	            break;  
	        default:  
	            break;  
	        }  
	    }  
	  
	    /** 
	     * ���ݴ����index����������ѡ�е�tabҳ�� 
	     *  
	     * @param index 
	     *            ÿ��tabҳ��Ӧ���±ꡣ0��ʾ��Ϣ��1��ʾ��ϵ�ˣ�2��ʾ��̬��3��ʾ���á� 
	     */
	    /*
	    private void setTabSelection(int index) {  
	        // ÿ��ѡ��֮ǰ��������ϴε�ѡ��״̬  
	        clearSelection();
	        Log.d("Cleared","TabSelection");
	        // ����һ��Fragment����  
	        FragmentTransaction transaction = fragmentManager.beginTransaction();  
	        // �����ص����е�Fragment���Է�ֹ�ж��Fragment��ʾ�ڽ����ϵ����  
	        hideFragments(transaction);  
	        switch (index) {  
	        case 0:  
	            // ���������Ϣtabʱ���ı�ؼ���ͼƬ��������ɫ  
	            //messageImage.setImageResource(R.drawable.message_selected);  
	            rulesText.setTextColor(Color.WHITE);  
	            if (rulesFragment == null) {  
	                // ���rulesFragmentΪ�գ��򴴽�һ������ӵ�������  
	                rulesFragment = new RulesFragment();  
	                transaction.add(R.id.content, rulesFragment);  
	            } else {  
	                // ���rulesFragment��Ϊ�գ���ֱ�ӽ�����ʾ����  
	                transaction.show(rulesFragment);  
	            }  
	            break;  
	        case 3:  
	            // ���������ϵ��tabʱ���ı�ؼ���ͼƬ��������ɫ  
	        	
	           // contactsImage.setImageResource(R.drawable.contacts_selected);  
	            contactText.setTextColor(Color.WHITE);  
	            if (contactFragment == null) {  
	                // ���ContactsFragmentΪ�գ��򴴽�һ������ӵ�������  
	                contactFragment = new ContactFragment();  
	                transaction.add(R.id.content, contactFragment);  
	            } else {  
	                // ���ContactsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����  
	                transaction.show(contactFragment);  
	            }
	              
	            break;  
	        case 1:  
	            // ������˶�̬tabʱ���ı�ؼ���ͼƬ��������ɫ  
	        	
	            //newsImage.setImageResource(R.drawable.news_selected);  
	            managementText.setTextColor(Color.WHITE);  
	            if (quickstartFragment== null) {  
	                // ���NewsFragmentΪ�գ��򴴽�һ������ӵ�������  
	            	quickstartFragment = new QuickStartFragment();  
	                transaction.add(R.id.content, quickstartFragment);  
	            } else {  
	                // ���NewsFragment��Ϊ�գ���ֱ�ӽ�����ʾ����  
	                transaction.show(quickstartFragment);  
	            }  
	            
	            break;  
	        case 2: 
	        	
	          
	            // �����������tabʱ���ı�ؼ���ͼƬ��������ɫ  
	           // settingImage.setImageResource(R.drawable.setting_selected);  
	            settingText.setTextColor(Color.WHITE);  
	            if (managementFragment == null) {  
	                // ���SettingFragmentΪ�գ��򴴽�һ������ӵ�������  
	                managementFragment = new ManagementFragment();  
	                transaction.add(R.id.content, managementFragment);  
	            } else {  
	                // ���SettingFragment��Ϊ�գ���ֱ�ӽ�����ʾ����  
	                transaction.show(managementFragment);  
	            }
	              
	            break;
	        	default:
	        	break;
	             
	        }  
	        transaction.commit();  
	    }  
	  
	    /** 
	     * ��������е�ѡ��״̬�� 
	     */  
	    /*
	    private void clearSelection() {  
	        //messageImage.setImageResource(R.drawable.message_unselected);  
	    	rulesText.setTextColor(Color.parseColor("#82858b"));  
	       // contactsImage.setImageResource(R.drawable.contacts_unselected);  
	        managementText.setTextColor(Color.parseColor("#82858b"));  
	        //newsImage.setImageResource(R.drawable.news_unselected);  
	        settingText.setTextColor(Color.parseColor("#82858b"));  
	        //settingImage.setImageResource(R.drawable.setting_unselected);  
	        contactText.setTextColor(Color.parseColor("#82858b"));  
	    }  
	  */
	    /** 
	     * �����е�Fragment����Ϊ����״̬�� 
	     *  
	     * @param transaction 
	     *            ���ڶ�Fragmentִ�в��������� 
	      
	    private void hideFragments(FragmentTransaction transaction) {  
	        if (rulesFragment != null) {  
	            transaction.hide(rulesFragment);  
	        }
	        
	        if (contactFragment != null) {  
	            transaction.hide(contactFragment);  
	        }  
	        if (managementFragment != null) {  
	            transaction.hide(managementFragment);  
	        }  
	        if (settingFragment != null) {  
	            transaction.hide(settingFragment);  
	        } 
	        if (quickstartFragment!=null){
	        	transaction.hide(quickstartFragment);
	        }
	        
	    }  
	}
}
	
	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
	*/


