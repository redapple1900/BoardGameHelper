package com.yuanwei.resistance.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
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
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.yuanwei.resistance.PrepareActivity;
import com.yuanwei.resistance.R;
import com.yuanwei.resistance.model.Player;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.ui.grid.GridAdapter;
import com.yuanwei.resistance.ui.grid.GridAdapter.Item;
import com.yuanwei.resistance.ui.list.ListAdapter;
import com.yuanwei.resistance.util.playerdatabase.PlayerDataSource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomGameFragment extends Fragment {
    private PlayerDataSource datasource;
    private ImageView mImageView;
    private GridView grid;
    private List<Player> playerList;
    private List<User> userList;
    private AlertDialog.Builder builder;
    private GridAdapter mGridAdapter;
    private ListAdapter mListAdapter;
    private ListView listView;
    private Dialog dialog;
    public static final String ARG_ITEM_ID = "item_id";
    //private Spinner spinner;
    private Button bn_stranger, bn_friend, bn_clear, bn_start;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_management, container, false);
        if (savedInstanceState == null) {

            playerList = new ArrayList<>();
            userList = new ArrayList<>();
            dialog = new Dialog(getActivity());
            datasource = new PlayerDataSource(getActivity());
            mGridAdapter = new GridAdapter(getActivity());
        }
        builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
        // TODO: How to make this asynchronous?
        datasource.open();
        mListAdapter = new ListAdapter(getActivity(), datasource.getAllPlayersByDate());
        datasource.close();

        listView = (ListView) v.findViewById(R.id.list_management);
        listView.setAdapter(mListAdapter);
        registerForContextMenu(listView);

        mImageView = (ImageView) v.findViewById(R.id.imageView_game);
        mImageView.setClickable(true);
        mImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                builder.setMessage(getResources().getString(R.string.string_manu)).setPositiveButton(getString(R.string.string_exit_negative),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }

        });
        bn_stranger = (Button) v.findViewById(R.id.button_management1);
        bn_stranger.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int totalPlayers = mGridAdapter.getCount();

                if (totalPlayers < 10) {
                    mGridAdapter.addItem(new Item(
                            getString(R.string.string_newplayer_customgame),
                            R.drawable.index));
                    mGridAdapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getActivity(), getActivity().getString(R.string.string_toast1_game), Toast.LENGTH_SHORT).show();

            }
        });

        bn_friend = (Button) v.findViewById(R.id.button_management2);
        bn_friend.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog();
            }
        });
        bn_clear = (Button) v.findViewById(R.id.button_clear_management);
        bn_clear.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                mGridAdapter.removeAllItems();
                mGridAdapter.notifyDataSetChanged();

            }
        });
        bn_start = (Button) v.findViewById(R.id.button_startgame_management2);
        bn_start.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (mGridAdapter.getCount() > 4) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();

                    ArrayList<User> users = new ArrayList<>(mGridAdapter.getCount());

                    for (int i = 0, j = mGridAdapter.getCount(); i < j; i++) {
                        Item item = mGridAdapter.getItem(i);
                        users.set(i, new User(item.id, item.text, item.resId));

                    }
                    bundle.putParcelableArrayList("gamerList", users);

                    intent.setClass(getActivity(), PrepareActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    getActivity().finish();
                } else
                    Toast.makeText(getActivity(), getString(R.string.toast_customgame), Toast.LENGTH_SHORT).show();

            }
        });

        grid = (GridView) (v.findViewById(R.id.grid_management));
        grid.setAdapter(mGridAdapter);
        setLayoutOnClick();

        return v;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //your own stuff to handle orientation change, if needed
    }

    @Override
    public void onCreateContextMenu(ContextMenu arg0, View arg1,
                                    ContextMenuInfo arg2) {
        super.onCreateContextMenu(arg0, arg1, arg2);
        arg0.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Delete");
    }

    //Super Important, Sounds like the reason is that do not return super.OnContextItemSelected.
    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) (menuItem).getMenuInfo();

        switch ((menuItem).getItemId()) {
            case Menu.FIRST:
                final long id = ((Player) (mListAdapter.getItem(info.position))).getId();
                datasource.deletePlayer(id);
                mListAdapter.removeItem(info.position);
                mListAdapter.notifyDataSetChanged();
                deselectPlayer(id);
                break;
        }
        return true;
    }

    private void deselectPlayer(long id) {

        for (int i = 0, j = mGridAdapter.getCount(); i < j; i++) {
            if (mGridAdapter.getItemId(i) == id) {
                mGridAdapter.removeItem(i);
                break;
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
            ed.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            final InputMethodManager inputMgr = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            Button positive = (Button) dialog
                    .findViewById(R.id.affirmative);

            Button negative = (Button) dialog
                    .findViewById(R.id.negative);

            positive.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ed.getText().toString().isEmpty()
                            || ed.getText().toString().trim().contentEquals("")) {
                        Animation shake = AnimationUtils.loadAnimation(
                                getActivity(), R.anim.shake);
                        ed.startAnimation(shake);

                    } else {

                        inputMgr.hideSoftInputFromWindow(ed.getWindowToken(), 0);
                        final Calendar myCalendar;
                        myCalendar = Calendar.getInstance();
                        myCalendar.setTimeInMillis(System.currentTimeMillis());
                        final int year = myCalendar.get(Calendar.YEAR);
                        final int month = myCalendar.get(Calendar.MONTH) + 1;
                        final int day = myCalendar.get(Calendar.DAY_OF_MONTH);
                        final String date;
                        if (month < 10) {
                            date = year + "-0" + month + "-" + day;
                        } else {
                            date = year + "-" + month + "-" + day;
                        }

                        final Player player = datasource.createPlayer(ed
                                .getText().toString().trim(), 0, 0, date);
                        mListAdapter.addPlayer(player);
                        mListAdapter.notifyDataSetChanged();

                        Item item = new GridAdapter.Item(player.getName(), R.drawable.index);
                        item.id = player.getId();
                        mGridAdapter.addItem(item);

                        mGridAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            });

            negative.setOnClickListener(new Button.OnClickListener() {
                @Override
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
                if (mGridAdapter.getCount() > 0) {
                    mGridAdapter.removeItem(arg2);
                }
                mGridAdapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                boolean flag = false;
                final Player player = (Player) arg0.getItemAtPosition(arg2);
                final long id = player.getId();
                for (int i = 0, j = mGridAdapter.getCount(); i < j; i++) {
                    if (id == mGridAdapter.getItemId(i)) {
                        Toast.makeText(getActivity(), "Already in team",Toast.LENGTH_LONG).show();
                        arg1.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
                        flag = true;
                        break;
                    }
                }
                if (flag == false) {
                    if (mGridAdapter.getCount() < 10) {
                        mGridAdapter.addItem(new GridAdapter.Item(player
                                .getName(), R.drawable.index));
                    } else {
                        Toast.makeText(getActivity(), "Too many members",
                                Toast.LENGTH_LONG).show();
                        arg1.setAnimation(AnimationUtils.loadAnimation(
                                getActivity(), R.anim.shake));
                    }
                    mGridAdapter.notifyDataSetChanged();
                }
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
