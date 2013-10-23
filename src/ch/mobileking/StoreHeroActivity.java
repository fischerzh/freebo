package ch.mobileking;

import java.util.ArrayList;

import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.utils.Crown;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class StoreHeroActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("ProductKing");
		
		setContentView(R.layout.activity_storehero_main);
		
		ListView storehero_lv = (ListView) findViewById(R.id.storehero_lv);
		
		ArrayList<Object> storeItems = new ArrayList<Object>();
		
		Crown cr = new Crown();
		cr.setCrownstatus(1);
		cr.setRank(22);
		cr.setSalespoint("Migros ZŸrich HB");
		
		Crown cr2 = new Crown();
		cr2.setCrownstatus(1);
		cr2.setRank(5);
		cr2.setSalespoint("Coop Sihlcity");
		
		storeItems.add(cr);
		storeItems.add(cr2);

		ImageAdapter adapter = new ImageAdapter(getApplicationContext(), storeItems, R.layout.activity_storehero_item); 
		
		storehero_lv.setAdapter(adapter);
		
	}

}
