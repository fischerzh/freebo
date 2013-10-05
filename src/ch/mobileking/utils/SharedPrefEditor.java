package ch.mobileking.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefEditor {
	
	private static String updateURL = "http://www.sagax.ch:8080/Freebo/product/updateUserInfo"; //192.168.0.16:8080;
	private static String loginURL = "http://www.sagax.ch:8080/Freebo/product/loginFromApp"; //192.168.0.16:8080;

	
	private static final String PREFS_PWD = "Password";
	private static final String PREFS_USERNAME = "Username";
	private static final String PREFS_FIRST_LOGIN = "Login";
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
	    firstRun = settings.getBoolean(PREFS_FIRST_LOGIN, false);
	    System.out.println("getSharedPref #FIRST_RUN: " + firstRun);
		
		return firstRun;
	}

	/**
	 * @return the activeURL
	 */
	public String getUpdateURL() {
		return updateURL;
	}

	/**
	 * @param activeURL the activeURL to set
	 */
	public void setUpdateURL(String updateURL) {
		this.updateURL = updateURL;
	}

	/**
	 * @return the loginURL
	 */
	public String getLoginURL() {
		return loginURL;
	}

	/**
	 * @param loginURL the loginURL to set
	 */
	public void setLoginURL(String loginURL) {
		SharedPrefEditor.loginURL = loginURL;
	}
	

}
