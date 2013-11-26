package ch.mobileking.classes.override;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import ch.mobileking.CrownDetailOverview;
import ch.mobileking.ProductDetailOverview;
import ch.mobileking.R;
import ch.mobileking.login.AsyncUpdate;
import ch.mobileking.utils.Crown;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductBaseAdapter extends BaseAdapter{
	
	private static ArrayList<Products> resultList;
	private ArrayList<Products> originalValues;
	
	private LayoutInflater mInflater;
	private ImageLoader imageLoader;
	
	private ViewHolder holder;
	private int prodLayoutResourceId;
	
	private DisplayImageOptions options;
	private Context cont;
	
	private SharedPrefEditor editor;
	
	public ProductBaseAdapter(Context context, int prodLayoutResourceId, ArrayList<Products> items)
	{
		resultList = items;
		setProdLayoutResourceId(prodLayoutResourceId);
		setCont(context);
		mInflater = LayoutInflater.from(context);

		/** IMAGE LOADER */
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,"/cache");

		// Get singletone instance of ImageLoader
		imageLoader = ImageLoader.getInstance();
		// Create configuration for ImageLoader (all options are optional)
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				// You can pass your own memory cache implementation
				.discCache(new UnlimitedDiscCache(cacheDir))
				// You can pass your own disc cache implementation
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.build();
		// Initialize ImageLoader with created configuration. Do it once.
		imageLoader.init(config);

		options = new DisplayImageOptions.Builder()
//				.showStubImage(R.drawable.empty)// display stub image
				.cacheInMemory(true)
		        .cacheOnDisc(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int result = 0;
		if(resultList!=null)
			result = resultList.size();
		return result;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return resultList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
				
		//Initialize convertView
		if(convertView==null)
		{
			convertView = mInflater.inflate(getProdLayoutResourceId(), parent, false); //R.layout.product_item
			
			holder = new ViewHolder();
			holder.setTxtName((TextView)convertView.findViewById(R.id.prod_item_name));
			holder.setTxtProducer((TextView)convertView.findViewById(R.id.prod_item_producer));
			holder.setTxtRank((TextView)convertView.findViewById(R.id.prod_rank));
			holder.setTxtCollectedCnt((TextView)convertView.findViewById(R.id.prod_collected_cnt));
			holder.setImgView((ImageView)convertView.findViewById(R.id.list_image));
			holder.setCrown1((ImageView)convertView.findViewById(R.id.prod_item_crown));

			holder.setChkBox((CheckBox)convertView.findViewById(R.id.prod_item_checkbox));

	        if(holder.getChkBox() != null)
			{
				
				holder.getChkBox().setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						int getPosition = (Integer) buttonView.getTag();
						if(resultList.get(getPosition).getIsdeleted() && isChecked)
						{
							System.out.println("Nothing to update!");
						}
						else
						{
							System.out.println("Checked item at Position: " + getPosition + " " + resultList.get(getPosition).getName() + " set to Favorite " + isChecked);
							resultList.get(getPosition).setIsdeleted(isChecked);
							resultList.get(getPosition).setOptin(isChecked);
						}
						ProductKing.setStaticProducts(resultList);

					}
				});
			}
	        
//	        holder.setCrownLayout((LinearLayout)convertView.findViewById(R.id.linearLayoutCrown));
	        
//	        holder.getCrownLayout().setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					int getPosition = (Integer) v.getTag();			        
//					Products prod = resultList.get(getPosition);
//
//					System.out.println("Position: " + getPosition + ", Crown clicked for item " + resultList.get(getPosition).getId() + resultList.get(getPosition).getName());
//			        Intent intent = new Intent(getCont(), CrownDetailOverview.class);
//			        intent.putExtra("product", String.valueOf(getPosition));
//
//			        ((Activity)getCont()).setResult(1, intent);
//			        ((Activity)getCont()).startActivityForResult(intent, 1);
//					
//				}
//			});
			
			convertView.setTag(holder);
			convertView.setTag(R.id.prod_item_name, holder.txtName);
			convertView.setTag(R.id.prod_item_checkbox, holder.chkBox);
		}
		
		else
			
		{
			holder = (ViewHolder)convertView.getTag();

		}
		
		/** SET INFORMATION  **/
		if(holder.getChkBox() != null)
		{
			holder.getChkBox().setTag(position);
			holder.getChkBox().setChecked(resultList.get(position).getIsdeleted());
		}
		
		/*
		 *  SET THE VALUES!
		 * 
		 */
		Products prod = resultList.get(position);
