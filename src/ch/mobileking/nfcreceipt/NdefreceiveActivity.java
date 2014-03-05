package ch.mobileking.nfcreceipt;

import java.util.Arrays;

import ch.mobileking.R;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("NewApi")
public class NdefreceiveActivity extends Activity implements OnClickListener{
	private TextView tv;
	private TextView tv0;

	private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
	private Button mbutton;


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

    String jsonstr;
	private ProgressBar nfc_ndefreceive_progress;
	
    public void onClick(View v) {
    	switch(v.getId()) {
    	case R.id.button1:
			Intent intent = new Intent();
    		Bundle b = new Bundle();
    		System.out.println("json onClick: " + jsonstr);
    		b.putString("jsonstr",jsonstr);
			intent.setClass(this, ShowDetail.class);
			intent.putExtras(b);
			startActivity(intent);
			this.finish();
    	}
    	
    }    
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfc_ndefreceive);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        nfc_ndefreceive_progress = (ProgressBar) findViewById(R.id.nfc_ndefreceive_progress);
        nfc_ndefreceive_progress.setVisibility(View.VISIBLE);
        
	    mAdapter = NfcAdapter.getDefaultAdapter(this);
	    
        tv = (TextView)findViewById(R.id.textView1);
        tv0 = (TextView)findViewById(R.id.textView0);

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


	}

	@Override
    public void onResume()
    {
        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        
    }

	@Override
    public void onPause()
    {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
    }	
	
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
        else {
        	jsonstr="";
        	tv.setText(""+new String(firstr.getPayload()));
        }
        nfc_ndefreceive_progress.setVisibility(View.INVISIBLE);
        
        Intent newIntent = new Intent();
		Bundle b = new Bundle();
		System.out.println("json onClick: " + jsonstr);
		b.putString("jsonstr",jsonstr);
		newIntent.setClass(this, ShowDetail.class);
		newIntent.putExtras(b);
		startActivity(newIntent);
		this.finish();
        
	}
	
	@Override
	public void onNewIntent(Intent intent) {
		System.out.println("New Intent: " +intent);
		 String action = intent.getAction();
		 if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			 processIntent(intent);
		 }		 
	 }

	

}
