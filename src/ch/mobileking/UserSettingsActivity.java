package ch.mobileking;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.Window;

@SuppressLint("NewApi")
public class UserSettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{

  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      setTitle("Settings");
      
      
	  getActionBar().setHomeButtonEnabled(true);
	  getActionBar().setDisplayHomeAsUpEnabled(true);
	  
      addPreferencesFromResource(R.xml.preferences);
  }

@SuppressWarnings("deprecation")
@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,	String key) {
		// TODO Auto-generated method stub
		System.out.println("onSharedPrefChanged: " + key);
	
		Preference connectionPref = findPreference(key);
	}
  
  

} 