package com.yuanwei.resistance.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.ui.fragment.BasePlotDialogFragment;

/**
 * Created by chenyuanwei on 15/7/26.
 */
public class NumberPickDialogFragment extends BasePlotDialogFragment
        implements NumberPicker.OnValueChangeListener {

    // TODO: Clear
    public static final String CLEAR_SELECTION = "NumberPickDialogFragment.Clear";
    public static final String TAG = "NumberPickDialogFragment";

    private int prev, next;
    private String type;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        type = TAG;
        prev = getArguments().getInt(Constants.TOTAL_PLAYERS_KEY, 8);
        next = Integer.MAX_VALUE;

        final Dialog dialog = new Dialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        dialog.setTitle("Select Number of Total Players");
        dialog.setContentView(R.layout.dialog_number_picker);
        Button negative = (Button) dialog.findViewById(R.id.game_setup_dialog_negativebutton);
        Button positive = (Button) dialog.findViewById(R.id.game_setup_dialog_positivebutton);

        NumberPicker picker = (NumberPicker) dialog.findViewById(R.id.game_setup_dialog_numberpicker);

        picker.setMaxValue(10);
        picker.setMinValue(5);
        picker.setValue(prev);

        picker.setWrapSelectorWheel(false);
        picker.setOnValueChangedListener(this);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prev > next) type = CLEAR_SELECTION;

                if (next == Integer.MAX_VALUE) next = prev;

                getPlotListener().onEventStart(type, next);

                dialog.dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i0, int i1) {
        next = i1;
    }
}
