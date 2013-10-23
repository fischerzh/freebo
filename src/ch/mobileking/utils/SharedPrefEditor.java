package ch.mobileking.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefEditor {
	
	private static String updateURL = "http://www.sagax.ch:8080/Freebo/product/updateUserInfo"; //192.168.0.16:8080;
	private static String loginURL = "http://www.sagax.ch:8080/Freebo/product/loginFromApp"; //192.168.0.16:8080;
	private static String updateCumulusURL = "http://www.sagax.ch:8080/Freebo/product/updateCumulusInfo"; //192.168.0.16:8080;

	
	private static final String PREFS_PWD = "Password";
	private static final String PREFS_USERNAME = "Username";
	private static final String PREFS_FIRST_LOGIN = "Login";
	private static final String PREFS_REGID = "Registration";
	private static final String PREFS_APPV = "Appversion";
	private Context cont;
	
	public SharedPrefEditor(Context context)
	{
		this.cont = context;
	}
	
	public void setUsername(String username)
	{	    
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_USERNAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString(PREFS_USERNAME, username);
	    System.out.println("setSharedPref #USERNAME: " + username);
	    // Commit the edits!
	    editor.commit();
	    
	}
	
	public String getUsername()
	{
	    String username;
	    // Restore preferences
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_USERNAME, 0);
	    username = settings.getString(PREFS_USERNAME, "");
	    System.out.println("getSharedPref #USERNAME: " + username);
		
		return username;
	}
	
	public void setPwd(String pwd)
	{	    
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_PWD, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString(PREFS_PWD, pwd);
	    System.out.println("setSharedPref #PWD: " + pwd);
	    // Commit the edits!
	    editor.commit();
	    
	}
	
	public String getPwd()
	{
	    String pwd;
	    // Restore preferences
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_PWD, 0);
	    pwd = settings.getString(PREFS_PWD, "");
	    System.out.println("getSharedPref #PWD: " + pwd);
		
		return pwd;
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



}