//		if(resultList.get(position).getOptin())
//		{
			holder.getTxtName().setText(""+prod.getName());
			holder.getTxtProducer().setText(""+prod.getProducer());
			if(prod.getOlduserrank()-prod.getUserrank()>0 && prod.getOlduserrank()!=0)
			{
				holder.getTxtRank().setText("MEIN RANG "+prod.getUserrank() + " (+"+(prod.getOlduserrank()-prod.getUserrank())+")");
				holder.getTxtRank().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_green, 0);
			}
			else if(prod.getOlduserrank()-prod.getUserrank()<0 && prod.getOlduserrank()!=0)
			{
				holder.getTxtRank().setText("MEIN RANG "+prod.getUserrank() + " ("+(prod.getOlduserrank()-prod.getUserrank())+")");
				holder.getTxtRank().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down_red, 0);
			}
			else if(prod.getOlduserrank()==0 && prod.getUserrank()==1)
			{
				holder.getTxtRank().setText("MEIN RANG "+prod.getUserrank() + " (+-0)");
				holder.getTxtRank().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_green, 0);
			}
			else
			{
				holder.getTxtRank().setText("MEIN RANG "+prod.getUserrank() + " (+-0)");
				holder.getTxtRank().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_nochange, 0);
			}
			holder.getTxtCollectedCnt().setText(prod.getPoints()+" Pkte.");
			
			if(prod.getUserrank() == 1)
			{
				holder.getCrown1().setImageResource(R.drawable.ic_krone_gold);
			}
			if(prod.getUserrank() == 2)
			{
				holder.getCrown1().setImageResource(R.drawable.ic_krone_silber);
			}
			if(prod.getUserrank() == 3)
			{
				holder.getCrown1().setImageResource(R.drawable.ic_krone_bronze);
			}
				
			/** SET THE POSITION TO REFERENCE FROM LATER **/
				
//			holder.getCrownLayout().setTag(position);
		
//		}
//		else
//		{
//			holder.getTxtName().setText(" ");
//			holder.getTxtProducer().setText("Scan product to join!");
//			holder.getTxtRank().setText(" ");
//		}
		
