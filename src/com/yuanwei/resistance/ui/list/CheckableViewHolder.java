package com.yuanwei.resistance.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuanwei.resistance.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenyuanwei on 15/11/26.
 */
public class CheckableViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.character_container)
    public View container;
    @Bind(R.id.character_name)
    public TextView name;
    @Bind(R.id.character_description)
    public TextView description;
    @Bind(R.id.character_image_view)
    public ImageView image;
    @Bind(R.id.character_checkbox)
    public CheckBox checkBox;

    public CheckableViewHolder(View v) {
        super(v);

        ButterKnife.bind(this, v);
    }
}
