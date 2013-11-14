package ch.mobileking;

import java.util.ArrayList;

import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.utils.Crown;
import ch.mobileking.utils.Leaderboard;
import ch.mobileking.utils.ProductKing;
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
		
    	ProductKing.getInstance().addLogMsg("LeaderboardActivity");
		
		ListView storehero_lv = (ListView) findViewById(R.id.leaderboard_lv);
		
		ArrayList<Object> storeItems = new ArrayList<Object>();
		
		storeItems.addAll(ProductKing.getStaticLeaderboard());
		
		ImageAdapter adapter = new ImageAdapter(getApplicationContext(), storeItems, R.layout.activity_leaderboard_item); 
		
		storehero_lv.setAdapter(adapter);
		
	}

}
