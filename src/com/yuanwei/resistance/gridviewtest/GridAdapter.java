package com.yuanwei.resistance.gridviewtest;

import com.yuanwei.resistance.R;
import java.util.LinkedList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class GridAdapter extends BaseAdapter{
static final int MaxRounds=5;
   public static class Item{
	   public String text,text_top;
	   public boolean selection;
	   private boolean isRevealed;
       public int resId;
       public Token token;
       public Item(String text,int resId,Token token,boolean selection){
    	   this.text=text;
    	   this.resId=resId;
    	   this.token=token;
    	   this.selection=selection;
    	   this.setRevealed(false);
       }
       public Item(String text,int resId,Token token){
    	   this.text=text;
    	   this.resId=resId;
    	   this.token=token;
    	   this.selection=false;
    	   this.setRevealed(false);
       }
	   public Item(String text,int resId){
		   this.text=text;
		   this.resId=resId;
		   this.selection=false;
		   this.token= new GridAdapter.Token(MaxRounds);//5 is the max rounds
		   this.setRevealed(false);
	   }
	   public Item(){
    	   this.text_top=null;
		  this.text=null;
		  this.resId=0;
		  this.token= new GridAdapter.Token(MaxRounds);
		  this.setRevealed(false);
	   }
	public boolean isRevealed() {
		return isRevealed;
	}
	public void setRevealed(boolean isRevealed) {
		this.isRevealed = isRevealed;
	}
   }
   public static class Token{
	   public static final int WIN=1;
	   public static final int LOSE=-1;
	   public static final int NEUTRAL=0;
	   public int mission;
	   public int total;
	   public int[] missionResult;
	   public int resId_win,resId_lose,resId_neutral,resId_sabotage;
	   public Token(int total){
		   this.mission=0;
		   this.total=total;
		   this.resId_neutral=R.drawable.token_neutral;
		   this.resId_lose=R.drawable.token_fail;
		   this.resId_win=R.drawable.token_win;
		   this.resId_sabotage=R.drawable.token_sabotage;
		   this.missionResult=new int[this.total];
		   for (int i=0;i<this.total;i++){
			   this.missionResult[i]=this.resId_neutral;
		   }
	   }
	   public Token(int total,int mission,int[] missionResult){
		   this.mission=mission;
		   this.total=total;
		   this.resId_neutral=R.drawable.token_neutral;
		   this.resId_lose=R.drawable.token_fail;
		   this.resId_win=R.drawable.token_win;
		   this.resId_sabotage=R.drawable.token_sabotage;
		   this.missionResult=missionResult;
	   }
	   public Token(int total,int mission,int status){
		   this.mission=mission;
		   this.total=total;
		   this.resId_neutral=R.drawable.token_neutral;
		   this.resId_lose=R.drawable.token_fail;
		   this.resId_win=R.drawable.token_win;
		   this.resId_sabotage=R.drawable.token_sabotage;
		   if (status==Token.WIN){
			this.missionResult[mission-1]=this.resId_win;   
		   }else if (status==Token.LOSE){
			this.missionResult[mission-1]=this.resId_lose;
		   }else if (status==Token.NEUTRAL);
		   
	   }
   }
   private List<Item> mItems = new LinkedList<GridAdapter.Item>();
  // private List<Token> mTokens =new ArrayList<GridAdapter.Token>();
   private Context mContext;
   public GridAdapter(Context context) {

       mContext = context;
   }
   public void addItem(Item item){
	   mItems.add(item);
   }
   public void removeAllItems(){
	   while (this.getCount()>0){
		   mItems.remove(this.getCount()-1);
	   }
   }
   public void removeItem(int position) {
		// TODO Auto-generated method stub
		if (this.getCount()>0){
			mItems.remove(position);
		}
	}
   public void replaceItem(Item item, int position){
	   if (this.getCount()>position){
		    mItems.remove(position);
	   }
		mItems.add(position, item);
  }
  public void addToken(Item item,int position,int status){
	   if (this.getCount()>position){
		    mItems.remove(position);
	   }
	   int mission =item.token.mission+1;
	   int[] missionResult=item.token.missionResult;
	   if (status==Token.WIN){
		   missionResult[item.token.mission]=item.token.resId_win;
	   }else if (status==Token.LOSE){
		   missionResult[item.token.mission]=item.token.resId_lose;
	   }
	   GridAdapter.Token token= new GridAdapter.Token(MaxRounds, mission, missionResult);
	   GridAdapter.Item mItem =new GridAdapter.Item(item.text, item.resId, token);
	  // mItem.text_top=item.text_top;
	   mItem.selection=item.selection;
		mItems.add(position, mItem);
  }
  public void setToken(Item item,int position,int round){
	   if (this.getCount()>position){
		    mItems.remove(position);
	   }
	   int[] missionResult=item.token.missionResult;
		   missionResult[round]=item.token.resId_sabotage;
	   GridAdapter.Token token= new GridAdapter.Token(MaxRounds, item.token.mission, missionResult);
	   GridAdapter.Item mItem =new GridAdapter.Item(item.text, item.resId, token);
	   //mItem.text_top=item.text_top;
	   mItem.selection=item.selection;
		mItems.add(position, mItem);
 }
  /*
  public void changeSelection(Item item,int position){
	   if (this.getCount()>position){
		    mItems.remove(position);
	   }
	   GridAdapter.Item new_item=item;
	   new_item.selection=!item.selection;
	   mItems.add(position,new_item);
  }
  public void deSelection(Item item,int position){
	   if (this.getCount()>position){
		    mItems.remove(position);
	   }GridAdapter.Item new_item=item;
	   new_item.selection=false;
	   mItems.add(position,new_item);
 }
 */
 public void setLeader(Item item,int position){
	   if (this.getCount()>position){
		    mItems.remove(position);
	   }
	   GridAdapter.Item new_item=item;
	   new_item.selection=!item.selection;
	   mItems.add(position,new_item);
 }
 public void revealIdentity(int position){
	 mItems.get(position).setRevealed(true);
 }
   @Override
   public int getCount() {
       return mItems.size();
   }

   @Override
   public Object getItem(int position) {
       return mItems.get(position);
   }

   @Override
   public long getItemId(int position) {
       return position;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
       if(convertView == null) {
    	   //TODO
           convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null, false);
       }
       ImageView image = (ImageView) convertView.findViewById(R.id.icon);
       ImageView sele =(ImageView)convertView.findViewById(R.id.selection);
       ImageView[] token=new ImageView[5];
       token[0]=(ImageView) convertView.findViewById(R.id.token1);
       token[1]=(ImageView) convertView.findViewById(R.id.token2);
       token[2]=(ImageView) convertView.findViewById(R.id.token3);
       token[3]=(ImageView) convertView.findViewById(R.id.token4);
       token[4]=(ImageView) convertView.findViewById(R.id.token5);
       TextView text = (TextView) convertView.findViewById(R.id.text);
       TextView text_top=(TextView)convertView.findViewById(R.id.text_top);
       Item item = (Item) getItem(position);
       image.setImageResource(item.resId);
       text.setText(item.text);
       text.setTextSize(mContext.getResources().getDimension(R.dimen.small_text_gridview));
       if (item.isRevealed()) text.setTextColor(Color.RED);
       text_top.setText(item.text_top);
       text_top.setTextSize(mContext.getResources().getDimension(R.dimen.small_text_gridview));
       text_top.setVisibility(View.INVISIBLE);
       
       for(int i=0;i<MaxRounds;i++){
    	   token[i].setVisibility(View.INVISIBLE);
       }
       for(int i=0;i<item.token.mission;i++){
    	   if (item.token.missionResult[i]!=0){
    		   token[i].setImageResource(item.token.missionResult[i]);
    		   token[i].setVisibility(View.VISIBLE);
    	   }else token[i].setVisibility(View.INVISIBLE);
       }
       if (!item.selection) {
    	   sele.setVisibility(View.INVISIBLE);
       } else sele.setVisibility(View.VISIBLE);
       
       return convertView;
   }

}

