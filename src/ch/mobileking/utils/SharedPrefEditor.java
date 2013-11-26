package ch.mobileking.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefEditor {
	
	private static String testServer = "http://192.168.0.16:8080";
	private static String liveServer = "http://www.sagax.ch:8080";
	private static String prefix = liveServer+"/Freebo/";
	private static String updateURL = prefix+"updateFavorites"; //192.168.0.16:8080;
	private static String loginURL = prefix+"loginFromApp"; //192.168.0.16:8080;
	private static String updateUserInfo = prefix+"updateUserInfo";
	private static String updateCumulusURL = prefix+"updateCumulusInfo"; //192.168.0.16:8080;
	private static String updateLogURL = prefix+"updateUserLog";
	private static String registerURL = prefix+"user/create";

	
	private static final String PREFS_PWD = "Password";
	private static final String PREFS_USERNAME = "Username";
	private static final String PREFS_MAIL = "Email";
	private static final String PREFS_FIRST_LOGIN = "Login";
	private static final String PREFS_STAY_LOGGED_IN = "LoggedIn";
	private static final String PREFS_REGID = "Registration";
	private static final String PREFS_APPV = "Appversion";
	private static final String PREFS_LOGIN_CNT = "LoginCount";
	private static final String PREFS_NOTIFICATIONS = "Notifications";
	
	private Context cont;
	
	SharedPreferences sharedPrefs;
	
	public SharedPrefEditor(Context context)
	{
		this.cont = context;
//		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(cont);
		
	}
	
	public void setUsername(String username)
	{	    
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_USERNAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
//		sharedPrefs.edit().putString(PREFS_USERNAME, username);
	    editor.putString(PREFS_USERNAME, username);
	    System.out.println("setSharedPref #USERNAME ");
	    // Commit the edits!
//	    sharedPrefs.edit().commit();
	    editor.commit();

	}
	
	public String getUsername()
	{
	    String username;
	    // Restore preferences
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_USERNAME, 0);
	    username = settings.getString(PREFS_USERNAME, "");
	    System.out.println("getSharedPref #USERNAME" + username);
		
		return username;
//		return sharedPrefs.getString(PREFS_USERNAME, "");
	}
	
	public void setPwd(String pwd)
	{	    
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_PWD, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString(PREFS_PWD, pwd);
//		sharedPrefs.edit().putString(PREFS_PWD, pwd);
	    System.out.println("setSharedPref #PWD");
	    // Commit the edits!
//	    sharedPrefs.edit().commit();
	    editor.commit();

	}
	
	public String getPwd()
	{
	    String pwd;
	    // Restore preferences
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_PWD, 0);
	    pwd = settings.getString(PREFS_PWD, "");
	    System.out.println("getSharedPref #PWD");
		
		return pwd;
