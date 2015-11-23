package com.yuanwei.resistance.ui.grid;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.model.Gamer;
import com.yuanwei.resistance.model.User;
import com.yuanwei.resistance.partygame.origin.model.Resistance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenyuanwei on 15/11/14.
 */
public class GameGridAdapter extends BaseAdapter {
    final int MaxRounds = 5;

    public class Model {
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

    private List<Model> models = new ArrayList<>();

    private Context mContext;

    public GameGridAdapter(Context context) {

        mContext = context;
    }

    public void bindModels(List<User> userList, List<Gamer> gamerList) {
        for (int i = 0; i < userList.size(); i++) {
            models.add(new Model(userList.get(i), gamerList.get(i)));
        }
    }

    public void toggleSelected(int position) {
        Model model = models.get(position);
        model.setSelection(!model.isSelected());
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Model getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_view, parent, false);
        }

        Model model = getItem(position);

        ImageView image = (ImageView) convertView.findViewById(R.id.icon);
        image.setImageResource(model.getUser().getResId());

        ImageView sele = (ImageView) convertView.findViewById(R.id.selection);
        sele.setVisibility(model.getGamer().isLeader() ? View.VISIBLE : View.INVISIBLE);

        ImageView[] badgeViews = new ImageView[5];
        badgeViews[0] = (ImageView) convertView.findViewById(R.id.token1);
        badgeViews[1] = (ImageView) convertView.findViewById(R.id.token2);
        badgeViews[2] = (ImageView) convertView.findViewById(R.id.token3);
        badgeViews[3] = (ImageView) convertView.findViewById(R.id.token4);
        badgeViews[4] = (ImageView) convertView.findViewById(R.id.token5);

        TextView text = (TextView) convertView.findViewById(R.id.text);

        text.setText(model.getUser().getName());

        if (model.isSelected)
            text.setTextColor(Color.RED);
        else
            text.setTextColor(Color.WHITE);

        for (int i = 0; i < MaxRounds; i++) {
            badgeViews[i].setVisibility(View.INVISIBLE);
        }

        List<Integer> badges = model.getGamer().results;

        for (int i = 0, j = badges.size(); i < j; i++) {

            int result = badges.get(i);

            switch (result){
                case Resistance.WIN:
                    badgeViews[i].setImageResource(R.drawable.token_win);
                    break;
                case Resistance.LOSE:
                    badgeViews[i].setImageResource(R.drawable.token_fail);
                    break;
                case Resistance.NEUTRAL:
                    badgeViews[i].setImageResource(R.drawable.token_neutral);
                    break;
                case Resistance.SABOTAGE:
                    badgeViews[i].setImageResource(R.drawable.token_sabotage);
                    break;
            }
                badgeViews[i].setVisibility(View.VISIBLE);

        }

        for (int i = badges.size(); i < MaxRounds; i ++) {
            badgeViews[i].setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
