package com.yuanwei.resistance.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.model.Gamer;
import com.yuanwei.resistance.model.GridParams;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.moderator.BaseSwitcher;
import com.yuanwei.resistance.partygame.origin.model.Resistance.GameStatus;
import com.yuanwei.resistance.ui.DoubleButtonView;
import com.yuanwei.resistance.ui.fragment.BasePlotFragment;
import com.yuanwei.resistance.ui.grid.GridRecyclerViewAdapter;
import com.yuanwei.resistance.ui.widget.ButtonOnTouchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenyuanwei on 15/11/1.
 */
public class GridFragment extends BasePlotFragment {

    @Bind(R.id.buttons)
    DoubleButtonView doubleButtonView;
    @Bind(R.id.end)
    TextView textView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private int limit;
    private int count;
    private Binder binder;
    private List<Gamer> mGamers;
    private GridRecyclerViewAdapter mGridRecycleViewAdapter;
    private ButtonOnTouchListener mOnTouchListener;

    private GridLayoutManager mGridLayoutManager;

    public static GridFragment createInstance(
            GridParams gridParams,
            ArrayList<User> list,
            ArrayList<Gamer> gamers) {
        GridFragment fragment = new GridFragment();
        Bundle bundle = new Bundle();
        //TODO:: No Hardcoding
        bundle.putParcelable("params", gridParams);
        bundle.putParcelableArrayList(Constants.USERLIST_KEY, list);
        bundle.putParcelableArrayList("gamers", gamers);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOnTouchListener = new ButtonOnTouchListener(getActivity());

        if (savedInstanceState == null) {

            GridParams gridParams = getArguments().getParcelable("params");

            List<User> userList = getArguments().getParcelableArrayList(Constants.USERLIST_KEY);

            mGamers = getArguments().getParcelableArrayList("gamers");

            count = 0;

            limit = gridParams.getLimit();

            binder = new Binder(gridParams);

            mGridRecycleViewAdapter = new GridRecyclerViewAdapter(getActivity());

            mGridRecycleViewAdapter.bindModels(userList, mGamers);

            mGridLayoutManager = new GridLayoutManager(
                    getActivity(),
                    userList.size() < 7 ? 3 : 4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);

        ButterKnife.bind(this, view);

        binder.bindAdapter(mGridRecycleViewAdapter);

        binder.bindText(textView);

        binder.bindRecyclerView(recyclerView);

        binder.bindButtons(doubleButtonView);

        return view;
    }

    private class Binder {
        private GameStatus status;

        private String Tag;

        private boolean allowPass;

        private int game;

        public Binder(GridParams gridParams) {

            game = gridParams.getGameId();

            status = gridParams.getGameStatus();

            allowPass = gridParams.isPassAllowed();

            limit = gridParams.getLimit();

            switch (status) {
                // TODO::NO HARDCODING
                case NOT_FINISHED:
                    Tag = gridParams.getTag();
                    break;
                case ASSASSINATION:
                    Tag = "assassination";
                    break;
                case MODIFYING_USERS:
                    Tag = gridParams.getTag();
                    break;
                default:
                    Tag = "over";
                    break;
            }
        }

        public void bindAdapter(final GridRecyclerViewAdapter adapter) {
            adapter.setOnItemToggleListener(
                    new GridRecyclerViewAdapter.OnItemToggleListener() {
                        @Override
                        public void onToggle(View view, int position, boolean isSelected) {
                            if (status == GameStatus.NOT_FINISHED) {
                                if (isSelected || count + 1 <= limit) {

                                    int diff = isSelected ? -1 : 1;
                                    count += diff;
                                    //TODO:: no hard coding
                                    getPlotListener().onEventStart("selection", position);
                                    adapter.toggleSelected(position);
                                    adapter.notifyItemChanged(position);

                                    doubleButtonView.setButtonVisibility(
                                            count < limit ? View.GONE : View.VISIBLE,
                                            allowPass ? View.VISIBLE : View.GONE);

                                } else {
                                    view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                                            R.anim.shake));
                                }
                            } else if (status == GameStatus.ASSASSINATION) {
                                if (mGamers.get(position).getRoleId() < 0) {
                                    view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                                            R.anim.shake));
                                } else {
                                    getPlotListener().onEventStart("assassination", position);
                                }
                            } else if (status == GameStatus.MODIFYING_USERS) {
                                getPlotListener().onEventStart(Tag, position);
                                adapter.removeModel(position);
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(
                                        position,
                                        adapter.getItemCount());
                            }
                        }
                    });
        }

        public void bindRecyclerView(RecyclerView recyclerView) {
            recyclerView.setAdapter(mGridRecycleViewAdapter);

            recyclerView.setLayoutManager(mGridLayoutManager);

            recyclerView.setHasFixedSize(true);
        }

        public void bindText(TextView textView) {
            switch (status) {
                case NOT_FINISHED:
                    textView.setVisibility(View.GONE);
                    break;
                case RESISTANCE_WIN:
                    textView.setText(game == Constants.ORIGIN ?
                            getString(R.string.string_thievies_win) :
                            getString(R.string.string_thievies_win_avalon));
                    textView.setVisibility(View.VISIBLE);
                    break;
                case SPY_WIN:
                    textView.setText(game == Constants.ORIGIN ?
                            getString(R.string.string_spies_win) :
                            getString(R.string.string_spies_win_avalon));
                    textView.setVisibility(View.VISIBLE);
                    break;
                case ASSASSINATION:
                    textView.setText(R.string.assassination_choose_target);
                    textView.setVisibility(View.VISIBLE);
                    break;
                case MODIFYING_USERS:
                    textView.setText(R.string.remove_gamer);
                    textView.setVisibility(View.VISIBLE);
                    break;
                default:
                    textView.setVisibility(View.GONE);
            }
        }

        public void bindButtons(DoubleButtonView doubleButtonView) {
            if (status == GameStatus.NOT_FINISHED) {
                doubleButtonView.setButtonImage(R.drawable.propose, R.drawable.token_fail);

                doubleButtonView.setButtonText(R.string.string_button_propose_game, R.string.sabotage);

                doubleButtonView.setButtonVisibility(View.GONE, allowPass ? View.VISIBLE : View.GONE);
            } else if (status == GameStatus.ASSASSINATION) {

                doubleButtonView.setButtonVisibility(View.GONE, View.GONE);

            } else {
                doubleButtonView.setButtonImage(R.drawable.replay, R.drawable.rate);

                doubleButtonView.setButtonText(R.string.replay, R.string.rate);

                doubleButtonView.setButtonVisibility(View.VISIBLE, View.VISIBLE);
            }

            doubleButtonView.setButtonOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getPlotListener().onEventStart(Tag, BaseSwitcher.PRIMARY);
                }

            }, new Button.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    getPlotListener().onEventStart(Tag, BaseSwitcher.SECONDARY);
                }
            });

            doubleButtonView.setButtonOnTouchListener(mOnTouchListener, mOnTouchListener);
        }
    }
}
