package ch.freebo.classes.override;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import ch.freebo.R;
import ch.freebo.utils.Products;
import ch.freebo.utils.SharedPrefEditor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class ProductBaseAdapter extends BaseAdapter implements Filterable{
	
	private static ArrayList<Products> resultList;
	private ArrayList<Products> originalValues;
	
	private LayoutInflater mInflater;
	private ImageLoader imageLoader;
	
	ViewHolder holder;
	
	private DisplayImageOptions options;
	private Context cont;
	
	private int checkBoxCounter, checkBoxInitialized;
	
	
	public ProductBaseAdapter(Context context, ArrayList<Products> items)
	{
		resultList = items;
		setCont(context);
		mInflater = LayoutInflater.from(context);
		
        /** IMAGE LOADER */
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "your folder");

		 // Get singletone instance of ImageLoader
		 imageLoader = ImageLoader.getInstance();
		 // Create configuration for ImageLoader (all options are optional)
		 ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		          // You can pass your own memory cache implementation
		         .discCache(new UnlimitedDiscCache(cacheDir)) // You can pass your own disc cache implementation
		         .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
		         .build();
		 // Initialize ImageLoader with created configuration. Do it once.
		 imageLoader.init(config);
		 
		 options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.empty)//display stub image
		.displayer(new RoundedBitmapDisplayer(20))
		.build();
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
		
//		final int pos = position;
		
		checkBoxCounter = 0;
		checkBoxInitialized = 0;
		
		//Initialize convertView
		if(convertView==null)
		{

			convertView = mInflater.inflate(R.layout.product_item, null);
			holder = new ViewHolder();
			holder.setTxtName((TextView)convertView.findViewById(R.id.prod_item_name));
			holder.setTxtProducer((TextView)convertView.findViewById(R.id.prod_item_producer));
			holder.setTxtEan((TextView)convertView.findViewById(R.id.prod_item_ean));
			holder.setChkBox((CheckBox)convertView.findViewById(R.id.prod_item_checkbox));
			holder.setImgView((ImageView)convertView.findViewById(R.id.list_image));
			
//			convertView.setTag(holder);
			holder.getChkBox().setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					int getPosition = (Integer) buttonView.getTag();
					System.out.println("Checked item at Position: " + getPosition + " " + resultList.get(getPosition).getName() + " set to Favorite " + isChecked);
//					Product currentProd = (Product) holder.chkBox.getTag();
//					currentProd.setFavorite(buttonView.isChecked());

					/** SAVE FAVORITES OPT-IN PRODUCTS TO SHARED PREF*/
					saveFavorites();
					holder.getChkBox().setChecked(true);
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
		holder.getChkBox().setTag(position);
		//setValues
		holder.getTxtName().setText(""+resultList.get(position).getEan());
		holder.getTxtProducer().setText(""+resultList.get(position).getId());
		holder.getTxtEan().setText(""+resultList.get(position).getName());
		
		//Image Stuff
		String imageUri = resultList.get(position).getImagelink();
		
		holder.getImgView().setTag(imageUri); //Add this line
		imageLoader.displayImage(imageUri, holder.getImgView());
//		MY IDEA!! holder.getImgView().setImageDrawable(drawable);
//		ProductsHelper.setProductList(resultList);
			
		return convertView;
	
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
	
    public void saveFavorites()
    {
    	SharedPrefEditor editor = new SharedPrefEditor(cont);
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
		private TextView txtEan;
		private CheckBox chkBox;
		private ImageView imgView;

		/**
		 * @return the txtCategory
		 */
		public TextView getTxtName() {
			return txtName;
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
		public TextView getTxtEan() {
			return txtEan;
		}
		/**
		 * @param txtDueDate the txtDueDate to set
		 */
		public void setTxtEan(TextView txtEan) {
			this.txtEan = txtEan;
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
		
	}
	
	
}
