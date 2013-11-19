package ch.mobileking;

import ch.mobileking.utils.Badge;
import ch.mobileking.utils.ProductKing;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BadgeActivity extends Activity {

	private ImageView act_badge_login1;
	private ImageView act_badge_login2;
	private ImageView act_badge_login3;

	private ImageView act_badge_optin1;
	private ImageView act_badge_optin2;
	private ImageView act_badge_optin3;
	private ImageView act_badge_shopping1;
	private ImageView act_badge_shopping2;
	private ImageView act_badge_shopping3;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("ProductKing");
		
		setContentView(R.layout.activity_badge_main);
		
    	ProductKing.getInstance().addLogMsg("BadgeActivity");
		
		act_badge_login1 = (ImageView) findViewById(R.id.act_badge_login1);
		act_badge_login2 = (ImageView) findViewById(R.id.act_badge_login2);
		act_badge_login3 = (ImageView) findViewById(R.id.act_badge_login3);
		
		act_badge_optin1 = (ImageView) findViewById(R.id.act_badge_optin1);
		act_badge_optin2 = (ImageView) findViewById(R.id.act_badge_optin2);
		act_badge_optin3 = (ImageView) findViewById(R.id.act_badge_optin3);

		act_badge_shopping1 = (ImageView) findViewById(R.id.act_badge_shopping1);
		act_badge_shopping2 = (ImageView) findViewById(R.id.act_badge_shopping2);
		act_badge_shopping3 = (ImageView) findViewById(R.id.act_badge_shopping3);
		
		System.out.println("Get Badges " + ProductKing.getStaticBadges());
		
		if(ProductKing.getStaticBadges()!=null)
		{
			for (Badge badge : ProductKing.getStaticBadges())
			{
				if(badge.getName().contentEquals("Login1"))
				{
					act_badge_login1.setImageResource(R.drawable.ic_1_log_in);
				}
				else if(badge.getName().contentEquals("Login2"))
				{
					act_badge_login2.setImageResource(R.drawable.ic_50_log_in);
				}
				else if(badge.getName().contentEquals("Login3"))
				{
					act_badge_login3.setImageResource(R.drawable.ic_100_log_in);
				}
				else if(badge.getName().contentEquals("OptIn1"))
				{
					act_badge_optin1.setImageResource(R.drawable.ic_1_opt_in);
				}
				else if(badge.getName().contentEquals("OptIn2"))
				{
					act_badge_optin2.setImageResource(R.drawable.ic_5_opt_in);
				}
				else if(badge.getName().contentEquals("OptIn3"))
				{
					act_badge_optin3.setImageResource(R.drawable.ic_10_opt_in);
				}
				else if(badge.getName().contentEquals("Shopping1"))
				{
					act_badge_shopping1.setImageResource(R.drawable.ic_5_purchase);
				}
				else if(badge.getName().contentEquals("Shopping2"))
				{
					act_badge_shopping2.setImageResource(R.drawable.ic_10_purchase);
				}
				else if(badge.getName().contentEquals("Shopping3"))
				{
					act_badge_shopping3.setImageResource(R.drawable.ic_30_purchase);
				}
			}
		}

		
	}
	
}
