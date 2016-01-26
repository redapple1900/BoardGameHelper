package com.yuanwei.resistance.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.partygame.origin.model.Resistance;

import java.util.List;

/**
 * Created by chenyuanwei on 15/11/24.
 */

public class OptionSetupRecyclerViewAdapter
        extends RecyclerView.Adapter<CheckableViewHolder> {

    private List<OptionItem> data;

    public OptionSetupRecyclerViewAdapter(List<OptionItem> data) {
        this.data = data;
    }

    @Override
    public CheckableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CheckableViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_game_setup, parent, false));
    }

    @Override
    public void onBindViewHolder(CheckableViewHolder holder, int position) {

        final OptionItem mainItem = data.get(position);
        final Resistance.Option option = mainItem.getOption();

        (holder).name.setText(option.getTitleResId());
        (holder).description.setText(option.getDescResId());
        (holder).image.setVisibility(View.GONE);
        (holder).checkBox.setChecked(mainItem.isChecked());

        (holder).checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mainItem.setChecked(!mainItem.isChecked());

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class OptionItem {

        private Resistance.Option option;
        private boolean checked;

        public OptionItem(Resistance.Option option, boolean checked) {
            this.option = option;
            this.checked = checked;
        }

        public Resistance.Option getOption() {
            return option;
        }

        public void setOption(Resistance.Option option) {
            this.option = option;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}
