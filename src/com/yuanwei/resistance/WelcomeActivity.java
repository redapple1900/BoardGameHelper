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


