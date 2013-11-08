package ch.mobileking;

import java.util.ArrayList;
import java.util.HashMap;

import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.utils.Crown;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class StoreKingActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("ProductKing");
		
		setContentView(R.layout.activity_storehero_main);
		
		ListView storehero_lv = (ListView) findViewById(R.id.storehero_lv);
		
		ArrayList<Object> storeKingItems = new ArrayList<Object>();
		
//		HashMap<String, Crown> storeCrownList = new HashMap<String, Crown>();
		
//		for (Products prod : ProductKing.getStaticProducts())
//		{
//			Location loc = new Location(prod.get)
//		}
//		storeItems.add(cr);
//		storeItems.add(cr2);
		
		storeKingItems.addAll(ProductKing.getLocations());

		ImageAdapter adapter = new ImageAdapter(getApplicationContext(), storeKingItems, R.layout.activity_storehero_item); 
		
		storehero_lv.setAdapter(adapter);
		
	}

}
