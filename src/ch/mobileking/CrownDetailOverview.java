package ch.mobileking;

import java.util.ArrayList;

import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.utils.Crown;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import ch.mobileking.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CrownDetailOverview extends Activity {

	private TextView prod_name, prod_manuf, prod_cat, prod_cnt;

	private ImageView prod_Image;
	
	private Products product = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.crown_detail);
		
		setTitle("ProductKing");

		if (getCallingActivity() != null && getIntent().getExtras() != null) {

			String prodId = getIntent().getStringExtra("product");
			System.out.println("got Product: " + prodId);

			product = ProductKing.getStaticProducts().get(
					Integer.parseInt(prodId));

			System.out.println("CrownDetailOverview: Found Product: " + product.getName());
			prod_name = (TextView) findViewById(R.id.badges_detail_name);
			prod_manuf = (TextView) findViewById(R.id.badges_detail_manufacturer);
//			prod_cat = (TextView) findViewById(R.id.badges_detail_category);
			prod_cnt = (TextView) findViewById(R.id.badges_detail_cnt);
			prod_Image = (ImageView) findViewById(R.id.badges_detail_image);

			prod_name.setText(product.getName());
			prod_manuf.setText(product.getProducer());
//			prod_cat.setText("Kategorie: Snacks ");
			prod_cnt.setText(product.getPoints()+" x Punkte gesammelt!");
			prod_Image.setImageBitmap(Utils.loadImageFromPath(product));

		}

		setElements();

	}

	private void setElements() {
		GridView gridView = (GridView) findViewById(R.id.badges_grid_view);

		// Instance of ImageAdapter Class
		ArrayList<Object> crowns = new ArrayList<Object>();

		//(ArrayList<Crown>) product.getCrowns();
		System.out.println("Get Crowns: " + product.getCrowns());
		
		crowns.addAll(product.getCrowns());
		
		ImageAdapter adapter = new ImageAdapter(getApplicationContext(), crowns, R.layout.crown_detail_item); 

		gridView.setAdapter(adapter);

	}

}
