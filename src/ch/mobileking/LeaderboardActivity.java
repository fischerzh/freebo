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
		
		ListView storehero_lv = (ListView) findViewById(R.id.leaderboard_lv);
		
		ArrayList<Object> storeItems = new ArrayList<Object>();
		
		storeItems.addAll(ProductKing.getStaticLeaderboard());
		
//		Leaderboard cr = new Leaderboard();
//		cr.setPoints(500);
//		cr.setRank(1);
//		cr.setUsername("test");
//		
//		Leaderboard cr2 = new Leaderboard();
//		cr2.setPoints(254);
//		cr2.setRank(2);
//		cr2.setUsername("shoppaholic");
//		
//		Leaderboard cr3 = new Leaderboard();
//		cr3.setPoints(75);
//		cr3.setRank(3);
//		cr3.setUsername("marco");
//		
//		Leaderboard cr4 = new Leaderboard();
//		cr4.setPoints(25);
//		cr4.setRank(4);
//		cr4.setUsername("ScoobyDoo");
//		
//		storeItems.add(cr);
//		storeItems.add(cr2);
//		storeItems.add(cr3);
//		storeItems.add(cr4);

		ImageAdapter adapter = new ImageAdapter(getApplicationContext(), storeItems, R.layout.activity_leaderboard_item); 
		
		storehero_lv.setAdapter(adapter);
		
	}

}
