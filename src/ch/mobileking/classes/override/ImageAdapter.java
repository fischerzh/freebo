package ch.mobileking.classes.override;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import ch.mobileking.R;
import ch.mobileking.utils.Crown;
import ch.mobileking.utils.Products;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * http://www.androidhive.info/2012/02/android-gridview-layout-tutorial/
 * 
 * http://developer.android.com/guide/topics/ui/layout/gridview.html
 * 
 * Height for GridView: http://stackoverflow.com/questions/16819135/set-fixed-gridview-row-height
 * 
 * @author marcofischer
 *
 */

public class ImageAdapter extends BaseAdapter{
	
	private Context mContext;
	
	private ArrayList<Crown> items;
	
	private ArrayList<Bitmap> bitmArray; 
	
	private ImageView imageView;
	
	private int position;
	
	public ImageAdapter(Context c, ArrayList<Crown> items)
	{
		System.out.println("Got Crowns: " + items);
		mContext = c;
		this.items = new ArrayList<Crown>(items);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		System.out.println("Get View from ImageAdapter");
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.position = position;
		imageView = new ImageView(mContext);
		
		View gridView;
		
		if(convertView == null)
		{
			gridView = new View(mContext);
			
			gridView = inflater.inflate(R.layout.badges_detail_item, parent, false);
		}
		else
		{
			gridView = (View)convertView;
		}
		
		TextView txtSalesPoint = (TextView) gridView.findViewById(R.id.badges_item_salespoint);
		txtSalesPoint.setText(""+items.get(position).getSalespoint());
		
		TextView txtRank = (TextView) gridView.findViewById(R.id.badges_item_rank);
		txtRank.setText("Rang "+items.get(position).getRank());
		
		ImageView prodItemCrown = (ImageView) gridView.findViewById(R.id.badges_item_crown);
		switch(items.get(position).getCrownstatus())
		{
		case 1:
			prodItemCrown.setImageResource(R.drawable.crown_gold);
			break;
		case 2:
			prodItemCrown.setImageResource(R.drawable.crown_silver);
			break;
		case 3:
			prodItemCrown.setImageResource(R.drawable.crown_bronce);
			break;
		default:
			prodItemCrown.setImageResource(R.drawable.crown_none);
		}
		
		return gridView;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void onClick(View v) {
	    
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	     protected Bitmap doInBackground(String... urls) {
	         return loadImageFromNetwork(urls[0]);
	     }

	     protected void onPostExecute(Bitmap result) {
	         //Do something with bitmap eg:
	    	 System.out.println("Got Image: " +result.toString());
	    	 bitmArray.add(position, result);
//	    	 imageView.setImageBitmap(result);
	     }
	 }

	 private Bitmap loadImageFromNetwork(String url){
		 Bitmap bitmap = null;
		 try {
			 System.out.println("Downloading Image from Link: " + url);
	          bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
	      } catch (Exception e) {
	          e.printStackTrace();
	    }
		return bitmap;
	 }

}
