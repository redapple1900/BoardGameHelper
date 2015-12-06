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

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.model.Gamer;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.partygame.origin.model.Resistance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyuanwei on 15/11/17.
 */
public class GridRecyclerViewAdapter 
        extends RecyclerView.Adapter<GridRecyclerViewAdapter.ViewHolder> {
    
    final static int MaxRounds = 5;

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
        
        holder.profile.setImageResource(model.getUser().getResId());
        
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
        /*
        if (badges.size() > holder.tokens.getChildCount()) {
            ImageView badge = (ImageView) LayoutInflater
                    .from(mContext)
                    .inflate(R.layout.grid_item_badge_view, holder.tokens, false);

            int result = badges.get(badges.size() - 1);

            switch (result){
                case Resistance.WIN:
                    badge.setImageResource(R.drawable.token_win);
                    break;
                case Resistance.LOSE:
                    badge.setImageResource(R.drawable.token_fail);
                    break;
                case Resistance.NEUTRAL:
                    badge.setImageResource(R.drawable.token_neutral);
                    break;
            }

            holder.tokens.addView(badge);
        }


        for (int i = 0, j = badges.size(); i < j; i++) {
            int result = badges.get(i);

            if (result == Resistance.SABOTAGE) {
                ((ImageView) holder.tokens.getChildAt(i))
                        .setImageResource(R.drawable.token_sabotage);
            }
        }

        holder.tokens.invalidate();
        */
        for (int i = 0, j = badges.size(); i < j; i++) {

            int result = badges.get(i);

            switch (result){
                case Resistance.WIN:
                    holder.statusBadge[i].setImageResource(R.drawable.token_win);
                    break;
                case Resistance.LOSE:
                    holder.statusBadge[i].setImageResource(R.drawable.token_fail);
                    break;
                case Resistance.NEUTRAL:
                    holder.statusBadge[i].setImageResource(R.drawable.token_neutral);
                    break;
                case Resistance.SABOTAGE:
                    holder.statusBadge[i].setImageResource(R.drawable.token_sabotage);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView profile;
        //ViewGroup tokens;
        ImageView[] statusBadge;

        TextView name;
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            profile = (ImageView) itemView.findViewById(R.id.icon);

            //tokens = (ViewGroup) itemView.findViewById(R.id.tokens);

            statusBadge = new ImageView[MaxRounds];
            statusBadge[0] = (ImageView) itemView.findViewById(R.id.token1);
            statusBadge[1] = (ImageView) itemView.findViewById(R.id.token2);
            statusBadge[2] = (ImageView) itemView.findViewById(R.id.token3);
            statusBadge[3] = (ImageView) itemView.findViewById(R.id.token4);
            statusBadge[4] = (ImageView) itemView.findViewById(R.id.token5);

            name = (TextView) itemView.findViewById(R.id.text);
            description = (TextView) itemView.findViewById(R.id.sub_text);
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

    public interface OnItemToggleListener{
        void onToggle(View view, int position, boolean isSelected);
    }
}
