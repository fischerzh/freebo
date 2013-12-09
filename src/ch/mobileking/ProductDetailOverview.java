package ch.mobileking;

import java.io.File;
import java.util.ArrayList;

import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;
import ch.mobileking.utils.classes.Products;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ProductDetailOverview extends Activity {
	
	private TextView prod_name, prod_manuf, product_detail_userRank, product_detail_userName, product_detail_points;
	private ImageView productImage;
	
	private SharedPrefEditor editor;
	private ImageView product_detail_avatar;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.product_detail_leaderboard);
		
		setTitle("ProductKing");
		
		editor = new SharedPrefEditor(this);

		Utils.addLogMsg(this.getLocalClassName());
		
		if(getCallingActivity() != null && getIntent().getExtras()!= null)
		{
			
			String prodId = getIntent().getStringExtra("product");
			Products product = null;
			product = ProductKing.getStaticProducts().get(Integer.parseInt(prodId));
			
			prod_name = (TextView) findViewById(R.id.prod_detail_name);
			prod_manuf = (TextView) findViewById(R.id.prod_detail_manufacturer);
			productImage = (ImageView) findViewById(R.id.prod_detail_image);
			
			product_detail_userRank = (TextView) findViewById(R.id.product_detail_userRank);
			product_detail_userRank.setText("RANG #" + product.getUserrank());
			
			product_detail_userName = (TextView) findViewById(R.id.product_detail_userName);
			product_detail_userName.setText(editor.getUsername());
			
			product_detail_points = (TextView) findViewById(R.id.product_detail_points);
			product_detail_points.setText(product.getPoints() + " Pkt.");
			
			product_detail_avatar = (ImageView) findViewById(R.id.product_detail_avatar);
			product_detail_avatar.setImageBitmap(Utils.loadImageFromPath(Utils.USER_AVATAR_PNG));
			
			prod_name.setText(product.getName());
			prod_manuf.setText(product.getProducer());
			
			productImage.setImageBitmap(Utils.loadImageFromPath(product.getEan()));
			
			ListView storehero_lv = (ListView) findViewById(R.id.leaderboard_lv);
			
			ArrayList<Object> storeItems = new ArrayList<Object>();
			
//			storeItems.addAll(ProductKing.getInstance().getStaticLeaderboard());
			storeItems.addAll(product.getLeaderboard());
			
			ImageAdapter adapter = new ImageAdapter(getApplicationContext(), storeItems, R.layout.activity_leaderboard_item); 
			
			storehero_lv.setAdapter(adapter);
			
			
		}
		
	}
	
	

}
