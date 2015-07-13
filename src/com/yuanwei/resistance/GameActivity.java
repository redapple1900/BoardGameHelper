package com.yuanwei.resistance;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ShareCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanwei.resistance.gridviewtest.GridAdapter;
import com.yuanwei.resistance.gridviewtest.TableGridAdapter;
import com.yuanwei.resistance.model.Game;
import com.yuanwei.resistance.model.GameEvent;
import com.yuanwei.resistance.model.Gamer;
import com.yuanwei.resistance.model.Player;
import com.yuanwei.resistance.model.protocol.Event;
import com.yuanwei.resistance.model.protocol.GameEventListener;
import com.yuanwei.resistance.model.protocol.GamePresenter;
import com.yuanwei.resistance.playerdatabase.PlayerDataSource;
import com.yuanwei.resistance.widget.ButtonOnTouchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends FragmentActivity implements
        GamePresenter, GameEventListener {
	private VoteFragment voteFragment;
	private TimerFragment timerFragment;
	private ExecutionFragment executionFragment;
	private MissionFragment missionFragment;
	private BlankFragment blankFragment;
	private android.support.v4.app.FragmentManager fragmentManager;
	private GridView view, view_bottom;
	private GridAdapter mGridAdapter;
	private TableGridAdapter mTableGridAdapter_bottom;
	private PlayerDataSource datasource;
    private TextView textview_topStatus;
    private View layout;
    private final String status_top_Main[] = new String[3];
	private ImageView mImageView;
	private Button button;
	private AlertDialog.Builder builder, builder_leader, builder_result;
	private AlertDialog mDialog;
	//private Handler handler;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneralMethodSet gms = new GeneralMethodSet();
        gms.updateLanguage(this);
        gms.setActivityTheme(this);
        setContentView(R.layout.activity_game);
        fragmentManager = getSupportFragmentManager();
        initGame();
        initViews();
        datasource = new PlayerDataSource(getApplicationContext());

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

    public void onTerminate() {
        finish();
    }

    public void initGame() {
        game = new Game(this);
        game.gamerList = this.getIntent().getExtras().getParcelableArrayList("gamerList");
        game.memberSelected = DataSet.NumOfMembersPerMission[game.getTotalPlayers()][game.currentMission];
        game.missionResult.add(new ArrayList<Integer>(game.memberSelected));
        status_top_Main[0] = getString(R.string.string_topstatus0_game);
        status_top_Main[1] = getString(R.string.string_topstatus1_game);
        status_top_Main[2] = getString(R.string.string_topstatus2_game);
    }

	public void initViews() {

		textview_topStatus = (TextView) findViewById(R.id.textview_topStatus_Game);
		layout = findViewById(R.id.content_gameactivity);
		setTopStatus(0);
		mGridAdapter = new GridAdapter(getApplicationContext());
		for (int i = 0, j = game.getTotalPlayers(); i < j; i++) {
            Gamer gamer = game.gamerList.get(i);
            mGridAdapter.addItem(new GridAdapter.Item(gamer.getName(),  gamer.getResId()));
		}
		setLeader(0);
		mTableGridAdapter_bottom = new TableGridAdapter(getApplicationContext());
		for (int i = 0; i < 6 * 3; i++) {
            TableGridAdapter.Item item = new TableGridAdapter.Item();
            if (i == 0) {
                item.text = getString(R.string.string_caption_bottom1_game);
                item.resId = R.drawable.blank;
            } else if (i > 0 && i < 6) {
                item.text = "";
                item.resId = R.drawable.waiting;
            } else if (i == 6) {
                item.text = getString(R.string.string_caption_bottom2_game);
                item.resId = R.drawable.blank;
            } else if (6 < i && i < 12) {
                item.text = ""
                        + DataSet.NumOfMembersPerMission[game.getTotalPlayers()][i - 7];
                item.resId = R.drawable.blank;
            } else if (i == 12) {
                item.text = getString(R.string.string_caption_bottom3_game);
                item.resId = R.drawable.blank;
            } else {
                item.text = "";
                item.resId = R.drawable.waiting;
            }
            mTableGridAdapter_bottom.addItem(item);
        }

        view = (GridView) findViewById(R.id.grid);
        view.setAdapter(mGridAdapter);

        view_bottom = (GridView) findViewById(R.id.grid_bottom);
        view_bottom.setAdapter(mTableGridAdapter_bottom);

        setLayoutOnClick();
        builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);

        builder_leader = new AlertDialog.Builder(this,
                AlertDialog.THEME_HOLO_DARK);
        builder_result = new AlertDialog.Builder(this,
                AlertDialog.THEME_HOLO_DARK);
        mImageView = (ImageView) findViewById(R.id.imageView_game);
        mImageView.setClickable(true);
        mImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showRules();
            }
        });
        button = (Button) findViewById(R.id.button_showIndentity_Game);
        button.setVisibility(View.GONE);// Invisible after onCreate();
        button.setText(getString(R.string.string_button_propose_game));
        button.setOnTouchListener(new ButtonOnTouchListener(this));
        button.setOnClickListener(mainButtonOnClickListener);
    }

    private void showRules() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_gamerules);
        Button negative = (Button) dialog
                .findViewById(R.id.button_dialog_rules);

        negative.setBackgroundColor(getResources().getColor(
                android.R.color.darker_gray));
        negative.setText(getString(R.string.string_builder_positive_game));
        negative.setTextColor(getResources().getColor(
                android.R.color.primary_text_light));
        negative.setPadding(1, 1, 1, 1);

        negative.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setBottomStatus(int MissionNumber, int Status) {
        TableGridAdapter.Item item = new TableGridAdapter.Item();
        item.text = "";
        if (Status == 0) {
            item.resId = R.drawable.execute;
        } else if (Status == 1) {
            item.resId = R.drawable.win;
        } else if (Status == -1) {
            item.resId = R.drawable.lose;
        }
        mTableGridAdapter_bottom.replaceItem(item, MissionNumber + 13);
        mTableGridAdapter_bottom.notifyDataSetChanged();
    }

    private void setTopStatus(int CurrentStatus) {
        textview_topStatus.setText(status_top_Main[CurrentStatus]);
    }

    private void setTeamProposedStatus(int TimeOfTeamPropose, boolean status) {
        TableGridAdapter.Item item = new TableGridAdapter.Item();
        item.text = "";
        item.resId = status == true ? R.drawable.approve : R.drawable.veto;

        mTableGridAdapter_bottom.replaceItem(item, TimeOfTeamPropose);
        mTableGridAdapter_bottom.notifyDataSetChanged();
    }

    private void clearTeamProposedStatus() {
        for (int i = 1; i < 6; i++) {

            TableGridAdapter.Item item = new TableGridAdapter.Item();
            item.text = "";
            item.resId = R.drawable.waiting;
            mTableGridAdapter_bottom.replaceItem(item, i);

        }
    }

    private void setLayoutOnClick() {

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3) {

                if (game.isPlayerSelected(id)) {
                    removeCandidate(id);
                } else if (game.memberSelected > game.candidatesId.size()) {
                    addCandidate(id);
                } else
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.string_toast1_game),
                            Toast.LENGTH_SHORT).show();

                button.setVisibility(game.memberSelected == game.candidatesId.size()
                        ? View.VISIBLE : View.GONE);
            }
        });
    }


    private void setLeader(int id) {
        mGridAdapter.setLeader(mGridAdapter.getItem(id), id);
    }

    private void addCandidate(int id) {
        game.addCandidate(id);
        mGridAdapter.getItem(id).setRevealed(true);
        mGridAdapter.notifyDataSetChanged();
    }

    private void removeCandidate(int id) {
        game.removeCandidate(id);
        mGridAdapter.getItem(id).setRevealed(false);
        mGridAdapter.notifyDataSetChanged();
    }

    private void resetCandidates(){
        for (Integer i:game.candidatesId){
            mGridAdapter.getItem(i).setRevealed(false);
        }
        game.resetCandidates();
        mGridAdapter.notifyDataSetChanged();
    }

    private void endGame(Game.Result result) {

        view_bottom.setVisibility(View.GONE);
        labelToken(result == Game.Result.WIN ? GridAdapter.Token.WIN : GridAdapter.Token.LOSE);

        button.setText(getString(R.string.string_topstatus3_game));
        button.setVisibility(View.GONE);
        View layout_top = findViewById(R.id.linearLayout1);
        layout_top.setVisibility(View.INVISIBLE);
        layout.setVisibility(View.VISIBLE);
        showFragments(-1);
        showFragments(10);
        for (int i = 0; i <= game.currentMission; i++) {
            List<Integer> list = game.missionResult.get(i);
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j) < 0) {
                    int position = Math.abs(list.get(j)) - 1;
                    GridAdapter.Item item = mGridAdapter.getItem(position);
                    mGridAdapter.setToken(item, position, i);
                }
            }
        }
        resetCandidates();
        for (int i = 0; i < game.gamerList.size(); i++) {
            if (game.gamerList.get(i).getIdentity() < 0)
                mGridAdapter.revealIdentity(i);
        }


        if (game.gamerList.size() < 7) {
            view.setColumnWidth((int) getResources().getDimension(
                    R.dimen.itemSize_large));
        } else {
            view.setColumnWidth((int) getResources().getDimension(
                    R.dimen.itemSize_medium));
        }
        mGridAdapter.notifyDataSetChanged();

        View share_layout = findViewById(R.id.layout_share);
        share_layout.setVisibility(View.VISIBLE);
        Button button_replay = (Button) findViewById(R.id.button_replay);
        Button button_rate = (Button) findViewById(R.id.button_rate);
        //Button button_facebook = (Button) findViewById(R.id.button_share_facebook);
        Button button_gplus = (Button) findViewById(R.id.button_share_gplus);
        //Button button_wechat = (Button) findViewById(R.id.button_share_wechat);

        Thread databaseThread = new Thread(new Runnable() {

            @Override
            public void run() {
                datasource.open();
                // SQLite D
                for (int i = 0; i < game.getTotalPlayers(); i++) {
                    long id = game.gamerList.get(i).getId();
                    int identity = game.gamerList.get(i).getIdentity();
                    if (id > 0) {
                        Player player = datasource.selectPlayerById(id);
                        player.setLastDate(game.date);
                        if (identity * game.result.num() > 0) {
                            player.setWin(player.getWin() + 1);
                        } else {
                            player.setLose(player.getLose() + 1);
                        }
                        datasource.updatePlayer(player);
                    } else if (id == 0) {
                        datasource.createPlayer(
                                game.gamerList.get(i).getName(),
                                identity * game.result.num() > 0 ? 1 : 0,
                                identity * game.result.num() < 0 ? 1 : 0,
                                game.date
                        );
                    }
                }
                datasource.close();
                Thread.currentThread().interrupt();
            }
        });
        databaseThread.start();
		/*
		final Thread mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// String mPath =
						// Environment.getExternalStorageDirectory().toString()
						// + "/";
						File mediaStorageDir = new File(
								Environment
										.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
								"BoardGameHelper");
						if (!mediaStorageDir.exists()) {
							if (!mediaStorageDir.mkdirs()) {
								Log.d("CameraTest",
										"Required media storage does not exist");
							}
						}

						DateFormat timeStamp = new SimpleDateFormat(
								"yyyyMMdd_HHmmss", Locale.US);
						Date da = new Date();
						mediaFile = new File(mediaStorageDir.getPath()
								+ File.separator + "IMG_"
								+ timeStamp.format(da) + ".jpg");

						Message msg = new Message();
						msg.what = 0;

						// create bitmap screen capture
						Bitmap bitmap;
						View v1 = getWindow().getDecorView().getRootView();
						v1.setDrawingCacheEnabled(true);
						bitmap = Bitmap.createBitmap(v1.getDrawingCache());
						v1.setDrawingCacheEnabled(false);

						OutputStream fout = null;
						// File imageFile = new File(mPath);

						try {
							fout = new FileOutputStream(mediaFile);
							bitmap.compress(Bitmap.CompressFormat.JPEG, 90,
									fout);
							fout.flush();
							fout.close();
							msg.what = 1;

						} catch (FileNotFoundException e) {

							e.printStackTrace();
						} catch (IOException e) {

							e.printStackTrace();
						}
						msg.setTarget(handler);
						msg.sendToTarget();						
					}				
				});
				Thread.currentThread().interrupt();
			}
		});
		/*
		 * Unneccesary to use wait(), The decorView holds the previous view
		 * instead of the current one. 07.30.2014 @Yuanwei try { synchronized
		 * (mThread){ mThread.wait(1000); } } catch (InterruptedException e) {
		 * 
		 * e.printStackTrace(); }
		 */
        // May need a handler to activiate share button??07.30.2014@Yuanwei
        button_replay.setOnTouchListener(new ButtonOnTouchListener(this));
        button_rate.setOnTouchListener(new ButtonOnTouchListener(this));
        button_gplus.setOnTouchListener(new ButtonOnTouchListener(this));
        //button_wechat.setOnTouchListener(new ButtonOnTouchListener(this));
        button_gplus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivityForResult(ShareCompat.IntentBuilder.from(GameActivity.this)
                        .setText(getString(R.string.string_share_content)).setType("text/plain")
                        .getIntent()
                        .setPackage("com.google.android.apps.plus"), 0);
            }});

        button_replay.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GameActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        button_rate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                rateAppOnGooglePlay();
            }
        });
		/*
		button_wechat.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ShareCompat.IntentBuilder.from(GameActivity.this).setType("text/plain")
				.setChooserTitle(getString(R.string.string_share_title))
				.setText(getString(R.string.string_share_content)).startChooser();
				/*
				try{
					mThread.start();
				}catch (IllegalThreadStateException e){
				e.printStackTrace();
				}				
				share_option = 3;
				
			}
		});
		*/
    }

    private void hideFragments() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (executionFragment != null) {
            transaction.hide(executionFragment);
            executionFragment = null;
        } else if (missionFragment != null) {
            transaction.remove(missionFragment);
            missionFragment = null;
        } else if (blankFragment != null) {
            transaction.remove(blankFragment);
            blankFragment = null;
        } else if (voteFragment != null) {
            transaction.remove(voteFragment);
            voteFragment = null;
        } else if (timerFragment != null) {
            transaction.remove(timerFragment);
            timerFragment = null;
        }
        transaction.commit();
    }

    private void showFragments(int id) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments();
        switch (id) {
            case 0: {
                if (missionFragment != null) { transaction.remove(missionFragment); }
                if (voteFragment != null) { transaction.remove(voteFragment); }
                if (executionFragment == null) {
                    executionFragment = new ExecutionFragment();
                    transaction.add(R.id.content_gameactivity, executionFragment);
                } else {
                    transaction
                            .replace(R.id.content_gameactivity, executionFragment);
                }
                Bundle args = new Bundle();

                args.putString("name", game.getFocusedCandidate().getName());
                executionFragment.setArguments(args);
                transaction
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();
                break;
            }

            case 1: {
                if (executionFragment != null) { transaction.remove(executionFragment); }
                if (missionFragment == null) {
                    missionFragment = new MissionFragment();
                    transaction.add(R.id.content_gameactivity, missionFragment);
                } else {
                    transaction.replace(R.id.content_gameactivity, missionFragment);

                }
                Bundle args = new Bundle();
                Gamer gamer = game.getFocusedCandidate();
                args.putString("name", gamer.getName());
                args.putInt("identity", gamer.getIdentity());
                missionFragment.setArguments(args);
                transaction
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;
            }
            case 2:
                if (timerFragment != null ) transaction.remove(timerFragment);
                if (voteFragment == null) {
                    voteFragment = new VoteFragment();
                    transaction
                            .add(R.id.content_gameactivity, voteFragment, "vote");
                } else {
                    transaction.replace(R.id.content_gameactivity, voteFragment,
                            "vote");
                    // transaction.addToBackStack(null);
                }
                Bundle args = new Bundle();
                args.putInt("Votes Needed", DataSet.NumOfMinPass[game.getTotalPlayers()]);
                voteFragment.setArguments(args);
                transaction
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;
            case -1: {
                if (missionFragment != null ) transaction.remove(missionFragment);
                if (executionFragment != null) transaction.remove(executionFragment);
                if (blankFragment == null) {
                    blankFragment = new BlankFragment();
                    transaction.add(R.id.content_gameactivity, blankFragment);
                } else {
                    transaction.replace(R.id.content_gameactivity, blankFragment);
                }
                transaction
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();
                break;
            }
            case -2:
                if (timerFragment == null) {
                    timerFragment = new TimerFragment();
                    transaction.add(R.id.content_gameactivity, timerFragment);
                } else {
                    transaction.replace(R.id.content_gameactivity, timerFragment);
                    // transaction.addToBackStack(null);
                }
                transaction
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;
            case 10:
                TopResultFragment topResultFragment = new TopResultFragment();
                transaction.add(R.id.content_top_gameactivity, topResultFragment);

                Bundle bundle = new Bundle();
                // TODO
                bundle.putInt("GameResult", game.result.num());

                topResultFragment.setArguments(bundle);

                transaction
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                break;
            default:
                break;
        }
    }

    private final void labelToken(int status) {
        for (int i = 0, j = game.getTotalPlayers(); i < j; i++) {
            if (game.isPlayerSelected(i)) {
                mGridAdapter.addToken(
                        mGridAdapter.getItem(i), i, status);
            } else
                mGridAdapter.addToken(
                        mGridAdapter.getItem(i), i,
                        GridAdapter.Token.NEUTRAL);
        }
        mGridAdapter.notifyDataSetChanged();
    }

    private boolean myStartActivity(Intent intent) {
        try {
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.string_toast2_game), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //On click event for rate this app button
    public void rateAppOnGooglePlay() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Try Google play
        intent.setData(Uri.parse("market://details?id=" + "com.yuanwei.resistance"));
        if (!myStartActivity(intent)) {
            //Market (Google play) app seems not installed, let's try to open a webbrowser
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?" + "com.yuanwei.resistance"));
            if (!myStartActivity(intent)) {
                //Well if this also fails, we have run out of options, inform the user.
                Toast.makeText(this, "Could not open Google Play, please install the market app.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private AlertDialog getMissionResultDialog(int failCount, boolean missionResult) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        if (missionResult) {
            if (failCount == 0) {
                dialogBuilder
                        .setIcon(R.drawable.win)
                        .setTitle(
                                getString(R.string.string_builder_title_winresult_game))
                        .setMessage(
                                getString(R.string.string_builder_message_winresult_game)
                                        + "\n"
                                        + "\n"
                                        + getString(R.string.string_builder_next_message_last_main)
                                        + ":" + game.gamerList.get(game.round % game.getTotalPlayers()).getName())
                        .setPositiveButton(
                                getString(R.string.string_builder_positive_game),
                                null);
            } else if (failCount == 1) {
                dialogBuilder
                        .setIcon(R.drawable.win)
                        .setTitle(
                                getString(R.string.string_builder_title_winresult_game)
                                        + "\n"
                                        + getString(
                                        R.string.string_message_failvotes_game,
                                        failCount))
                        .setMessage(
                                getString(R.string.string_builder_message_winresult_game)
                                        + "\n"
                                        + "\n"
                                        + getString(R.string.string_builder_next_message_last_main)
                                        + ":" + game.gamerList.get(game.round % game.getTotalPlayers()).getName())
                        .setPositiveButton(
                                getString(R.string.string_builder_positive_game),
                                null);
            }
        }else {
            if (failCount == 1)
                dialogBuilder
                        .setIcon(R.drawable.lose)
                        .setTitle(
                                getString(R.string.string_builder_title_loseresult_game)
                                        + "\n"
                                        + getString(
                                        R.string.string_message_failvote_game,
                                        failCount))
                        .setMessage(
                                getString(R.string.string_builder_message_loseresult_game)
                                        + "\n"
                                        + "\n"
                                        + getString(R.string.string_builder_next_message_last_main)
                                        + ":" + game.gamerList.get(game.round % game.getTotalPlayers()).getName())
                        .setPositiveButton(
                                getString(R.string.string_builder_positive_game),
                                null);
            else
                dialogBuilder
                        .setIcon(R.drawable.lose)
                        .setTitle(
                                getString(R.string.string_builder_title_loseresult_game)
                                        + "\n"
                                        + getString(
                                        R.string.string_message_failvotes_game,
                                        failCount))
                        .setMessage(
                                getString(R.string.string_builder_message_loseresult_game)
                                        + "\n"
                                        + "\n"
                                        + getString(R.string.string_builder_next_message_last_main)
                                        + ":" + game.gamerList.get(game.round % game.getTotalPlayers()).getName())
                        .setPositiveButton(
                                getString(R.string.string_builder_positive_game),
                                null);
        }
        return dialogBuilder.create();
    }

    private void showFocusTransitionDialog(boolean isLastOne) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        if (isLastOne) {
            builder
                    .setMessage(getString(R.string.string_builder_next_message_last_main));
        } else {
            builder
                    .setMessage(getString(R.string.string_builder_next_message_main)
                            + ":"
                            + game.getFocusedCandidate().getName());
        }
        final AlertDialog dlg = builder.create();
        dlg.show();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                dlg.dismiss();
            }
        }, 2000);
    }

    private View.OnClickListener mainButtonOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            builder.setTitle(status_top_Main[0])
                    .setMessage(getString(R.string.string_builder_message_team_propose_game))
                    .setPositiveButton(
                            getString(R.string.string_builder_positive_game),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(
                                        DialogInterface dialog, int which) {
                                    game.updateGameByEvent(GameEvent.PROPOSE, 0);
                                }})
                    .setNegativeButton(
                            getString(R.string.string_builder_negative_game),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {}});
            if (mDialog == null || (!mDialog.isShowing())) {
                mDialog = builder.create();
                mDialog.show();
            }
        }
    };

    private View.OnClickListener reportResultButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            game.updateGameByEvent(GameEvent.SHOW_RESULTS, 0);
        }
    };


    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void updateViewBeforeEvent(Event event, int extra) {
        if (event.getEventType().equals(GameEvent.PROPOSE.name())) {
            layout.setVisibility(View.GONE);
            setBottomStatus(game.currentMission, 0);
            setTopStatus(1);
        } else if (event.getEventType().equals(GameEvent.PROPOSE_PASSED.name())) {
            setTeamProposedStatus(game.timeOfTeamPropose, true);//Handle bottom status
            setTopStatus(2);
            button.setText(getString(R.string.string_button_start_game));
            button.setOnClickListener(reportResultButtonOnClickListener);
            view.setEnabled(false);//To avoid team member selection after mission execution 09.01.2014
        }else if (event.getEventType().equals(GameEvent.PROPOSE_VETO.name())) {
            setTeamProposedStatus(game.timeOfTeamPropose, false);
            setTopStatus(0);
            setBottomStatus(game.currentMission, 0);
            setLeader(game.getLeaderPosition());
            layout.setVisibility(View.VISIBLE);
            button.setText(getString(R.string.string_button_propose_game));
            button.setVisibility(View.GONE);
            resetCandidates();
            hideFragments();
            showFragments(-1);
        }else if (event.getEventType().equals(GameEvent.SHOW_RESULTS.name())) {
            setTopStatus(0);
            view.setEnabled(true);
            clearTeamProposedStatus();
            setLeader(game.getLeaderPosition());
            mGridAdapter.notifyDataSetChanged();
        } else if (event.getEventType().equals(GameEvent.MISSION_SUCCEED.name())) {
            if (mDialog == null || (!mDialog.isShowing())) {
                mDialog = getMissionResultDialog(extra, true);
                mDialog.show();
            }
            setBottomStatus(game.currentMission, 1);
            labelToken(GridAdapter.Token.WIN);
            resetCandidates();
        } else if (event.getEventType().equals(GameEvent.MISSION_FAILED.name())) {
            if (mDialog == null || (!mDialog.isShowing())) {
                mDialog = getMissionResultDialog(extra, false);
                mDialog.show();
            }
            setBottomStatus(game.currentMission, -1);
            labelToken(GridAdapter.Token.LOSE);
            resetCandidates();
        }
    }

    @Override
    public void updateViewAfterEvent(Event event) {
        if (event.getEventType().equals(GameEvent.PROPOSE.name())) {
            builder_leader
                    .setMessage(
                            getString(R.string.string_builder_vote_message_before_Game))
                    .setPositiveButton(
                            getString(R.string.string_builder_positive_game),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
            showFragments(-2);// show vote Fragment
        } else if (event.getEventType().equals(GameEvent.PROPOSE_VETO.name())){
            builder_result
                    .setIcon(R.drawable.veto)
                    .setTitle(getString(R.string.string_title_veto_game))
                    .setMessage(
                            getString(R.string.string_message_veto_game) + ":"
                                    + game.getLeader().getName())
                    .setPositiveButton(
                            getString(R.string.string_builder_positive_game), null)
                    .show();

            setLeader(game.getLeaderPosition());
            mGridAdapter.notifyDataSetChanged();
        } else if (event.getEventType().equals(GameEvent.MISSION_EXECUTION_CONTINUE.name())) {
            showFocusTransitionDialog(false);
            showFragments(0);
        } else if (event.getEventType().equals(GameEvent.MISSION_EXECUTION_COMPLETED.name())) {
            showFocusTransitionDialog(true);
            layout.setVisibility(View.VISIBLE);
            hideFragments();
            showFragments(-1);
        } else if (event.getEventType().equals(GameEvent.MISSION_EXECUTION_START.name())) {
            hideFragments();
            showFocusTransitionDialog(false);
            showFragments(0);
        } else if (event.getEventType().equals(GameEvent.SHOW_RESULTS.name())) {
            setLeader(game.getLeaderPosition());
            button.setText(getString(R.string.string_button_propose_game));
            button.setVisibility(View.GONE);
            button.setOnClickListener(mainButtonOnClickListener);
            layout.setVisibility(View.VISIBLE);
        } else if (event.getEventType().equals(GameEvent.RESISTANCE_WIN.name())) {
            endGame(Game.Result.WIN);
        } else if (event.getEventType().equals(GameEvent.SPY_WIN.name())) {
            endGame(Game.Result.LOSE);
        }
    }

    @Override
    public void onEventStart(String type, int extra) {

        switch (type) {
            case ExecutionFragment.TAG:
                showFragments(1);
                break;
            case MissionFragment.TAG:
                game.updateGameByEvent(GameEvent.MISSION_EXECUTION_CONTINUE, extra);
                break;
            case VoteFragment.TAG:
                if (extra == 1) {
                    game.updateGameByEvent(GameEvent.PROPOSE_PASSED, 0);
                } else {
                    game.updateGameByEvent(GameEvent.PROPOSE_VETO, 0);
                }
                break;
            case TimerFragment.TAG:
                hideFragments();
                showFragments(2);
                break;
        }
    }

    /*
    @SuppressLint("HandlerLeak")
    private class HandlerExtension extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int info = msg.what * share_option;

            switch (info) {
                case 3:
                    Intent intent = new Intent();
                    ComponentName comp = new ComponentName("com.tencent.mm",
                            "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                    intent.setComponent(comp);
                    intent.setAction("android.intent.action.SEND");
                    // intent.setType("text/plain");
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_TEXT, "Resistance!");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mediaFile));
                    myStartActivity(intent);

                    break;
                case 0:
                    break;
            }
        }
    }
    */

}
