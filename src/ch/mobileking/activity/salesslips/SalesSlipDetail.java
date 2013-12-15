package ch.mobileking.activity.salesslips;

import java.util.ArrayList;

import ch.mobileking.R;
import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Utils;
import ch.mobileking.utils.classes.SalesSlip;
import ch.mobileking.utils.classes.SalesSlipItem;
import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class SalesSlipDetail extends Activity {
	
	private TextView salesslip_detail_pos_time;
	private TextView salesslip_detail_points_cnt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("ProductKing");
		
		setContentView(R.layout.activity_salesslip_detail);
		
		Utils.addLogMsg(this.getLocalClassName());
		
	    Integer salesItemId = getIntent().getIntExtra("itemId", 0);
	    
	    SalesSlip slip = ProductKing.getInstance().getStaticSalesSlips().get(salesItemId);
		
		TextView salesslip_detail_pos = (TextView) findViewById(R.id.salesslip_detail_pos);
		salesslip_detail_pos.setText(slip.getSalespoint());
		
		salesslip_detail_pos_time = (TextView) findViewById(R.id.salesslip_detail_pos_time);
		salesslip_detail_pos_time.setText(slip.getScanDate());
		
		ArrayList<Object> salesItemList = new ArrayList<Object>();
		
		SalesSlipItem item = new SalesSlipItem();
		item.setName("Zweifel Chips");
		item.setPrice("2.50 CHF");
		item.setProducer("Zweifel Inc.");
		item.setQuantity(4);
		
		salesItemList.add(item);
		int count = 0;
		for(Object slipItem : salesItemList)
		{
			count+=((SalesSlipItem)slipItem).getQuantity();
		}
		salesslip_detail_points_cnt = (TextView) findViewById(R.id.salesslip_detail_points_cnt);
		salesslip_detail_points_cnt.setText(count+" x Pkt.");
		
		GridView salesSlipItem_Grid = (GridView) findViewById(R.id.salesslip_detail_grid);

		ImageAdapter adapter = new ImageAdapter(getApplicationContext(), salesItemList, R.layout.activity_salesslips_detail_item); 
		
		salesSlipItem_Grid.setAdapter(adapter);
		
	}

}
