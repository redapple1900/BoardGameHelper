package com.yuanwei.resistance.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.partygame.avalon.model.Avalon;

import java.util.List;

/**
 * Created by chenyuanwei on 15/11/24.
 */
public class RoleSetupRecyclerViewAdapter extends RecyclerView.Adapter<CheckableViewHolder> {

    public interface OnRoleCheckListener {
        void onToggle(RoleItem item);
    }

    public static class RoleItem {

        private Avalon.Role role;
        private boolean checked;
        private boolean selectable;


        public RoleItem(Avalon.Role role, boolean checked, boolean selectable) {
            this.role = role;
            this.checked = checked;
            this.selectable = selectable;
        }

        public Avalon.Role getRole() {
            return role;
        }

        public void setRole(Avalon.Role role) {
            this.role = role;
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

    private List<RoleItem> data;

    private OnRoleCheckListener onRoleCheckListener;

    public RoleSetupRecyclerViewAdapter(List<RoleItem> data) {
        this.data = data;
    }

    /*
    public static class ViewHolder extends RecyclerView.ViewHolder {
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
    */

    @Override
    public CheckableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CheckableViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.listitem_game_setup, parent, false));
    }

    @Override
    public void onBindViewHolder(CheckableViewHolder holder, final int position) {

        final RoleItem mainItem = data.get(position);
        final Avalon.Role role = mainItem.getRole();

        (holder).image.setImageResource(role.getImgResId());
        (holder).name.setText(role.getTitleResId());
        (holder).description.setText(role.getDescResId());
        (holder).checkBox.setChecked(mainItem.isChecked());

        (holder).checkBox.setVisibility(mainItem.isSelectable() ? View.VISIBLE : View.GONE);

        (holder).checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onRoleCheckListener.onToggle(mainItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnRoleCheckListener(OnRoleCheckListener onRoleCheckListener) {
        this.onRoleCheckListener = onRoleCheckListener;
    }
}
