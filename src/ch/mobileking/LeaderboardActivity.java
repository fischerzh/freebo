package ch.mobileking;

import java.util.ArrayList;

import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.utils.Crown;
import ch.mobileking.utils.Leaderboard;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class LeaderboardActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("ProductKing");
		
		setContentView(R.layout.activity_leaderboard_main);
		
    	Utils.addLogMsg(this.getLocalClassName());
		
		ListView storehero_lv = (ListView) findViewById(R.id.leaderboard_lv);
		
		ArrayList<Object> storeItems = new ArrayList<Object>();
		
		storeItems.addAll(ProductKing.getInstance().getStaticLeaderboard());
		
		ImageAdapter adapter = new ImageAdapter(getApplicationContext(), storeItems, R.layout.activity_leaderboard_item); 
		
		storehero_lv.setAdapter(adapter);
		
	}

}
