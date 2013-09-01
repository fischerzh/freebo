package ch.freebo;

import ch.freebo.login.AsyncLogin;
import ch.freebo.userdata.OnTokenAcquired;
import android.os.Bundle;
import android.os.Handler;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	private AccountManager am;
	
	private EditText username, password;
	private Button login;
	private ImageButton googleLogin;
	
	private Activity act;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setAct(this);
		super.onCreate(savedInstanceState);
		
		setElements();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	
	public void setElements()
	{
		
		setContentView(R.layout.activity_main);

		am = AccountManager.get(this); // "this" references the current Context
		
        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        
        
        login = (Button) findViewById(R.id.btnLogin);
        googleLogin = (ImageButton) findViewById(R.id.btnGoogleLogin);
        
        login.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(LoginActivity.this, ProductListActivity.class);
//				startActivity(intent);
				String loginStr, pwdStr;
		        loginStr = username.getText().toString();
		        pwdStr = password.getText().toString();
				new AsyncLogin(getAct()).execute("http://192.168.0.16:8080/Freebo/product/loginFromApp", loginStr, pwdStr);
				
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
