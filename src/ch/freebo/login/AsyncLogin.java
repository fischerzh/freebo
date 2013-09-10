package ch.freebo.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;

import ch.freebo.ProductOverview;
import ch.freebo.R;
import ch.freebo.utils.SharedPrefEditor;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncLogin extends AsyncTask<String, String, String>{
	
	private Activity act;
	
	private SharedPrefEditor editor;
	
	public AsyncLogin(Activity act)
	{
		this.act = act;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		System.out.println("Params: " + params[0]+", "+params[1]+", "+params[2]);
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(params[0]+"?username="+params[1]); //"http://browserspy.dk/password-ok.php"
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
		String result = null;
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
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            result = convertStreamToString(instream);
            // now you have the string representation of the HTML request
            if(result.contains("HTTP Status 401") || result.contains("Error 500"))
            {
            	return "FAILED";
            }
            else
            {
    			/**LOGIN WORKED, STORE USERNAME AND PASSWORD IN SHARED PREF **/
    			editor = new SharedPrefEditor(getAct());
    			
    			editor.setUsername(params[1]);
    			editor.setPwd(params[2]);
            }
            
            System.out.println("result: " + result);
            try {
				instream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
		return result;
	}
	
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result.contains("FAILED"))
		{
			Toast toast = Toast.makeText(getAct(), "Failed to Login or Synchronize App!", Toast.LENGTH_LONG);
			toast.show();
		}
		else
		{
			Intent intent = new Intent(getAct(), ProductOverview.class);
			getAct().startActivity(intent);
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
