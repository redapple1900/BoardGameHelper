package com.yuanwei.resistance.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuanwei.resistance.R;

/**
 * Created by chenyuanwei on 15/11/26.
 */
public class CheckableViewHolder extends RecyclerView.ViewHolder {

    public View container;
    public TextView name;
    public TextView description;
    public ImageView image;
    public CheckBox checkBox;

    public CheckableViewHolder(View v) {
        super(v);
        container = v.findViewById(R.id.character_container);
        name = (TextView) v.findViewById(R.id.character_name);
        description = (TextView) v.findViewById(R.id.character_description);
        image = (ImageView) v.findViewById(R.id.character_image_view);
        checkBox = (CheckBox) v.findViewById(R.id.character_checkbox);
    }
}
