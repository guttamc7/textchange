package com.textchange;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.content.Context;
import java.util.ArrayList;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;

public class UserListAdapter extends BaseAdapter {
	   private ArrayList<UserListItem> listData;
	   
	    private LayoutInflater layoutInflater;
	 
	    public UserListAdapter(Context context, ArrayList<UserListItem> listData) {
	        this.listData = listData;
	        layoutInflater = LayoutInflater.from(context);
	    }
	 
	    @Override
	    public int getCount() {
	        return listData.size();
	    }
	 
	    @Override
	    public Object getItem(int position) {
	        return listData.get(position);
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        return position;
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder holder;
	        if (convertView == null) {
	        	 convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
	             holder = new ViewHolder();
	             holder.titleView = (TextView) convertView.findViewById(R.id.title);
	             holder.authorView = (TextView) convertView.findViewById(R.id.author);
	             holder.priceView = (TextView) convertView.findViewById(R.id.price);
	             convertView.setTag(holder);
	         } else {
	             holder = (ViewHolder) convertView.getTag();
	         }
	  
	         holder.titleView.setText(listData.get(position).getbookTitle());
	         holder.authorView.setText(listData.get(position).getbookAuthor());
	         holder.priceView.setText(listData.get(position).getbookPrice());
	  
	         return convertView;
	    }
	 
	    static class ViewHolder {
	        TextView titleView;
	        TextView authorView;
	        TextView priceView;
	    }
	 
	}

