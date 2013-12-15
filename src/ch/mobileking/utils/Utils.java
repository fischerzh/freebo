package ch.mobileking.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
import ch.mobileking.utils.classes.GcmMessage;
import ch.mobileking.utils.classes.Products;
import ch.mobileking.utils.classes.SalesSlip;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;
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

	private static final int PRODUCT_FAV_REQUEST = 99;
	private static final int SALES_SLIPS_REQUEST = 88;

	public static final String USER_AVATAR_PNG = "user_avatar";
	public static final String JSON_FILE_NAME = "productKing.json";

	public static int[] resourceAvatarId = { R.drawable.no_image_icon,
			R.drawable.ic_avatar_01, R.drawable.ic_avatar_03,
			R.drawable.ic_avatar_05, R.drawable.ic_avatar_06_round,
			R.drawable.ic_avatar_07, R.drawable.ic_avatar_08 };

	private static String SENDER_ID = "73370755379";

	static final String TAG = "GCM Demo";

	private static ITaskComplete listener;

	public Utils(Context cont) {
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

	public static void setListener(ITaskComplete listener) {
		Utils.listener = listener;
	}

	/**
	 * @return the context
	 */
	public static Context getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public static void setContext(Context context) {
		Utils.context = context;
	}

	public static String getPath(String addFolder) {
		String file_path = null;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			if (addFolder != null)
				file_path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/MobileKingImages/" + addFolder;
			else
				file_path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/MobileKingImages";
			File dir = new File(file_path);
			if (!dir.exists())
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

	public static JSONResponse parseJSONSmall(String json) {
		System.out.println("Json Stream reading..");
		Gson gson = new Gson();
		JSONResponse jsonClass = null;
		try {
			jsonClass = gson.fromJson(json, JSONResponse.class); // Product.class
			if (jsonClass.equals(null))
				throw new Exception("Parser Error!");
			else
				System.out.println("JSONSmall loaded: " + jsonClass);
		} catch (JsonSyntaxException e) {
			System.out.println("JSON Syntax Exception" + e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("JSON Parse Exception " + e.toString());
			e.printStackTrace();
		}
		return jsonClass;
	}

	public static ProductKing parseJSON(String json) {
		System.out.println("Json Stream reading..");
		Gson gson = new Gson();
		ProductKing prodKing = null;
		try {
			prodKing = gson.fromJson(json, ProductKing.class); // Product.class
			if (prodKing.equals(null))
				throw new Exception("Parser Error!");
			else
				System.out.println("ProductKing loaded: " + prodKing);
		} catch (JsonSyntaxException e) {
			System.out.println("JSON Syntax Exception" + e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("JSON Parse Exception " + e.toString());
			e.printStackTrace();
		}
		ProductKing.setIsActive(prodKing.getIsactiveapp());
		ProductKing.setStaticProducts(prodKing.getProducts());
		ProductKing.setRecommenderProducts(prodKing.getRecommendations());
		ProductKing.setStaticBadges(prodKing.getBadges());
		ProductKing.setStaticLeaderboard(prodKing.getLeaderboard());
		ProductKing.setStaticSalesSlips(prodKing.getSalesslips());
		return prodKing;
	}

	public static void onSyncRequest() {
		System.out.println("MainTabActivity.onSyncRequest()");
		ServerRequest request = new ServerRequest(getContext(), editor);
		request.startUpdateLogs();
	}

	public static void processFileFromSD(String filePath) {
		// bitmap = BitmapFactory.decodeStream((InputStream)new
		// URL(imgUri).getContent());
		System.out.println("processFile..." + filePath);

		LoadImageTask newImageLoader = new LoadImageTask(null, filePath, false,
				true, null);
		newImageLoader.execute();
	}

	public static void loadAllImagesFromWeb(ITaskComplete listener) {
		setListener(listener);

		LoadImageTask newImageLoader = new LoadImageTask(null, null, true,
				false, null);
		newImageLoader.execute();
	}

	public static Bitmap loadImageFromPath(String fileName) {
		File imgFile = new File(getPath(null), fileName + ".png");
		Bitmap myBitmap = null;

		if (imgFile.exists()) {
			System.out.println("Found Image on SD card: "
					+ imgFile.getAbsolutePath());
			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		}
		return myBitmap;

	}

	public static void loadBitmapFromURL(String url, String filename) {
		LoadImageTask newImageLoader = new LoadImageTask(filename, null, false,
				false, url);
		newImageLoader.execute();

	}

	public static boolean imageExists(String fileName) {
		File imgFile = new File(getPath(null), fileName + ".png");
		System.out.println("imgFile.exists(): " + imgFile.exists());
		return imgFile.exists();
	}

	public static void saveBitmapAsync(Bitmap bmp, String filename,
			SharedPrefEditor editor) {
		SaveImageTask newImageSaver = new SaveImageTask(bmp, filename, editor);
		// new LoadImageTask().execute(filePath);
		newImageSaver.execute();
	}

	public static void saveBitmapAsync(Bitmap bmp, String filename) {
		SaveImageTask newImageSaver = new SaveImageTask(bmp, filename);
		// new LoadImageTask().execute(filePath);
		newImageSaver.execute();
	}

	public static void saveAllBitmapAsync(SharedPrefEditor editor) {
		SaveImageTask newImageSaver = new SaveImageTask(true, true, editor);
		newImageSaver.execute();
	}
	
	public static void saveAllBitmapAsyncOffline(SharedPrefEditor editor) {
		SaveImageTask newImageSaver = new SaveImageTask(false, true, editor);
		newImageSaver.execute();
	}

	public static String saveBitmap(Bitmap bmp, String fileName) {
		File file = new File(getPath(null), fileName + ".png");
		// Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
			if (bmp != null && fOut != null) {
				bmp.compress(Bitmap.CompressFormat.PNG, 75, fOut);
				fOut.flush();
				fOut.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Saved Image to SD card: " + file.getAbsolutePath());
		return file.getAbsolutePath();
	}

	public static void writeJsonResultLocal(String jsonInput) {
		try {
			FileWriter file = new FileWriter(getPath("/json/") + JSON_FILE_NAME);
			file.write(jsonInput);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ProductKing getJsonResultLocal() {
		System.out.println("Reading JSON from File..");
		String mResponse = null;
		try {
			File f = new File(getPath("/json/") + JSON_FILE_NAME);
			if (f.exists()) {
				FileInputStream is = new FileInputStream(f);
				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				mResponse = new String(buffer);
				System.out.println("Json file read: " + mResponse);
			} else {
				System.out.println("JSON File not found!");
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ProductKing prodKing = parseJSON(mResponse);
		return prodKing;
	}

	/**
	 * @return the notifications
	 */
	public static List<GcmMessage> getNotifications() {
		if (notifications == null)
			initNotifications();
		return notifications;
	}

	/**
	 * @param notifications
	 *            the notifications to set
	 */
	public static void initNotifications() {
		Utils.notifications = new ArrayList<GcmMessage>();
	}

	public static void addNotificationMsg(String msg, String title, String uuid) {
		System.out.println("Adding GCM-Notification-Message " + msg);
		GcmMessage gcmMsg = new GcmMessage(msg, title, uuid,
				getLocationInformation());
		getNotifications().add(gcmMsg);
	}

	public static GcmMessage getMessageById(String uuid) {
		GcmMessage returnMsg = null;
		Boolean found = false;
		for (GcmMessage msg : Utils.notifications) {
			System.out.println("msgId: " + msg.getUuid() + "messageCreateDate:"
					+ msg.getCreateDate() + "messageReadDate: "
					+ msg.getReadDate());
			if (msg.getUuid().contentEquals(uuid) && !msg.getRead()) {
				returnMsg = msg;
				Utils.notifications.get(Utils.notifications.indexOf(msg))
						.setRead(true);
				Utils.notifications.get(Utils.notifications.indexOf(msg))
						.setReadDate(new Date());
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
	 * @param Set
	 *            the userLogData
	 */
	public static void setUserLogData(List<GcmMessage> logData) {
		Utils.userLogData = logData;
	}

	public static void addLogMsg(String msg) {

		GcmMessage gcmMsg = new GcmMessage(msg, "UserActivityLog", "",
				getLocationInformation());
		if (getUserLogData() == null)
			initUserLogData();
		getUserLogData().add(gcmMsg);

		System.out.println("Add Log Message: " + msg + gcmMsg.getCreateDate());

	}

	private static String getLocationInformation() {
		String locationResponse = "";

		// Get the location manager
		if (getContext() != null) {
			LocationManager locationManager = (LocationManager) getContext()
					.getSystemService(Context.LOCATION_SERVICE);
			// Define the criteria how to select the locatioin provider -> use
			// default
			Criteria criteria = new Criteria();
			String provider = locationManager.getBestProvider(criteria, true);
			android.location.Location location = locationManager
					.getLastKnownLocation(provider);
			System.out.println("Location " + provider + " has been selected.");

			if (location != null) {
				locationResponse += provider + "; lat: "
						+ location.getLatitude() + "; long: "
						+ location.getLongitude();
			}
			System.out.println("Location: " + locationResponse);
		}

		return locationResponse;
	}

	public static void registerDevice(Context cont) {
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

	public static void registerInBackground() {
		System.out.println("Register in Background!");
		try {
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(Utils.context);
			}
			regId = gcm.register(SENDER_ID);
			// msg = "Device registered, registration ID=" + regId;
			System.out.println("Device registered: " + regId);
			// You should send the registration ID to your server over
			// HTTP, so it
			// can use GCM/HTTP or CCS to send messages to your app.
			// sendRegistrationIdToBackend();

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
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(cont);
		System.out.println("Check play services");
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode,
						(Activity) cont, PLAY_SERVICES_RESOLUTION_REQUEST)
						.show();
			} else {
				System.out
						.println("checkPlayServices: This device is not supported");
				Log.i(TAG, "This device is not supported.");
			}
			return false;
		}
		System.out.println("true");
		return true;
	}

	public static String getRandomMsgId() {
		return UUID.randomUUID().toString();
	}

	public static boolean isNetworkAvailable(Context cont) {
		boolean hasInternet = false;

		ConnectivityManager connectivityManager = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

		hasInternet = activeNetworkInfo != null;
		if (hasInternet) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * @return the firstRun
	 */
	public static Boolean getFirstRun() {
		return firstRun;
	}

	/**
	 * @param firstRun
	 *            the firstRun to set
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
	 * @param username
	 *            the username to set
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
	 * @param regId
	 *            the regId to set
	 */
	public static void setRegId(String regId) {
		editor.setRegId(regId);
	}

}

class SaveImageTask extends AsyncTask<String, Integer, String> {

	private Bitmap image;
	private String filename;
	private SharedPrefEditor editor;
	private Boolean updateToBackend = false;
	private Boolean loadFromUrl = false;
	private Boolean saveMultiple = false;
	private String url;

	public SaveImageTask(Boolean updateToBackend, Boolean saveMultipleImages,SharedPrefEditor editor) {
		this.updateToBackend = updateToBackend;
		this.saveMultiple = saveMultipleImages;
		this.editor = editor;

	}

	public SaveImageTask(Bitmap image, String filename, SharedPrefEditor editor) {
		this.image = image;
		this.filename = filename;
		this.editor = editor;
		this.updateToBackend = true;
	}

	public SaveImageTask(Bitmap image, String filename) {
		this.image = image;
		this.filename = filename;
		this.updateToBackend = false;

	}

	public SaveImageTask(String url, String filename) {
		this.loadFromUrl = true;
		this.updateToBackend = false;
		this.url = url;
		this.filename = filename;
	}

	protected String doInBackground(String... params) {

		if (saveMultiple && updateToBackend)
			saveMultipleSalesSlips();
		else if (saveMultiple && !updateToBackend)
			saveMultipleOffline();
		else
			saveOneImage(this.url);

		return "SUCCESS";
	}

	protected void onPostExecute(String result) {
		// Do something with bitmap eg:
		System.out.println("Saved Image async finished (including server upload: "
						+ updateToBackend + " " + result.toString());

		if (Utils.getListener() != null && result.contains("SUCCESS")) {
			Utils.getListener().onUpdateCompleted(true, "");
		}
	}

	protected void onProgressUpdate(Integer... progress) {
		System.out.println("Send progress update to listener: " + progress[0]);
		Utils.getListener().sendProgressUpdate(progress[0]);

	}

	private String saveOneImage(String url) {
		String filePath = null;
		String response = null;
		if (loadFromUrl) {
			try {
				image = BitmapFactory.decodeStream((InputStream) new URL(url)
						.getContent());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (image != null)
			filePath = Utils.saveBitmap(image, filename);
		else {
			Bitmap icon = BitmapFactory.decodeResource(Utils.getContext()
					.getResources(), R.drawable.no_image_icon);
			filePath = Utils.saveBitmap(icon, filename);
			response = "SUCCESS";
		}

		if (updateToBackend) {
			ServerRequest request = new ServerRequest(editor);
			response = request.startUpdateImageToServer(filePath);
		}
		return response;
	}

	private void saveMultipleSalesSlips() {
		File path = new File(Utils.getPath(null));
		Integer partsToUpload;
		ServerRequest request = new ServerRequest(editor);

		publishProgress(5);
		int size = ProductKing.getInstance().getSalesSlipsParts().size();

		int count = 0;
		for (int i = 0; i < size; i++) {
			SalesSlip slip = ProductKing.getInstance().getSalesSlipsParts().get(i);
			Utils.saveBitmap(slip.getBitmapFile(), slip.getFilename());

			count += 1;
			int currentProgress = (int) Math.round(count * 100.0 / size);
			publishProgress(currentProgress);
			if (!slip.getIsuploaded()) {
				String uploadResponse = request.startUpdateSalesSlipToServer(slip);
				if (uploadResponse.contains("SUCCESS")) {
					ProductKing.getInstance().getSalesSlipsParts().get(i).setIsuploaded(true);
				}
			}
		}
	}
	
	private void saveMultipleOffline() {
		int size = ProductKing.getInstance().getSalesSlipsParts().size();

		int count = 0;
		for (int i = 0; i < size; i++) {
			SalesSlip slip = ProductKing.getInstance().getSalesSlipsParts().get(i);
			Utils.saveBitmap(slip.getBitmapFile(), slip.getFilename());
			ProductKing.getInstance().getStaticSalesSlips().add(slip);
		}
	}

}

class LoadImageTask extends AsyncTask<String, Void, String> {

	private Bitmap image;
	private Boolean loadMultiple = false;
	private Boolean loadFromPath = false;
	private String fileName;
	private String filePath;
	private String url;

	public LoadImageTask(String filename, String filePath,
			Boolean loadMultiFromWeb, Boolean loadFromPath, String url) {
		this.fileName = filename;
		this.filePath = filePath;
		this.loadMultiple = loadMultiFromWeb;
		this.loadFromPath = loadFromPath;
		this.url = url;
	}

	protected String doInBackground(String... params) {

		if (loadMultiple && !loadFromPath)
			loadMultipleImagesFromWeb();
		if (!loadMultiple && !loadFromPath)
			loadSingleImageFromWeb(url, fileName);
		if (loadFromPath)
			loadImageFromExternalPath(fileName);
		return "SUCCESS";
	}

	protected void onPostExecute(String result) {
		// Do something with bitmap eg:
		System.out.println("Image(s) loaded! " + "LoadedMultiple: "
				+ this.loadMultiple + " Filename: " + this.fileName);
		// this.image = result;
		if (Utils.getListener() != null) {
			Utils.getListener().onUpdateCompleted(true, "");
		}
	}

	private void loadMultipleImagesFromWeb() {
		for (int i = 0; i < ProductKing.getInstance().getStaticProducts()
				.size(); i++) {
			Products prod = ProductKing.getInstance().getStaticProducts()
					.get(i);
			if (!Utils.imageExists(prod.getEan())) {

				Bitmap image = loadImageFromUrl(prod.getImagelink(),
						prod.getEan());
				ProductKing.getInstance().getStaticProducts().get(i)
						.setProductImage(image);
				System.out.println("Added image to Product: " + prod.getName()
						+ "  " + prod.getProductImage());
			}

		}

	}

	private void loadSingleImageFromPath(String path) {
		setImage(loadImageFromExternalPath(path));
	}

	private void loadSingleImageFromWeb(String url, String filename) {
		setImage(loadImageFromUrl(url, filename));
	}

	private Bitmap loadImageFromUrl(String uri, String filename) {
		{
			try {
				image = BitmapFactory.decodeStream((InputStream) new URL(uri)
						.getContent());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (image != null)
			Utils.saveBitmap(image, filename);
		else {
			Bitmap icon = BitmapFactory.decodeResource(Utils.getContext()
					.getResources(), R.drawable.no_image_icon);
			Utils.saveBitmap(icon, filename);
		}
		return image;
	}

	private Bitmap loadImageFromExternalPath(String path) {
		Bitmap bitmap = null;
		try {
			System.out.println("Loading Image from external Path: " + path);
			File imgFile = new File(path);
			bitmap = resizeBitmap(path);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Bitmap resized: " + " height: "
				+ bitmap.getHeight() + " width: " + bitmap.getWidth());

		// Utils.saveBitmap(bitmap, "user_avatar.png");

		return bitmap;
	}

	public Bitmap resizeBitmap(String fileName) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = 2;
		BitmapFactory.decodeFile(fileName, options);

		// Calculate inSampleSize
		options.inSampleSize = 2;// calculateInSampleSize(options,
									// R.dimen.user_avatar_width,
									// R.dimen.user_avatar_width);

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
	 * @param image
	 *            the image to set
	 */
	public void setImage(Bitmap image) {
		this.image = image;
	}
}