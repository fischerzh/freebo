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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
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
		
		if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC))
	        Toast.makeText(this, "NFC available", Toast.LENGTH_LONG).show();
		else
	        Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show();
		
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
	
	private void onResumeNFC()
	{
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
	    
	    mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

	    // Setup an intent filter for all MIME based dispatches
	    IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
	    IntentFilter ntag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);	    
	    try {
	        ndef.addDataType("*/*");
	    } catch (MalformedMimeTypeException e) {
	        throw new RuntimeException("fail", e);
	    }
	    mFilters = new IntentFilter[] {
	                ndef, ntag
	    };
	    mTechLists = new String[][] { new String[] { MifareUltralight.class.getName(), Ndef.class.getName(), NfcA.class.getName()},
	            new String[] { MifareClassic.class.getName(), Ndef.class.getName(), NfcA.class.getName()}};
		
		String jsonstr = null;
//		jsonstr = processMessage(mPendingIntent);
		
		createNFCBackendCall(jsonstr);
	}
	
	@Override
	public void onNewIntent(Intent intent) {
		 String action = intent.getAction();
		 if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			 processMessage(intent);
		 }		 
	 }
	
	
//	@Override
//    public void onResume()
//    {
//        super.onResume();
//
//        nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
//        
//        onResumeNFC();
//        
//    }
//	
//
//	@Override
//    public void onPause()
//    {
//        super.onPause();
//        nfcAdapter.disableForegroundDispatch(this);
//    }	
	
	private void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage[] msgs;
        NFCMainActivity.t2 = System.currentTimeMillis();
        if (rawMsgs != null) 
        {
            msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) 
            {
                msgs[i] = (NdefMessage) rawMsgs[i];
            }
        } 
        else 
        {
            // Unknown tag type
            byte[] empty = new byte[] {};
            NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
            NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
            msgs = new NdefMessage[] {msg};
        }
        NdefMessage first = msgs[0];
        NdefRecord firstr = first.getRecords()[0];
        
        String type = new String(firstr.getType());
        if(firstr.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(firstr.getType(), NdefRecord.RTD_TEXT)) {
        	jsonstr=parseText(firstr);
        }

	}
	
	private String processMessage(Intent intent)
	{
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage[] msgs;
		if (rawMsgs != null) 
        {
            msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) 
            {
                msgs[i] = (NdefMessage) rawMsgs[i];
            }
        } 
        else 
        {
            // Unknown tag type
            byte[] empty = new byte[] {};
            NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
            NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
            msgs = new NdefMessage[] {msg};
        }
        NdefMessage first = msgs[0];
        NdefRecord firstr = first.getRecords()[0];
        
        String type = new String(firstr.getType());
        String jsonstr;
		if(firstr.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(firstr.getType(), NdefRecord.RTD_TEXT)) {
        	jsonstr=parseText(firstr);
//        	tv0.setText("TTL="+(NFCMainActivity.t2-NFCMainActivity.t1)+",len="+jsonstr.length());
//        	tv.setText(jsonstr);
        }
        else {
        	jsonstr="";
        	tv.setText(""+new String(firstr.getPayload()));
        }
		return jsonstr;
		
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
    
    
    private void createNFCBackendCall(String jsonstr)
    {
    	
		if (jsonstr.trim().length() > 0) {
			try {

				Hashtable hash = MessageGenerator.verifySignedInvoiceOperation(jsonstr);
				String request = (String) hash.get("request");

				// System.out.println(request);
				String type = (String) hash.get("type");
				InvoiceFactory fac = null;
				InvoiceOperation op = null;
				if (type.compareTo("C0401") == 0) {
					fac = new C0401Factory();
					op = fac.getInstance(request);
					// tv2.setText(request);
				} else {
					throw new Exception("error");
				}

//				mdesc.setText(hash.get("mDesc").toString());
//				setContent(op);

				// tv0.setText("T=" + (NFCMainActivity.t4 - NFCMainActivity.t3)
				// + ",pk len=" + NFCMainActivity.len3 + ",len="
				// + NFCMainActivity.len4);
				
				/** REGISTER SHOPPING TO BACKEND SERVER **/
				addShoppingToServer(op);
				
			} catch (Exception e) {
				// tv1.setText("Error");
			}
			
		}
    }
    	
		private void addShoppingToServer(InvoiceOperation io)
		{
//			String[] str = (String[]) ((C0401Operation) io).details.elementAt();
			int size = ((C0401Operation) io).details.size();
			for (int i = 0; i < size; i++) {
				String[] str = (String[]) ((C0401Operation) io).details.elementAt(i);
				//Product Name
				System.out.println("NFC product name: " + str[0]);
				//Product Qty
				System.out.println("NFC product qty: " + str[1]);
				//Product price
				System.out.println("NFC product price: " + str[3]);
				//Product Amount
				System.out.println("NFC product amount: " + str[4]);
			}
			sendServerRequest(io);
		}
		
		private String getNameFromEAN(String productName)
		{
			return nameToEan.get(productName);
		}
		
		private String addElementToURL(String element, String type)
		{
			return "&"+type+"="+element;
		}
		
		private Vector getElements()
		{        
			Vector details = new Vector();
			for (HashMap.Entry<String, String> entry : nameToEan.entrySet()) {
				String key = entry.getKey();
			    String [] str = new String[7];
		        str[0] = key;//("description");
		        str[1] = "2";//("quantity");
		        str[2] = "0.5L";//("Unit");
		        str[3] = "1.25";//("unitPrice");
		        str[4] = "5";//("amount");
				System.out.println("getElements: " + str);

		        details.add(str);
			}

//			for(int i = 0; i < 3; i++)
//			{
//		        String [] str = new String[7];
//		        str[0] = nameToEan.//("description");
//		        str[1] = "2";//("quantity");
//		        str[2] = "0.5L";//("Unit");
//		        str[3] = "1.25";//("unitPrice");
//		        str[4] = "5";//("amount");
////		        str[5] = productinfo.getString("sequenceNumber");
////		        str[6] = productinfo.getString("remark");
//		        details.add(str);
//			}

	        return details;
		}
		
		private void sendServerRequest(InvoiceOperation io )
		{
			
			nameToEan = new HashMap<String, String>();
			nameToEan.put("Coca-Cola Zero", "3");
			nameToEan.put("Rivella Rot","12" );
			
			HttpClient httpClient = new DefaultHttpClient();

			httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 25000);
			httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, 25000);
			
			//http://localhost:8080/Freebo/controlPanel/createShopping?user=2&retailer=2&product=2&anzahl=2&preis=5.5
			
			//localhost:8080/Freebo/controlPanel/createShopping?user=2&product=[5449000131836,7610097111072]&anzahl=[1,3]&preis=[2.5,3.5]&retailer=2
			
			/**
			 * shoppingDate_year:2014, anzahl:[2, 3], salesVerified:Verify, product:[11, 1], 
			 * rejectMessage:, shoppingDate_minute:22, shoppingDate:Sun Feb 02 21:22:00 CET 2014, 
			 * selectedScannedReceipt:30, shoppingDate_day:2, preis:[2.5, 3.5], shoppingDate_hour:21, 
			 * retailerList.name:1, retailerList:[name:1], user:test, shoppingDate_month:2, action:create, controller:controlPanel]
			 * 
			 * 
			 * http://localhost:8080/Freebo/controlPanel/createShopping?user=2&retailer=2&product=5449000131836&product=7610097111072&anzahl=2&anzahl=3&preis=2&preis=3
			 */
			
