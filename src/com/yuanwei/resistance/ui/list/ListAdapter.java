package com.yuanwei.resistance.ui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yuanwei.resistance.R;
import com.yuanwei.resistance.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {
	private class ViewHolder {
		TextView name;
		TextView description;
		TextView date;
	}

	private final Context context;
	private List<Player> mItems = new ArrayList<Player>();


	public ListAdapter(Context context, List<Player> list) {

		this.context = context;
		this.mItems = list;
	}

    public ListAdapter(Context context) {
        this(context, null);
    }

    public void setDataSource (List<Player> list) {
        this.mItems = list;
    }

	public void addPlayer(Player item) {
		mItems.add(item);
	}

	public void removeAllItems() {
		while (this.getCount() > 0) {
			mItems.remove(this.getCount() - 1);
		}
	}

	public void removeItem(int position) {
		// TODO Auto-generated method stub
		if (this.getCount() > 0) {
			mItems.remove(position);
		}

	}

	public void replaceItem(Player item, int position) {
		if (this.getCount() > position) {
			mItems.remove(position);
		}
		mItems.add(position, item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listitem, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.description = (TextView) convertView
					.findViewById(R.id.description);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		Player player = (Player) getItem(position);
		holder.name.setText(player.getName());
		holder.description.setText(context.getString(R.string.listitem_win1)
				+ player.getWin() + context.getString(R.string.listitem_total)
				+ (player.getWin() + player.getLose()));
		holder.date.setText(player.getLastDate());
		// imageView.setImageResource(R.drawable.index);

		return convertView;
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
