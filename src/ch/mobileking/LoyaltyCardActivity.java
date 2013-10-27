package ch.mobileking;

import ch.mobileking.login.ServerRequest;
import ch.mobileking.utils.ITaskComplete;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class LoyaltyCardActivity extends Activity implements ITaskComplete{
	
	private Activity act;
	
	private ServerRequest request;
	
	private Boolean isEdit = false;
	
	private LinearLayout loyalty_ll_cumulus;
	
	private EditText loyalty_edit_card_txt;
	
	private Button loyalty_edit_btnNext;

	private Button loyalty_card_next_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setAct(this);
	
		request = new ServerRequest(this, this);
		
		setContentView(R.layout.loyaltycard_view);
		
		loyalty_card_next_btn = (Button)findViewById(R.id.loyalty_card_next_btn);
		loyalty_card_next_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				//TODO: Call Service request and send cumulus info to server
//				request.startUdateCumulus();
				System.out.println("Loyalty Card needs Update!!!!");
				
				Intent intent = new Intent(LoyaltyCardActivity.this, MainTabActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	/**
	 * @return the act
	 */
	public Activity getAct() {
		return act;
	}

	/**
	 * @param act the act to set
	 */
	public void setAct(Activity act) {
		this.act = act;
	}

	@Override
	public void onLoginCompleted(boolean completed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateCompleted(boolean completed) {
		// TODO Auto-generated method stub
		System.out.println("Loyalty Card updated!!!!");
		Intent intent = new Intent(LoyaltyCardActivity.this, MainTabActivity.class);
		startActivity(intent);
	}

	@Override
	public void startUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startLogin() {
		// TODO Auto-generated method stub
		
	}
}
