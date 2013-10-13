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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setAct(this);
	
		request = new ServerRequest(this, this);

		
		if(isEdit)
		{
			setEditContent();
		}
		else
		{
			setNormalContent();
		}
		
	}
	
	private void setNormalContent()
	{
		setContentView(R.layout.loyaltycard_view);
		loyalty_ll_cumulus = (LinearLayout) findViewById(R.id.loyalty_ll_cumulus);
		loyalty_ll_cumulus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setEditContent();
			}
		});
	}
	
	private void setEditContent()
	{
		setContentView(R.layout.loyaltycard_edit);
		loyalty_edit_card_txt = (EditText) findViewById(R.id.loyalty_edit_card_txt);
		
		loyalty_edit_btnNext = (Button) findViewById(R.id.loyalty_edit_btnNext);
		loyalty_edit_btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				request.startUdateCumulus();
				
				Intent intent = new Intent(LoyaltyCardActivity.this, RecommActivity.class);
				startActivity(intent);
				
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{

		// TODO Auto-generated method stub
		Intent intent = new Intent(LoyaltyCardActivity.this, RecommActivity.class);
		startActivity(intent);
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
	public void onLoginCompleted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateCompleted() {
		// TODO Auto-generated method stub
		System.out.println("Loyalty Card updated!!!!");
		Intent intent = new Intent(LoyaltyCardActivity.this, RecommActivity.class);
		startActivity(intent);
	}
}
