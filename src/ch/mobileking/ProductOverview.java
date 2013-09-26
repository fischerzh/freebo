package ch.mobileking;

import java.util.ArrayList;

import ch.mobileking.R;
import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.classes.override.ProductBaseAdapter;
import ch.mobileking.login.AsyncLogin;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ProductOverview extends Activity implements ITaskComplete{
	
	private Activity act;
	
	private ProductBaseAdapter adapter;
	
	private SharedPrefEditor editor;
	
	private BaseActivity baseActivityMenu;
	
	private int prodLayoutResourceId;
	private boolean editVisible = false;
	private boolean canUpdateServer = false;
		
	private ListView listView;
	private EditText editTxt;
	private ImageButton imageBtn, editBtn, shareBtn;
	private Button deleteBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setAct(this);
		
		baseActivityMenu = new BaseActivity(this);
		
		editor = new SharedPrefEditor(getAct());
		
		System.out.println("ProductOverview called!");
		
		System.out.println("Extras from Intent: " +getCallingActivity() + getIntent().getExtras());
		
		setProdLayoutResourceId(R.layout.product_item);
		if(getCallingActivity() != null && getIntent().getExtras()!= null)
		{
			System.out.println("Barcode 1: " + getIntent().getStringExtra("barcode")  );
			Toast.makeText(this, "Barcode scanned: " + getIntent().getStringExtra("barcode"), Toast.LENGTH_LONG).show();
			updateUserInfo(true, true, getIntent().getStringExtra("barcode"));
//			reloadUserInfo();

		}
	//	else //getCallingActivity().getShortClassName().contains("BarCodeScanner") && getIntent().getExtras()!=null)
		
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
		
//		setContentView(R.layout.product_overview_layout);
		
        setContentView(R.layout.grid_layout);

		
        //setContentView(R.layout.products_main);
		
//        listView = (ListView) findViewById(R.id.product_list_view);
//        editTxt = (EditText) findViewById(R.id.product_search_box);
//	    
//        imageBtn = (ImageButton) findViewById(R.id.product_btn_scan);
//        editBtn = (ImageButton) findViewById(R.id.product_btn_edit);
//        shareBtn = (ImageButton) findViewById(R.id.product_btn_share);
//        
//        deleteBtn = (Button) findViewById(R.id.product_btn_delete);
//        
//        if(isEditVisible())
//        	deleteBtn.setVisibility(View.VISIBLE);
//        else
//        	deleteBtn.setVisibility(View.INVISIBLE);
//        
//        imageBtn.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(ProductOverview.this, BarCodeScanner.class);
//				startActivity(intent);
//				
//			}
//		});
//        
//        editBtn.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(!isEditVisible())
//				{
//					setEditVisible(true);
//					setProdLayoutResourceId(R.layout.product_item_edit);
//				}
//				else
//				{
//					setEditVisible(false);
//					setProdLayoutResourceId(R.layout.product_item);
//
//				}
//				
//				setElements();
//			}
//		});
//        
//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				System.out.println("Delete clicked, UPDATING TO SERVER!");
//				updateAllUserInfo();
//			}
//			
//			
//		});
        
		setTitle("MobileKing");
		
 
        GridView gridView = (GridView) findViewById(R.id.grid_view);
 
        // Instance of ImageAdapter Class
        gridView.setAdapter(new ImageAdapter(this,  (ArrayList<Products>) ProductKing.getStaticProducts()));
        
		
//		adapter = new ProductBaseAdapter(this, getProdLayoutResourceId(), (ArrayList<Products>) ProductKing.getStaticProducts()); //R.layout.product_item
		
//        listView.setAdapter(adapter);
//        
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
////				editTxt.setText("");
//				
//				System.out.println("Position clicked: " + position + " " + listView.getItemAtPosition(position));
//		        Intent intent = new Intent(getApplicationContext(), ProductDetailOverview.class);
//		        intent.putExtra("product", position);
//		        setResult(1, intent);
//		        startActivityForResult(intent, 1);
//			}
//        });
		
	}

	
	private void restartActivity() {
		// TODO Auto-generated method stub
		finish();
		startActivity(getIntent());
	}
	
	private void reloadUserInfo()
	{
		new AsyncLogin(getAct(), true, this).execute(editor.getUsername(), editor.getPwd());
	}
	

	private void updateUserInfo(Boolean optIn, Boolean update, String productID)
	{
        new AsyncUpdate(getAct(), optIn, update, productID, this).execute(editor.getUsername(),editor.getPwd()); //http://192.168.0.16:8080
	}
	
	private void updateAllUserInfo()
	{
        new AsyncUpdate(getAct(), false, true, this).execute(editor.getUsername(),editor.getPwd()); //http://192.168.0.16:8080
	}
	
	@Override
	public void onLoginCompleted() {
		// TODO Auto-generated method stub
		System.out.println("LoginCompleted! Restarting Activity...");
		restartActivity();
	}

	@Override
	public void onUpdateCompleted() {
		System.out.println("UpdateCompleted! Restarting Activity...");
		reloadUserInfo();
	}
	
    @Override
    public void onBackPressed() {
    	System.out.println("Back pressed");
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

	/**
	 * @return the prodLayoutResourceId
	 */
	public int getProdLayoutResourceId() {
		return prodLayoutResourceId;
	}

	/**
	 * @param prodLayoutResourceId the prodLayoutResourceId to set
	 */
	public void setProdLayoutResourceId(int prodLayoutResourceId) {
		this.prodLayoutResourceId = prodLayoutResourceId;
	}

	/**
	 * @return the editVisible
	 */
	public boolean isEditVisible() {
		return editVisible;
	}

	/**
	 * @param editVisible the editVisible to set
	 */
	public void setEditVisible(boolean editVisible) {
		this.editVisible = editVisible;
	}

	/**
	 * @return the canUpdateServer
	 */
	public boolean isCanUpdateServer() {
		return canUpdateServer;
	}

	/**
	 * @param canUpdateServer the canUpdateServer to set
	 */
	public void setCanUpdateServer(boolean canUpdateServer) {
		this.canUpdateServer = canUpdateServer;
	}


}
