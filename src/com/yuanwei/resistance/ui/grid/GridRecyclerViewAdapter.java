package com.yuanwei.resistance.ui.grid;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yuanwei.resistance.R;
import com.yuanwei.resistance.model.Gamer;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.partygame.origin.model.Resistance;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenyuanwei on 15/11/17.
 */
public class GridRecyclerViewAdapter 
        extends RecyclerView.Adapter<GridRecyclerViewAdapter.ViewHolder> {

    private Context mContext;

    private List<Model> models = new ArrayList<>();

    private OnItemToggleListener onItemToggleListener;

    public GridRecyclerViewAdapter(Context context){
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.grid_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Model model = models.get(position);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSelected = models.get(position).isSelected();

                if (onItemToggleListener != null)
                    onItemToggleListener.onToggle(view, position, isSelected);
            }
        });

        Picasso.with(mContext).load(model.getUser().getResId()).into(holder.profile);
        
        holder.name.setText(model.getUser().getName());
        
        holder.name.setTextColor(model.isSelected() ? Color.BLUE : Color.BLACK);

        if (model.getGamer() == null) return;

        Gamer gamer = model.getGamer();

        holder.description.setText(gamer.getRoleName());

        if (gamer.isLeader()) {
            holder.description.setText(mContext.getResources().getText(R.string.string_leader));
        }

        List<Integer> badges = model.getGamer().getResults();

        // TODO:: FIND a way to implement multirow, current way is too hacky
        for (int i = 0, j = badges.size(); i < j; i++) {

            int result = badges.get(i);

            switch (result){
                case Resistance.WIN:
                    Picasso.with(mContext).load(R.drawable.token_win).into(holder.statusBadge[i]);
                    break;
                case Resistance.LOSE:
                    Picasso.with(mContext).load(R.drawable.token_fail).into(holder.statusBadge[i]);
                    break;
                case Resistance.NEUTRAL:
                    Picasso.with(mContext).load(R.drawable.token_neutral).into(holder.statusBadge[i]);
                    break;
                case Resistance.SABOTAGE:
                    Picasso.with(mContext).load(R.drawable.token_sabotage).into(holder.statusBadge[i]);
                    break;
            }

            holder.statusBadge[i].setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void setOnItemToggleListener(OnItemToggleListener onItemToggleListener) {
        if (onItemToggleListener != null)
            this.onItemToggleListener = onItemToggleListener;
    }

    public void bindModels(List<User> userList, List<Gamer> gamerList) {
        for (int i = 0; i < userList.size(); i++) {
            models.add(new Model(userList.get(i), gamerList != null ? gamerList.get(i) : null));
        }
    }

    public void removeModel(int position) {
        models.remove(position);
    }

    public void toggleSelected(int position) {
        Model model = models.get(position);
        model.setSelection(!model.isSelected());
    }

    public interface OnItemToggleListener {
        void onToggle(View view, int position, boolean isSelected);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cv)
        CardView cardView;
        @Bind(R.id.icon)
        ImageView profile;
        @Bind(R.id.text)
        TextView name;
        @Bind(R.id.sub_text)
        TextView description;
        @Bind({R.id.token1, R.id.token2, R.id.token3, R.id.token4, R.id.token5})
        ImageView[] statusBadge;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public static class Model {
        Gamer gamer;
        User user;
        boolean isSelected;

        public Model(User user, Gamer gamer) {
            this.user = user;
            this.gamer = gamer;
            isSelected = false;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelection(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public Gamer getGamer() {
            return gamer;
        }

        public User getUser() {
            return user;
        }
    }
}
