package ch.mobileking;

import java.util.ArrayList;

import ch.mobileking.R;
import ch.mobileking.classes.override.ProductBaseAdapter;
import ch.mobileking.login.AsyncUpdate;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import ch.mobileking.utils.SharedPrefEditor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class ProductOverview extends Activity {
	
	private Activity act;
	
	private ProductBaseAdapter adapter;
	
	private SharedPrefEditor editor;
	
	private BaseActivity baseActivityMenu;
		
	private ListView listView;
	private EditText editTxt;
	private ImageButton imageBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setAct(this);
		super.onCreate(savedInstanceState);
		
		baseActivityMenu = new BaseActivity(this);
		
		System.out.println("ProductOverview called!");
		
		System.out.println("Extras from Intent: " +getCallingActivity() + getIntent().getExtras());
		
		setElements();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
//	        case R.id.create_new:
//	        	baseActivityMenu.logOut();
//	            return true;
	        case R.id.log_out:
	        	baseActivityMenu.logOut();
	            return true;
	        case R.id.app_sync:
	        	baseActivityMenu.syncAppToServer();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	public void setElements()
	{
		
		setContentView(R.layout.product_overview_layout);
		
        //setContentView(R.layout.products_main);
		
        listView = (ListView) findViewById(R.id.product_list_view);
        editTxt = (EditText) findViewById(R.id.product_search_box);
	    
        imageBtn = (ImageButton) findViewById(R.id.product_btn_scan);
        
        imageBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProductOverview.this, BarCodeScanner.class);
				startActivity(intent);
				
			}
		});
        
		setTitle("Mobile Product King - Home");
		
		adapter = new ProductBaseAdapter(this, (ArrayList<Products>) ProductKing.getStaticProducts());
		
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
//				editTxt.setText("");
				
				System.out.println("Position clicked: " + position + " " + listView.getItemAtPosition(position));
				updateUserInfo(true, ((Products)listView.getItemAtPosition(position)).getId());
				
				if(ProductKing.getStaticProducts().get(position).getOptin())
				{
					ProductKing.getStaticProducts().get(position).setOptin(false);
				}
				else
				{
					ProductKing.getStaticProducts().get(position).setOptin(true);
				}
				restartActivity();
			}
        });
		
	}

	
	protected void restartActivity() {
		// TODO Auto-generated method stub
		finish();
		startActivity(getIntent());
//		new AsyncLogin(getAct()).execute("http://192.168.0.16:8080/Freebo/product/loginFromApp", editor.getUsername(), editor.getPwd());
		
	}

	private void updateUserInfo(Boolean optIn, Integer productID)
	{
		String loginStr, pwdStr;
		
		editor = new SharedPrefEditor(getAct());
		
        loginStr = editor.getUsername();
        pwdStr = editor.getPwd();
		new AsyncUpdate(getAct(), optIn, productID).execute(loginStr,pwdStr); //http://192.168.0.16:8080
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
