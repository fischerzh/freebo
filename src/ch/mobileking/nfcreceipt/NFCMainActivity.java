package ch.mobileking.nfcreceipt;

import java.nio.charset.Charset;
import java.util.Locale;

import ch.mobileking.R;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("NewApi")
public class NFCMainActivity extends Activity implements CreateNdefMessageCallback, OnNdefPushCompleteCallback {

	private EditText textBox;
	private TextView tv;
	private NfcAdapter mAdapter;
	private Button button;
	public static long t1;
	public static long t2;
	public static long t3;
	public static long t4;
	public static int len1;
	public static int len2;
	public static int len3;
	public static int len4;

	
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
	
	@Override
    public NdefMessage createNdefMessage(NfcEvent event) {
		String text = textBox.getText().toString();
		if(text==null || text.trim().length()==0)
			text = "no data";
        NdefMessage msg = new NdefMessage(
        		new NdefRecord[] {createTextRecord(text,Locale.ENGLISH, true)});
        return msg;
    }

	@Override
	public void onNdefPushComplete(NfcEvent event) {
		// TODO Auto-generated method stub
		t1=System.currentTimeMillis();
		Intent intent = new Intent();
		intent.setClass(this, NdefreceiveActivity.class);
		startActivity(intent);
		this.finish();
	}	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);         
		setContentView(R.layout.nfc_activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        textBox = (EditText)findViewById(R.id.editText1);
        tv = (TextView)findViewById(R.id.textView1);
        button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
	    		Bundle b = new Bundle();
//	    		b.putString("jsonstr",jsonstr);
				intent.setClass(NFCMainActivity.this, ShowDetail.class);
//				intent.putExtras(b);
				startActivity(intent);
			}
		});

	    mAdapter = NfcAdapter.getDefaultAdapter(this);
	    mAdapter.setNdefPushMessageCallback(this, this);
	    mAdapter.setOnNdefPushCompleteCallback(this, this);
        
        
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nfc_ndefreceive_menu, menu);
		return true;
	}

}
