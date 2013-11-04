package ch.mobileking.login;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;

public class AsyncUpdate extends AsyncTask<String, String, String>{
	
	private Activity act;
	
	private SharedPrefEditor editor;
	
	private ITaskComplete listener;
	
	private String jsonResult;

	private String productID;

	private Boolean optIn, updateFromRemote = false, updateAll = false;
	
	public AsyncUpdate(Activity act, Boolean optIn, Boolean update, String productID, ITaskComplete listener)
	{
		this.setAct(act);
		this.productID = productID;
		this.optIn = optIn;
		this.updateFromRemote = update;
		this.updateAll = false;
		this.listener = listener;
		
		editor = new SharedPrefEditor(getAct());
	}
	
	public AsyncUpdate(Activity act, Boolean optIn, Boolean updateAll, ITaskComplete listener)
	{
		this.setAct(act);
		this.optIn = optIn;
		this.updateFromRemote = false;
		this.updateAll = updateAll;
		this.listener = listener;
		
		editor = new SharedPrefEditor(getAct());
	}

	@Override
	protected String doInBackground(String... params) {
		
		System.out.println("Params: " + params[0]+", "+params[1]);
		String response = "";
		if(updateAll)
		{
			response = updateAllProducts();
		}
		else
		{
			response = updateSingleProduct(this.productID, this.optIn);
		}
		
		return response;
	}
	
	private String updateAllProducts()
	{
		String response = "";
		for (Products prod : ProductKing.getStaticProducts())
		{
			if(prod.getIsdeleted() && prod.getOptin())
			{
				System.out.println("Update Server");
				response = response+updateSingleProduct(prod.getEan(), !prod.getIsdeleted());
			}
		}
		return response;
	}
	
	private String updateSingleProduct(String ean, Boolean optIn)
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet;
		String response = "";
		String jsonResult = "";
		if(ean.isEmpty() || ean.contentEquals(""))
		{
			return "FAILED";
		}
		if(optIn)
		{
			httpGet = new HttpGet(editor.getUpdateURL()+"?ean="+ean+"&optin=true"); //"username=test&productid=3&optin="true""
			response = "OPT-IN!";
		}
		else
		{
			httpGet = new HttpGet(editor.getUpdateURL()+"?ean="+ean+"&optout=true"); //"username=test&productid=3&optin="true""
			response = "OPT-OUT!";
		}
		httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(editor.getUsername(), editor.getPwd()),"UTF-8", false));

		HttpResponse httpResponse = null;
		try {
			
			httpResponse = httpClient.execute(httpGet);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpEntity responseEntity = null;
		
		ProductKing prodKing = null;
		
		if(httpResponse != null)
		{
			responseEntity = httpResponse.getEntity();
			try {
				
				jsonResult = Utils.convertStreamToString(responseEntity.getContent());
				System.out.println("jsonResult: " + jsonResult);

				prodKing = Utils.parseJSON(jsonResult);
				
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response = response + prodKing.getStatus() + ": "+prodKing.getException();
		}
		else
		{
			return "FAILED";
		}
		return response;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result.contains("FAILED"))
		{
			Toast toast = Toast.makeText(getAct(), result, Toast.LENGTH_LONG);
			toast.show();
			listener.onUpdateCompleted(false);
		}
		else
		{
			if(this.updateFromRemote)
			{
				new AsyncLogin(getAct(), true, listener).execute(editor.getUsername(), editor.getPwd());
			}
			listener.onUpdateCompleted(true);

		}
		
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
