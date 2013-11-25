package ch.mobileking.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import ch.mobileking.LoyaltyCardActivity;
import ch.mobileking.MainActivity;
import ch.mobileking.MainTabActivity;
import ch.mobileking.activity.old.ProductOverview;
import ch.mobileking.activity.old.RecommActivity;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncLogin extends AsyncTask<String, String, String>{
	
	private Activity act;
	
	private SharedPrefEditor editor;
	
	private Utils utils;
	
	private ITaskComplete listener;
	
	private Boolean update = false;
	
	private String jsonResult;
	
	public AsyncLogin(Activity act, Boolean update, ITaskComplete listener)
	{
		this.setAct(act);
		this.update = update;
		this.listener = listener;
		editor = new SharedPrefEditor(getAct());
		utils = new Utils(getAct());
	}
	
	public AsyncLogin(Activity act, Boolean update)
	{
		this.setAct(act);
		this.update = update;
		editor = new SharedPrefEditor(getAct());
	}

	@Override
	protected String doInBackground(String... params) {

		String user = params[0];
		String pwd = params[1];
		
		if(!this.update)
		{
			editor.updateLoginCount();
			System.out.println("Update Login Count..");
		}
		
		System.out.println("Params: "+user+", "+pwd);
		HttpClient httpClient = new DefaultHttpClient();
		
		String loginUrl = editor.getLoginURL();
		if(editor.getFirstRun() || editor.getUsername().contentEquals(""))
		{
			Utils.registerDevice(act);
			loginUrl=loginUrl+getAndroidInfo();
			System.out.println("New Login URL: " + loginUrl);
		}

		httpClient.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 20000);
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, 20000);

		
		HttpGet httpGet = new HttpGet(loginUrl);
		httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(user, pwd),"UTF-8", false));

		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpGet);
		} catch (Exception e) {
			e.printStackTrace();
			return "FAILED: " +e.getMessage();
//			if(listener!=null)
//			{
//				listener.onLoginCompleted(false, e.getMessage());
//			}
		}
		HttpEntity responseEntity = null;
		
		if(httpResponse != null)
		{
			responseEntity = httpResponse.getEntity();
			System.out.println("responseEntity: " + responseEntity.toString());
		}
		else
		{
			return "FAILED";
		}
		if (responseEntity != null) {
			
            // A Simple JSON Response Read
            InputStream instream = null;
			try {
				instream = responseEntity.getContent();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//				listener.onLoginCompleted(false, e.getMessage());
				return "FAILED: " +e.getMessage();
			}
            setJsonResult(convertStreamToString(instream));
            // now you have the string representation of the HTML request
            if(getJsonResult().toLowerCase().contains("failed") || getJsonResult().toLowerCase().contains("error"))
            {
            	System.out.println("JSON Result: " +getJsonResult());
            	return "FAILED";
            }
            else
            {
    			/**LOGIN WORKED, STORE USERNAME AND PASSWORD IN SHARED PREF **/
            	System.out.println("Login worked, set Username/Password");

            	editor.setUsername(user);
    			editor.setPwd(pwd);
            }
            
            System.out.println("result: " + getJsonResult());
            try {
				instream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
		return getJsonResult();
	}
	
	private String getAndroidInfo() {
		String osVersion = android.os.Build.VERSION.RELEASE;
		String manufacturer = android.os.Build.MANUFACTURER;
		String model = android.os.Build.MODEL;
		
		String regId = Utils.getRegistrationId(getAct());
		
		return "?regId="+regId+"&deviceType='"+manufacturer+"'&deviceOs="+osVersion;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		if(result.contains("FAILED"))
		{
			ProductKing.getInstance().addLogMsg("Login failed: " +result);
			listener.onLoginCompleted(false, result);
		}
		else
		{
			parseJSON();

			System.out.println("update after Sync: " +update);
			
			listener.onLoginCompleted(true, "Update");
		}
	}	
	
	private void parseJSON()
	{
		System.out.println("Json Stream reading..");
		Gson gson = new Gson();
//		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(getJSONDataFromFile(), "UTF-8")));
		ProductKing prodKing = null;
		try {
    		prodKing = gson.fromJson(getJsonResult(), ProductKing.class); //Product.class
    		if(prodKing.equals(null))
    			throw new Exception("Parser Error!"+prodKing);
    		System.out.println("ProductKing loaded: " +prodKing);
//    		ProductKing prodKing = gson.fromJson(reader, ProductKing.class); //Product.class
    	}
    	catch (JsonSyntaxException e) {
			System.out.println("JSON Syntax Exception" + e.toString());
			Toast toast = Toast.makeText(getAct(), "JSON Syntax Exception!", Toast.LENGTH_LONG);
			toast.show();
    	}
    	catch (Exception e)	{
    		System.out.println("Exception " + e.toString());
    		Toast toast = Toast.makeText(getAct(), "Exception!", Toast.LENGTH_LONG);
			toast.show();
    	}
		ProductKing.setIsActive(prodKing.getIsactiveapp());
		ProductKing.setStaticProducts(prodKing.getProducts());
		ProductKing.setRecommenderProducts(prodKing.getRecommendations());
		ProductKing.setStaticBadges(prodKing.getBadges());
		ProductKing.setStaticLeaderboard(prodKing.getLeaderboard());
	}
	
	
	private static String convertStreamToString(InputStream is) {
	    /*
	     * To convert the InputStream to String we use the BufferedReader.readLine()
	     * method. We iterate until the BufferedReader return null which means
	     * there's no more data to read. Each line will appended to a StringBuilder
	     * and returned as String.
	     */
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}

	/**
	 * @return the jsonResult
	 */
	public String getJsonResult() {
		return jsonResult;
	}

	/**
	 * @param jsonResult the jsonResult to set
	 */
	public void setJsonResult(String jsonResult) {
		this.jsonResult = jsonResult;
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
