package ch.freebo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class ProductOverview extends Activity {
	
	private Activity act;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setAct(this);
		super.onCreate(savedInstanceState);
		
		setElements();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	public void setElements()
	{
		
		setContentView(R.layout.activity_main);
		setTitle("Prodcts Loyalty Program");
		
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

}
