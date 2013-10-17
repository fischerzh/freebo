package ch.mobileking;

import ch.mobileking.R;
import ch.mobileking.login.AsyncLogin;
import ch.mobileking.userdata.OnTokenAcquired;
import ch.mobileking.utils.BaseActivity;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.SharedPrefEditor;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements ITaskComplete{

	private AccountManager am;
	
	private EditText username, password;
	private TextView forgotPw, registerTxt;
	private Button login;
	private ImageButton googleLogin;
	private ProgressBar progressBar;
	
	private Activity act;
	private SharedPrefEditor editor;
	BaseActivity baseActivityMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setAct(this);
		super.onCreate(savedInstanceState);
		
		baseActivityMenu = new BaseActivity(this);
		
		setElements();
		
	}
	
	public void setElements()
	{
		setTitle("Product King");
		
		setContentView(R.layout.activity_main);

		am = AccountManager.get(this); // "this" references the current Context
		
        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        
        forgotPw = (TextView) findViewById(R.id.main_forgot_pw_txt);
        
        registerTxt = (TextView) findViewById(R.id.main_register_txt);
        
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        
        login = (Button) findViewById(R.id.btnLogin);
        
        
//        googleLogin = (ImageButton) findViewById(R.id.btnGoogleLogin);
        
        forgotPw.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, RecommActivity.class);
				startActivity(intent);
			}
		});
        
        registerTxt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, MainTabActivity.class);
				startActivity(intent);
			}
		});
        
        editor = new SharedPrefEditor(this);
        if(isNetworkAvailable())
        {
        	if(editor.getUsername()!="" && editor.getPwd()!= "")
            {
        		setProgressBarDisableContent();
        		
        		loginUserFromSettings(editor.getUsername(), editor.getPwd());
        		
//        		setProgressBarEnableContent();
            }

        }
        else
        {
        	Toast.makeText(this,"Internet wird benštigt!", Toast.LENGTH_LONG).show();	
	        finish();
	        return;
        }
        
        login.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(isNetworkAvailable())
				{
	        		setProgressBarEnableContent();

					loginUser();
				}
				else
				{
					Toast.makeText(MainActivity.this,"Internet wird benštigt!", Toast.LENGTH_LONG).show();	
				}
			}
        }
        );
	}
	
	public void setProgressBarDisableContent() {
		progressBar.setVisibility(View.INVISIBLE);
		username.setEnabled(true);
		password.setEnabled(true);
		login.setEnabled(true);
	}
	
	public void setProgressBarEnableContent() {
		progressBar.setVisibility(View.VISIBLE);
		username.setEnabled(false);
		password.setEnabled(false);
		login.setEnabled(false);
	}

	private void loginUserFromSettings(String loginStr, String pwdStr)
	{
		/** REFACTOR, CALL ASYNC LOGIN TASK, WAIT FOR CALLBACK, OnPostExecute() or similar AND THEN CALL ACCORDING VIEW BASED ON LGOIN SUCCESS, FAILURE OR FIRST LOGIN!! */
		new AsyncLogin(getAct(), false, this).execute(loginStr, pwdStr);
		this.setProgressBarEnableContent();

	}
	
	private void loginUser()
	{
		String loginStr, pwdStr;
        loginStr = username.getText().toString();
        pwdStr = password.getText().toString();
//		new AsyncLogin(getAct(), false).execute(loginStr, pwdStr);
		new AsyncLogin(getAct(), false, this).execute(loginStr, pwdStr);
		this.setProgressBarEnableContent();
	}
	
	private boolean isNetworkAvailable() 
	{
	    ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
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
	

	@Override
	public void onLoginCompleted(boolean completed) {
		// TODO Auto-generated method stub
		System.out.println("MainActivity: LoginCompleted: " + completed);
		if(completed)
		{
			Intent intent = new Intent(getAct(), MainTabActivity.class);
			getAct().startActivity(intent);
		}
		else
		{
			Toast.makeText(MainActivity.this,"Internet wird benštigt!", Toast.LENGTH_LONG).show();	

		}
		setProgressBarDisableContent();
	}

	@Override
	public void onUpdateCompleted(boolean completed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startLogin() {
		// TODO Auto-generated method stub
		
	}

}
