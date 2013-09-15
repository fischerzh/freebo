package ch.freebo.login;

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
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import ch.freebo.ProductOverview;
import ch.freebo.utils.SharedPrefEditor;

public class AsyncUpdate extends AsyncTask<String, String, String>{
	
	private Activity act;
	
	private SharedPrefEditor editor;
	
	private String jsonResult;

	private Integer productID;

	private Boolean optIn;
	
	public AsyncUpdate(Activity act, Boolean optIn, Integer productID)
	{
		this.setAct(act);
		this.productID = productID;
		this.optIn = optIn;
	}

	@Override
	protected String doInBackground(String... params) {
		
		System.out.println("Params: " + params[0]+", "+params[1]+", "+params[2]);
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet;
		String response = "";
		if(this.optIn)
		{
			httpGet = new HttpGet(params[0]+"?username="+params[1]+"&productid="+this.productID+"&optin=true"); //"username=test&productid=3&optin="true""
			response = "OPT-IN!";
		}
		else
		{
			httpGet = new HttpGet(params[0]+"?username="+params[1]+"&productid="+this.productID+"&optout=true"); //"username=test&productid=3&optin="true""
			response = "OPT-OUT!";
		}
		httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(params[1], params[2]),"UTF-8", false));

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
		
		if(httpResponse != null)
		{
			responseEntity = httpResponse.getEntity();
			try {
				System.out.println("responseEntity: " + responseEntity.getContent().toString());
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response = response + " SUCCESS!";
		}
		else
		{
			return "FAILED";
		}
		
		return response;
	}
	
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result.contains("FAILED"))
		{
			Toast toast = Toast.makeText(getAct(), "Failed to Login!", Toast.LENGTH_LONG);
			toast.show();
		}
		else
		{
//			Intent intent = new Intent(getAct(), ProductOverview.class);
//			getAct().startActivity(intent);
			Toast toast = Toast.makeText(getAct(), result, Toast.LENGTH_LONG);
			toast.show();
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
