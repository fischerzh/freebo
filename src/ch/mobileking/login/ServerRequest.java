package ch.mobileking.login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import ch.mobileking.R;
import ch.mobileking.activity.old.ProductOverview;
import ch.mobileking.activity.old.RecommActivity;
import ch.mobileking.utils.GcmMessage;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.JSONResponse;
import ch.mobileking.utils.ProductKing;
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
	
	CountDownLatch latch;
	
	private HttpClient httpClient;
	private HttpGet httpGet;
	private HttpPost httpPost;
	
	private SharedPrefEditor editor;
	
	private ITaskComplete listener;
	
	private String serverURL;
	
	public ServerRequest(SharedPrefEditor editor)
	{
		this.editor = editor;
	}
	
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
		setUpHttpGet(getServerURL(), editor.getUsername(), editor.getPwd());
		new UpdateCumulusInfo().execute();
		
	}
	
	public void startRegisterUser(String username, String pwd, String mail)
	{
		System.out.println("Start registration to Backend...");
		setServerURL(editor.getRegisterURL()+"?username="+username+"&password="+pwd+"&email="+mail+"&enabled=true"+"&createFromApp=true");
		setUpHttpPost(getServerURL(), username, pwd);
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
	
	public void startUpdateUserSettings( String newPwd, String newMail, Boolean notificationEnabled, Boolean anonymousUser)
	{
		
		setServerURL(editor.getUpdateUserSettingURL()+"?password="+newPwd+"&email="+newMail+"&isNotificationEnabled="+notificationEnabled+"&isAnonymous="+anonymousUser+"&updateFromApp=true");
		System.out.println("updateUserSettings: " +getServerURL());
		setUpHttpPost(getServerURL(), editor.getUsername(), editor.getPwd());

		new UpdateUserSettings(newPwd, newMail, notificationEnabled, anonymousUser).execute();

	}
	
	public void startUpdateLogs()
	{
		System.out.println("Start update Logs to Backend...");
		
		if(Utils.isNetworkAvailable(getContext()))
		{
			System.out.println("userLogData: " + Utils.getUserLogData());
			
			UpdateLogs updater = new UpdateLogs();
			
			System.out.println("Calling UpdateLogs().execute()");
			new UpdateLogs().execute();
					
		}
		else
		{
			System.out.println("Can not sync, no Internet!");
		}
		
	}
	
	public void startUpdateImageToServer(String filePath)
	{
		uploadFileToServer(filePath);
	}
	
	
	private Boolean isLoggedIn()
	{
		return false;
	}
	
	private void setUpHttpClient()
	{
		httpClient = new DefaultHttpClient();
	}
	
	
	private void setUpHttpGet(String url, String user, String pwd)
	{
		
		httpGet = new HttpGet(url);
		
		httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(user, pwd),"UTF-8", false));

	}
	
	private void setUpHttpPost(String url, String user, String pwd)
	{

		httpPost = new HttpPost(url);
		
		httpPost.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(user, pwd),"UTF-8", false));

	}
	
	private String getHttpResponse(HttpGet httpGet, HttpPost httpPost)
	{
		HttpResponse httpResponse = null;
		
		setUpHttpClient();
		
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
	
	private class UpdateUserSettings extends AsyncTask<String, String, String>
	{
		private String  pwd, mail;
		private Boolean enableNoti, enableAnon;
		
		public UpdateUserSettings(String pwd, String mail, Boolean noti, Boolean anon)
		{
//			this.username = username;
			this.pwd = pwd;
			this.mail = mail;
			this.enableNoti = noti;
			this.enableAnon = anon;
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String response = getHttpResponse(null, httpPost);
			System.out.println("UpdateUserSettings: " + response);
			return response;
		}
		
		
		@Override
		protected void onPostExecute(String result) 
		{
			JSONResponse response = getJSONResponse(result);
			if(response.getStatus().toLowerCase().contains("success"))
			{
				System.out.println("SUCCESS UpdateUserSettings: " + response.getException());
				
				editor.setEmail(mail);
				editor.setNotifications(enableNoti);
				editor.setAnonymous(enableAnon);
				
				listener.onLoginCompleted(true, response.getException());
			}
			else
			{
				System.out.println("FAILED UpdateUserSettings: " + response.getException());
				listener.onLoginCompleted(false, response.getException());
			}
		}
	}
	
	
	private class UpdateCumulusInfo extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String response = getHttpResponse(httpGet, null);
			System.out.println("response: "  +response);
			
			return response;
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			Boolean responseOk = checkForErrors(result);

			if(responseOk)
			{
				listener.onUpdateCompleted(true, result);
			}
			else
			{
				listener.onUpdateCompleted(false, result);
			}
		}
		
	}
	
	private class RegisterUser extends AsyncTask<String, String, String>
	{
		private String username, pw, email;
		
		@Override
		protected String doInBackground(String... params) {
			username = params[0];
			pw = params[1];
			email = params[2];
			System.out.println("register, params: " +params[0] + params[1] + params[2]);
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
				editor.setEmail(this.email);
				this.pw="";
				this.username="";
				listener.onUpdateCompleted(true, "User Registrierung erfolgreich!");
			}
			else
			{
				System.out.println("#RegisterUser FAILED: "+result);
				this.pw="";
				this.username="";
				this.email="";
				listener.onUpdateCompleted(false, result);
			}
		}
	}
	
	private class UpdateLogs extends AsyncTask<String, String, String>
	{
		private String uuid; //param0
		private String content; //param1
		private String title; //param2
		private String createDate; //param3
		
		private String username, pwd;
		
		ArrayList<GcmMessage> failedMsgList = new ArrayList<GcmMessage>();
		
		@Override
		protected String doInBackground(String... params) {
			
//			uuid = params[0];
//			content = params[1];
//			title = params[2];
//			createDate = params[3];
			
			System.out.println("doInBackground called");
			
			Boolean allSynced = true;
			
			String errorResponse = "SUCCESS";
			
			username = editor.getUsername();
			pwd = editor.getPwd();
			
			if(Utils.getUserLogData()!=null)
			{
				int size = Utils.getUserLogData().size();
				
				for(int i = 0; i < size; i++)
				{
					GcmMessage msg;
					msg = Utils.getUserLogData().get(i);

					if(!msg.getIsSynced())
					{
						if(sendMessageToServer(msg))
						{
							System.out.println("Set Synced for Message: " +msg.getUuid());
							Utils.getUserLogData().get(i).setIsSynced(true);
						}
						else
						{
							failedMsgList.add(msg);
						}
					}
					
				}
			}
			
			if(Utils.getNotifications()!=null)
			{
				int sizeNotifications = Utils.getNotifications().size();

				for(int i = 0; i < sizeNotifications; i++)
				{
					GcmMessage msg;
					msg = Utils.getNotifications().get(i);
					
					if(!msg.getIsSynced())
					{
						
						if(sendMessageToServer(msg))
						{
							System.out.println("Set Synced for Message: " +msg.getUuid());
							Utils.getNotifications().get(i).setIsSynced(true);
						}
						else
						{
							failedMsgList.add(msg);
						}
						
					}
						
				}
			}

			
			if(failedMsgList.size()>0)
			{
				Utils.getUserLogData().clear();
				Utils.setUserLogData(failedMsgList);
				errorResponse = "FAILED";
			}
			
			return errorResponse;
			
		}
		
		private boolean sendMessageToServer(GcmMessage msg)
		{
			uuid = msg.getUuid();
			content = getEncodedValueForURL(msg.getContent());
			title = getEncodedValueForURL(msg.getTitle());
			createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(msg.getCreateDate());
			createDate = getEncodedValueForURL(createDate);

			setServerURL(editor.getUpdateLogURL()+"?logMessageId="+uuid+"&title='"+title+"'&createDate='"+createDate+"'&message='"+content+"'");
			
			setUpHttpPost(getServerURL(),username, pwd);
			
			System.out.println("Send Message to Server: " + getServerURL());

			String response = getHttpResponse(null, httpPost);
			
			System.out.println("Response from Server: " +response);
			
			if(((JSONResponse)getJSONResponse(response)).getStatus().toLowerCase().contains("success"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			
			if(result.toLowerCase().contains("failed"))
			{
					System.out.println("Update von LogMessages fehlerhaft! " + result);
					for(GcmMessage msg : failedMsgList)
					{
						System.out.println("FAILED MESSAGE: " + msg.getUuid());
					}
			}
			else
			{
				System.out.println("Update von LogMessages erfolgreich! " + result);

			}
		}
		

	}
	
	private void uploadFileToServer(String filePath)
	{
		HttpClient httpclient = new DefaultHttpClient();
		
		File f = new File(filePath);
		FileInputStream fileInputStream = null;
		
		try {
			setUpHttpPost(editor.getUpdateUserFilesURL(), editor.getUsername(), editor.getPwd());
//			HttpPost httpost = new HttpPost();
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("uploadFile", new FileBody(f));
			httpPost.setEntity(entity);
			HttpResponse response = null;
			response = httpclient.execute(httpPost);
			System.out.println("FileUploadResponse: " + response.getEntity().toString());
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   

		   httpclient.getConnectionManager().shutdown();
		   
	}

	private JSONResponse getJSONResponse(String result)
	{
		Gson gson = new Gson();
		
		JSONResponse jsonMessage = gson.fromJson(result, JSONResponse.class); //Product.class
		System.out.println("jsonMessage: " + jsonMessage);
		return jsonMessage;
	}

	private String getEncodedValueForURL(String encode)
	{
		String encodeUrl = null;
		try {
			encodeUrl = URLEncoder.encode(encode, "UTF-8").toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encodeUrl;
	}

}
