package com.yuanwei.resistance.gridviewtest;

/**
 * Created by chenyuanwei on 15/6/25.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.image.ImageCache;
import com.yuanwei.resistance.image.ImageFetcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class PlayerDetailViewAdapter extends BaseAdapter {
    static final int MaxRounds = 5;

    private FragmentManager mFragmentManager;
    private ImageFetcher mImageFetcher;
    private int mImageThumbSize, rightCount, leftCount;
    private int mImageThumbSpacing;
    private Map<Integer, Integer> mBadgeMap;
    private static final String IMAGE_CACHE_DIR = "thumbs";

    public class PlayerModel {
        private String name;
        private String url;
        private int resId;
        // This list logs the behavior of the player in each round.
        private List<Integer> records;
        // This map contain the rules of the game
        private Map<Integer, Boolean> conditions;

        public PlayerModel(String name, String url) {
            this.name = name;
            this.url = url;
            this.records = new ArrayList<>(10); // Hard coded max num initially
            this.conditions = new HashMap<>();
        }

        public PlayerModel(String name, int resId) {
            this.name = name;
            this.resId = resId;
            this.records = new ArrayList<>(10); // Hard coded max num initially
            this.conditions = new HashMap<>();
        }

        public PlayerModel(String name) {
            this(name, null);
        }
    }


    public class Item {
        public String text;
        public boolean selection;
        private boolean isRevealed;
        public int resId;
        public Token token;

        public Item(String text, int resId, Token token, boolean selection) {
            this.text = text;
            this.resId = resId;
            this.token = token;
            this.selection = selection;
            this.setRevealed(false);
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
        public static final int WIN = 1;
        public static final int LOSE = -1;
        public static final int NEUTRAL = 0;
        public int mission;
        public int total;
        public int[] missionResult;
        public int resId_win, resId_lose, resId_neutral, resId_sabotage;

        public Token(int total) {
            this.mission = 0;
            this.total = total;
            this.resId_neutral = R.drawable.token_neutral;
            this.resId_lose = R.drawable.token_fail;
            this.resId_win = R.drawable.token_win;
            this.resId_sabotage = R.drawable.token_sabotage;
            this.missionResult = new int[this.total];
            for (int i = 0; i < this.total; i++) {
                this.missionResult[i] = this.resId_neutral;
            }
        }

        public Token(int total, int mission, int[] missionResult) {
            this.mission = mission;
            this.total = total;
            this.resId_neutral = R.drawable.token_neutral;
            this.resId_lose = R.drawable.token_fail;
            this.resId_win = R.drawable.token_win;
            this.resId_sabotage = R.drawable.token_sabotage;
            this.missionResult = missionResult;
        }

    }

    private List<Item> mItems = new LinkedList<>();
    private List<PlayerModel> mPlayerModels = new ArrayList<>(10);

    private Context mContext;

    public PlayerDetailViewAdapter(Context context, FragmentManager fragmentManager) {

        mContext = context;
        mFragmentManager = fragmentManager;
        mImageThumbSize = mContext.getResources().getDimensionPixelSize(R.dimen.itemSize_medium);
        mImageThumbSpacing = mContext.getResources().getDimensionPixelSize(R.dimen.padding_layout);


        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(mContext.getApplicationContext(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(mContext.getApplicationContext(), mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.ic_launcher);
        mImageFetcher.addImageCache(mFragmentManager, cacheParams);

        mBadgeMap = new HashMap();
        mBadgeMap.put(1, R.drawable.token_win);
        mBadgeMap.put(-1, R.drawable.token_fail);
        mBadgeMap.put(0, R.drawable.token_neutral);
        mBadgeMap.put(-2, R.drawable.token_sabotage);
        // MARK: HERE to add new badge or change badge.
    }

    public void addItem(Item item) {
        mItems.add(item);
    }
    
    public void addPlayer(PlayerModel playerModel) {
        mPlayerModels.add(playerModel);
    }

    public void removeAllItems() {
        while (this.getCount() > 0) {
            mItems.remove(this.getCount() - 1);
        }
    }

    public void removeAllPlayers() {
        mPlayerModels.removeAll(mPlayerModels);
    }

    public void removeItem(int position) {

        if (this.getCount() > 0) {
            mItems.remove(position);
        }
    }
    public void removePlayer(int position) {
        if (mPlayerModels.size() > 0)
            mPlayerModels.remove(position);
    }

    public void replaceItem(Item item, int position) {
        if (this.getCount() > position) {
            mItems.remove(position);
        }
        mItems.add(position, item);
    }

    public void addToken(Item item, int position, int status) {
        if (this.getCount() > position) {
            mItems.remove(position);
        }
        int mission = item.token.mission + 1;
        int[] missionResult = item.token.missionResult;
        if (status == Token.WIN) {
            missionResult[item.token.mission] = item.token.resId_win;
        } else if (status == Token.LOSE) {
            missionResult[item.token.mission] = item.token.resId_lose;
        }
        Token token = new Token(MaxRounds, mission, missionResult);
        Item mItem = new Item(item.text, item.resId, token);

        mItem.selection = item.selection;
        mItems.add(position, item);
    }

    public void setToken(Item item, int position, int round) {
        item.token.missionResult[round] = item.token.resId_sabotage;
    }

    public void setLeader(Item item, int position) {
        if (this.getCount() > position) {
            mItems.remove(position);
        }
        Item new_item = item;
        new_item.selection = !item.selection;
        mItems.add(position, new_item);
    }

    public void revealIdentity(int position) {
        mItems.get(position).setRevealed(true);
    }

    private int getBadgeResource(int record) {
        return mBadgeMap.get(record);
    }

    private void addBadgeToAttachment(int position, PlayerModel mPlayerModel, LinearLayout linearLayout) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(getBadgeResource(mPlayerModel.records.get(position)));
        linearLayout.addView(
                imageView,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
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
            // convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null, false);
            convertView = LayoutInflater.from(mContext).inflate(R.layout.player_detail, null, false);
        }
        ImageView image = (ImageView) convertView.findViewById(R.id.icon);
        ImageView sele = (ImageView) convertView.findViewById(R.id.selection);
        TextView text = (TextView) convertView.findViewById(R.id.text);
        LinearLayout left_attachment = (LinearLayout) convertView.findViewById(R.id.playerdetail_left_attachment);
        LinearLayout right_attachment = (LinearLayout) convertView.findViewById(R.id.playerdetail_right_attachment);
        ImageView[] token = new ImageView[5];
        token[0] = (ImageView) convertView.findViewById(R.id.token1);
        token[1] = (ImageView) convertView.findViewById(R.id.token2);
        token[2] = (ImageView) convertView.findViewById(R.id.token3);
        token[3] = (ImageView) convertView.findViewById(R.id.token4);
        token[4] = (ImageView) convertView.findViewById(R.id.token5);

        PlayerModel mPlayerModel = mPlayerModels.get(position);
        text.setText(mPlayerModel.name);
        if (mPlayerModel.url != null) {
            mImageFetcher.loadImage(mPlayerModel.url, image);
        } else {
            image.setImageResource(mPlayerModel.resId);
        }
        rightCount = mPlayerModel.records.size()
                - left_attachment.getChildCount()
                - right_attachment.getChildCount();
        leftCount = rightCount >= 5 ? 5 : rightCount;
        rightCount -= leftCount;

        for (int i = 0; i < leftCount; i++) {
            addBadgeToAttachment(i, mPlayerModel, left_attachment);
        }
        for (int i = 0; i < rightCount; i++) {
            addBadgeToAttachment(i + 5, mPlayerModel, right_attachment);
        }

        Item item = getItem(position);
        image.setImageResource(item.resId);
        text.setText(item.text);
        if (item.isRevealed()) text.setTextColor(Color.RED);

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


