package ch.mobileking;

import ch.mobileking.utils.Badge;
import ch.mobileking.utils.ProductKing;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

public class BadgeActivity extends Activity {

	
	private TextView badge_main_ach1_starter, badge_main_ach1_shopper, badge_main_ach1_shoppaholic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("ProductKing");
		
		setContentView(R.layout.activity_badge_main);
		
		badge_main_ach1_starter = (TextView) findViewById(R.id.badge_main_ach1_starter);
		
		badge_main_ach1_shopper = (TextView) findViewById(R.id.badge_main_ach1_shopper);
		
		badge_main_ach1_shoppaholic = (TextView) findViewById(R.id.badge_main_ach1_shoppaholic);
		
		System.out.println("Get Badges " + ProductKing.getStaticBadges());
		
		if(ProductKing.getStaticBadges()!=null)
		{
			for (Badge badge : ProductKing.getStaticBadges())
			{
				if(badge.getName().contentEquals("Starter"))
				{
					badge_main_ach1_starter.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_badge_ach1_blue));
				}
				else if(badge.getName().contentEquals("Shopper"))
				{
					badge_main_ach1_shopper.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_badge_ach1_blue));
				}
				else if(badge.getName().contentEquals("Shoppaholic"))
				{
					badge_main_ach1_shoppaholic.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_badge_ach1_blue));

				}
			}
		}

		
	}
	
}
