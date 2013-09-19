package ch.mobileking;

import ch.mobileking.R;
import ch.mobileking.login.AsyncLogin;
import ch.mobileking.userdata.OnTokenAcquired;
import ch.mobileking.utils.SharedPrefEditor;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {

	private AccountManager am;
	
	private EditText username, password;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
//	        case R.id.create_new:
//	        	baseActivityMenu.logOut();
//	            return true;
	        case R.id.log_out:
	        	baseActivityMenu.logOut();
	            return true;
	        case R.id.app_sync:
	        	baseActivityMenu.syncAppToServer();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	public void setElements()
	{
		
		setContentView(R.layout.activity_main);

		am = AccountManager.get(this); // "this" references the current Context
		
        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        
        login = (Button) findViewById(R.id.btnLogin);
        googleLogin = (ImageButton) findViewById(R.id.btnGoogleLogin);
        
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
				// TODO Auto-generated method stub
//				Intent intent = new Intent(LoginActivity.this, ProductListActivity.class);
//				startActivity(intent);
				
				if(isNetworkAvailable())
				{
	        		setProgressBarDisableContent();

					loginUser();
					
//					setProgressBarEnableContent();
				}
			}
        }
        );
        
        googleLogin.setOnClickListener(new Button.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		
        		
        		Account[] accounts = am.getAccountsByType("com.google");
        		Account myAccount_ = null;

        		//show Intent and get account!!
        		System.out.println("ACCOUNT: " + accounts[0]);

        		myAccount_ = accounts[0];

        		Bundle options = new Bundle();
        		
				am.getAuthToken(
        			    myAccount_,                     // Account retrieved using getAccountsByType()
        			    "Manage your tasks",            // Auth scope
        			    options,                        // Authenticator-specific options
        			    getAct(),                           // Your activity
        			    new OnTokenAcquired(),          // Callback called when a token is successfully acquired
        			    new Handler());    // Callback called if an error occurs
        	}
        }
        );
        
        
	}
	
	public void setProgressBarDisableContent() {
		progressBar.setVisibility(View.VISIBLE);
		username.setEnabled(false);
		password.setEnabled(false);
		login.setEnabled(false);
	}
	
	public void setProgressBarEnableContent() {
		progressBar.setVisibility(View.INVISIBLE);
		username.setEnabled(true);
		password.setEnabled(true);
		login.setEnabled(true);
	}

	private void loginUserFromSettings(String loginStr, String pwdStr)
	{
		new AsyncLogin(getAct()).execute(loginStr, pwdStr);
	}
	
	private void loginUser()
	{
		String loginStr, pwdStr;
        loginStr = username.getText().toString();
        pwdStr = password.getText().toString();
		new AsyncLogin(getAct()).execute(loginStr, pwdStr);
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

}
