package game.redapple1900.gridviewtest;


import java.util.ArrayList;
import java.util.List;

import game.redapple1900.resistance.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TextGridAdapter extends BaseAdapter{
   public static class Item{
       public String text1;
       public String text2;
   }

   private List<Item> mItems = new ArrayList<TextGridAdapter.Item>();
   private Context mContext;
   public TextGridAdapter(Context context) {
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
           convertView = LayoutInflater.from(mContext).inflate(R.layout.textitem, null);
       }
       TextView text1 = (TextView) convertView.findViewById(R.id.textitem1);
       TextView text2 = (TextView) convertView.findViewById(R.id.textitem2);
       Item item = (Item) getItem(position);
       text1.setText(item.text1);
       text2.setText(item.text2);
       return convertView;
   }
}