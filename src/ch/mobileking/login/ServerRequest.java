package ch.mobileking.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import ch.mobileking.R;
import ch.mobileking.RecommActivity;
import ch.mobileking.activity.old.ProductOverview;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class ServerRequest {
	
	private Activity activity;
	
	private Context context;
	
	private HttpClient httpClient;
	private HttpGet httpGet;
	private HttpPost httpPost;
	
	private SharedPrefEditor editor;
	
	private ITaskComplete listener;
	
	private String serverURL;
	
	public ServerRequest(Activity act, ITaskComplete listener)
	{
		this.activity = act;
		setContext(act);
		this.editor = new SharedPrefEditor(act);
		this.listener = listener;
	}
	
	public void startUdateCumulus()
	{
		setServerURL(editor.getUpdateCumulusURL());
		setUpHttpClient(editor.getUsername(), editor.getPwd());
		new UpdateCumulusInfo().execute();
		
	}
	
	public void startRegisterUser(String username, String pwd, String mail)
	{
		System.out.println("Start registration to Backend...");
		setServerURL(editor.getRegisterURL()+"?username="+username+"&password="+pwd+"&email="+mail+"&enabled=true"+"&createFromApp=true");
		setUpHttpPost();
		System.out.println("registration URL: " +getServerURL());
		new RegisterUser().execute(username, pwd);
	}
	
	public void startUpdateOptIn()
	{
		//@TODO: Refactor from AsyncUpdate
		
	}
	
	
	public void startLogin(String username, String pwd)
	{
		//@TODO: Refactor from AsyncLogin
		
	}
	
	
	private Boolean isLoggedIn()
	{
		return false;
	}
	
	
	private void setUpHttpClient(String user, String pwd)
	{
		httpClient = new DefaultHttpClient();
		httpGet = new HttpGet(getServerURL());
		
		httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(user, pwd),"UTF-8", false));

	}
	
	private void setUpHttpPost()
	{
		httpClient = new DefaultHttpClient();
		httpPost = new HttpPost(getServerURL());
	}
	
	private String getHttpResponse(HttpGet httpGet, HttpPost httpPost)
	{
		HttpResponse httpResponse = null;
		String response = "";
		try {
			if(httpPost!=null)
				httpResponse = httpClient.execute(httpPost);
			else if (httpGet != null)
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
		}
		if (responseEntity != null) {
			
	        // A Simple JSON Response Read
	        InputStream instream = null;
			try {
				
				instream = responseEntity.getContent();
				response = convertStreamToString(instream);
				instream.close();
				
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return response;
	}
	
	public Boolean checkForErrors(String response) {
		if(response.contains("FAILED") || response.contains("ERROR"))
        {
        	return false;
        }
        else
        {
			/**LOGIN WORKED, STORE USERNAME AND PASSWORD IN SHARED PREF **/
//			editor.setUsername(getUsername());
//			editor.setPwd(getPassword());
			return true;
        }
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
	 * @return the httpClient
	 */
	public HttpClient getHttpClient() {
		return httpClient;
	}

	/**
	 * @param httpClient the httpClient to set
	 */
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	/**
	 * @return the serverURL
	 */
	public String getServerURL() {
		return serverURL;
	}

	/**
	 * @param serverURL the serverURL to set
	 */
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	
	/**
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}


	private class Login extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	
	private class UpdateCumulusInfo extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String response = getHttpResponse(httpGet, null);
			System.out.println("response: "  +response);
			Boolean responseOk = checkForErrors(response);
			System.out.println("responseOk: " +responseOk);
			
			listener.onUpdateCompleted(true);
			return response;
//			if(responseOk)
//			{
//				return response;
//				listener.onUpdateCompleted(true);
//			}
//			else
//			{
//				return null;
//			listener.onUpdateCompleted(false);
//			}
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			
			if(result!=null)
			{
				listener.onUpdateCompleted(true);
			}
		}
		
	}
	
	private class RegisterUser extends AsyncTask<String, String, String>
	{
		private String username, pw;
		
		@Override
		protected String doInBackground(String... params) {
			username = params[0];
			pw = params[1];
			System.out.println("register, params" +params[0] + params[1]);
			String response = getHttpResponse(null, httpPost);
			System.out.println("Response from HTTP call: " +response);
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			if(result.toLowerCase().contains("success"))
			{
				System.out.println("#RegisterUser SUCCESS: onPostExecute, got Result: " +result);
				editor.setUsername(this.username);
				editor.setPwd(this.pw);
				this.pw="";
				this.username="";
				listener.onUpdateCompleted(true);
			}
			else
			{
				System.out.println("#RegisterUser FAILED: ");
				this.pw="";
				this.username="";
				listener.onUpdateCompleted(false);
			}
		}
	}




}
