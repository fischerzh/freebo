package ch.mobileking.activity.old;

import java.util.ArrayList;

import ch.mobileking.R;
import ch.mobileking.R.drawable;
import ch.mobileking.R.id;
import ch.mobileking.R.layout;
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

	private TextView prod_name, prod_manuf, prod_cat, prod_cnt, prod_rank;

	private ImageView prod_Image;
	
	private Products product = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.crown_detail);
		
		setTitle("ProductKing");
		

		if (getCallingActivity() != null && getIntent().getExtras() != null) {

			String prodId = getIntent().getStringExtra("product");
			System.out.println("got Product: " + prodId);

			product = ProductKing.getStaticProducts().get(Integer.parseInt(prodId));
			
	    	Utils.addLogMsg(this.getLocalClassName());

			System.out.println("CrownDetailOverview: Found Product: " + product.getName());
			prod_name = (TextView) findViewById(R.id.badges_detail_name);
			prod_manuf = (TextView) findViewById(R.id.badges_detail_manufacturer);
			prod_rank = (TextView) findViewById(R.id.badges_detail_prod_rank);
			prod_cnt = (TextView) findViewById(R.id.badges_detail_cnt);
			prod_Image = (ImageView) findViewById(R.id.badges_detail_image);

			prod_name.setText(product.getName());
			prod_manuf.setText(product.getProducer());
			
			if(product.getOlduserrank()-product.getUserrank()>0 && product.getOlduserrank()!=0)
			{
				prod_rank.setText("MEIN RANG "+product.getUserrank() + " (+"+(product.getOlduserrank()-product.getUserrank())+")");
				prod_rank.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_green, 0);
			}
			else if(product.getOlduserrank()-product.getUserrank()<0 && product.getOlduserrank()!=0)
			{
				prod_rank.setText("MEIN RANG "+product.getUserrank() + " ("+(product.getOlduserrank()-product.getUserrank())+")");
				prod_rank.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down_red, 0);
			}
			else if(product.getOlduserrank()==0 && product.getUserrank()==1)
			{
				prod_rank.setText("MEIN RANG "+product.getUserrank() + " (+-0)");
				prod_rank.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_green, 0);
			}
			else
			{
				prod_rank.setText("MEIN RANG "+product.getUserrank() + " (+-0)");
				prod_rank.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_nochange, 0);
			}
			
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
		
		if(product.getCrowns()!=null)
		{
			crowns.addAll(product.getCrowns());
			
			ImageAdapter adapter = new ImageAdapter(getApplicationContext(), crowns, R.layout.crown_detail_item); 

			gridView.setAdapter(adapter);
		}


	}

}
