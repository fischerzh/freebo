package ch.mobileking.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import ch.mobileking.DemoActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Utils {
	
	private static Context context;
	
	private static SharedPrefEditor editor;
	private static GoogleCloudMessaging gcm;

	
	private static Boolean firstRun;
	private static String username;
	private static String regId;
	
	
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	private static String SENDER_ID = "73370755379";

	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "GCM Demo";
	
	public Utils(Context cont)
	{
		this.context = cont;
		editor = new SharedPrefEditor(cont);
		Utils.firstRun = editor.getFirstRun();
		Utils.setUsername(editor.getUsername());
		Utils.setRegId(editor.getRegId());
	}
	
	public static String convertStreamToString(InputStream is) {

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
	
	public static ProductKing parseJSON(String json)
	{
		System.out.println("Json Stream reading..");
		Gson gson = new Gson();
		ProductKing prodKing = null;
		try {
    		prodKing = gson.fromJson(json, ProductKing.class); //Product.class
    		if(prodKing.equals(null))
    			throw new Exception("Parser Error!"+prodKing);
    		System.out.println("ProductKing loaded: " +prodKing);
    	}
    	catch (JsonSyntaxException e) {
			System.out.println("JSON Syntax Exception" + e.toString());
			prodKing.setException("JSON Syntax Exception" + e.toString());
			return prodKing;
    	}
    	catch (Exception e)	{
    		System.out.println("Exception " + e.toString());
    		prodKing.setException("Exception " + e.toString());
    		return prodKing;
    	}
		ProductKing.setIsActive(prodKing.getIsactiveapp());
		ProductKing.setStaticProducts(prodKing.getProducts());
		ProductKing.setRecommenderProducts(prodKing.getRecommendations());
		ProductKing.setStaticBadges(prodKing.getBadges());
		ProductKing.setStaticLeaderboard(prodKing.getLeaderboard());
		return prodKing;
	}
	
	public static Bitmap loadImage(Products prod)
	{
		String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages";
		File imgFile = new File(file_path, prod.getId()+".png");
		Bitmap myBitmap = null;
		
		if(!imgFile.exists())
		{
			try {
				myBitmap = BitmapFactory.decodeStream((InputStream)new URL(prod.getImagelink()).getContent());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return myBitmap;
	}
	
	public static Bitmap loadImageFromPath(Products prod) {
		String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages";
		File imgFile = new File(file_path, prod.getId()+".png");
		Bitmap myBitmap = null;
		
		if(imgFile.exists())
		{
			System.out.println("Found Image on SD card: " + imgFile.getAbsolutePath());
			
			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		}
		return myBitmap;

	}

	public static boolean imageExists(Products prod)
	{
		String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages";
		File imgFile = new File(file_path, prod.getId()+".png");
		if(imgFile.exists())
		{
			System.out.println("Image exists SD card: " + imgFile.getAbsolutePath());
		}
		else
		{
			System.out.println("Image does not exist on SD card... loading and saving Image to: " + imgFile.getAbsolutePath());
			file_path = saveBitmap(loadImage(prod), prod);
		}
		
		return imgFile.exists();
	}
	

	public static String saveBitmap(Bitmap bmp, Products prod)
	{
		
	    String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages";
        File dir = new File(file_path);
        if(!dir.exists())
           dir.mkdirs();
        File file = new File(dir, prod.getId()+".png");
//		Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
			fOut.flush();
	        fOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    System.out.println("Saved Image to SD card: " +file.getAbsolutePath());
	    return file.getAbsolutePath();
	}
	
	public static void registerDevice(Context cont)
	{
		System.out.println("Register Device!");
		Utils.context = cont;
		// Check device for Play Services APK. If check succeeds, proceed with
		// GCM registration.
		if (checkPlayServices(Utils.context)) {
			gcm = GoogleCloudMessaging.getInstance(Utils.context);
			regId = getRegistrationId(Utils.context);
			System.out.println("Regid: " + regId);
			// sendRegistrationIdToBackend();
			if (regId.isEmpty()) {
				registerInBackground();
			}
		} else {
			System.out.println("No valid Google Play Services APK found");
			Log.i(TAG, "No valid Google Play Services APK found.");
		}
	}
	
	public static void registerInBackground()
	{
		System.out.println("Register in Background!");
		try {
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(Utils.context);
			}
			regId = gcm.register(SENDER_ID);
//			msg = "Device registered, registration ID=" + regId;
			System.out.println("Device registered: " + regId);
			// You should send the registration ID to your server over
			// HTTP, so it
			// can use GCM/HTTP or CCS to send messages to your app.
//			sendRegistrationIdToBackend();

			// Persist the regID - no need to register again.
			storeRegistrationId(Utils.context, regId);
		} catch (IOException e) {
			System.out.println("Error :" + e.getMessage());

		}
	}
	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	public static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	
	/**
	 * Stores the registration ID and the app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private static void storeRegistrationId(Context context, String regId) {
		int appVersion = getAppVersion(context);
		System.out.println("Store regId on app");
		Log.i(TAG, "Saving regId on app version " + appVersion);
		editor.setAppVersion(appVersion);
		editor.setRegId(regId);
	}

	/**
	 * Gets the current registration ID for application on GCM service, if there
	 * is one.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	public static String getRegistrationId(Context context) {
		String registrationId = editor.getRegId();
				
		if (registrationId.isEmpty()) {
			System.out.println("Registration not found.");
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = editor.getAppVersion();
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}
	
	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private static boolean checkPlayServices(Context cont) {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(cont);
		System.out.println("Check play services");
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) cont,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				System.out.println("checkPlayServices: This device is not supported");
				Log.i(TAG, "This device is not supported.");
			}
			return false;
		}
		System.out.println("true");
		return true;
	}

	/**
	 * @return the firstRun
	 */
	public static Boolean getFirstRun() {
		return firstRun;
	}

	/**
	 * @param firstRun the firstRun to set
	 */
	public static void setFirstRun(Boolean firstRun) {
		editor.setIsFirstRun(firstRun);
	}


	/**
	 * @return the username
	 */
	public static String getUsername() {
		return username;
	}


	/**
	 * @param username the username to set
	 */
	public static void setUsername(String username) {
		Utils.username = username;
	}


	/**
	 * @return the regId
	 */
	public static String getRegId() {
		return regId;
	}


	/**
	 * @param regId the regId to set
	 */
	public static void setRegId(String regId) {
		editor.setRegId(regId);
	}



}
