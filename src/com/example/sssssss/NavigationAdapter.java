package com.example.sssssss;

import java.util.ArrayList;
import java.util.List;

import notactivity.ItemNavigation;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationAdapter extends ArrayAdapter<ItemNavigation> {

	Context context;
	ArrayList<ItemNavigation> listItem;
	
	public NavigationAdapter(Context context, int resource, ArrayList<ItemNavigation> listItem) {
		super(context, R.layout.activity_navigation_adapter, listItem);
		this.context = context;
		this.listItem = listItem;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		View row = inflater.inflate(R.layout.activity_navigation_adapter, null);
		ImageView icon = (ImageView)row.findViewById(R.id.icon);
		TextView label = (TextView)row.findViewById(R.id.label);
		
		ItemNavigation item = getItem(position);
		icon.setImageResource(item.get_icon());
		label.setText(item.get_label());
		
		return (row);
	}
	
	


}
