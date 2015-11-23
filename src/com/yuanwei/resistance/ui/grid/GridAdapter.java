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

import java.util.LinkedList;
import java.util.List;

// TODO::Remove its usage
public class GridAdapter extends BaseAdapter {
    static final int MaxRounds = 5;

    public static class Item {
        public long id;
        public String text;
        public boolean selection;
        private boolean isRevealed;
        public int resId;
        public Token token;

        public Item(String text, int resId, Token token, boolean selection, long id) {
            this.text = text;
            this.resId = resId;
            this.token = token;
            this.selection = selection;
            this.setRevealed(false);
            this.id = id;
        }

        public Item(String text, int resId, Token token, boolean selection) {
            this(text, resId, token, selection, -1);
        }

        public Item(String text, int resId, Token token) {
            this(text, resId, token, false);
        }

        public Item(String text, int resId) {
            this(text, resId, new Token(MaxRounds));
        }

        public Item() {
            this(null, 0);
        }

        public boolean isRevealed() {
            return isRevealed;
        }

        public void setRevealed(boolean isRevealed) {
            this.isRevealed = isRevealed;
        }
    }

    public static class Token {
        public static final int WIN = R.drawable.token_win;
        public static final int LOSE = R.drawable.token_fail;
        public static final int SABOTAGE = R.drawable.token_sabotage;
        public static final int NEUTRAL = R.drawable.token_neutral;
        public int mission;
        public int total;
        public int[] missionResult;

        public Token(int total) {
            this.mission = 0;
            this.total = total;
            this.missionResult = new int[this.total];
            for (int i = 0; i < this.total; i++) {
                this.missionResult[i] = this.NEUTRAL;
            }
        }
    }

    private List<Item> mItems = new LinkedList<>();

    private Context mContext;

    public GridAdapter(Context context) {

        mContext = context;
    }

    public void addItem(Item item) {
        mItems.add(item);
    }

    public void removeAllItems() {
         mItems.removeAll(mItems);
    }

    public void removeItem(int position) {
        if (this.getCount() > position) {
            mItems.remove(position);
        }
    }

    public void replaceItem(Item item, int position) {
        mItems.set(position, item);
    }


    public void setMissionStatus(Item item, int position, int status) {

        item.token.missionResult[item.token.mission++] = status;
    }

    public void setToken(Item item, int position, int round) {
        item.token.missionResult[round] = Token.SABOTAGE;
    }

    public void setLeader(Item item, int position) {
        item.selection = !item.selection;
    }

    public void revealIdentity(int position) {
        mItems.get(position).setRevealed(true);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
        }
        ImageView image = (ImageView) convertView.findViewById(R.id.icon);
        ImageView sele = (ImageView) convertView.findViewById(R.id.selection);

        ImageView[] token = new ImageView[5];
        token[0] = (ImageView) convertView.findViewById(R.id.token1);
        token[1] = (ImageView) convertView.findViewById(R.id.token2);
        token[2] = (ImageView) convertView.findViewById(R.id.token3);
        token[3] = (ImageView) convertView.findViewById(R.id.token4);
        token[4] = (ImageView) convertView.findViewById(R.id.token5);
        TextView text = (TextView) convertView.findViewById(R.id.text);
        Item item =  getItem(position);
        image.setImageResource(item.resId);
        text.setText(item.text);
        if (item.isRevealed()) text.setTextColor(Color.RED);
        else text.setTextColor(Color.WHITE);

        for (int i = 0; i < MaxRounds; i++) {
            token[i].setVisibility(View.INVISIBLE);
        }
        for (int i = 0; i < item.token.mission; i++) {
            if (item.token.missionResult[i] != 0) {
                token[i].setImageResource(item.token.missionResult[i]);
                token[i].setVisibility(View.VISIBLE);
            } else token[i].setVisibility(View.INVISIBLE);
        }
        if (!item.selection) {
            sele.setVisibility(View.INVISIBLE);
        } else sele.setVisibility(View.VISIBLE);

        return convertView;
    }

}

