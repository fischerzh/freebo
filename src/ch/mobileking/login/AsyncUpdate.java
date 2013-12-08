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
import ch.mobileking.utils.JSONResponse;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;
import ch.mobileking.utils.classes.Products;

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
		String responseMessage = "";
		Boolean allUpdated = false;
		for (Products prod : ProductKing.getStaticProducts())
		{
			allUpdated = true;
			if(prod.getIsdeleted() && prod.getOptin())
			{
				response = updateSingleProduct(prod.getEan(), !prod.getIsdeleted());
				System.out.println("Response: " + response);
				responseMessage += response +prod.getName()+"\n";
				
			}
		}
		if(response.contains("FAILED"))
			allUpdated = false;
		if(allUpdated)
			return responseMessage +"\n\n"+"Schade, Du sammelst jetzt keine Punkte mehr!";
		else
			return "Fehler beim aktualisieren!\n" + responseMessage;
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
		
		JSONResponse parseJSONSmall = null;
		
		if(httpResponse != null)
		{
			responseEntity = httpResponse.getEntity();
			try {
				
				jsonResult = Utils.convertStreamToString(responseEntity.getContent());
				System.out.println("jsonResult: " + jsonResult);
	            if(httpResponse.getStatusLine().getStatusCode() >= 300)
	            	return "FAILED: " + httpResponse.getStatusLine().getStatusCode() + " - " + httpResponse.getStatusLine().getReasonPhrase();
	            else
	            	parseJSONSmall = Utils.parseJSONSmall(jsonResult);
				
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(optIn)
			{
				if(parseJSONSmall.getStatus().contentEquals("SUCCESS"))
					response = parseJSONSmall.getException() + "\nViel Spass beim Punkte sammeln!";
				else
					response = response + parseJSONSmall.getStatus() + ": "+parseJSONSmall.getException();
			}
			else
			{
				if(parseJSONSmall.getStatus().contentEquals("SUCCESS"))
					response = parseJSONSmall.getException();
				else
					response = response + parseJSONSmall.getStatus() + ": "+parseJSONSmall.getException();
			}
				
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
			listener.onUpdateCompleted(false, result);
		}
		else
		{
//			if(this.updateFromRemote)
//			{
//				new AsyncLogin(getAct(), true, listener).execute(editor.getUsername(), editor.getPwd());
//			}
			listener.onUpdateCompleted(true, result);

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
