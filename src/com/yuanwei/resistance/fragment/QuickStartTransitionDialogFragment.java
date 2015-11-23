package com.yuanwei.resistance.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.ui.fragment.BasePlotDialogFragment;

/**
 * Created by chenyuanwei on 15/10/4.
 */
public class QuickStartTransitionDialogFragment extends BasePlotDialogFragment {
    public static final String TAG = "QuickStartTransitionDialogFragment";

    private int mGame;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mGame = getArguments().getInt(Constants.GAME);

        final Dialog dialog = new Dialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        dialog.setContentView(R.layout.dialog_quick_start_transition);

        Button negative = (Button) dialog.findViewById(R.id.quickstart_transition_dialog_negative_button);
        Button positive = (Button) dialog.findViewById(R.id.quickstart_transition_dialog_positive_button);

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPlotListener().onEventStart(Constants.GAME_WITHOUT_CARDS, mGame);
                dialog.dismiss();
            }
        });

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPlotListener().onEventStart(Constants.GAME_WITH_CARDS, mGame);
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