//			String updateURL = "http://www.sagax.ch:8080/Freebo/controlPanel/createShopping?retailer=3&user="+editor.getUserId();
			
			String updateURL = editor.getUpdateShoppingForNfcURL()+"retailer=3&user="+editor.getUserId();
			
			int size = ((C0401Operation) io).details.size();
			
//			int size = getElements().size();
//			int size = 
			String name = "", qty = "", price = "", amount;
			boolean first = true;
			for (int i = 0; i < size; i++) {
				String[] str = (String[]) ((C0401Operation) io).details.elementAt(i);
//				String[] str = (String[]) getElements().elementAt(i);
				System.out.println("NFC product name: " + str[0]);
				System.out.println("NFC product qty: " + str[1]);
				System.out.println("NFC product price: " + str[3]);
				System.out.println("NFC product amount: " + str[4]);
				
				name += addElementToURL(getNameFromEAN(str[0]), "product");
				qty += addElementToURL(str[1], "anzahl");
				price += addElementToURL(str[3], "preis");
				
				first = false;

			}

			updateURL += name;
			updateURL += qty;
			updateURL += price;
			
			System.out.println("updateURL: " + updateURL);
			
			HttpPost httpPost = new HttpPost(updateURL);
			
			httpPost.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials("admin", "test"),"UTF-8", false));
			
			HttpResponse httpResponse = null;
			
			String response = "";

			try {
				
				httpResponse = httpClient.execute(httpPost);
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			HttpEntity responseEntity = null;
			
			if(httpResponse != null)
			{
				responseEntity = httpResponse.getEntity();
			}
			if (responseEntity != null) {
				
		        InputStream instream = null;
				try {
					
					instream = responseEntity.getContent();
					response = Utils.convertStreamToString(instream);
					instream.close();
					
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Response from createShopping: " + response);
		}
		
    	

}
