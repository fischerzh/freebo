package ch.mobileking.classes.override;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import ch.mobileking.R;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;
import ch.mobileking.utils.classes.Crown;
import ch.mobileking.utils.classes.Products;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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

	@SuppressWarnings("deprecation")
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
			holder.setImgProgress((ProgressBar)convertView.findViewById(R.id.list_image_progress));
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
		if(prod.getSize().toLowerCase().contains("null") || prod.getSize().trim()=="")
			holder.getTxtName().setText(""+prod.getName());
		else
			holder.getTxtName().setText(""+prod.getName()+" (" +prod.getSize()+")");
		if(prod.getProducer().toLowerCase().contains("null") || prod.getProducer().trim()=="")
			prod.setProducer("Hersteller unbekannt");
		holder.getTxtProducer().setText(""+prod.getProducer());
		
		if(prod.getIsactive())
		{

			if(prod.getOlduserrank()-prod.getUserrank()>0 && prod.getOlduserrank()!=0)
			{
				holder.getTxtRank().setText("DEIN RANG #"+prod.getUserrank() + " (+"+(prod.getOlduserrank()-prod.getUserrank())+")");
				holder.getTxtRank().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_green, 0);
			}
			else if(prod.getOlduserrank()-prod.getUserrank()<0 && prod.getOlduserrank()!=0)
			{
				holder.getTxtRank().setText("DEIN RANG #"+prod.getUserrank() + " ("+(prod.getOlduserrank()-prod.getUserrank())+")");
				holder.getTxtRank().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down_red, 0);
			}
			else if(prod.getOlduserrank()==0 && prod.getUserrank()==1)
			{
				holder.getTxtRank().setText("DEIN RANG #"+prod.getUserrank() + " (+-0)");
				holder.getTxtRank().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_green, 0);
			}
			else
			{
				holder.getTxtRank().setText("DEIN RANG #"+prod.getUserrank() + " (+-0)");
				holder.getTxtRank().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_nochange, 0);
			}
			holder.getTxtCollectedCnt().setText(prod.getPoints()+" Pkte.");
			
			if(getProdLayoutResourceId()==R.layout.product_item)
			{
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
				holder.getCrown1().setAlpha(255);
			}

				
			/** SET THE POSITION TO REFERENCE FROM LATER **/
				
//			holder.getCrownLayout().setTag(position);
		
		}
		else
		{
			holder.getTxtRank().setText("DEIN RANG #"+prod.getUserrank() + " (+-0)");
			if(getProdLayoutResourceId()==R.layout.product_item)
			{
				holder.getCrown1().setImageResource(R.drawable.ic_krone_inactive);
				holder.getCrown1().setAlpha(75);

			}
			holder.getTxtRank().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_nochange, 0);
			holder.getTxtCollectedCnt().setText("Keine Eink�ufe!");
		}
		
//		Products prod = resultList.get(position);
		
		String imageName = prod.getEan();
//		String imagePath = "";
		Bitmap image = null;
		
		if(Utils.imageExists(prod.getEan()))
		{
			holder.getImgProgress().setVisibility(View.INVISIBLE);
			
			image = Utils.loadImageFromPath(imageName);
			
			resultList.get(position).setProductImage(image);
			
			holder.getImgView().setImageBitmap(image);
		}
		else
		{
//			holder.getImgProgress().setVisibility(View.VISIBLE);

			Utils.loadBitmapFromURL(prod.getImagelink(), imageName);
			holder.getImgView().setImageResource(R.drawable.empty);

//			Utils.saveBitmapAsync(image, imageName);
			
		}
		//Image Stuff
		
//		prodItemPict.setImageBitmap(image);
		
		if(!prod.getIsactive())
		{
			holder.getImgView().setAlpha(125);
		}
		else
		{
			holder.getImgView().setAlpha(255);
		}
		
//		String imageUri = resultList.get(position).getImagelink();
//		holder.getImgView().setTag(imageUri); //Add this line
//		imageLoader.displayImage(imageUri, holder.getImgView());
//		imageLoader.displayImage(imageUri, holder.getImgView(), new ImageLoadingListener() {
//			
//			@Override
//			public void onLoadingStarted(String imageUri, View view) {
//				// TODO Auto-generated method stub
//				System.out.println("loading of image started: " + imageUri);
//				holder.getImgProgress().setVisibility(View.INVISIBLE);
//
//			}
//			
//			@Override
//			public void onLoadingFailed(String imageUri, View view,
//					FailReason failReason) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//				System.out.println("loading of image finished: " + imageUri);
//				int getPosition = (Integer) view.getTag();
//
//				holder.getImgProgress().setVisibility(View.INVISIBLE);
//
//			}
//			
//			@Override
//			public void onLoadingCancelled(String imageUri, View view) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
//		holder.getImgView().setImageDrawable(drawable);
		ProductKing.setStaticProducts(resultList);
			
		return convertView;
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
		private ProgressBar imageProgress;
		private LinearLayout crownLayout;

		public void setImgProgress(ProgressBar imageProgressId) {
			// TODO Auto-generated method stub
			imageProgress = imageProgressId;
		}
		
		public ProgressBar getImgProgress()
		{
			return imageProgress;
		}

		public void setCrownLayout(LinearLayout crownLayoutId) {
			// TODO Auto-generated method stub
			this.crownLayout = crownLayoutId; 
		}
		
		public LinearLayout getCrownLayout()
		{
			return this.crownLayout;
		}

		
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
