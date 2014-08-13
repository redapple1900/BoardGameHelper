package com.yuanwei.resistance;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import game.redapple1900.resistance.R;

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
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	

		GeneralMethodSet gms=new GeneralMethodSet();
		gms.updateLanguage(this);
		gms.setActivityTheme(this);
		gms=null;
		
	    setContentView(R.layout.newactivity_welcome);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setCurrentItem(0, true);
		

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
			//Move CustomGameFragment to another Activity 07.31.2014 @Yuanwei
			
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
			// Show 2 total pages.
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
}

//private class OldWelcomeActivity extends Activity implements OnClickListener {  
	  
	    /** 
	     * 用于展示消息的Fragment 
	     */  
	   // private RulesFragment rulesFragment;  
	  
	    /** 
	     * 用于展示联系人的Fragment 
	     */  
	  //  private ContactFragment contactFragment;  
	  
	    /** 
	     * 用于展示动态的Fragment 
	     */  
	    //private ManagementFragment managementFragment;
	   // private QuickStartFragment quickstartFragment;  
	  
	    /** 
	     * 用于展示设置的Fragment 
	     */  
	   // private SettingFragment settingFragment;  
	  
	    /** 
	     * 消息界面布局 
	     */  
	   // private View rulesLayout;  
	  
	    /** 
	     * 联系人界面布局 
	     */  
	   // private View contactsLayout;  
	  
	    /** 
	     * 动态界面布局 
	     */  
	    //private View managementLayout;  
	  
	    /** 
	     * 设置界面布局 
	     */  
	  //  private View settingLayout;  
	  
	    /** 
	     * 在Tab布局上显示消息图标的控件 
	      
	    private ImageView rulesImage;  
	  
	   
	     * 在Tab布局上显示联系人图标的控件 
	    
	    private ImageView managementImage;  
	  
	    
	     * 在Tab布局上显示动态图标的控件 
	     
	    private ImageView settingImage;  
	  
	    /** 
	     * 在Tab布局上显示设置图标的控件 
	     
	    private ImageView contactsImage;  
	  
	    /** 
	     * 在Tab布局上显示消息标题的控件 
	     */  
	    //private TextView rulesText;  
	  
	    /** 
	     * 在Tab布局上显示联系人标题的控件 
	     */  
	    //private TextView managementText;  
	  
	    /** 
	     * 在Tab布局上显示动态标题的控件 
	     */  
	   // private TextView settingText;  
	  
	    /** 
	     * 在Tab布局上显示设置标题的控件 
	     */  
	    //private TextView contactText;  
	  
	    /** 
	     * 用于对Fragment进行管理 
	     
	    private FragmentManager fragmentManager;  
	  
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        this.setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);  
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);   
	        setContentView(R.layout.activity_welcome);  
	        // 初始化布局元素  
	        initViews();  
	       // fragmentManager = getFragmentManager();  
	        // 第一次启动时选中第1个tab  
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
	            // 当点击了消息tab时，选中第1个tab 
	        	Log.d("Clicked","rules");
	            setTabSelection(0);  
	            break;  
	        case R.id.management_layout:  
	            // 当点击了联系人tab时，选中第2个tab  
	        	Log.d("Clicked","management");
	            setTabSelection(1);  
	            break;  
	        case R.id.setting_layout:  
	            // 当点击了动态tab时，选中第3个tab 
	        	Log.d("Clicked","setting");
	            setTabSelection(2);  
	            break;  
	        case R.id.contact_layout:  
	            // 当点击了设置tab时，选中第4个tab  
	        	Log.d("Clicked","contact");
	            setTabSelection(3);  
	            break;  
	        default:  
	            break;  
	        }  
	    }  
	  
	    /** 
	     * 根据传入的index参数来设置选中的tab页。 
	     *  
	     * @param index 
	     *            每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。 
	     */
	    /*
	    private void setTabSelection(int index) {  
	        // 每次选中之前先清楚掉上次的选中状态  
	        clearSelection();
	        Log.d("Cleared","TabSelection");
	        // 开启一个Fragment事务  
	        FragmentTransaction transaction = fragmentManager.beginTransaction();  
	        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况  
	        hideFragments(transaction);  
	        switch (index) {  
	        case 0:  
	            // 当点击了消息tab时，改变控件的图片和文字颜色  
	            //messageImage.setImageResource(R.drawable.message_selected);  
	            rulesText.setTextColor(Color.WHITE);  
	            if (rulesFragment == null) {  
	                // 如果rulesFragment为空，则创建一个并添加到界面上  
	                rulesFragment = new RulesFragment();  
	                transaction.add(R.id.content, rulesFragment);  
	            } else {  
	                // 如果rulesFragment不为空，则直接将它显示出来  
	                transaction.show(rulesFragment);  
	            }  
	            break;  
	        case 3:  
	            // 当点击了联系人tab时，改变控件的图片和文字颜色  
	        	
	           // contactsImage.setImageResource(R.drawable.contacts_selected);  
	            contactText.setTextColor(Color.WHITE);  
	            if (contactFragment == null) {  
	                // 如果ContactsFragment为空，则创建一个并添加到界面上  
	                contactFragment = new ContactFragment();  
	                transaction.add(R.id.content, contactFragment);  
	            } else {  
	                // 如果ContactsFragment不为空，则直接将它显示出来  
	                transaction.show(contactFragment);  
	            }
	              
	            break;  
	        case 1:  
	            // 当点击了动态tab时，改变控件的图片和文字颜色  
	        	
	            //newsImage.setImageResource(R.drawable.news_selected);  
	            managementText.setTextColor(Color.WHITE);  
	            if (quickstartFragment== null) {  
	                // 如果NewsFragment为空，则创建一个并添加到界面上  
	            	quickstartFragment = new QuickStartFragment();  
	                transaction.add(R.id.content, quickstartFragment);  
	            } else {  
	                // 如果NewsFragment不为空，则直接将它显示出来  
	                transaction.show(quickstartFragment);  
	            }  
	            
	            break;  
	        case 2: 
	        	
	          
	            // 当点击了设置tab时，改变控件的图片和文字颜色  
	           // settingImage.setImageResource(R.drawable.setting_selected);  
	            settingText.setTextColor(Color.WHITE);  
	            if (managementFragment == null) {  
	                // 如果SettingFragment为空，则创建一个并添加到界面上  
	                managementFragment = new ManagementFragment();  
	                transaction.add(R.id.content, managementFragment);  
	            } else {  
	                // 如果SettingFragment不为空，则直接将它显示出来  
	                transaction.show(managementFragment);  
	            }
	              
	            break;
	        	default:
	        	break;
	             
	        }  
	        transaction.commit();  
	    }  
	  
	    /** 
	     * 清除掉所有的选中状态。 
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
	     * 将所有的Fragment都置为隐藏状态。 
	     *  
	     * @param transaction 
	     *            用于对Fragment执行操作的事务 
	      
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


