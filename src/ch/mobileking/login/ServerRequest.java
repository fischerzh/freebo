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
		setUpHttpClient();
		new UpdateCumulusInfo().execute();
		
	}
	
	public void startUpdateOptIn()
	{
		
	}
	
	
	public void startLogin(String username, String pwd)
	{
		
		
		
	}
	
	private void sendRegistrationIdToBackend() {
		System.out.println("Send registration ID to Backend!");
		String responseString = null;
		String regid = getRegistrationId(getContext());
		try {
			URI url = new URI("http://192.168.0.16:8080/gcm-demo/register?regId=" + regid);
			HttpGet httpGet = new HttpGet(url);
			// defaultHttpClient
			HttpParams httpParameters = new BasicHttpParams();

			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();

			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				Log.e(getContext().getString(R.string.app_name),
						"Server Call Failed : Got Status Code "
								+ httpResponse.getStatusLine().getStatusCode()
								+ " and ContentType "
								+ httpEntity.getContentType().getValue());
			}

			responseString = EntityUtils.toString(httpEntity);
		} catch (Exception e) {
			Log.e(getContext().getString(R.string.app_name),
					e.toString(), e);
			// add code to handle error
		}

	}
	
	private String getRegistrationId(Context context) {
		
		String registrationId = editor.getRegId();
		if (registrationId.isEmpty()) {
			System.out.println("Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = editor.getAppVersion();
		int currentVersion = Utils.getAppVersion(context);
		if (registeredVersion != currentVersion) {
			System.out.println("App version changed.");
			return "";
		}
		return registrationId;
	}
	
	
	
	private Boolean isLoggedIn()
	{
		return false;
	}
	
	
	private void setUpHttpClient()
	{
		httpClient = new DefaultHttpClient();
		httpGet = new HttpGet(getServerURL());
		
		httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(editor.getUsername(), editor.getPwd()),"UTF-8", false));

	}
	
	private String getHttpResponse()
	{
		HttpResponse httpResponse = null;
		String response = "";
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
		if(response.contains("HTTP Status 401") || response.contains("Error"))
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
			String response = getHttpResponse();
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




}
