package ch.mobileking;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import ch.mobileking.classes.override.ProductBaseAdapter;
import ch.mobileking.login.AsyncLogin;
import ch.mobileking.login.AsyncUpdate;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import ch.mobileking.utils.SharedPrefEditor;


public class ProductOverview extends Activity implements ITaskComplete{
	
	private Activity act;
	
	private ProductBaseAdapter adapter;
	
	private SharedPrefEditor editor;
	
	private BaseActivity baseActivityMenu;
	
	private int prodLayoutResourceId;
	private boolean editVisible = false;
	private boolean canUpdateServer = false;
		
	private ListView listView;
	private View topLevelLayout, progressBarLayout;
	private EditText editTxt;
	private TextView prodUpdateText;
	private ImageButton imageBtn, editBtn, shareBtn;
	private Button deleteBtn;
	private ProgressBar pgBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setAct(this);
		
		baseActivityMenu = new BaseActivity(this);
		
		editor = new SharedPrefEditor(getAct());
		
		System.out.println("ProductOverview called!");
		
//		System.out.println("Extras from Intent: " +getCallingActivity() + getIntent().getExtras());
		
		
		Intent iin= getIntent();
	    Bundle b = iin.getExtras();
	    String barcode = "";
	    if(b!=null)
	    	barcode = (String) b.get("barcode");
		
		setProdLayoutResourceId(R.layout.product_item);
		
		setElements();
		
//		if(getCallingActivity() != null && getIntent().getExtras()!= null)
		if(!barcode.isEmpty())
		{
			showInfoUpdated(false);
			
			System.out.println("Barcode: " + getIntent().getStringExtra("barcode")  );
			Toast.makeText(this, "Bitte warten, wir aktualisieren....: ", Toast.LENGTH_LONG).show();
			updateUserInfo(true, true, barcode);
//			getIntent().getStringExtra("barcode")
//			reloadUserInfo();

		}
	//	else //getCallingActivity().getShortClassName().contains("BarCodeScanner") && getIntent().getExtras()!=null)
		
//		setElements();
		
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		System.out.println("Request Code: " + requestCode);
		System.out.println("Result Code:  " +resultCode);
            if (resultCode == RESULT_OK) {
                // A contact was picked.  Here we will just display it
                // to the user.
            }
    }
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar, menu);
	    return true;
	  } 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_show_help:
	        	baseActivityMenu.showFirstRunMenu();
	            return true;
	        case R.id.action_logout:
	        	baseActivityMenu.logOut();
	            return true;
	        case R.id.action_scan:
	        	startBarcodeScanner();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	public void setElements()
	{
		
		setContentView(R.layout.product_overview_layout);
		
		topLevelLayout = findViewById(R.id.product_menuoverlay_layout);
		
		progressBarLayout = findViewById(R.id.prod_relative_layout);
		progressBarLayout.setVisibility(View.INVISIBLE);
		
		pgBar = (ProgressBar) findViewById(R.id.product_progressBar);
		
		prodUpdateText = (TextView) findViewById(R.id.prod_update_info_text);
		
		if(isFirstTime())
		{
			topLevelLayout.setVisibility(View.INVISIBLE);
		}
		
//        setContentView(R.layout.grid_layout);
		
        listView = (ListView) findViewById(R.id.product_list_view);
        editTxt = (EditText) findViewById(R.id.product_search_box);
	    
//        imageBtn = (ImageButton) findViewById(R.id.product_btn_scan);
//        editBtn = (ImageButton) findViewById(R.id.product_btn_edit);
//        shareBtn = (ImageButton) findViewById(R.id.product_btn_share);
        deleteBtn = (Button) findViewById(R.id.product_btn_delete);

        if(isEditVisible())
        	deleteBtn.setVisibility(View.VISIBLE);
        else
        	deleteBtn.setVisibility(View.INVISIBLE);
        
//        imageBtn.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(ProductOverview.this, BarCodeScanner.class);
//				startActivity(intent);
//				
//			}
//		});
        
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
//				}
//				
//				setElements();
//			}
//		});
        
        deleteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("Delete clicked, UPDATING TO SERVER!");
				updateAllUserInfo();
			}
			
			
		});
        
		setTitle("Product King");
		
		adapter = new ProductBaseAdapter(this, getProdLayoutResourceId(), (ArrayList<Products>) ProductKing.getStaticProducts()); //R.layout.product_item
		
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
//				editTxt.setText("");
				
				System.out.println("Position clicked: " + position + " " + ((Products)listView.getItemAtPosition(position)).getName());
		        Intent intent = new Intent(getApplicationContext(), ProductDetailOverview.class);
//		        Products prod = (Products) listView.getItemAtPosition(position);
		        intent.putExtra("product", String.valueOf(position));
		        setResult(1, intent);
		        startActivityForResult(intent, 1);
			}
        });
		
	}
	
	private boolean isFirstTime()
	{
    Boolean ranBefore = editor.getFirstRun();
    if (!ranBefore) 
    {
    	editor.setIsFirstRun(true);
        topLevelLayout.setVisibility(View.VISIBLE);
        topLevelLayout.setOnTouchListener(new View.OnTouchListener()
        {

			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				topLevelLayout.setVisibility(View.INVISIBLE);
				return false;
			}
	    });

	}
	return ranBefore;
	}
	
	private void startBarcodeScanner()
	{
		Intent intent = new Intent(ProductOverview.this, BarCodeScanner.class);
		startActivityForResult(intent, 1);
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
	
	private void showInfoUpdated(boolean isUpdate)
	{
		if(isUpdate)
		{
			pgBar.setVisibility(View.INVISIBLE);
			progressBarLayout.setVisibility(View.VISIBLE);
			prodUpdateText.setVisibility(View.VISIBLE);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			pgBar.setVisibility(View.VISIBLE);
			progressBarLayout.setVisibility(View.VISIBLE);
			prodUpdateText.setVisibility(View.INVISIBLE);
		}

	}
	
	@Override
	public void onLoginCompleted() {
		// TODO Auto-generated method stub
		showInfoUpdated(true);
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
