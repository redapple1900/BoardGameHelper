package com.yuanwei.resistance.gridviewtest;

import com.yuanwei.resistance.R;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class TableGridAdapter extends BaseAdapter{
   public static class Item{
       public String text;
       public int resId;
   }

   private List<Item> mItems = new ArrayList<TableGridAdapter.Item>();
   private Context mContext;
   public TableGridAdapter(Context context) {
       //≤‚ ‘ ˝æ›
	   /*
       for (int i = 0; i < 50; i++) {
           Item object = new Item();
           object.text = "Text "+i;
           object.resId = R.drawable.ic_launcher;
           mItems.add(object);
       }
       */
       mContext = context;
   }
   public void addItem(Item item){
	   mItems.add(item);
   }
   public void replaceItem(Item item, int position){
	   if (this.getItem(position)!=null){
		    mItems.remove(position);
	   }
		mItems.add(position, item);

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
           convertView = LayoutInflater.from(mContext).inflate(R.layout.tableitem, null);
       }
       ImageView image = (ImageView) convertView.findViewById(R.id.icon);
       TextView text = (TextView) convertView.findViewById(R.id.text);
       Item item = (Item) getItem(position);
       if (item.resId==R.drawable.blank||item.resId==R.drawable.waiting)
    	   image.setVisibility(View.INVISIBLE);
       else 
    	   image.setVisibility(View.VISIBLE);
       image.setImageResource(item.resId);
       text.setText(item.text);
       text.setGravity(Gravity.CENTER_HORIZONTAL);
       return convertView;
   }
}