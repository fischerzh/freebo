package ch.mobileking;

import java.util.ArrayList;

import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.utils.Crown;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

public class BadgesDetailOverview extends Activity {

	private TextView prod_name, prod_manuf, prod_cat, prod_cnt;

	private Products product = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.badges_detail);

		if (getCallingActivity() != null && getIntent().getExtras() != null) {

			String prodId = getIntent().getStringExtra("product");
			System.out.println("got Product: " + prodId);

			product = ProductKing.getStaticProducts().get(
					Integer.parseInt(prodId));

			System.out.println("BadgesDetailOverview: Found Product: " + product.getName());
			prod_name = (TextView) findViewById(R.id.badges_detail_name);
			prod_manuf = (TextView) findViewById(R.id.badges_detail_manufacturer);
			prod_cat = (TextView) findViewById(R.id.badges_detail_category);
			prod_cnt = (TextView) findViewById(R.id.badges_detail_cnt);

			prod_name.setText(product.getName());
			prod_manuf.setText(product.getProducer());
			prod_cat.setText("Kategorie: Snacks ");
			prod_cnt.setText(product.getPoints()+" Punkte gesammelt!");

		}

		setElements();

	}

	private void setElements() {
		GridView gridView = (GridView) findViewById(R.id.badges_grid_view);

		// Instance of ImageAdapter Class
		ArrayList<Crown> crowns;
		crowns = new ArrayList<Crown>();

		Crown cr = new Crown();
		cr.setCrownstatus(1);
		cr.setRank(1);
		cr.setSalespoint("Migros ZŸrich HB");

		Crown cr2 = new Crown();
		cr2.setCrownstatus(3);
		cr2.setRank(10);
		cr2.setSalespoint("Coop St. Annahof");

		Crown cr3 = new Crown();
		cr3.setCrownstatus(2);
		cr3.setRank(10);
		cr3.setSalespoint("Migros Brunaupark");
		
		Crown cr4 = new Crown();
		cr4.setRank(99);
		cr4.setSalespoint("Coop City Stadelhofen");
		
		crowns.add(cr);
		crowns.add(cr2);
		crowns.add(cr3);
		crowns.add(cr4);

		//(ArrayList<Crown>) product.getCrowns();
		System.out.println("Get Crowns: " + product.getCrowns());

		ImageAdapter adapter = new ImageAdapter(getApplicationContext(), crowns); //(ArrayList<Crown>)product.getCrowns()

		gridView.setAdapter(adapter);

	}

}
