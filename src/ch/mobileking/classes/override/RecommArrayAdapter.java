package ch.mobileking.classes.override;

import java.util.ArrayList;
import java.util.List;

import ch.mobileking.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecommArrayAdapter extends ArrayAdapter<String> {
	
	private ArrayList<String> objects;
	
	private Activity act;

	public RecommArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		
		this.act = (Activity) context;
		this.objects = (ArrayList<String>) objects;
		
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View rowView = convertView;
		
		if(rowView==null)
		{
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.simple_selectable_item, parent, false);
		}
		
		String i = objects.get(position);
		
		TextView tv = (TextView) rowView.findViewById(R.id.simple_select_item_txt);
		tv.setText(i);
		tv.setTypeface(null, Typeface.NORMAL);
//		tv.setBackgroundColor(act.getResources().getColor(android.R.color.holo_red_light));
		
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getItem(position);
				TextView tv = (TextView)v;
				System.out.println("Clicked on " + tv.toString());
				tv.setTypeface(null, Typeface.BOLD);
//				
//				tv.setBackgroundColor(act.getResources().getColor(android.R.color.holo_blue_light));
				
			}
		});
		
		return rowView;
	}

}
