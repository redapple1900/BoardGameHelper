package com.yuanwei.resistance;

import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.speech.tts.UtteranceProgressListener;

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

import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.yuanwei.resistance.gridviewtest.GridAdapter;
import com.yuanwei.resistance.texttospeech.Config;
import com.yuanwei.resistance.texttospeech.ScriptGenerator;
import com.yuanwei.resistance.widget.ButtonOnTouchListener;

@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity implements OnInitListener,
		ExecutionFragment.OnPreMissionExcutionListener,
		OnUtteranceCompletedListener {
	private ExecutionFragment excutionFragment = null;
	private BlankFragment blankFragment = null;
	private FragmentManager fragmentManager;
	private GridView view;
	private GridAdapter mGridAdapter;
	private long idlist[];
	private static int identity[];
	private static String name[];
	private static int image[];

	private TypedArray pictures;
	private int TOTAL_PLAYERS;
	private int NORMAL_PLAYERS;
	private int SHUFFLE_COUNT;
	private EditText ed;
	private Button button, button_playsound;
	private TextView textview_gameSetting;
	private AlertDialog.Builder builder, builder_inputName, builder_next,
			builder_last;
	private Dialog mDialog;
	// private MediaPlayer mediaPlayer;
	private TextToSpeech mTTS;
	private ScriptGenerator mGenerator;
	private Config mConfig;
	private boolean isSoundPlayed;
	private SharedPreferences share;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GeneralMethodSet gms = new GeneralMethodSet();
		gms.updateLanguage(this);
		gms.setActivityTheme(this);
		gms = null;
		
		setContentView(R.layout.activity_main);
		fragmentManager = getSupportFragmentManager();
		share = PreferenceManager.getDefaultSharedPreferences(this);
		initViews();
		shuffle(TOTAL_PLAYERS, NORMAL_PLAYERS);
		shufflePicture(TOTAL_PLAYERS, NORMAL_PLAYERS);
		showFragments(0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			showExitGameAlert();
		}

		return super.onKeyDown(keyCode, event);
	}

	private void showExitGameAlert() {
		final AlertDialog.Builder dlg = new AlertDialog.Builder(this);
		dlg.setTitle(getString(R.string.string_exit_title));
		dlg.setMessage(getString(R.string.string_exit_message));

		// Window window = dlg.getWindow();
		// window.setContentView(R.layout.shrew_exit_dialog);
		// ImageButton ok = (ImageButton) window.findViewById(R.id.btn_ok);
		dlg.setPositiveButton(getString(R.string.string_exit_positive),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						onTerminate();
					}
				});

		dlg.setNegativeButton(getString(R.string.string_exit_negative),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
		dlg.show();
	}

	public void initViews() {

		SHUFFLE_COUNT = 0;
		Bundle bundle = this.getIntent().getExtras();
		TOTAL_PLAYERS = bundle.getInt("TOTAL_PLAYERS");
		NORMAL_PLAYERS = bundle.getInt("NORMAL_PLAYERS");
		identity = new int[TOTAL_PLAYERS];
		
		name = bundle.getStringArray("name");
		if (name == null) 
			name = new String[TOTAL_PLAYERS];
		
		idlist = bundle.getLongArray("idlist");
		if (idlist == null) 
			idlist = new long[TOTAL_PLAYERS];
		
		if (share.getString(DataSet.THEME,"").equals(DataSet.THEME_MILITARY)){
			pictures = getResources().obtainTypedArray(R.array.images);
			pictures.recycle();
		}		
		else{ pictures=getResources().obtainTypedArray(R.array.icon);
		pictures.recycle();
		}
		image = new int[TOTAL_PLAYERS];
		// mediaPlayer=MediaPlayer.create(getApplicationContext(),
		// R.raw.sound_male);
		mGridAdapter = new GridAdapter(getApplicationContext());
		for (int i = 0; i < TOTAL_PLAYERS; i++) {
			GridAdapter.Item item = new GridAdapter.Item();
			if (name != null && name[i] != null && (!name[i].equals(" "))) {
				item.text = name[i];
			} else {
				item.text = getString(R.string.string_item_game) + " "
						+ (1 + i);
			}

			item.resId = R.drawable.index;
			mGridAdapter.addItem(item);
		}
		mDialog = new Dialog(this);
		view = (GridView) findViewById(R.id.grid);
		view.setAdapter(mGridAdapter);
		if (TOTAL_PLAYERS < 7) {
			view.setColumnWidth((int) getResources().getDimension(
					R.dimen.itemSize_large));
		} else
			view.setColumnWidth((int) getResources().getDimension(
					R.dimen.itemSize_medium));
		textview_gameSetting = (TextView) findViewById(R.id.textView_gamesetting_main);
		textview_gameSetting.setText(getString(
				R.string.string_gamesetting_main, NORMAL_PLAYERS, TOTAL_PLAYERS
						- NORMAL_PLAYERS));
		builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
		builder.setTitle(getString(R.string.string_buildertitle_main))
				.setCancelable(false)
				.setPositiveButton(
						getString(R.string.string_builder_positivebutton_main),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								SHUFFLE_COUNT++;
								if (SHUFFLE_COUNT < TOTAL_PLAYERS) {
									builder_next
											.setMessage(getString(R.string.string_builder_next_message_main));
								}

								else if (SHUFFLE_COUNT == TOTAL_PLAYERS) {
									builder_next
											.setMessage(getString(R.string.string_builder_next_message_last_main));
									showFragments(-1);
									button.setVisibility(View.VISIBLE);
									button_playsound
											.setVisibility(View.VISIBLE);
									Toast.makeText(
											getApplicationContext(),
											getString(R.string.string_toast2_main),
											Toast.LENGTH_LONG).show();

								}
								final AlertDialog dlg = builder_next.create();
								if (!dlg.isShowing())
									dlg.show();
								TimerTask mTimerTask = new TimerTask() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										dlg.dismiss();
									}
								};
								Timer mTimer = new Timer();
								mTimer.schedule(mTimerTask, 1000);
								dialog.dismiss();
							}
						});
		builder_next = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_DARK);

		builder_inputName = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_DARK);
		builder_inputName
				.setTitle(
						getString(R.string.string_builder_inputname_title_main))
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						if ((name[SHUFFLE_COUNT] = ed.getText().toString()
								.trim()).contentEquals("")) {
							Animation shake = AnimationUtils.loadAnimation(
									getApplicationContext(), R.anim.shake);
							ed.startAnimation(shake);
							/*
							 * name[SHUFFLE_COUNT]=defaultName[SHUFFLE_COUNT];
							 * Toast
							 * .makeText(getApplicationContext(),getString(R
							 * .string.string_toast1_main)+name[SHUFFLE_COUNT],
							 * Toast.LENGTH_SHORT).show();
							 */
						}
						GridAdapter.Item item = new GridAdapter.Item();
						item.text = name[SHUFFLE_COUNT];
						item.resId = image[SHUFFLE_COUNT];
						mGridAdapter.replaceItem(item, SHUFFLE_COUNT);
						mGridAdapter.notifyDataSetChanged();
						if (identity[SHUFFLE_COUNT] == DataSet.SOILDER) {
							builder.setIcon(R.drawable.thief)
									.setMessage(
											name[SHUFFLE_COUNT]
													+ getString(R.string.string_builder_message_positive_main));
						} else if (identity[SHUFFLE_COUNT] == DataSet.SPY) {
							builder.setIcon(R.drawable.spy)
									.setMessage(
											name[SHUFFLE_COUNT]
													+ getString(R.string.string_builder_message_negative_main));
						}
						if (mDialog.isShowing()) {
							mDialog = builder.create();
							mDialog.show();
						}
					}
				});
		builder_last = new AlertDialog.Builder(this,
				AlertDialog.THEME_HOLO_DARK);

		builder_last
				.setTitle(getString(R.string.string_main_sound_title))
				.setCancelable(false)
				.setPositiveButton(
						getString(R.string.string_main_sound_positive),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								if (SHUFFLE_COUNT == TOTAL_PLAYERS) 
									startNextActivity();							
							}
						})
				.setNeutralButton(
						getString(R.string.string_main_sound_neutral),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								if (getVolume() < 6) {
									Toast.makeText(
											getApplicationContext(),
											getString(R.string.string_playsound_toast),
											Toast.LENGTH_LONG).show();
								} else{
									playSound();
									isSoundPlayed=true;
								}
									
								
							}
						});

		builder_last.setMessage(getString(R.string.string_main_sound_message)
				+ "\n\n" + getString(R.string.script_close_eyes) + "\n\n"
				+ getString(R.string.script_spies_find_each_other) + "\n\n"
				+ getString(R.string.script_spies_close_eyes) + "\n\n"
				+ getString(R.string.script_open_eyes));// "间谍知道自己同伴了么?请播放“语音提示”\n\n或者诵读以下提示语：\n大家请闭眼。\n间谍请睁眼。\n间谍相互认识。\n间谍请闭眼。\n请大家都睁眼。\n\n两句之间留出几秒时间. ");
		button = (Button) findViewById(R.id.button_showIndentity_Main);
		button.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isSoundPlayed)
				builder_last.show();
				else 
					startNextActivity();
					
			}

		});
		button.setOnTouchListener(new ButtonOnTouchListener(this));
		button_playsound = (Button) findViewById(R.id.button_playsound_Main);
		button_playsound.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (getVolume() < 6) {
					Toast.makeText(getApplicationContext(),
							getString(R.string.string_playsound_toast),
							Toast.LENGTH_LONG).show();
				} else{
					playSound();
					button_playsound.setEnabled(false);
					isSoundPlayed=true;
				}
				
			}
		});
		button_playsound.setOnTouchListener(new ButtonOnTouchListener(this));
		button.setVisibility(View.GONE);

		button_playsound.setVisibility(View.GONE);

		mTTS = new TextToSpeech(this, this);
		mConfig = new Config();
		mConfig.load(this);
		
		isSoundPlayed=false;
	}
	private void startNextActivity(){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putIntArray("identity", identity);
		bundle.putStringArray("name", name);
		bundle.putInt("TOTAL_PLAYERS",
				TOTAL_PLAYERS);
		bundle.putIntArray("image", image);
		bundle.putLongArray("idlist", idlist);
		intent.putExtras(bundle);
		intent.setClass(getApplicationContext(),
				GameActivity.class);
		startActivity(intent);
		finish();
	}
	private void shuffle(int TotalPlayers, int NormalPlayers) {
		Random random = new Random(System.currentTimeMillis());
		int i, j;
		for (i = 0; i < TOTAL_PLAYERS; i++) {
			j = random.nextInt(TotalPlayers);
			if (j < NormalPlayers) {
				identity[i] = DataSet.SOILDER;
				TotalPlayers--;
				NormalPlayers--;

			} else {
				identity[i] = DataSet.SPY;
				TotalPlayers--;
			}
		}
	}

	private final void shufflePicture(int TotalPlayers, int NormalPlayers) {
		Random random = new Random(System.currentTimeMillis());
		int temp[] = new int[10];
		int temp2[] = new int[10];
		for (int i = 0; i < 10; i++) {
			temp[i] = pictures.getResourceId(i, -1);
		}
		int count = 10;
		for (int i = 0; i < 10; i++) {
			int j = random.nextInt(count);
			temp2[i] = temp[j];
			count--;
			for (int k = j; k < count; k++) {
				temp[k] = temp[k + 1];
			}

		}
		for (int i = 0; i < TotalPlayers; i++) {
			image[i] = temp2[i];
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// your own stuff to handle orientation change, if needed
	}

	public void playSound() {
		mGenerator.saySpeech(mConfig);
		/*
		 * Do not delete. It will be useful it we need recorder.
		 * 
		 * 
		 * try { mediaPlayer.prepare(); } catch (IllegalStateException e) {
		 * 
		 * e.printStackTrace(); } catch (IOException e) {
		 * 
		 * e.printStackTrace(); } mediaPlayer.start();
		 * //button_playsound.setEnabled(false);
		 * 
		 * mediaPlayer.setOnCompletionListener(new
		 * MediaPlayer.OnCompletionListener() {
		 * 
		 * @Override public void onCompletion(MediaPlayer mp) {
		 * 
		 * if (SHUFFLE_COUNT==TOTAL_PLAYERS){ //button.setEnabled(true);
		 * builder_last.show(); mediaPlayer.release(); } } });
		 */
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
			positive.setBackgroundColor(getResources().getColor(
					android.R.color.darker_gray));
			positive.setTextColor(getResources().getColor(
					android.R.color.primary_text_light));
			positive.setOnTouchListener(mOnTouchListener);
			positive.setPadding(1, 1, 1, 1);
			Button negative = (Button) mDialog
					.findViewById(R.id.button_Cancel_input);
			negative.setBackgroundColor(getResources().getColor(
					android.R.color.darker_gray));
			negative.setTextColor(getResources().getColor(
					android.R.color.primary_text_light));
			negative.setPadding(1, 1, 1, 1);
			negative.setVisibility(View.GONE);
			positive.setOnClickListener(new Button.OnClickListener() {
				@Override
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
						name[SHUFFLE_COUNT] = ed.getText().toString().trim();
						GridAdapter.Item item = new GridAdapter.Item();

						item.text = name[SHUFFLE_COUNT];
						item.resId = image[SHUFFLE_COUNT];
						mGridAdapter.replaceItem(item, SHUFFLE_COUNT);
						mGridAdapter.notifyDataSetChanged();
						if (identity[SHUFFLE_COUNT] == DataSet.SOILDER) {
							builder.setTitle(
									getString(R.string.string_buildertitle_main)
											+ getString(R.string.string_identity_positive))
									.setIcon(R.drawable.thief)
									.setMessage(
											name[SHUFFLE_COUNT]
													+ getString(R.string.string_builder_message_positive_main))
									.show();
						} else if (identity[SHUFFLE_COUNT] == DataSet.SPY) {
							builder.setTitle(
									getString(R.string.string_buildertitle_main)
											+ getString(R.string.string_identity_negative))
									.setIcon(R.drawable.spy)
									.setMessage(
											name[SHUFFLE_COUNT]
													+ getString(R.string.string_builder_message_negative_main))
									.show();

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

	private void showFragments(int id) {
		// hideFragments(transaction);
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		switch (id) {
		case 0: {
			if (excutionFragment == null) {
				excutionFragment = new ExecutionFragment();
				transaction.add(R.id.layout_content_main, excutionFragment);
				Log.d("new", "1123");
			} else {
				transaction.replace(R.id.layout_content_main, excutionFragment);
				Log.d("old", "1123");
			}
			Bundle args = new Bundle();
			args.putString("name", "");
			excutionFragment.setArguments(args);
			transaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.commit();
			break;
		}

		case -1: {
			if (excutionFragment != null)
				transaction.remove(excutionFragment);
			if (blankFragment == null) {
				blankFragment = new BlankFragment();
				transaction.add(R.id.layout_content_main, blankFragment);
			} else {
				transaction.replace(R.id.layout_content_main, blankFragment);
			}
			transaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.commit();
			break;
		}

		default:
			break;
		}
	}

	@Override
	public void onPreMissionExcution(int id) {

		if (name == null || name[SHUFFLE_COUNT] == null) {
			showDialog();
		} else {
			GridAdapter.Item item = new GridAdapter.Item();
			item.text = name[SHUFFLE_COUNT];
			item.resId = image[SHUFFLE_COUNT];
			mGridAdapter.replaceItem(item, SHUFFLE_COUNT);
			mGridAdapter.notifyDataSetChanged();
			if (identity[SHUFFLE_COUNT] == DataSet.SOILDER) {
				builder.setTitle(
						getString(R.string.string_buildertitle_main)
								+ getString(R.string.string_identity_positive))
						.setIcon(R.drawable.thief)
						.setMessage(
								name[SHUFFLE_COUNT]
										+ getString(R.string.string_builder_message_positive_main))
						.show();
			} else if (identity[SHUFFLE_COUNT] == DataSet.SPY) {
				builder.setTitle(
						getString(R.string.string_buildertitle_main)
								+ getString(R.string.string_identity_negative))
						.setIcon(R.drawable.spy)
						.setMessage(
								name[SHUFFLE_COUNT]
										+ getString(R.string.string_builder_message_negative_main))
						.show();
			}

		}
	}

	private OnTouchListener mOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {

				((Button) v).setBackgroundColor(getResources().getColor(
						android.R.color.primary_text_light_nodisable));
				((Button) v).setTextColor(getResources().getColor(
						android.R.color.darker_gray));
				v.invalidate();

				break;
			}
			case MotionEvent.ACTION_UP: {

				((Button) v).setBackgroundColor(getResources().getColor(
						android.R.color.darker_gray));
				((Button) v).setTextColor(getResources().getColor(
						android.R.color.primary_text_light));
				v.invalidate();

				v.performClick();
				break;
			}
			case MotionEvent.ACTION_CANCEL: {

				((Button) v).setBackgroundColor(getResources().getColor(
						android.R.color.darker_gray));
				((Button) v).setTextColor(getResources().getColor(
						android.R.color.primary_text_light));
			
				v.invalidate();

				break;
			}
			}
			return true;
		}
	};

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			mGenerator = new ScriptGenerator(this, mTTS);
			
			String language=share.getString(DataSet.LANGUAGE, Locale.getDefault().getDisplayLanguage());
			mTTS.setLanguage( new Locale(language));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
				setMTTSlistenerICS();
			else
				setMTTSlistener();
		}
		if (status == TextToSpeech.ERROR) {
			//TODO Handle the error
		}
	}

	@Override
	public void onUtteranceCompleted(String arg0) {
		button_playsound.setEnabled(true);

	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
	private void setMTTSlistenerICS() {
		mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {

			@Override
			public void onDone(String utteranceId) {
				

			}

			@Override
			public void onError(String utteranceId) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStart(String utteranceId) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void setMTTSlistener() {
		mTTS.setOnUtteranceCompletedListener(this);
	}

}
