package com.yuanwei.resistance;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.model.protocol.PlotListener;
import com.yuanwei.resistance.partygame.origin.model.Resistance;
import com.yuanwei.resistance.ui.fragment.ResistanceGamingFragment;
import com.yuanwei.resistance.util.playerdatabase.PlayerDataSource;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends BasePlotActivity {
    private boolean qe = false;
    private PlayerDataSource datasource;
    private PlotListener plotListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ArrayList<User> list = new ArrayList<>();

        int game;

        if (qe) {

            for (int i = 0; i < 4; i++) {
                User user = new User();
                user.setIdentity(Resistance.Role.RESISTANT.getRoleId());
                user.setName("Resistant" + i);
                user.setResId(R.drawable.index);
                list.add(user);
            }

            for (int i = 0; i < 3; i++) {
                User user = new User();
                user.setIdentity(Resistance.Role.SPY.getRoleId());
                user.setName("SPY" + i);
                user.setResId(R.drawable.index);
                list.add(user);
            }

            Collections.shuffle(list);
            game = 1;
        } else {
            list = getIntent().getExtras().getParcelableArrayList(Constants.USERLIST_KEY);
            game = getIntent().getExtras().getInt(Constants.GAME);
        }


        Fragment fragment = new ResistanceGamingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.USERLIST_KEY, list);
        bundle.putInt(Constants.GAME, game);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
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

                        Intent intent = new Intent();
                        intent.setClass(getParent(), WelcomeActivity.class);
                        startActivity(intent);
                        finish();
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

    /*
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
        for (int i = 0; i < game.userList.size(); i++) {
            if (game.userList.get(i).getIdentity() < 0)
                mGridAdapter.revealIdentity(i);
        }


        if (game.userList.size() < 7) {
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
                    long id = game.userList.get(i).getId();
                    int identity = game.userList.get(i).getIdentity();
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
                                game.userList.get(i).getName(),
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
            }
        });

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

    }
    */

    @Override
    public PlotListener getPlotListener() {
        return this.plotListener;
    }

    @Override
    public void setPlotListener(PlotListener listener) {
        plotListener = listener;
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
