package ch.mobileking;

import java.util.ArrayList;

import ch.mobileking.login.AsyncLogin;
import ch.mobileking.login.AsyncUpdate;
import ch.mobileking.login.ServerRequest;
import ch.mobileking.tabs.MainProductFragment;
import ch.mobileking.tabs.TabsPagerAdapter;
import ch.mobileking.utils.Badge;
import ch.mobileking.utils.GcmMessage;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;


public class MainTabActivity extends ActionBarActivity implements ActionBar.TabListener, ITaskComplete{

	private static final int BARCODE_REQUEST = 11;
	private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private ITaskComplete listener;
    
    private String[] tabs = { "FAVORITEN", "ERRUNGENSCHAFTEN"};
	private View topLevelLayout;
	private SharedPrefEditor editor;
	private boolean doubleBackToExitPressedOnce;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity_main);
        
		setTitle("ProductKing");
		
		editor = new SharedPrefEditor(this);
	    String intentResponse = getIntent().getStringExtra("gcmnotification");
	    String gcmUUID = getIntent().getStringExtra("messageId");
	    System.out.println("Response from GCM: " + intentResponse);
	    System.out.println("UUID Msg from GCM: " +gcmUUID);

//	    if(ProductKing.getNotifications()!=null)
	    if(gcmUUID!=null && !gcmUUID.isEmpty())
	    {
	    	GcmMessage msg = ProductKing.getInstance().getMessageById(gcmUUID);
	    	if(msg!=null)
	    		createAlert(msg.getContent().toString(), "Neuigkeiten", R.drawable.ic_launcher);
	    }
		
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
	        	editor.setPwd("");
	        	editor.setUsername("");
	        	startMainActivity();
	            return true;
	        case R.id.action_user_settings:
	        	Intent i = new Intent(this, UserSettingsActivity.class);
	        	startActivityForResult(i, 1);
	        	return true;
	        case R.id.action_sync:
	        	onSyncRequest();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void startMainActivity()
	{
		Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    	finish();
	}
	
	private void createAlert(String message, String title, int iconId) {
		// Build the dialog
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(title);
		alert.setMessage(message);
		// Create TextView
		final TextView input = new TextView (this);
		alert.setView(input);
		alert.setIcon(iconId);

		alert.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		    // Do something with value!
		  }
		});

		alert.show();
	}
 
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
    	ProductKing.getInstance().addLogMsg(tab.getText()+"TabReselected");

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
    	ProductKing.getInstance().addLogMsg(tab.getText()+"TabSelcted");

		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
	
	public void setTaskListener(ITaskComplete listener)
	{
		System.out.println("Setting Listener from Fragment");
		this.listener = listener;
	}
	
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // code to handle data from CAMERA_REQUEST
    	ProductKing.getInstance().addLogMsg("MainTabActivity.onActivityResult()" +resultCode + data + requestCode);
		
    	System.out.println("MainTabActivity, Barcode request: " + resultCode);
		if(resultCode == BARCODE_REQUEST)
		{
        	System.out.println("MainTabActivity, Barcode" + data.getStringExtra("barcode"));
        	String barcode = data.getStringExtra("barcode");
        	
        	ProductKing.getInstance().addLogMsg("Barcode scanned: " +barcode);
        	
			Toast.makeText(this, "Bitte warten, wir aktualisieren....", Toast.LENGTH_LONG).show();
			this.listener.startUpdate();
			updateUserInfo(true, true, barcode);
		}

    }
	
	@Override
	public void onLoginCompleted(boolean completed, String message) {
		System.out.println("MainTabActivity, LoginCompleted! Restarting Activity...");
	}

	@Override
	public void onUpdateCompleted(boolean completed, String message) {
		System.out.println("MainTabActivity, UpdateCompleted! Restarting Activity...");
//		if(message!=null)
//    		createAlert(message.toString(), "Aktualisierung!", R.drawable.ic_launcher);
		this.listener.onUpdateCompleted(completed, "");
	}
	

	
	private void updateUserInfo(Boolean optIn, Boolean update, String productID)
	{
        new AsyncUpdate(this, optIn, update, productID, this).execute(editor.getUsername(),editor.getPwd()); //http://192.168.0.16:8080
	}

	@Override
	public void startUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startLogin() {
		// TODO Auto-generated method stub
		
	}
	
	public void onSyncRequest()
	{
		System.out.println("MainTabActivity.onSyncRequest()");
		ServerRequest request = new ServerRequest(this, this);
		request.startUpdateLogs();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		onSyncRequest();
	}
	
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
        	ProductKing.getInstance().addLogMsg("Exit clicked");
        	onSyncRequest();
        	
        	Intent intent = new Intent(Intent.ACTION_MAIN);
        	intent.addCategory(Intent.CATEGORY_HOME);
        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(intent);
        	return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        ProductKing.getInstance().addLogMsg("Back Exit clicked 1");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
             doubleBackToExitPressedOnce=false;   

            }
        }, 2000);
    }

}
