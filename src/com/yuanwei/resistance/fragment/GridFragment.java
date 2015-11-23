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
import com.yuanwei.resistance.model.Gamer;
import com.yuanwei.resistance.model.Params;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.moderator.BaseSwitcher;
import com.yuanwei.resistance.ui.DoubleButtonView;
import com.yuanwei.resistance.ui.fragment.BasePlotFragment;
import com.yuanwei.resistance.ui.grid.GridRecyclerViewAdapter;
import com.yuanwei.resistance.ui.widget.ButtonOnTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyuanwei on 15/11/1.
 */
public class GridFragment extends BasePlotFragment {

    private int status;

    private String Tag;

    private boolean allowPass;

    private int limit;

    private int count;

    private GridRecyclerViewAdapter mGridRecycleViewAdapter;

    private DoubleButtonView doubleButtonView;

    private ButtonOnTouchListener mOnTouchListener;

    private GridLayoutManager mGridLayoutManager;

    public static GridFragment createInstance(
            Params params,
            ArrayList<User> list,
            ArrayList<Gamer> gamers) {
        GridFragment fragment = new GridFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("params", params);
        bundle.putParcelableArrayList("gamerList", list);
        bundle.putParcelableArrayList("gamers", gamers);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOnTouchListener = new ButtonOnTouchListener(getActivity());

        if (savedInstanceState == null) {

            count = 0;

            Params params = getArguments().getParcelable("params");

            status = params.getStats();

            Tag = status == Params.GAME_NOT_OVER ? params.getTag() : "over";

            // TODO::SetTagAccording to game status

            allowPass = params.isPassAllowed();

            limit = params.getLimit();

            List<User> userList = getArguments().getParcelableArrayList("gamerList");

            List<Gamer> gamers = getArguments().getParcelableArrayList("gamers");

            mGridRecycleViewAdapter = new GridRecyclerViewAdapter(getActivity());

            mGridRecycleViewAdapter.bindModels(userList, gamers);

            mGridLayoutManager = new GridLayoutManager(
                    getActivity(),
                    userList.size() < 7 ? 3 : 4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_propose, container, false);

        mGridRecycleViewAdapter.setOnItemToggleListener(
                new GridRecyclerViewAdapter.OnItemToggleListener() {
                    @Override
                    public void onToggle(View view, int position, boolean isSelected) {
                        if (isSelected || count + 1 <= limit) {
                            int diff = isSelected ? -1 : 1;
                            count += diff;
                            getPlotListener().onEventStart("selection", position);
                            mGridRecycleViewAdapter.toggleSelected(position);
                            mGridRecycleViewAdapter.notifyItemChanged(position);
                            if (status == Params.GAME_NOT_OVER) {
                                doubleButtonView.setButtonVisibility(
                                        count < limit ? View.GONE : View.VISIBLE,
                                        allowPass ? View.VISIBLE : View.GONE);
                            }
                        } else {
                            view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                                    R.anim.shake));
                        }
                    }
                });

        TextView textView = (TextView) view.findViewById(R.id.end);

        if (status == Params.GAME_NOT_OVER) {
            textView.setVisibility(View.GONE);
        } else if (status == Params.RESISTANCE_WIN){
            textView.setText(getString(R.string.string_thievies_win));
            textView.setVisibility(View.VISIBLE);
        } else if (status == Params.SPY_WIN) {
            textView.setText(getString(R.string.string_spies_win));
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setAdapter(mGridRecycleViewAdapter);

        recyclerView.setLayoutManager(mGridLayoutManager);

        recyclerView.setHasFixedSize(true);

        doubleButtonView = (DoubleButtonView) view.findViewById(R.id.buttons);

        if (status == Params.GAME_NOT_OVER) {
            doubleButtonView.setButtonImage(R.drawable.propose, R.drawable.token_fail);

            doubleButtonView.setButtonText(R.string.string_button_propose_game, R.string.dummy_button);

            doubleButtonView.setButtonVisibility(View.GONE, allowPass ? View.VISIBLE : View.GONE);
        } else if (status == Params.RESISTANCE_WIN || status == Params.SPY_WIN) {
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

        return view;
    }
}
