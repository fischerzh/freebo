package ch.mobileking;

import java.util.ArrayList;

import ch.mobileking.classes.override.MessageDialog;
import ch.mobileking.classes.override.ProductBaseAdapter;
import ch.mobileking.login.AsyncLogin;
import ch.mobileking.login.AsyncUpdate;
import ch.mobileking.tabs.MainProductFragment;
import ch.mobileking.tabs.TabsPagerAdapter;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import ch.mobileking.utils.SharedPrefEditor;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class MainTabActivity extends ActionBarActivity implements ActionBar.TabListener, ITaskComplete{

	private static final int BARCODE_REQUEST = 11;
	private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    
    private String[] tabs = { "Meine Produkte", "Recommendations"};
	private View topLevelLayout;
	private SharedPrefEditor editor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity_main);
        
		setTitle("Product King");
		
		editor = new SharedPrefEditor(this);
 
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
 
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
        
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }
 
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
    
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.main, menu);
	    return true;
	  }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_logout:
	            return true;
	        case R.id.action_sync:
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
 
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
//	private void setHelpActive()
//	{
//		DialogFragment newFragment = new MessageDialog();
//	    newFragment.show(getSupportFragmentManager(), "Hilfe");
//	}
//	
//	private void startBarcodeScanner()
//	{
//		Intent intent = new Intent(MainTabActivity.this, BarCodeScanner.class);
//		startActivityForResult(intent, 1);
//	}
//	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
            // code to handle data from CAMERA_REQUEST
		System.out.println("onActivityResult: " +requestCode+", " + resultCode);
		if(resultCode == BARCODE_REQUEST)
		{
        	System.out.println("MainTabActivity, Barcode request: " + resultCode);
        	System.out.println("MainTabActivity, Extras from Barcode: " + getIntent().getStringExtra("barcode"));
//        	mAdapter.getItem(0).
//        	((MainProductFragment)mAdapter.getItem(0)).updateAdapterData();
        	System.out.println("MainTabActivity, Barcode" + data.getStringExtra("barcode"));
        	String barcode = data.getStringExtra("barcode");
			Toast.makeText(this, "Bitte warten, wir aktualisieren....", Toast.LENGTH_LONG).show();
			updateUserInfo(true, true, barcode);
		}

    }
	
	@Override
	public void onLoginCompleted() {
		// TODO Auto-generated method stub
//		clearInfoUpdate();
		System.out.println("LoginCompleted! Restarting Activity...");
//		restartActivity();
		((MainProductFragment)mAdapter.getItem(0)).updateAdapterData();
	}

	@Override
	public void onUpdateCompleted() {
		System.out.println("UpdateCompleted! Restarting Activity...");
		reloadUserInfo();
	}
	
	private void reloadUserInfo()
	{
		new AsyncLogin(this, true, this).execute(editor.getUsername(), editor.getPwd());
	}
	
	private void updateUserInfo(Boolean optIn, Boolean update, String productID)
	{
        new AsyncUpdate(this, optIn, update, productID, this).execute(editor.getUsername(),editor.getPwd()); //http://192.168.0.16:8080
	}

}
