package com.yuanwei.resistance;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanwei.resistance.gridviewtest.GridAdapter;
import com.yuanwei.resistance.model.Gamer;
import com.yuanwei.resistance.model.protocol.GameEventListener;
import com.yuanwei.resistance.texttospeech.Config;
import com.yuanwei.resistance.texttospeech.ScriptGenerator;
import com.yuanwei.resistance.widget.ButtonOnTouchListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity implements OnInitListener,
        GameEventListener,
        OnUtteranceCompletedListener {
    private ExecutionFragment excutionFragment;
    private BlankFragment blankFragment;
    private FragmentManager fragmentManager;
    private GridView view;
    private GridAdapter mGridAdapter;
    private ArrayList<Gamer> gamerList;
    private TypedArray pictures;
    private int TOTAL_PLAYERS;
    private int NORMAL_PLAYERS;
    private int SHUFFLE_COUNT;
    private Button button, button_playsound;
    private TextView textview_gameSetting;
    private AlertDialog.Builder builder, builder_next, builder_last;
    private Dialog mDialog;
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
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        share = PreferenceManager.getDefaultSharedPreferences(this);
        initViews();
        shuffle(TOTAL_PLAYERS, NORMAL_PLAYERS);
        shufflePicture();
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

    public void initViews() {

        SHUFFLE_COUNT = 0;
        Bundle bundle = this.getIntent().getExtras();
        gamerList = bundle.getParcelableArrayList("gamerList");
        TOTAL_PLAYERS = gamerList.size();

        NORMAL_PLAYERS = DataSet.getNormalPlayers(TOTAL_PLAYERS);
        mGridAdapter = new GridAdapter(getApplicationContext());
        for (int i = 0; i < TOTAL_PLAYERS; i++) {
            GridAdapter.Item item = new GridAdapter.Item();
            item.text = gamerList.get(i).getName();
            if (item.text == null) {
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
                                } else if (SHUFFLE_COUNT == TOTAL_PLAYERS) {
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
                                } else {
                                    playSound();
                                    isSoundPlayed = true;
                                }


                            }
                        });

        builder_last.setMessage(getString(R.string.string_main_sound_message)
                + "\n\n" + getString(R.string.script_close_eyes) + "\n\n"
                + getString(R.string.script_spies_find_each_other) + "\n\n"
                + getString(R.string.script_spies_close_eyes) + "\n\n"
                + getString(R.string.script_open_eyes));
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
                } else {
                    playSound();
                    button_playsound.setEnabled(false);
                    isSoundPlayed = true;
                }

            }
        });
        button_playsound.setOnTouchListener(new ButtonOnTouchListener(this));
        button.setVisibility(View.GONE);

        button_playsound.setVisibility(View.GONE);

        mTTS = new TextToSpeech(this, this);
        mConfig = new Config();
        mConfig.load(this);

        isSoundPlayed = false;
    }

    private void showExitGameAlert() {
        final AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle(getString(R.string.string_exit_title));
        dlg.setMessage(getString(R.string.string_exit_message));
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

    private void startNextActivity() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("gamerList", gamerList);
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
            j = random.nextInt(TotalPlayers--);
            if (j < NormalPlayers) {
                gamerList.get(i).setIdentity(DataSet.SOILDER);
                NormalPlayers--;
            } else {
                gamerList.get(i).setIdentity(DataSet.SPY);
            }
        }
    }

    private void shufflePicture() {

        try {
            if (share.getString(DataSet.THEME, "").equals(DataSet.THEME_MILITARY)) { //TODO: Should use a global variable to denote the 'Theme';

                pictures = getResources().obtainTypedArray(R.array.images);

            } else {

                pictures = getResources().obtainTypedArray(R.array.icon);
            }

            int count = 10;// TODO: Should not use hardcode number;
            int temp[] = new int[count];

            for (int i = 0; i < count; i++) {
                temp[i] = pictures.getResourceId(i, -1);
            }
            Random random = new Random(System.currentTimeMillis());
            for (int i = temp.length - 1; i > 0; i--) {
                int index = random.nextInt(i + 1);
                // Simple swap
                int a = temp[index];
                temp[index] = temp[i];
                temp[i] = a;
            }
            for (int i = TOTAL_PLAYERS - 1; i >= 0; i--) {
                gamerList.get(i).setResId(temp[i]);
            }
        } finally {
            pictures.recycle();
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
            ed.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            final InputMethodManager inputMgr = (InputMethodManager) getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            // inputMgr.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);

            Button positive = (Button) mDialog
                    .findViewById(R.id.Button_OK_input);

            Button negative = (Button) mDialog
                    .findViewById(R.id.button_Cancel_input);


            negative.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    inputMgr.hideSoftInputFromWindow(ed.getWindowToken(), 0);
                    gamerList.get(SHUFFLE_COUNT)
                            .setName(getString
                                    (R.string.string_item_game) + " " + (1 + SHUFFLE_COUNT))
                            .setId(-1); // Important, -1 indicates this is an anonymous player

                    mGridAdapter.replaceItem(
                            new GridAdapter.Item(
                                    gamerList.get(SHUFFLE_COUNT).getName(),
                                    gamerList.get(SHUFFLE_COUNT).getResId()),
                            SHUFFLE_COUNT);
                    mGridAdapter.notifyDataSetChanged();
                    setViewerMessage();
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.string_toast1_main), Toast.LENGTH_SHORT).show();
                }
            });
            positive.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ed.getText().toString().isEmpty()
                            || ed.getText().toString().trim().contentEquals("")) {
                        Animation shake = AnimationUtils.loadAnimation(
                                getApplicationContext(), R.anim.shake);
                        ed.startAnimation(shake);

                    } else {
                        inputMgr.hideSoftInputFromWindow(ed.getWindowToken(), 0);
                        gamerList.get(SHUFFLE_COUNT).setName(ed.getText().toString().trim());

                        mGridAdapter.replaceItem(
                                new GridAdapter.Item(
                                        gamerList.get(SHUFFLE_COUNT).getName(),
                                        gamerList.get(SHUFFLE_COUNT).getResId()),
                                SHUFFLE_COUNT);
                        mGridAdapter.notifyDataSetChanged();
                        setViewerMessage();
                        mDialog.dismiss();
                    }
                }
            });

            mDialog.show();
        }

    }

    private void setViewerMessage() {
        switch (gamerList.get(SHUFFLE_COUNT).getIdentity()) {
            case DataSet.SOILDER:
                builder.setTitle(
                        getString(R.string.string_buildertitle_main)
                                + getString(R.string.string_identity_positive))
                        .setIcon(R.drawable.thief)
                        .setMessage(
                                gamerList.get(SHUFFLE_COUNT).getName()
                                        + getString(R.string.string_builder_message_positive_main))
                        .show();
                break;
            case DataSet.SPY:
                builder.setTitle(
                        getString(R.string.string_buildertitle_main)
                                + getString(R.string.string_identity_negative))
                        .setIcon(R.drawable.spy)
                        .setMessage(
                                gamerList.get(SHUFFLE_COUNT).getName()
                                        + getString(R.string.string_builder_message_negative_main))
                        .show();
                break;
            default:
                break;
        }
    }

    private int getVolume() {
        return ((AudioManager) getSystemService(Context.AUDIO_SERVICE))
                .getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public void onTerminate() {

        finish();

        onDestroy();

        System.exit(0);
    }

    @Override
    protected  void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

    private void showFragments(int id) {
        // hideFragments(transaction);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (id) {
            case 0: {
                if (excutionFragment == null) {
                    excutionFragment = new ExecutionFragment();
                    transaction.add(R.id.layout_content_main, excutionFragment);

                } else {
                    transaction.replace(R.id.layout_content_main, excutionFragment);

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
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mGenerator = new ScriptGenerator(this, mTTS);

            String language = share.getString(DataSet.LANGUAGE, getResources().getConfiguration().locale.getDisplayLanguage());
            mTTS.setLanguage(new Locale(language));
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

    @Override
    public void onEventStart(String type, int extra) {

        if (gamerList.get(SHUFFLE_COUNT).getName() == null) {
            showDialog();
        } else {
            mGridAdapter.replaceItem(
                    new GridAdapter.Item(
                            gamerList.get(SHUFFLE_COUNT).getName(),
                            gamerList.get(SHUFFLE_COUNT).getResId()),
                    SHUFFLE_COUNT);
            mGridAdapter.notifyDataSetChanged();
            setViewerMessage();
        }

    }
}
