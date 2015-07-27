package com.yuanwei.resistance;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.yuanwei.resistance.texttospeech.Config;
import com.yuanwei.resistance.texttospeech.ScriptGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameSetupActivity extends Activity implements TextToSpeech.OnInitListener {
    /* Model or Data Variables */
    private int TOTAL_PLAYERS = 8;
    private final String[] NumOfGamers = {"5", "6", "7", "8", "9", "10"};
    private List<GameSetupItem> data;
    /* View */
    private TextView tv0, tv1;
    private Spinner mSpinner;
    private RecyclerView mRecyclerView;
    private Button mButton;
    /* Prensenter, Adapter and Utility */
    private ArrayAdapter<String> mSpinnerAdapter;
    private GameSetupSelectableRule rule;
    private GameSetupRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Config mConfig;
    private TextToSpeech mTTS;
    private ScriptGenerator mGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneralMethodSet gms = new GeneralMethodSet();
        gms.updateLanguage(this);
        gms.setActivityTheme(this);

        setContentView(R.layout.activity_game_setup);

        mTTS = new TextToSpeech(this, this);

        mConfig = new Config();
        mConfig.load(this);

        rule = new GameSetupSelectableRule();

        initData();
        initRecyclerView(data);
        initSpinner();
        initButton();

        /*
        setListAdapter(new SimpleAdapter(this, data, R.layout.row_option, from, to));

        // Configure ListView for multiple choice mode (and set checked items)
        final ListView listView = getListView();

        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        syncConfigToList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mTTS.shutdown();
    }
    */

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        for (GameSetupItem item : data) {
            mConfig.setOptionEnabled(item.getOption(), item.isChecked());
        }
        mConfig.save(this);
        super.onDestroy();
    }

    // Begin implementing TextToSpeech.onInitListener
    @Override
    public void onInit(int status) {
        switch (status) {
            case TextToSpeech.SUCCESS:
                mGenerator = new ScriptGenerator(this, mTTS);
                if (Build.VERSION.SDK_INT >= 15) {
                    mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String s) {

                        }

                        @Override
                        public void onDone(String s) {
                            mButton.setEnabled(true);
                        }

                        @Override
                        public void onError(String s) {

                        }
                    });
                } else {
                    mTTS.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
                        @Override
                        public void onUtteranceCompleted(String s) {
                            mButton.setEnabled(true);
                        }
                    });
                }
                break;
            case TextToSpeech.ERROR:
                break;
        }
    }

    private void initButton() {
        mButton = (Button) findViewById(R.id.game_setup_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGenerator.saySpeech(mConfig);
                mButton.setEnabled(false);
            }
        });
    }

    private void initData() {
        data = new ArrayList<>();
        for (Config.Option option : Config.OPTIONS_ORDERED) {

            boolean selected = mConfig.isOptionEnabled(option);
            boolean selectable = true;

            if (selected) {
                rule.notifyRoleSelected(option);
            } else {
                selectable = rule.isRoleSelectable(option, TOTAL_PLAYERS);
            }
            data.add(new GameSetupItem(option, selected, selectable));
        }
    }

    private void initRecyclerView(List<GameSetupItem> data) {
        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) findViewById(R.id.game_setup_recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GameSetupRecyclerViewAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)
    }

    private void initSpinner() {
        tv0 = (TextView) findViewById(R.id.game_setup_resistance);
        tv1 = (TextView) findViewById(R.id.game_setup_spy);
        mSpinner = (Spinner) findViewById(R.id.game_setup_spinner);
        mSpinnerAdapter = new ArrayAdapter<>(this, R.layout.myspinner, NumOfGamers);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mSpinnerAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                TOTAL_PLAYERS = Integer.parseInt(NumOfGamers[arg2]);
                // arg0.setVisibility(View.VISIBLE);
                tv0.setText("Resistance:" +
                        DataSet.getNormalPlayers(TOTAL_PLAYERS));
                tv1.setText("Spy:" + DataSet.getSpyPlayers(TOTAL_PLAYERS));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    private class GameSetupSelectableRule {

        private Set<Config.Option> spy_selections;
        private Set<Config.Option> resistant_selections;

        public GameSetupSelectableRule() {
            spy_selections = new HashSet<>();
            resistant_selections = new HashSet<>();
        }

        public synchronized boolean isRoleSelectable(Config.Option option, int limit) {
            boolean result = true;

            // We do not need to worry numbers of resistant.
            final int spy_size_limit = DataSet.getSpyPlayers(limit);
            int spy_size_expected = spy_selections.size();
            switch (option) {
                case MERLIN_ASSASSIN:
                    spy_size_expected++;
                    break;
                case PERCIVAL:
                    result = result && resistant_selections.contains(Config.Option.MERLIN_ASSASSIN);
                    break;
                case MORDRED:
                    result = result && resistant_selections.contains(Config.Option.MERLIN_ASSASSIN);
                    spy_size_expected++;
                    break;
                case OBERON:
                    spy_size_expected++;
                    break;
                case MORGANA:
                    spy_size_expected++;
                    result = result
                            && resistant_selections.contains(Config.Option.MERLIN_ASSASSIN)
                            && resistant_selections.contains(Config.Option.PERCIVAL);
                    break;
                case LANCELOT_VARIANT_3:
                    spy_size_expected++;
                    break;
            }
            result = spy_size_expected <= spy_size_limit && result;
            return result;
        }

        public void notifyRoleSelected(Config.Option option) {
            if (option == Config.Option.MERLIN_ASSASSIN || option == Config.Option.LANCELOT_VARIANT_3) {
                resistant_selections.add(option);
                spy_selections.add(option);
            } else if (option == Config.Option.PERCIVAL) {
                resistant_selections.add(option);
            } else {
                spy_selections.add(option);
            }
        }

        public void notifyRoleRemoved(Config.Option option) {
            if (option == Config.Option.MERLIN_ASSASSIN || option == Config.Option.LANCELOT_VARIANT_3) {
                resistant_selections.remove(option);
                spy_selections.remove(option);
            } else if (option == Config.Option.PERCIVAL) {
                resistant_selections.remove(option);
            } else {
                spy_selections.remove(option);
            }
        }
    }

    private class GameSetupItem {

        private Config.Option option;
        private boolean checked;
        private boolean selectable;


        public GameSetupItem(Config.Option option, boolean checked, boolean selectable) {
            this.option = option;
            this.checked = checked;
            this.selectable = selectable;
        }

        public Config.Option getOption() {
            return option;
        }

        public void setOption(Config.Option option) {
            this.option = option;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public boolean isSelectable() {
            return selectable;
        }

        public void setSelectable(boolean selectable) {
            this.selectable = selectable;
        }
    }

    private class GameSetupRecyclerViewAdapter
            extends RecyclerView.Adapter<GameSetupRecyclerViewAdapter.ViewHolder> {

        private List<GameSetupItem> data;

        public GameSetupRecyclerViewAdapter(List<GameSetupItem> data) {
            this.data = data;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView name;
            public TextView description;
            public CheckBox checkBox;

            public ViewHolder(View v) {
                super(v);
                imageView = (ImageView) v.findViewById(R.id.character_image_view);
                name = (TextView) v.findViewById(R.id.character_name);
                description = (TextView) v.findViewById(R.id.character_description);
                checkBox = (CheckBox) v.findViewById(R.id.character_checkbox);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.listitem_game_setup, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final GameSetupItem item = data.get(position);
            final Config.Option option = item.getOption();

            holder.imageView.setImageResource(option.getImgResId());
            holder.name.setText(option.getTitleResId());
            holder.description.setText(option.getDescResId());
            holder.checkBox.setChecked(item.isChecked());

            holder.checkBox.setVisibility(item.isSelectable() ? View.VISIBLE : View.GONE);

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (item.isChecked()) {
                        rule.notifyRoleRemoved(option);
                        for (GameSetupItem gameSetupItem : data) {
                            if (gameSetupItem.getOption() == option) continue;
                            if (!rule.isRoleSelectable(gameSetupItem.getOption(), TOTAL_PLAYERS)) {
                                gameSetupItem.setChecked(false);
                                gameSetupItem.setSelectable(false);
                                rule.notifyRoleRemoved(gameSetupItem.getOption());
                            } else {
                                gameSetupItem.setSelectable(true);
                            }
                        }
                        item.setChecked(false);
                    } else {
                        rule.notifyRoleSelected(option);
                        for (GameSetupItem gameSetupItem : data) {
                            if (gameSetupItem.getOption() == option) continue;
                            if (gameSetupItem.isChecked()) continue;
                            gameSetupItem.setSelectable(
                                    rule.isRoleSelectable(
                                            gameSetupItem.getOption(), TOTAL_PLAYERS));
                        }
                        item.setChecked(true);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

    }
}