//		Products prod = resultList.get(position);
		
		String imageName = prod.getId()+".png";
		String imagePath = "";
		Bitmap image = null;
		
		if(Utils.imageExists(prod))
		{
			image = Utils.loadImageFromPath(prod);
			imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages/"+imageName;
		}
		else
		{
			try {
				image = BitmapFactory.decodeStream((InputStream)new URL(prod.getImagelink()).getContent());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			imagePath = saveBitmap(image, imageName);
			
		}
		//Image Stuff
		resultList.get(position).setImagepath(imagePath);
		
//		prodItemPict.setImageBitmap(image);
		
		holder.getImgView().setImageBitmap(image);
		
//		String imageUri = resultList.get(position).getImagelink();
//		holder.getImgView().setTag(imageUri); //Add this line
//		imageLoader.displayImage(imageUri, holder.getImgView());
		
//		holder.getImgView().setImageDrawable(drawable);
		ProductKing.setStaticProducts(resultList);
			
		return convertView;
	}
	
	private String saveBitmap(Bitmap bmp, String imageName)
	{
	    String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages";
        File dir = new File(file_path);
        if(!dir.exists())
           dir.mkdirs();
        File file = new File(dir, imageName);
        FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
			fOut.flush();
	        fOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    System.out.println("Saved Image to SD card: " +file.getAbsolutePath());
	    return file.getAbsolutePath();
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
	
    public void saveFavorites()
    {
    	editor = new SharedPrefEditor(cont);
    	/** SAVE OPT-IN PRODUCTS TO SHARED PREF!! AND SYNC TO BACKEND!! */
//    	editor.saveCurrentFavList();    	
    }

	/**
	 * @return the cont
	 */
	public Context getCont() {
		return cont;
	}

	/**
	 * @param cont the cont to set
	 */
	public void setCont(Context cont) {
		this.cont = cont;
	}


	/**
	 * @return the prodLayoutResourceId
	 */
	public int getProdLayoutResourceId() {
		return prodLayoutResourceId;
	}

	/**
	 * @param prodLayoutResourceId the prodLayoutResourceId to set
	 */
	public void setProdLayoutResourceId(int textLayoutResourceId) {
		this.prodLayoutResourceId = textLayoutResourceId;
	}

	/**
	 * @return the holder
	 */
	public ViewHolder getHolder() {
		return holder;
	}

	/**
	 * @param holder the holder to set
	 */
	public void setHolder(ViewHolder holder) {
		this.holder = holder;
	}


	/* STATIC VIEW HOLDER CLASS FOR REUSE => SEE VIEWHOLDER PATTERN */
	static class ViewHolder 
	{
		private TextView txtName;
		private TextView txtProducer;
		private TextView txtRank, txtCollectedCnt;
		private TextView txtCrownCnt1, txtCrownCnt2, txtCrownCnt3;
		private CheckBox chkBox;
		private ImageView imgView;
		private ImageView crown1, crown2, crown3;
		private LinearLayout crownLayout;
		/**
		 * @return the txtCategory
		 */
		public TextView getTxtName() {
			return txtName;
		}

		public void setCrownLayout(LinearLayout crownLayoutId) {
			// TODO Auto-generated method stub
			this.crownLayout = crownLayoutId; 
		}
		
		public LinearLayout getCrownLayout()
		{
			return this.crownLayout;
		}

		/**
		 * @param txtName the txtName to set
		 */
		public void setTxtName(TextView txtName) {
			this.txtName = txtName;
		}
		/**
		 * @return the txtDueDate
		 */
		public TextView getTxtRank() {
			return txtRank;
		}
		/**
		 * @param txtDueDate the txtDueDate to set
		 */
		public void setTxtRank(TextView txtRank) {
			this.txtRank = txtRank;
		}
		/**
		 * @return the txtCollectedCnt
		 */
		public TextView getTxtCollectedCnt() {
			return txtCollectedCnt;
		}

		/**
		 * @param txtCollectedCnt the txtCollectedCnt to set
		 */
		public void setTxtCollectedCnt(TextView txtCollectedCnt) {
			this.txtCollectedCnt = txtCollectedCnt;
		}

		/**
		 * @return the txtDescription
		 */
		public TextView getTxtProducer() {
			return txtProducer;
		}
		/**
		 * @param txtDescription the txtDescription to set
		 */
		public void setTxtProducer(TextView txtProducer) {
			this.txtProducer = txtProducer;
		}
		/**
		 * @return the txtCrownCnt
		 */
		public TextView getTxtCrownCnt1() {
			return txtCrownCnt1;
		}

		/**
		 * @param txtCrownCnt the txtCrownCnt to set
		 */
		public void setTxtCrownCnt1(TextView txtCrownCnt) {
			this.txtCrownCnt1 = txtCrownCnt;
		}

		/**
		 * @return the txtCrownCnt2
		 */
		public TextView getTxtCrownCnt2() {
			return txtCrownCnt2;
		}

		/**
		 * @param txtCrownCnt2 the txtCrownCnt2 to set
		 */
		public void setTxtCrownCnt2(TextView txtCrownCnt2) {
			this.txtCrownCnt2 = txtCrownCnt2;
		}

		/**
		 * @return the txtCrownCnt3
		 */
		public TextView getTxtCrownCnt3() {
			return txtCrownCnt3;
		}

		/**
		 * @param txtCrownCnt3 the txtCrownCnt3 to set
		 */
		public void setTxtCrownCnt3(TextView txtCrownCnt3) {
			this.txtCrownCnt3 = txtCrownCnt3;
		}

		/**
		 * @return the chkBox
		 */
		public CheckBox getChkBox() {
			return chkBox;
		}
		/**
		 * @param chkBox the chkBox to set
		 */
		public void setChkBox(CheckBox chkBox) {
			this.chkBox = chkBox;
		}

		/**
		 * @return the imgView
		 */
		public ImageView getImgView() {
			return imgView;
		}

		/**
		 * @param imgView the imgView to set
		 */
		public void setImgView(ImageView imgView) {
			this.imgView = imgView;
		}

		/**
		 * @return the crown1
		 */
		public ImageView getCrown1() {
			return crown1;
		}

		/**
		 * @param crown1 the crown1 to set
		 */
		public void setCrown1(ImageView crown1) {
			this.crown1 = crown1;
		}

		/**
		 * @return the crown2
		 */
		public ImageView getCrown2() {
			return crown2;
		}

		/**
		 * @param crown2 the crown2 to set
		 */
		public void setCrown2(ImageView crown2) {
			this.crown2 = crown2;
		}

		/**
		 * @return the crown3
		 */
		public ImageView getCrown3() {
			return crown3;
		}

		/**
		 * @param crown3 the crown3 to set
		 */
		public void setCrown3(ImageView crown3) {
			this.crown3 = crown3;
		}
		
	}


	public void setData(List<Products> staticProducts) {
		// TODO Auto-generated method stub
		System.out.println("DatasetChanged!");
		ProductBaseAdapter.resultList = (ArrayList<Products>) staticProducts;
	}
	
	
}
