package ch.mobileking.classes.override;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

import ch.mobileking.BadgesDetailOverview;
import ch.mobileking.ProductDetailOverview;
import ch.mobileking.R;
import ch.mobileking.login.AsyncUpdate;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import ch.mobileking.utils.SharedPrefEditor;

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

public class ProductBaseAdapter extends BaseAdapter implements Filterable{
	
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
		return resultList.size();
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
			holder.setChkBox((CheckBox)convertView.findViewById(R.id.prod_item_checkbox));
			holder.setImgView((ImageView)convertView.findViewById(R.id.list_image));
			holder.setCrown1((ImageView)convertView.findViewById(R.id.prod_item_crown));
			holder.setCrown2((ImageView)convertView.findViewById(R.id.prod_item_crown_2));
			holder.setCrown3((ImageView)convertView.findViewById(R.id.prod_item_crown_3));
	        holder.setTxtCrownCnt((TextView)convertView.findViewById(R.id.prod_crown_cnt));
	        holder.setCrownLayout((LinearLayout)convertView.findViewById(R.id.linearLayoutCrown));
	        
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
	        
	        holder.getCrownLayout().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int getPosition = (Integer) v.getTag();			        
					Products prod = resultList.get(getPosition);

					System.out.println("Position: " + getPosition + ", Crown clicked for item " + resultList.get(getPosition).getId() + resultList.get(getPosition).getName());
			        Intent intent = new Intent(getCont(), BadgesDetailOverview.class);
			        intent.putExtra("product", String.valueOf(getPosition));

			        ((Activity)getCont()).setResult(1, intent);
			        ((Activity)getCont()).startActivityForResult(intent, 1);
					
				}
			});
			
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
		
		//setValues
		if(resultList.get(position).getOptin())
		{
			holder.getTxtName().setText(""+resultList.get(position).getName());
			holder.getTxtProducer().setText(""+resultList.get(position).getProducer());
			holder.getTxtRank().setText("Rang "+resultList.get(position).getId() + " (+2)");
			holder.getCrownLayout().setTag(position);
			if(holder.getTxtCrownCnt()!= null)
			{
				holder.getTxtCrownCnt().setText("x 1"); //holder.getTxtCrownCnt().setText(""+resultList.get(position).getPoints()+" x");
				holder.getCrown1().setImageResource(R.drawable.crown_gold);
				holder.getCrown2().setImageResource(R.drawable.crown_silver);
				holder.getCrown3().setImageResource(R.drawable.crown_bronce);
			}
		}
		else
		{
			holder.getTxtName().setText(" ");
			holder.getTxtProducer().setText("Scan product to join!");
			holder.getTxtRank().setText(" ");
		}
		
		String imageName = resultList.get(position).getId()+".png";
		String imagePath = "";
		Bitmap image = null;
		
		if(imageExists(imageName))
		{
			image = loadImage(imageName);
			imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages/"+imageName;
		}
		else
		{
			try {
				image = BitmapFactory.decodeStream((InputStream)new URL(resultList.get(position).getImagelink()).getContent());
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
	
	private boolean imageExists(String imageName)
	{
		String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages";
		File imgFile = new File(file_path, imageName);
		if(imgFile.exists())
			System.out.println("Image exists SD card: " + imgFile.getAbsolutePath());
		return imgFile.exists();
	}
	
	private Bitmap loadImage(String imageName)
	{
		String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages";
		File imgFile = new File(file_path, imageName);
		Bitmap myBitmap = null;
		
		if(imgFile.exists())
		{
			System.out.println("Found Image on SD card: " + imgFile.getAbsolutePath());
			
			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		}
		return myBitmap;
	}
	

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		
		Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                resultList = (ArrayList<Products>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<Products> FilteredArrList = new ArrayList<Products>();

                if (originalValues == null) {
                	originalValues = new ArrayList<Products>(resultList); // saves the original data in mOriginalValues
                }

                /* 
                 * If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 * else does the Filtering and returns FilteredArrList(Filtered)  
                 */
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return  
                    results.count = originalValues.size();
                    results.values = originalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < originalValues.size(); i++) {
                        Products data = originalValues.get(i);
                        if (data.getName().toLowerCase().contains(constraint.toString())) {
                        	System.out.println("Add Filter: " + data.getName());
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
	}
	
	public void refreshActivity()
	{
		
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
		private TextView txtRank;
		private TextView txtCrownCnt;
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
		public TextView getTxtCrownCnt() {
			return txtCrownCnt;
		}

		/**
		 * @param txtCrownCnt the txtCrownCnt to set
		 */
		public void setTxtCrownCnt(TextView txtCrownCnt) {
			this.txtCrownCnt = txtCrownCnt;
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
