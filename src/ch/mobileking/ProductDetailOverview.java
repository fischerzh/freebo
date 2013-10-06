package ch.mobileking;

import java.io.File;

import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailOverview extends Activity {
	
	private TextView prod_name, prod_manuf, prod_cat, prod_cnt;
	private TextView prod_ean, prod_package, prod_content;
	private ImageView productImage;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.product_detail);
		
		if(getCallingActivity() != null && getIntent().getExtras()!= null)
		{
			
			String prodId = getIntent().getStringExtra("product");
			Products product = null;
			
			System.out.println("got Product: " +prodId);
			
//			for (Products prod : ProductKing.getStaticProducts())
//			{
//				if(prod.getId() == Integer.parseInt(prodId));
//					product = prod;
//			}
			product = ProductKing.getStaticProducts().get(Integer.parseInt(prodId));
			
			prod_name = (TextView) findViewById(R.id.prod_detail_name);
			prod_manuf = (TextView) findViewById(R.id.prod_detail_manufacturer);
			prod_cat = (TextView) findViewById(R.id.prod_detail_category);
			prod_cnt = (TextView) findViewById(R.id.prod_detail_cnt);
			prod_ean = (TextView) findViewById(R.id.prod_detail_ean_txt);
			prod_package = (TextView) findViewById(R.id.prod_detail_package_txt);
			prod_content = (TextView) findViewById(R.id.prod_detail_content_txt);
			productImage = (ImageView) findViewById(R.id.prod_detail_image);
			
			prod_name.setText(product.getName());
			prod_manuf.setText(product.getProducer());
//			prod_cat.setText(product.);
			prod_cnt.setText(""+product.getPoints()+" x gesammelt");
			prod_ean.setText(product.getEan());
//			prod_package.setText(product.get());
			prod_content.setText(""+product.getIngredients());
			
			productImage.setImageBitmap(loadImage(product.getImagepath()));
			
		}
		
	}
	
	private Bitmap loadImage(String imagePath)
	{
		File imgFile = new File(imagePath);
		Bitmap myBitmap = null;
		
		if(imgFile.exists())
		{
			System.out.println("Found Image on SD card: " + imgFile.getAbsolutePath());
			
			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		}
		return myBitmap;
	}

}
