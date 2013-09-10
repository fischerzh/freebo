package ch.freebo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefEditor {
	
	private String activeURL;
	
	private static final String PREFS_PWD = "Password";
	private static final String PREFS_USERNAME = "Username";
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
	

}
