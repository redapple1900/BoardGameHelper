package com.yuanwei.resistance.listviewtest;


import java.util.ArrayList;
import java.util.List;

import com.yuanwei.resistance.playerdatabase.Player;

import game.redapple1900.resistance.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	static class ViewHolder {
		  TextView name;
		  TextView description;
		  TextView date;
		}
  private final Context context;
  private List<Player> mItems = new ArrayList<Player>();
   public ListAdapter(Context context) {
       //测试数据
	   /*
       for (int i = 0; i < 50; i++) {
           Item object = new Item();
           object.text = "Text "+i;
           object.resId = R.drawable.ic_launcher;
           mItems.add(object);
       }
       */
       this.context = context;
   }
   public ListAdapter(Context context,List<Player> list) {
       //测试数据
	   /*
       for (int i = 0; i < 50; i++) {
           Item object = new Item();
           object.text = "Text "+i;
           object.resId = R.drawable.ic_launcher;
           mItems.add(object);
       }
       */
       this.context = context;
       this.mItems=list;
   }
   public void addPlayer(Player item){
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
   public void replaceItem(Player item, int position){
	   if (this.getCount()>position){
		    mItems.remove(position);
	   }
		mItems.add(position, item);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
	  
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.listitem, parent, false);
    ViewHolder holder = new ViewHolder();
    holder.name = (TextView) rowView.findViewById(R.id.name);
    holder.description=(TextView)rowView.findViewById(R.id.description);
    holder.date=(TextView)rowView.findViewById(R.id.date);
    //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
    Player player = (Player) getItem(position);
    holder.name.setText(player.getName());
    holder.description.setText(context.getString(R.string.listitem_descr1) +player.getWin()+context.getString(R.string.listitem_descr2)+(player.getWin()+player.getLose())+context.getString(R.string.listitem_descr3));
    holder.date.setText(player.getLastDate());
    //imageView.setImageResource(R.drawable.index);
    rowView.setTag(holder);
    return rowView;
  }

@Override
public int getCount() {
	// TODO Auto-generated method stub
	
	return mItems.size();
}

@Override
public Object getItem(int arg0) {
	// TODO Auto-generated method stub
	 return mItems.get(arg0);
}

@Override
public long getItemId(int arg0) {
	// TODO Auto-generated method stub
	return arg0;
}

} 