//		return sharedPrefs.getString(PREFS_PWD, "");
	}
	
	public void setEmail(String mail)
	{	    
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_MAIL, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString(PREFS_MAIL, mail);
	    System.out.println("setSharedPref #PREFS_MAIL " + mail);
	    editor.commit();

	}
	
	public String getEmail()
	{
	    String mail;
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_MAIL, 0);
	    mail = settings.getString(PREFS_MAIL, "");
	    System.out.println("getSharedPref #PREFS_MAIL " + mail);
		
		return mail;
	}
	
	public void setIsFirstRun(Boolean firstRun)
	{	    
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_FIRST_LOGIN, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean(PREFS_FIRST_LOGIN, firstRun);
	    System.out.println("setSharedPref #FIRST_RUN: " + firstRun);
	    // Commit the edits!
	    editor.commit();
	    
	}
	
	public Boolean getFirstRun()
	{
	    Boolean firstRun;
	    // Restore preferences
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_FIRST_LOGIN, 0);
	    Boolean tmp = null;
	    firstRun = settings.getBoolean(PREFS_FIRST_LOGIN, true);
	    System.out.println("getSharedPref #FIRST_RUN: " + firstRun);
		if(firstRun!=null)
			return firstRun;
		else
			return true;
	}
	
	public void setStayLoggedIn(Boolean stayLoggedIn)
	{	    
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_STAY_LOGGED_IN, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean(PREFS_STAY_LOGGED_IN, stayLoggedIn);
	    System.out.println("setSharedPref #STAY_LOGGED_IN: " + stayLoggedIn);
	    // Commit the edits!
	    editor.commit();
	    
	}
	
	public Boolean getStayLoggedIn()
	{
	    Boolean loggedIn;
	    // Restore preferences
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_STAY_LOGGED_IN, 0);
	    loggedIn = settings.getBoolean(PREFS_STAY_LOGGED_IN, true);
	    System.out.println("getSharedPref #STAY_LOGGED_IN: " + loggedIn);
		if(loggedIn!=null)
			return loggedIn;
		else
			return true;
	}

	public void setRegId(String regId)
	{	    
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_REGID, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString(PREFS_REGID, regId);
	    System.out.println("setRegistrationId #REGID: " + regId);
	    // Commit the edits!
	    editor.commit();
	    
	}
	
	public String getRegId()
	{
	    String regId;
	    // Restore preferences
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_REGID, 0);
	    regId = settings.getString(PREFS_REGID, "");
	    System.out.println("getRegistrationId #REGID: " + regId);
		
		return regId;
	}
	
	public void setAppVersion(int appVersion)
	{	    
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_APPV, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putInt(PREFS_APPV, appVersion);
	    System.out.println("setAppVersion #APPV: " + appVersion);
	    // Commit the edits!
	    editor.commit();
	    
	}
	
	public int getAppVersion()
	{
	    int appVersion;
	    // Restore preferences
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_APPV, 0);
	    appVersion = settings.getInt(PREFS_APPV, Integer.MIN_VALUE);
	    System.out.println("getAppVersion #APPV: " + appVersion);
		return appVersion;
	}
	
	public void updateLoginCount()
	{	    
		int loginCnt = getLoginCount();
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_LOGIN_CNT, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putInt(PREFS_LOGIN_CNT, loginCnt+1);
	    System.out.println("setAppVersion #LOGIN_CNT: " + loginCnt+1);
	    // Commit the edits!
	    editor.commit();
	    
	}
	
	public int getLoginCount()
	{
	    int loginCnt;
	    // Restore preferences
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_LOGIN_CNT, 0);
	    loginCnt = settings.getInt(PREFS_LOGIN_CNT, Integer.MIN_VALUE);
	    System.out.println("getAppVersion #LOGIN_CNT: " + loginCnt);
		return loginCnt;
	}
	
	public void setNotifications(Boolean notifications)
	{	    
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_NOTIFICATIONS, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean(PREFS_NOTIFICATIONS, notifications);
	    System.out.println("setSharedPref #PREFS_NOTIFICATIONS: " + notifications);
//		sharedPrefs.edit().putBoolean(PREFS_NOTIFICATIONS, notifications);
	    // Commit the edits!
//		sharedPrefs.edit().commit();
	    editor.commit();

	}
	
	public Boolean getNotifications()
	{
	    Boolean notifications;
	    // Restore preferences
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_NOTIFICATIONS, 0);
	    notifications = settings.getBoolean(PREFS_NOTIFICATIONS, false);
//	    return sharedPrefs.getBoolean(PREFS_NOTIFICATIONS, false);
	    return notifications;
	}
	
	/**
	 * @return the activeURL
	 */
	public String getUpdateURL() {
		return updateURL;
	}

	/**
	 * @return the loginURL
	 */
	public String getLoginURL() {
		return loginURL;
	}

	/**
	 * @return the updateCumulusURL
	 */
	public String getUpdateCumulusURL() {
		return updateCumulusURL;
	}

	/**
	 * @return the registerURL
	 */
	public static String getRegisterURL() {
		return registerURL;
	}

	/**
	 * @return the updateLogURL
	 */
	public static String getUpdateLogURL() {
		return updateLogURL;
	}

	/**
	 * @param updateLogURL the updateLogURL to set
	 */
	public static void setUpdateLogURL(String updateLogURL) {
		SharedPrefEditor.updateLogURL = updateLogURL;
	}


}
