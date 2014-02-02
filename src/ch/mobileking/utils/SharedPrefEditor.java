package ch.mobileking.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ch.mobileking.utils.classes.SalesSlip;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefEditor {
	
	private static String testServer = "http://192.168.0.16:8080";
	private static String liveServer = "http://www.sagax.ch:8080";
	private static String prefix = testServer+"/Freebo/";
	private static String updateURL = prefix+"updateFavorites"; //192.168.0.16:8080;
	private static String loginURL = prefix+"loginFromApp"; //192.168.0.16:8080;
//	private static String updateUserInfo = prefix+"updateUserInfo";
	private static String updateCumulusURL = prefix+"updateCumulusInfo"; //192.168.0.16:8080;
	private static String updateLogURL = prefix+"updateUserLog";
	private static String registerURL = prefix+"user/create";
	private static String updateUserSettingURL = prefix+"updateUserSettings";
	private static String updateUserFilesURL = prefix+"updateUserFiles?";
	private static String updateErrorLogsURL = prefix+"updateErrorLogs?";

	
	private static final String PREFS_PWD = "Password";
	private static final String PREFS_USERNAME = "Username";
	private static final String PREFS_MAIL = "Email";
	private static final String PREFS_FIRST_LOGIN = "Login";
	private static final String PREFS_STAY_LOGGED_IN = "LoggedIn";
	private static final String PREFS_REGID = "Registration";
	private static final String PREFS_APPV = "Appversion";
	private static final String PREFS_TOTAL_PTS = "TotalPoints";
	private static final String PREFS_NOTIFICATIONS = "Notifications";
	private static final String PREFS_ANON = "Anonymous";
	private static final String PREFS_AVATAR = "Avatar";
	private static final String PREFS_SALES_ITEM = "SalesSlip";
	
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
	
	public void setTotalPoints(int points)
	{	    
		int loginCnt = 0;
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_TOTAL_PTS, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putInt(PREFS_TOTAL_PTS, points);
	    System.out.println("setTotalPoints #PREFS_TOTAL_PTS: " + points);
	    // Commit the edits!
	    editor.commit();
	    
	}
	
	public int getTotalPoints()
	{
	    int totalPoints;
	    // Restore preferences
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_TOTAL_PTS, 0);
	    totalPoints = settings.getInt(PREFS_TOTAL_PTS, Integer.MIN_VALUE);
	    System.out.println("getTotalPoints #PREFS_TOTAL_PTS: " + totalPoints);
		return totalPoints;
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
	
	public void setAnonymous(Boolean anon)
	{	    
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_ANON, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean(PREFS_ANON, anon);
	    System.out.println("setSharedPref #PREFS_ANON: " + anon);
	    // Commit the edits!
	    editor.commit();

	}
	
	public Boolean getAnonymous()
	{
	    Boolean anon;
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_ANON, 0);
	    anon = settings.getBoolean(PREFS_ANON, false);
	    return anon;
	}
	
	public void setAvatarId(Integer avatarId)
	{	    
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_AVATAR, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putInt(PREFS_AVATAR, avatarId);
	    System.out.println("setSharedPref #PREFS_AVATAR: " + avatarId);
	    // Commit the edits!
	    editor.commit();

	}
	
	public Integer getAvatarId()
	{
	    Integer avatarId;
	    SharedPreferences settings = cont.getSharedPreferences(PREFS_AVATAR, 0);
	    avatarId = settings.getInt(PREFS_AVATAR, 0);
	    return avatarId;
	}
	
	public void setSalesSlips(ArrayList<SalesSlip> list) {
		System.out.println("setSharedPref #PREF_SALES_ITEM: " +list);
        SharedPreferences settings = cont.getSharedPreferences(PREFS_SALES_ITEM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(PREFS_SALES_ITEM);
        editor.commit();
        try {
            editor.putString(PREFS_SALES_ITEM, ObjectSerializer.serialize(list));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }
	
	public ArrayList<SalesSlip> getSalesSlips() {
        //      load tasks from preference
		ArrayList<SalesSlip> salesSlipList = null;
        SharedPreferences settings = cont.getSharedPreferences(PREFS_SALES_ITEM, Context.MODE_PRIVATE);

        try {
				salesSlipList = (ArrayList<SalesSlip>) ObjectSerializer.deserialize(settings.getString(PREFS_SALES_ITEM, ObjectSerializer.serialize(new ArrayList<SalesSlip>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return salesSlipList;
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

	/**
	 * @return the updateUserSettingURL
	 */
	public static String getUpdateUserSettingURL() {
		return updateUserSettingURL;
	}

	/**
	 * @param updateUserSettingURL the updateUserSettingURL to set
	 */
	public static void setUpdateUserSettingURL(String updateUserSettingURL) {
		SharedPrefEditor.updateUserSettingURL = updateUserSettingURL;
	}

	/**
	 * @return the updateUserFilesURL
	 */
	public static String getUpdateUserFilesURL() {
		return updateUserFilesURL;
	}

	/**
	 * @param updateUserFilesURL the updateUserFilesURL to set
	 */
	public static void setUpdateUserFilesURL(String updateUserFilesURL) {
		SharedPrefEditor.updateUserFilesURL = updateUserFilesURL;
	}

	/**
	 * @return the updateErrorLogsURL
	 */
	public static String getUpdateErrorLogsURL() {
		return updateErrorLogsURL;
	}

	/**
	 * @param updateErrorLogsURL the updateErrorLogsURL to set
	 */
	public static void setUpdateErrorLogsURL(String updateErrorLogsURL) {
		SharedPrefEditor.updateErrorLogsURL = updateErrorLogsURL;
	}


}
