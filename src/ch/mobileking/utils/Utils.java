package ch.mobileking.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import ch.mobileking.DemoActivity;
import ch.mobileking.R;
import ch.mobileking.login.ServerRequest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class Utils {
	
	private static Context context;
	
	private static SharedPrefEditor editor;
	private static GoogleCloudMessaging gcm;

	
	private static Boolean firstRun;
	private static String username;
	private static String regId;
	
	private static List<GcmMessage> notifications;
	private static List<GcmMessage> userLogData;
	
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
	public static final String USER_AVATAR_PNG ="user_avatar";
	public static final String JSON_FILE_NAME = "productKing.json";

	private static String SENDER_ID = "73370755379";

	static final String TAG = "GCM Demo";
	
	private static ITaskComplete listener;
	
	public Utils(Context cont)
	{
		this.context = cont;
		editor = new SharedPrefEditor(cont);
		Utils.firstRun = editor.getFirstRun();
		Utils.setUsername(editor.getUsername());
		Utils.setRegId(editor.getRegId());
	}
	
	
	/**
	 * @return the listener
	 */
	public static ITaskComplete getListener() {
		return listener;
	}
	
	public static void setListener(ITaskComplete listener)
	{
		Utils.listener = listener;
	}

	/**
	 * @return the context
	 */
	public static Context getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public static void setContext(Context context) {
		Utils.context = context;
	}
	
	public static String getPath(String addFolder)
	{
		String file_path = null;
		String state = Environment.getExternalStorageState();
		
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			if(addFolder!=null)
				file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages/"+addFolder;
			else
				file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages";
	        File dir = new File(file_path);
	        if(!dir.exists())
	           dir.mkdirs();
		}
        return file_path;
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
	
	public static JSONResponse parseJSONSmall(String json)
	{
		System.out.println("Json Stream reading..");
		Gson gson = new Gson();
		JSONResponse jsonClass = null;
		try {
			jsonClass = gson.fromJson(json, JSONResponse.class); //Product.class
    		if(jsonClass.equals(null))
    			throw new Exception("Parser Error!");
    		else
    			System.out.println("JSONSmall loaded: " +jsonClass);
    	}
    	catch (JsonSyntaxException e) {
			System.out.println("JSON Syntax Exception" + e.toString());
			e.printStackTrace();
    	}
    	catch (Exception e)	{
    		System.out.println("JSON Parse Exception " + e.toString());
    		e.printStackTrace();
    	}
		return jsonClass;
	}
	
	public static ProductKing parseJSON(String json)
	{
		System.out.println("Json Stream reading..");
		Gson gson = new Gson();
		ProductKing prodKing = null;
		try {
    		prodKing = gson.fromJson(json, ProductKing.class); //Product.class
    		if(prodKing.equals(null))
    			throw new Exception("Parser Error!");
    		else
    			System.out.println("ProductKing loaded: " +prodKing);
    	}
    	catch (JsonSyntaxException e) {
			System.out.println("JSON Syntax Exception" + e.toString());
			e.printStackTrace();
    	}
    	catch (Exception e)	{
    		System.out.println("JSON Parse Exception " + e.toString());
    		e.printStackTrace();
    	}
		ProductKing.setIsActive(prodKing.getIsactiveapp());
		ProductKing.setStaticProducts(prodKing.getProducts());
		ProductKing.setRecommenderProducts(prodKing.getRecommendations());
		ProductKing.setStaticBadges(prodKing.getBadges());
		ProductKing.setStaticLeaderboard(prodKing.getLeaderboard());
		return prodKing;
	}
	
	public static void processFileFromSD(String filePath)
	{
//        bitmap = BitmapFactory.decodeStream((InputStream)new URL(imgUri).getContent());
		System.out.println("processFile..." + filePath);
		
		LoadImageTask newImageLoader = new LoadImageTask(filePath, false);
		newImageLoader.execute();
	}
	
	public static void loadAllImages(ITaskComplete listener) {
		setListener(listener);
		
		LoadImageTask newImageLoader = new LoadImageTask(true);
		newImageLoader.execute();
	}
	
	public static Bitmap loadImage(Products prod)
	{
		
		File imgFile = new File(getPath(null), prod.getId()+".png");
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
		File imgFile = new File(getPath(null), prod.getId()+".png");
		Bitmap myBitmap = null;
		
		if(imgFile.exists())
		{
			System.out.println("Found Image on SD card: " + imgFile.getAbsolutePath());
			
			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		}
		return myBitmap;

	}

	public static Bitmap loadImageFromPath(String fileName) {
		File imgFile = new File(getPath(null), fileName+".png");
		Bitmap myBitmap = null;
		
		if(imgFile.exists())
		{
			System.out.println("Found Image on SD card: " + imgFile.getAbsolutePath());
			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		}
		return myBitmap;

	}
	
//	public static boolean imageExists(Products prod)
//	{
//		String file_path = getPath(null);
//		File imgFile = new File(file_path, prod.getId()+".png");
//		if(imgFile.exists())
//		{
//			System.out.println("Image exists SD card: " + imgFile.getAbsolutePath());
//		}
//		else
//		{
//			System.out.println("Image does not exist on SD card... loading and saving Image to: " + imgFile.getAbsolutePath());
//			file_path = saveBitmap(loadImage(prod), prod);
//		}
//		
//		return imgFile.exists();
//	}
//	
	public static boolean imageExists(String fileName)
	{
		File imgFile = new File(getPath(null), fileName+".png");
		System.out.println("imgFile.exists(): " + imgFile.exists());
		return imgFile.exists();
	}
	

	public static String saveBitmap(Bitmap bmp, Products prod)
	{
		
        File file = new File(getPath(null), prod.getId()+".png");
//		Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 50, fOut);
			fOut.flush();
	        fOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    System.out.println("Saved Image to SD card: " +file.getAbsolutePath());
	    return file.getAbsolutePath();
	}
	
	public static void saveBitmapAsync(Bitmap bmp, String filename, SharedPrefEditor editor)
	{
		SaveImageTask newImageSaver = new SaveImageTask(bmp, filename, editor);
//		new LoadImageTask().execute(filePath);
		newImageSaver.execute();
	}
	
	public static void saveBitmapAsync(Bitmap bmp, String filename)
	{
		SaveImageTask newImageSaver = new SaveImageTask(bmp, filename);
//		new LoadImageTask().execute(filePath);
		newImageSaver.execute();
	}
	
	public static void loadBitmapFromURL(String url, String filename)
	{
		SaveImageTask newImageSaver = new SaveImageTask(url, filename);
		newImageSaver.execute();

	}
	
	public static String saveBitmap(Bitmap bmp, String fileName)
	{
        File file = new File(getPath(null), fileName+".png");
//		Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        FileOutputStream fOut = null;
		try {
				fOut = new FileOutputStream(file);
				if(bmp!=null && fOut!=null)
				{
					bmp.compress(Bitmap.CompressFormat.PNG, 75, fOut);
					fOut.flush();
			        fOut.close();
				}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    System.out.println("Saved Image to SD card: " +file.getAbsolutePath());
	    return file.getAbsolutePath();
	}	
	
	public static void writeJsonResultLocal(Context cont, String file)
	{
		System.out.println("Save JSON local: " +file);
	        FileOutputStream fos;
			try {
				fos = cont.openFileOutput(JSON_FILE_NAME, Context.MODE_PRIVATE);
	            fos.write(file.getBytes());
	            fos.close();
	            
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static ProductKing getProductKingFromLocal()
	{
		System.out.println("Reading JSON from File..");
		FileInputStream fis = null;
		try {
			if(getContext()!=null)
				fis = getContext().openFileInput(JSON_FILE_NAME);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("InputStream: " + fis);
		
		ProductKing prodKing = parseJSON(convertStreamToString(fis));
		return prodKing;
	}
	
	/**
	 * @return the notifications
	 */
	public static List<GcmMessage> getNotifications() {
		if(notifications==null)
			initNotifications();
		return notifications;
	}

	/**
	 * @param notifications the notifications to set
	 */
	public static void initNotifications() {
		Utils.notifications = new ArrayList<GcmMessage>();
	}
	
	public static void addNotificationMsg(String msg, String title, String uuid)
	{
		System.out.println("Adding GCM-Notification-Message " +msg);
		GcmMessage gcmMsg = new GcmMessage(msg, title, uuid);
		getNotifications().add(gcmMsg);
	}
	
	public static GcmMessage getMessageById(String uuid)
	{
		GcmMessage returnMsg = null;
		Boolean found = false;
		for(GcmMessage msg : Utils.notifications)
		{
			System.out.println("msgId: " + msg.getUuid() +"messageCreateDate:" +msg.getCreateDate()+ "messageReadDate: " +msg.getReadDate());
			if(msg.getUuid().contentEquals(uuid) && !msg.getRead())
			{
				returnMsg = msg;
				Utils.notifications.get(Utils.notifications.indexOf(msg)).setRead(true);
				Utils.notifications.get(Utils.notifications.indexOf(msg)).setReadDate(new Date());
				found = true;
				break;
			}
		}

		return returnMsg;
	}
	
	public static void initUserLogData() {
		Utils.userLogData = new ArrayList<GcmMessage>();
	}
	
	/**
	 * @return the userLogData
	 */
	public static List<GcmMessage> getUserLogData() {
		return userLogData;
	}
	
	/**
	 * @param Set the userLogData
	 */
	public static void setUserLogData(List<GcmMessage> logData) {
		Utils.userLogData = logData;
	}

	public static void addLogMsg(String msg)
	{
		GcmMessage gcmMsg = new GcmMessage(msg, "UserActivityLog", "");
		if(getUserLogData()==null)
			initUserLogData();
		getUserLogData().add(gcmMsg);

		System.out.println("Add Log Message: " +msg +  gcmMsg.getCreateDate());
		
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
	
	public static String getRandomMsgId()
	{
		return UUID.randomUUID().toString();
	}
	
	public static boolean isNetworkAvailable(Context cont) 
	{
	    ConnectivityManager connectivityManager = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
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

   class SaveImageTask extends AsyncTask<String, Void, String> {
	   
	   private Bitmap image;
	   private String filename;
	   private SharedPrefEditor editor;
	   private Boolean updateToBackend = false;
	   private Boolean loadFromUrl = false;
	   private String url;
	   
	   public SaveImageTask(Bitmap image, String filename, SharedPrefEditor editor)
	   {
		   this.image = image;
		   this.filename = filename;
		   this.editor = editor;
		   this.updateToBackend = true;
	   }
	   
	   public SaveImageTask(Bitmap image, String filename)
	   {
		   this.image = image;
		   this.filename = filename;
		   this.updateToBackend = false;

	   }
	   
	   public SaveImageTask(String url, String filename)
	   {
		   this.loadFromUrl = true;
		   this.updateToBackend = false;
		   this.url = url;
		   this.filename = filename;
	   }
	   
	   
	   protected String doInBackground(String... params) {
		   
		   saveOneImage(this.url);

	       return "SUCCESS";
	     }
	   
	   private void saveOneImage(String url)
	   {
		   String filePath;

		   if(loadFromUrl)
		   {
				try {
					image = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		   }
		   if(image!=null)
			   filePath = Utils.saveBitmap(image, filename);
		   else
		   {
			   Bitmap icon = BitmapFactory.decodeResource(Utils.getContext().getResources(), R.drawable.no_image_icon);
			   filePath = Utils.saveBitmap(icon, filename);
		   }
		   
		   if(updateToBackend)
		   {
			   ServerRequest request = new ServerRequest( editor);
			   request.startUpdateImageToServer(filePath);
		   }
	   }
	   
	   protected void onPostExecute(String result) {
	         //Do something with bitmap eg:
	    	 System.out.println("Saved Image async finished (including server upload: "+ updateToBackend + " " +result.toString());
	    	 
//	    	 if(Utils.getListener()!=null && result.contains("SUCCESS"))
//	    	 {
//	    		 Utils.getListener().onUpdateCompleted(true, "");
//	    	 }
	     }

   }

  class LoadImageTask extends AsyncTask<String, Void, String> {
	
	private Bitmap image;
	private Boolean loadMultiple = false;
	private String fileName;
	
	public LoadImageTask(String filename, Boolean loadMulti)
	{
		this.fileName = filename;
		this.loadMultiple = loadMulti;
	}
	
	public LoadImageTask(Boolean loadMultiple)
	{
		this.loadMultiple = true;
	}
	
     protected String doInBackground(String... params) {
    	 
    	 if(loadMultiple)
    		 loadMultipleImages();
    	 else
    		 loadImageFromPath(this.fileName);
    	 return "SUCCESS";
     }

     protected void onPostExecute(String result) {
         //Do something with bitmap eg:
    	 System.out.println("Image(s) loaded! " + "LoadedMultiple: " +this.loadMultiple + " Filename: " +this.fileName);
//    	 this.image = result;
    	 if(Utils.getListener()!=null)
    	 {
    		 Utils.getListener().onUpdateCompleted(true,"");
    	 }
     }
     
     private void loadMultipleImages()
     {
  		   for(int i = 0; i < ProductKing.getInstance().getStaticProducts().size(); i++)
  		   {
  			   Products prod = ProductKing.getInstance().getStaticProducts().get(i);
  			   if(!Utils.imageExists(prod.getEan()))
  			   {

				   Bitmap image = loadImageFromUrl(prod.getImagelink(), prod.getEan());
				   ProductKing.getInstance().getStaticProducts().get(i).setProductImage(image);
				   System.out.println("Added image to Product: " +prod.getName() + "  " + prod.getProductImage());
  			   }
  			   
  		   }
  		   
     }
     
     private void loadSingleImage(String path)
     {
    	 setImage(loadImageFromPath(path));
     }
     
     private Bitmap loadImageFromUrl(String uri, String filename)
     {
  	   {
			try {
				image = BitmapFactory.decodeStream((InputStream)new URL(uri).getContent());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
  		   }
  		   if(image!=null)
  			   Utils.saveBitmap(image, filename);
  		   else
  		   {
  			   Bitmap icon = BitmapFactory.decodeResource(Utils.getContext().getResources(), R.drawable.no_image_icon);
  			   Utils.saveBitmap(icon, filename);
  		   }
  		   return image;
     }
     
	 private Bitmap loadImageFromPath(String path){
		 Bitmap bitmap = null;
		 try {
			 System.out.println("Loading Image from Link: " + path);
			 File imgFile = new File(path);
	         bitmap = resizeBitmap(path);
	      } catch (Exception e) {
	          e.printStackTrace();
	    }
		 
		System.out.println("Bitmap resized: " + " height: " + bitmap.getHeight() +  " width: " + bitmap.getWidth());
		
//   	 	Utils.saveBitmap(bitmap, "user_avatar.png");
		
		return bitmap;
	 }
	 
	 public Bitmap resizeBitmap(String fileName) {

		    // First decode with inJustDecodeBounds=true to check dimensions
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    options.inSampleSize = 2;
		    BitmapFactory.decodeFile(fileName, options);

		    // Calculate inSampleSize
		    options.inSampleSize = 2;//calculateInSampleSize(options, R.dimen.user_avatar_width, R.dimen.user_avatar_width);

		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    return BitmapFactory.decodeFile(fileName, options);
		}

	/**
	 * @return the image
	 */
	public Bitmap getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Bitmap image) {
		this.image = image;
	}
 }