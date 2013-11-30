package ch.mobileking;

import ch.mobileking.R;
import ch.mobileking.exception.CustomExceptionHandler;
import ch.mobileking.login.AsyncLogin;
import ch.mobileking.login.ServerRequest;
import ch.mobileking.userdata.OnTokenAcquired;
import ch.mobileking.utils.BaseActivity;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements ITaskComplete{

	private AccountManager am;
	
	private EditText username, password;
	private TextView forgotPw, registerTxt;
	private Button login;
	private ImageButton googleLogin;
	private ProgressBar progressBar;
	private FrameLayout main_rel_layout;
	
	private Activity act;
	private SharedPrefEditor editor;
	BaseActivity baseActivityMenu;
	
	private String gcmResponseMessage;

	private CheckBox main_stay_loggedIn_chkB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setAct(this);
		super.onCreate(savedInstanceState);
		
		baseActivityMenu = new BaseActivity(this);
		
        editor = new SharedPrefEditor(this);
        
        Utils.initUserLogData();
        
        Utils.setContext(getApplicationContext());
		
		setElements();
		
	}
	
	public void setElements()
	{
		setTitle("ProductKing");
		
		setContentView(R.layout.activity_main);
		
		if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
		    Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(editor.getUsername()));
		}

    	Utils.addLogMsg(this.getLocalClassName());
		
		am = AccountManager.get(this); // "this" references the current Context
		
        username = (EditText) findViewById(R.id.txtUsername);
        username.setText(editor.getUsername());
        
        password = (EditText) findViewById(R.id.txtPassword);
        password.setText(editor.getPwd());
        
        forgotPw = (TextView) findViewById(R.id.main_forgot_pw_txt);
        
        registerTxt = (TextView) findViewById(R.id.main_register_txt);
        
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        
        main_stay_loggedIn_chkB = (CheckBox) findViewById(R.id.main_stay_loggedIn_chkB);
        main_stay_loggedIn_chkB.setChecked(editor.getStayLoggedIn());
        main_stay_loggedIn_chkB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				editor.setStayLoggedIn(isChecked);
			}
		});

        main_rel_layout = (FrameLayout) findViewById(R.id.main_rel_layout);
        
        login = (Button) findViewById(R.id.btnLogin);
        
        gcmResponseMessage = getIntent().getStringExtra("gcmnotification");
        
        forgotPw.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(MainActivity.this, LoyaltyCardActivity.class);
//				startActivity(intent);
			}
		});
        
        registerTxt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
        
        if(isNetworkAvailable())
        {
        	if(editor.getStayLoggedIn() && editor.getUsername()!="" && editor.getPwd()!= "" )
            {
        		setProgressBarDisableContent();
        		
        		loginUserFromSettings(editor.getUsername(), editor.getPwd());
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
		main_rel_layout.setVisibility(View.INVISIBLE);
		username.setEnabled(true);
		password.setEnabled(true);
		login.setEnabled(true);
	}
	
	public void setProgressBarEnableContent() {
		progressBar.setVisibility(View.VISIBLE);
		main_rel_layout.setVisibility(View.VISIBLE);
		progressBar.bringToFront();
		username.setEnabled(false);
		password.setEnabled(false);
		login.setEnabled(false);
	}

	private void loginUserFromSettings(String loginStr, String pwdStr)
	{
    	Utils.addLogMsg(this.getLocalClassName()+": loginUserFromSettings");
		
		/** REFACTOR, CALL ASYNC LOGIN TASK, WAIT FOR CALLBACK, OnPostExecute() or similar AND THEN CALL ACCORDING VIEW BASED ON LGOIN SUCCESS, FAILURE OR FIRST LOGIN!! */
		new AsyncLogin(getAct(), false, this).execute(loginStr, pwdStr);
		this.setProgressBarEnableContent();

	}
	
	private void loginUser()
	{
		
    	Utils.addLogMsg(this.getLocalClassName()+": loginUser");

		String loginStr, pwdStr;
        loginStr = username.getText().toString();
        pwdStr = password.getText().toString();

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
	public void onResume()
	{
		super.onResume();
		
        username.setText(editor.getUsername());
        password.setText(editor.getPwd());
	}
	
//	@Override
//	public void onStop()
//	{
//		super.onStop();
//		System.out.println("MainActivity.onStop()");
//
//		ServerRequest request = new ServerRequest(this, this);
//		request.startUpdateLogs();
//	}
	

	@Override
	public void onLoginCompleted(boolean completed, String message) {
		// TODO Auto-generated method stub
		
		final String msg = message;
		
		System.out.println("msg: " + msg);
		
//		if(msg.toLowerCase().contains("))
		
		
		System.out.println("MainActivity: LoginCompleted: " + completed);
		if(completed && !editor.getFirstRun())
		{
		    String gcmUUID = getIntent().getStringExtra("messageId");
			
			Intent intent = new Intent(getAct(), MainTabActivity.class);
			if(gcmUUID!=null)
			{
				intent.putExtra("messageId", gcmUUID);
			}
			getAct().startActivity(intent);
		}
		else if (completed && editor.getFirstRun())
		{
			/** FIRST LOGIN **/
			Intent intent = new Intent(getAct(), LoyaltyCardActivity.class);
			getAct().startActivity(intent);
		}
		else
		{
			runOnUiThread(new Runnable() 
			{
			   public void run() 
			   {
				   Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show(); 
				   finish();
			   }
			}); 
			
		}
		
		runOnUiThread(new Runnable() 
		{
		   public void run() 
		   {
			   setProgressBarDisableContent();
			   finish();
		   }
		}); 
		

	}

	@Override
	public void onUpdateCompleted(boolean completed, String message) {
		// TODO Auto-generated method stub
		System.out.println("Message from updateCompleted: "  +message);
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
