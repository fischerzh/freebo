package ch.mobileking;

import android.app.Activity;
import android.os.Bundle;

public class ProductDetailOverview extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.product_detail);
		
		if(getCallingActivity() != null && getIntent().getExtras()!= null)
		{
			
		}
		
	}

}
