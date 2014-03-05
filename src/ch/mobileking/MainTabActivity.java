package ch.mobileking;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import ch.mobileking.exception.CustomExceptionHandler;
import ch.mobileking.login.AsyncUpdate;
import ch.mobileking.login.ServerRequest;
import ch.mobileking.nfcreceipt.C0401Factory;
import ch.mobileking.nfcreceipt.C0401Operation;
import ch.mobileking.nfcreceipt.InvoiceAdapter;
import ch.mobileking.nfcreceipt.InvoiceFactory;
import ch.mobileking.nfcreceipt.InvoiceOperation;
import ch.mobileking.nfcreceipt.MessageGenerator;
import ch.mobileking.nfcreceipt.NFCMainActivity;
import ch.mobileking.nfcreceipt.NdefreceiveActivity;
import ch.mobileking.tabs.MainProductFragment;
import ch.mobileking.tabs.TabsPagerAdapter;
import ch.mobileking.userdata.UserSettingsActivity;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;
import ch.mobileking.utils.classes.GcmMessage;
import ch.mobileking.utils.classes.Products;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("NewApi")
public class MainTabActivity extends ActionBarActivity implements ActionBar.TabListener, ITaskComplete, CreateNdefMessageCallback, OnNdefPushCompleteCallback{

	public static final int BARCODE_REQUEST = 11;
	public static final int CAMERA_REQUEST = 22;
	private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private ITaskComplete listener;
	private NfcAdapter nfcAdapter;

    
    private String[] tabs = { "FAVORITEN", "EINKÄUFE"};
	private View topLevelLayout;
	private SharedPrefEditor editor;
	private boolean doubleBackToExitPressedOnce;
	
	// NFC Menu Items
	private EditText textBox;
    private TextView tv;
	TextView mdesc, seller, invoiceN, invoiceD, carrier, total;
	ListView invoiceList;
	InvoiceAdapter adapter;
	private HashMap<String, String> nameToEan;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity_main);
        
		setTitle("ProductKing");
		
		editor = new SharedPrefEditor(this);
		
		if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
		    Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(editor.getUsername()));
		}
		
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		Utils.addLogMsg(getLocalClassName());
		
	    String intentResponse = getIntent().getStringExtra("gcmnotification");
	    String gcmUUID = getIntent().getStringExtra("messageId");
	    System.out.println("Response from GCM: " + intentResponse);
	    System.out.println("UUID Msg from GCM: " +gcmUUID);

	    if(gcmUUID!=null && !gcmUUID.isEmpty())
	    {
	    	GcmMessage msg = Utils.getMessageById(gcmUUID);
	        Utils.addNotificationMsg(msg.getContent(), "UserNotificationRead", "");
	    	if(msg!=null)
	    		createAlert(msg.getContent().toString(), msg.getTitle(), R.drawable.ic_launcher);
	    }
		
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager_main);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        
	    nfcAdapter = NfcAdapter.getDefaultAdapter(this);
	    nfcAdapter.setNdefPushMessageCallback(this, this);
	    nfcAdapter.setOnNdefPushCompleteCallback(this, this);
 
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
                // on changing the page make respected tab selected
                actionBar.setSelectedNavigationItem(position);
	        	Utils.onSyncRequest();

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
	        	Utils.addLogMsg(this.getLocalClassName()+ " onOptionsItemSelected: action_logout");
	        	startMainActivity();
	            return true;
	        case R.id.action_user_settings:
	        	Utils.addLogMsg(this.getLocalClassName()+ " onOptionsItemSelected: action_user_settings");
	        	Intent i = new Intent(this, UserSettingsActivity.class);
	        	startActivityForResult(i, 1);
	        	return true;
