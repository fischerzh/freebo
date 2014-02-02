package ch.mobileking.nfcreceipt;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.params.HttpConnectionParams;

import ch.mobileking.R;
import ch.mobileking.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowDetail extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.nfc_show_detail);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		findView();
		setListener();

		Bundle b = this.getIntent().getExtras();
//		String jsonstr = b.getString("jsonstr");
		
		
		
		sendServerRequest();


//		if (jsonstr.trim().length() > 0) {
//			try {
//
//				Hashtable hash = MessageGenerator.verifySignedInvoiceOperation(jsonstr);
//				String request = (String) hash.get("request");
//
//				// System.out.println(request);
//				String type = (String) hash.get("type");
//				InvoiceFactory fac = null;
//				InvoiceOperation op = null;
//				if (type.compareTo("C0401") == 0) {
//					fac = new C0401Factory();
//					op = fac.getInstance(request);
//					// tv2.setText(request);
//				} else {
//					throw new Exception("error");
//				}
//
//				mdesc.setText(hash.get("mDesc").toString());
//				setContent(op);
//
//				// tv0.setText("T=" + (NFCMainActivity.t4 - NFCMainActivity.t3)
//				// + ",pk len=" + NFCMainActivity.len3 + ",len="
//				// + NFCMainActivity.len4);
//				
//				/** REGISTER SHOPPING TO BACKEND SERVER **/
//				addShoppingToServer(op);
//				
//			} catch (Exception e) {
//				// tv1.setText("Error");
//			}
//		}

	}

	
	TextView mdesc, seller, invoiceN, invoiceD, carrier, total;
	ListView invoiceList;
	InvoiceAdapter adapter;
	private HashMap<String, String> nameToEan;

	private void findView() {
//		mdesc = (TextView) findViewById(R.id.mDesc);
//		seller = (TextView) findViewById(R.id.seller);
//		invoiceN = (TextView) findViewById(R.id.invoiceNumber);
//		invoiceD = (TextView) findViewById(R.id.invoiceDate);
//		carrier = (TextView) findViewById(R.id.carrierId1);
//		total = (TextView) findViewById(R.id.totalAmount);
		invoiceList = (ListView) findViewById(R.id.inoviceList);
	}

	private void setListener() {
		// Button mbutton = (Button) findViewById(R.id.button1);
		// mbutton.setOnClickListener(this);
	}

	private void setContent(InvoiceOperation io) {

		seller.setText(((C0401Operation) io).seller);
		invoiceN.setText(((C0401Operation) io).invoiceNumber);
		invoiceD.setText(((C0401Operation) io).invoiceDate);
		carrier.setText(((C0401Operation) io).carrierId1);
		total.setText(((C0401Operation) io).totalAmount);

		
		adapter = new InvoiceAdapter(this, io);
		invoiceList.setAdapter(adapter);

	}
	
	private void addShoppingToServer(InvoiceOperation io)
	{
//		String[] str = (String[]) ((C0401Operation) io).details.elementAt();
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
//		sendServerRequest(io);
	}
	
	private String getNameFromEAN(String productName)
	{
		nameToEan = new HashMap<String, String>();

		nameToEan.put("Coca-Cola Zero", "5449000131836");
		nameToEan.put("Rivella Rot","7610097111072" );
		
		return nameToEan.get(productName);
	}
	
	private String addElementToURL(String element, boolean first)
	{
		String responseElement = "";
		if(!first)
			responseElement +=",";

		return responseElement += element;
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
	        details.add(str);
		}
		
//		for(int i = 0; i < 3; i++)
//		{
//	        String [] str = new String[7];
//	        str[0] = nameToEan.//("description");
//	        str[1] = "2";//("quantity");
//	        str[2] = "0.5L";//("Unit");
//	        str[3] = "1.25";//("unitPrice");
//	        str[4] = "5";//("amount");
////	        str[5] = productinfo.getString("sequenceNumber");
////	        str[6] = productinfo.getString("remark");
//	        details.add(str);
//		}

        return details;
	}
	
//	private void sendServerRequest(InvoiceOperation io )
	private void sendServerRequest()
	{
		
		nameToEan = new HashMap<String, String>();
		
		HttpClient httpClient = new DefaultHttpClient();

		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 25000);
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, 25000);
		
		//http://localhost:8080/Freebo/controlPanel/createShopping?user=2&retailer=2&product=2&anzahl=2&preis=5.5
		
		String updateURL = "http://192.168.0.16:8080/Freebo/controlPanel/createShopping?user=2&salesVerified='Verified'&retailer=3";
//		int size = ((C0401Operation) io).details.size();
        
		int size = getElements().size();
		String name = null, qty = null, price = null, amount;
		boolean first = true;
		for (int i = 0; i < size; i++) {
//			String[] str = (String[]) ((C0401Operation) io).details.elementAt(i);
			String[] str = (String[]) getElements().elementAt(i);
			System.out.println("NFC product name: " + str[0]);
			System.out.println("NFC product qty: " + str[1]);
			System.out.println("NFC product price: " + str[3]);
			System.out.println("NFC product amount: " + str[4]);
			
			name += addElementToURL(getNameFromEAN(str[0]), first);
			qty += addElementToURL(str[1], first);
			price += addElementToURL(str[3], first);
			first = false;

		}
		updateURL += "&product=["+name+"]";
		updateURL += "&anzahl=["+qty+"]";
		updateURL += "&preis=["+price+"]";
		
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
