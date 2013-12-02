package ch.mobileking.classes.override;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ch.mobileking.R;
import ch.mobileking.utils.Crown;
import ch.mobileking.utils.Leaderboard;
import ch.mobileking.utils.Location;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;
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
	
	private ArrayList<Object> items;
	
	private ArrayList<Bitmap> bitmArray; 
	
	private int layoutId;
	
	private ImageView imageView;
	
	private int position;
	
	private SharedPrefEditor editor;
	
	public ImageAdapter(Context c, ArrayList<Object> items, int layoutId)
	{
		System.out.println("Got ArrayList of items: " + items);
		mContext = c;
		this.items = new ArrayList<Object>(items);
		this.layoutId = layoutId;
		editor = new SharedPrefEditor(c);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
//		System.out.println("Get View from ImageAdapter");
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.position = position;
		imageView = new ImageView(mContext);
		
		View gridView;
		
		if(convertView == null)
		{
			gridView = new View(mContext);
			gridView = inflater.inflate(this.layoutId, parent, false);
		}
		else
		{
			gridView = (View)convertView;
		}
		/** 
		 * Layout items for CROWN DETAIL list view items
		**/
		if(this.layoutId == R.layout.crown_detail_item)
		{
			TextView txtSalesPoint = (TextView) gridView.findViewById(R.id.badges_item_salespoint);
			txtSalesPoint.setText(""+((Crown) items.get(position)).getSalespoint());
			
			TextView txtRank = (TextView) gridView.findViewById(R.id.badges_item_rank);
			txtRank.setText("Rang "+((Crown) items.get(position)).getRank());
			
			ImageView prodItemCrown = (ImageView) gridView.findViewById(R.id.badges_item_crown);
			switch(((Crown) items.get(position)).getCrownstatus())
			{
			case 1:
				prodItemCrown.setImageResource(R.drawable.ic_krone_gold);
				break;
			case 2:
				prodItemCrown.setImageResource(R.drawable.ic_krone_silber);
				break;
			case 3:
				prodItemCrown.setImageResource(R.drawable.ic_krone_bronze);
				break;
			default:
				prodItemCrown.setImageResource(R.drawable.ic_krone_inactive);
			}
		}
		/** 
		 * Layout items for RECOMMENDATION ITEM list view items
		**/
		else if(this.layoutId == R.layout.recommend_item)
		{
//			TextView txtProduct = (TextView) gridView.findViewById(R.id.prod_item_name);
//			txtProduct.setText(""+((Products) items.get(position)).getName());
			
			Products prod = (Products)items.get(position);
			
			ImageView recommItem = (ImageView) gridView.findViewById(R.id.recomm_item_pict);
			if(Utils.imageExists(prod))
				recommItem.setImageBitmap(Utils.loadImageFromPath(prod));
		}
		/** 
		 * Layout items for STORE HERO list view items
		**/
		else if(this.layoutId == R.layout.activity_storehero_item)
		{
			Location loc = ((Location) items.get(position));
			
			TextView storehero_item_location = (TextView) gridView.findViewById(R.id.storehero_item_location);
			storehero_item_location.setText(""+loc.getName());
			
			ImageView storehero_item_store_img = (ImageView) gridView.findViewById(R.id.storehero_item_store_img);
			if(loc.getName().toLowerCase().contains("coop"))
				storehero_item_store_img.setImageResource(R.drawable.ic_logo_coop);
			if(loc.getName().toLowerCase().contains("migros"))
				storehero_item_store_img.setImageResource(R.drawable.ic_logo_migros);
			
			List<Crown> crownList = ProductKing.getCrowns(loc.getName());
			System.out.println("CrownListSize: " + loc.getName() +", size: " + crownList.size());

			int gold = 0, silver = 0, bronce = 0;
			
			gold = countCrowns(crownList, 1);
			silver = countCrowns(crownList, 2);
			bronce = countCrowns(crownList, 3);

			TextView storehero_item_crown_gold_cnt = (TextView) gridView.findViewById(R.id.storehero_item_crown_gold_cnt);
			storehero_item_crown_gold_cnt.setText("x "+gold);
			
			TextView storehero_item_crown_silver_cnt = (TextView) gridView.findViewById(R.id.storehero_item_crown_silver_cnt);
			storehero_item_crown_silver_cnt.setText("x "+silver);
			
			TextView storehero_item_crown_cronce_cnt = (TextView) gridView.findViewById(R.id.storehero_item_crown_bronce_cnt);
			storehero_item_crown_cronce_cnt.setText("x "+bronce);
			

			
		}
		/** 
		 * Layout items for LEADERBOARD list view items
		**/
		else if(this.layoutId == R.layout.activity_leaderboard_item)
		{
			TextView leaderboard_item_pts_txt = (TextView) gridView.findViewById(R.id.leaderboard_item_pts_txt);
			leaderboard_item_pts_txt.setText(((Leaderboard) items.get(position)).getPoints()+" Pkt.");
			
			TextView leaderboard_item_user = (TextView) gridView.findViewById(R.id.leaderboard_item_user);
			leaderboard_item_user.setText(""+((Leaderboard) items.get(position)).getUsername());

			TextView leaderboard_item_rank_txt = (TextView) gridView.findViewById(R.id.leaderboard_item_rank_txt);
			leaderboard_item_rank_txt.setText(""+((Leaderboard) items.get(position)).getRank());
			
//			ImageView leaderboard_item_user_img = (ImageView) gridView.findViewById(R.id.leaderboard_item_user_img);
			
			if (((Leaderboard) items.get(position)).getUsername().toLowerCase().contentEquals(editor.getUsername().toLowerCase()))
			{
//				leaderboard_item_user.setTextColor(mContext.getResources().getColor(R.color.red_light));
				
//				gridView.setBackgroundColor(mContext.getResources().getColor(R.color.light_green));
				
//				leaderboard_item_user_img.setImageResource(R.drawable.ic_launcher);
				
				leaderboard_item_rank_txt.setTextColor(mContext.getResources().getColor(android.R.color.black));
			}

			ImageView storehero_item_crown = (ImageView) gridView.findViewById(R.id.leaderboard_item_user_img);
			
			if( ((Leaderboard) items.get(position)).getRank() == 1 )
			{
				storehero_item_crown.setImageResource(R.drawable.ic_leaderboard_gold);
			}
			else if( ((Leaderboard) items.get(position)).getRank() == 2 )
			{
				storehero_item_crown.setImageResource(R.drawable.ic_leaderboard_silver);
			}
			else if( ((Leaderboard) items.get(position)).getRank() == 3 )
			{
				storehero_item_crown.setImageResource(R.drawable.ic_leaderboard_bronce);
			}
			else
			{
				storehero_item_crown.setImageResource(R.drawable.empty);
			}
				
			
		}
		
		return gridView;
	}
	
	public int countCrowns(List<Crown> crowns, int color)
	{
		int gold=0, silver=0, bronze=0;
		
		if(crowns!=null)
		{
			for (Crown crown : crowns)
			{
				switch(crown.getCrownstatus())
				{
				case 1:
					gold+=1;
					break;
				case 2:
					silver+=1;
					break;
				case 3:
					bronze+=1;
					break;
				}
			}
			switch(color)
			{
			case 1:
				return gold;
			case 2:
				return silver;
			case 3:
				return bronze;
			default:
				return 0;
			}
		}
		else
		{
			return 0;
		}
		
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