//	        case R.id.action_sync:
//	        	Utils.onSyncRequest();
//	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void startMainActivity()
	{
		Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    	editor.setPwd("");
    	editor.setUsername("");
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
    	Utils.addLogMsg(tab.getText()+"TabReselected");

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
    	Utils.addLogMsg(tab.getText()+"TabSelcted");

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

    	System.out.println("MainTabActivity, Barcode request: " + resultCode);
		if(resultCode == BARCODE_REQUEST)
		{
        	System.out.println("MainTabActivity, Barcode" + data.getStringExtra("barcode"));
        	String barcode = data.getStringExtra("barcode");
        	
        	Utils.addLogMsg(this.getLocalClassName()+ " BARCODE_REQUEST: " +barcode);
        	
			this.listener.startUpdate();
			updateUserInfo(true, true, barcode);
		}
		
		if(resultCode == CAMERA_REQUEST)
		{
        	String fileName = data.getStringExtra("salesslip");

        	System.out.println("MainCameraScanFragment, SalesSlips scanned: " +fileName);
        	
        	Utils.addLogMsg(this.getLocalClassName()+ " CAMERA_REQUEST: " +fileName);

			if(Utils.isNetworkAvailable(this)){
	        	this.listener.startUpdate();
        		createAlert("Besten Dank, Dein Einkauf wird uns übermittelt. Wir werden diesen in kürze prüfen!\n", "Einkauf registriert!", R.drawable.ic_store_hero );
				updateSalesSlipInfo();

			}
			else
			{
        		createAlert("Kein Internet vorhanden!", "Offline!", R.drawable.ic_empfehlungen );
        		updateSalesSlipInfoOffline();
			}

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
		this.listener.onUpdateCompleted(completed, message);
	}
	

	
	private void updateUserInfo(Boolean optIn, Boolean update, String productID)
	{
        new AsyncUpdate(this, optIn, update, productID, this).execute(editor.getUsername(),editor.getPwd()); //http://192.168.0.16:8080
	}
	
	private void updateSalesSlipInfo()
	{
		Utils.setListener(this.listener);
		Utils.saveAllBitmapAsync(editor);
	}
	
	private void updateSalesSlipInfoOffline()
	{
		Utils.saveAllBitmapAsyncOffline(editor);
	}

	@Override
	public void startUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startLogin() {
		// TODO Auto-generated method stub
		
	}

	
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
        	Utils.addLogMsg(this.getLocalClassName()+": Exit clicked");
        	Intent intent = new Intent(Intent.ACTION_MAIN);
        	intent.addCategory(Intent.CATEGORY_HOME);
        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(intent);
        	return;
        }
    	Utils.onSyncRequest();
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        Utils.addLogMsg(this.getLocalClassName()+": Back Exit clicked 1");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
             doubleBackToExitPressedOnce=false;   

            }
        }, 2000);
    }


	@Override
	public void sendProgressUpdate(int progress) {
		// TODO Auto-generated method stub
		
	}
	
	NdefMessage nfcMessage;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	private String jsonstr;
	// NEW NFC IMPLEMENTATOIN
	@Override
    public NdefMessage createNdefMessage(NfcEvent event) {
		System.out.println("createNDefMessage: " + event);
		textBox = (EditText)findViewById(R.id.nfc_editText1);
        tv = (TextView)findViewById(R.id.textView1);
        
		String text = textBox.getText().toString();
		if(text==null || text.trim().length()==0)
			text = "no data";
        NdefMessage msg = new NdefMessage(new NdefRecord[] {createTextRecord("12345678",Locale.ENGLISH, true)});
        nfcMessage = msg;
        System.out.println("nfcMessage: " +msg);
        return msg;
    }

	@Override
	public void onNdefPushComplete(NfcEvent event) {
		System.out.println("onNdefPushComplete: " + event);
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(this, NdefreceiveActivity.class);
		startActivity(intent);
		
		this.finish();
	}	
	
	
    public static String parseText(NdefRecord record){
    	//Validation
    	try {                
    		byte[] payload = record.getPayload();                               
        
    		String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
    		//Get the Language Code                
    		int languageCodeLength = payload[0] & 0077;                
    		String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
    		//Get the Text                
    		String text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    		return text;
    	} catch(Exception e) { 
    		//throw new RuntimeException("Record Parsing Failure!!");     
    		return "";
    	}
    }
	
    public static NdefRecord createTextRecord(String payload, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = payload.getBytes(utfEncoding);
        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
        NdefRecord.RTD_TEXT, new byte[0], data);
        return record;
    }
    

}
