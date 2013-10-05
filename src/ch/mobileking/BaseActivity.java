package ch.mobileking;

import ch.mobileking.utils.SharedPrefEditor;
import android.app.Activity;
import android.content.Intent;

public class BaseActivity {
	
	private Activity act;
	
	private SharedPrefEditor editor;
	
	public BaseActivity(Activity act)
	{
		setAct(act);
		editor = new SharedPrefEditor(getAct());
	}
	
	
	public void logOut()
	{
		editor.setPwd("");
		editor.setUsername("");
		restartApp();
	}
	
	public void syncAppToServer() {
		// TODO Auto-generated method stub
		
	}
	
	public void showFirstRunMenu() {
		
		editor.setIsFirstRun(false);
		
		Intent intent = new Intent(getAct(), ProductOverview.class);
		getAct().finish();
		getAct().startActivity(intent);
	}
	
	private void restartApp()
	{
//        finish();

		Intent intent = new Intent(getAct(), MainActivity.class);
//        intent.putExtra("symbology", symbology);
//        setResult(1, intent);
		getAct().finish();
		getAct().startActivity(intent);
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
