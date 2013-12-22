package ch.mobileking.activity.salesslips;

import java.util.ArrayList;

import ch.mobileking.R;
import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Utils;
import ch.mobileking.utils.classes.SalesSlip;
import ch.mobileking.utils.classes.SalesSlipItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SalesSlipDetail extends ActionBarActivity {
	
	private TextView salesslip_detail_pos_time;
	private TextView salesslip_detail_points_cnt;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("ProductKing");
		
		setContentView(R.layout.activity_salesslip_detail);
		
		Utils.addLogMsg(this.getLocalClassName());
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) 
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		else
		{
			getActionBar().setHomeButtonEnabled(true);
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
	    Integer salesItemId = getIntent().getIntExtra("itemId", 0);
	    
	    SalesSlip slip = ProductKing.getInstance().getStaticSalesSlips().get(salesItemId);
		
		TextView salesslip_detail_pos = (TextView) findViewById(R.id.salesslip_detail_pos);
		salesslip_detail_pos.setText(slip.getSalespoint());
		
		ImageView salesslip_detail_pos_image = (ImageView) findViewById(R.id.salesslip_detail_pos_image);
		
		if(Utils.imageExists(slip.getSalespoint()))
			salesslip_detail_pos_image.setImageBitmap(Utils.loadImageFromPath(slip.getSalespoint()));
		else
			Utils.loadBitmapFromURL(slip.getImageLink(), slip.getSalespoint());
		salesslip_detail_pos_time = (TextView) findViewById(R.id.salesslip_detail_pos_time);
		salesslip_detail_pos_time.setText(slip.getPurchasedate());
		
		ArrayList<Object> salesItemList = new ArrayList<Object>();
		salesItemList.addAll(slip.getSalesslipitems());
//		SalesSlipItem item = new SalesSlipItem();
//		item.setName("Zweifel Chips");
//		item.setPrice("2.50 CHF");
//		item.setProducer("Zweifel Inc.");
//		item.setQuantity(4);
//		
//		salesItemList.add(item);
		double count = 0.0;
		for(Object slipItem : salesItemList)
		{
			count=count+((SalesSlipItem)slipItem).getQuantity();
		}
		salesslip_detail_points_cnt = (TextView) findViewById(R.id.salesslip_detail_points_cnt);
		salesslip_detail_points_cnt.setText("Total: " +count+" x Pkt.");
		
		GridView salesSlipItem_Grid = (GridView) findViewById(R.id.salesslip_detail_grid);

		ImageAdapter adapter = new ImageAdapter(getApplicationContext(), salesItemList, R.layout.activity_salesslips_detail_item); 
		
		salesSlipItem_Grid.setAdapter(adapter);
		
	}

}
