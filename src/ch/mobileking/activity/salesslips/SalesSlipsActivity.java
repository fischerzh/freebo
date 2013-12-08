package ch.mobileking.activity.salesslips;

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

public class SalesSlipsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("ProductKing");
		
		setContentView(R.layout.activity_salesslips_main);
		
		ListView storehero_lv = (ListView) findViewById(R.id.salesslip_lv);
		
		ArrayList<Object> salesSlipItems = new ArrayList<Object>();
		
		Utils.addLogMsg(this.getLocalClassName());
		
		salesSlipItems.addAll(ProductKing.getInstance().getStaticSalesSlips());

		ImageAdapter adapter = new ImageAdapter(getApplicationContext(), salesSlipItems, R.layout.activity_salesslips_item); 
		
		storehero_lv.setAdapter(adapter);
		
	}

}
