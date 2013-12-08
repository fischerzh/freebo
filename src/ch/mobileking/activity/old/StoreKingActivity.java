package ch.mobileking.activity.old;

import java.util.ArrayList;
import java.util.HashMap;

import ch.mobileking.R;
import ch.mobileking.R.id;
import ch.mobileking.R.layout;
import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Utils;
import ch.mobileking.utils.classes.Crown;
import ch.mobileking.utils.classes.Products;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class StoreKingActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("ProductKing");
		
		setContentView(R.layout.activity_salesslips_main);
		
		ListView storehero_lv = (ListView) findViewById(R.id.salesslip_lv);
		
		ArrayList<Object> storeKingItems = new ArrayList<Object>();
		
//		HashMap<String, Crown> storeCrownList = new HashMap<String, Crown>();
		
//		for (Products prod : ProductKing.getStaticProducts())
//		{
//			Location loc = new Location(prod.get)
//		}
//		storeItems.add(cr);
//		storeItems.add(cr2);
		
		Utils.addLogMsg(this.getLocalClassName());
		
		storeKingItems.addAll(ProductKing.getInstance().getLocations());

		ImageAdapter adapter = new ImageAdapter(getApplicationContext(), storeKingItems, R.layout.activity_salesslips_item); 
		
		storehero_lv.setAdapter(adapter);
		
	}

}
