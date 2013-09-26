package ch.mobileking.classes.override;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ch.mobileking.R;
import ch.mobileking.utils.Products;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
	
	private ArrayList<Products> items;
	
	private ArrayList<Bitmap> bitmArray; 
	
	private ImageView imageView;
	
	private int position;
	
	public ImageAdapter(Context c, ArrayList<Products> items)
	{
		mContext = c;
		this.items = new ArrayList<Products>(items);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.position = position;
		imageView = new ImageView(mContext);
		
		View gridView;
		
		if(convertView == null)
		{
			gridView = new View(mContext);
			
			gridView = inflater.inflate(R.layout.grid_item, null);
		}
		else
		{
			gridView = (View)convertView;
		}
		
		TextView txtProdName = (TextView) gridView.findViewById(R.id.prod_item_name);
		txtProdName.setText(""+items.get(position).getName());
		
		TextView txtProdProducer = (TextView) gridView.findViewById(R.id.prod_item_producer);
		txtProdProducer.setText(""+items.get(position).getProducer());
		
		TextView txtProdRanking = (TextView) gridView.findViewById(R.id.prod_item_ranking);
		txtProdRanking.setText("Rang #"+items.get(position).getPoints()+" (vorher 50)");
		
		ImageView prodItemCrown = (ImageView) gridView.findViewById(R.id.prod_item_crown);
		prodItemCrown.setImageResource(R.drawable.crown_gold);
		
		
//		ImageView prodItemProd = (ImageView) gridView.findViewById(R.id.prod_item_product);

		
//		new DownloadImageTask().execute(items.get(position).getImagelink());
//		imageView.setImageResource(R.drawable.zweifelpaprika);
		Bitmap image = null;
		try {
			image = BitmapFactory.decodeStream((InputStream)new URL(items.get(position).getImagelink()).getContent());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		prodItemCrown.setImageBitmap(image);
		
		prodItemCrown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("Clicked on item! ");
			}
		});

		
		return gridView;

		//		imageView.setImageBitmap(image);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setLayoutParams(new GridView.LayoutParams(120, 70));
//        return imageView;
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
