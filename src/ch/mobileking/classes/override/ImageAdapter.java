package ch.mobileking.classes.override;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ch.mobileking.R;
import ch.mobileking.login.ServerRequest;
import ch.mobileking.utils.Location;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;
import ch.mobileking.utils.classes.Crown;
import ch.mobileking.utils.classes.Leaderboard;
import ch.mobileking.utils.classes.Products;
import ch.mobileking.utils.classes.SalesSlip;
import ch.mobileking.utils.classes.SalesVerified;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
	
	private ArrayList<SalesSlip> loadingArrayItemList; 
	
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
			if(Utils.imageExists(prod.getEan()))
				recommItem.setImageBitmap(Utils.loadImageFromPath(prod.getEan()));
		}
		/** 
		 * Layout items for SALES SLIP list view items
		**/
		else if(this.layoutId == R.layout.activity_salesslips_item)
		{
			SalesSlip salesslip = ((SalesSlip) items.get(position));
			TextView salesslip_item_location = (TextView) gridView.findViewById(R.id.salesslip_item_location);
			if(salesslip.getSalespoint()!=null && salesslip.getSalespoint()!="")
				salesslip_item_location.setText(salesslip.getSalespoint());
			
			TextView salesslip_item_date = (TextView) gridView.findViewById(R.id.salesslip_item_date);
			salesslip_item_date.setText(salesslip.getScanDate());
			
			ImageView salesslip_item_icon = (ImageView) gridView.findViewById(R.id.salesslip_item_icon);

			ImageView salesslip_image_verified = (ImageView) gridView.findViewById(R.id.salesslip_image_verified);
			TextView salesslip_item_verified_text = (TextView) gridView.findViewById(R.id.salesslip_item_verified_text);

			if(salesslip.getIsapproved() == 1)
			{
				salesslip_image_verified.setImageResource(R.drawable.ic_salesslip_inprogress);
				salesslip_item_verified_text.setText("IN BEARBEITUNG!");
			}
			if(salesslip.getIsapproved() == 0)
			{
				salesslip_image_verified.setImageResource(R.drawable.ic_salesslip_notok);
				salesslip_item_verified_text.setText("UNG†LTIG!");
			}
			if(salesslip.getIsapproved() == 2)
			{
				salesslip_image_verified.setImageResource(R.drawable.ic_salesslip_ok);
				salesslip_item_verified_text.setText("VERIFIZIERT!");
			}
			
			ProgressBar salesslip_progress = (ProgressBar) gridView.findViewById(R.id.salesslip_item_progress);
			if(salesslip.getIsuploaded())
			{
				salesslip_item_icon.setVisibility(View.VISIBLE);
				salesslip_progress.setVisibility(View.INVISIBLE);
			}
			else
			{
				salesslip_item_icon.setVisibility(View.INVISIBLE);
				salesslip_progress.setVisibility(View.VISIBLE);
			}

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



}
