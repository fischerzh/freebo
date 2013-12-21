package ch.mobileking;

import ch.mobileking.exception.CustomExceptionHandler;
import ch.mobileking.login.ServerRequest;
import ch.mobileking.utils.BaseActivity;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class RegisterActivity extends ActionBarActivity implements ITaskComplete{
	
	private Activity act;
	
	private EditText pwd1, pwd2, mail, register_username_txt;
	
	private Button register_btn;

	private RelativeLayout register_progressLayout;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setAct(this);
		
		setTitle("ProductKing");
		
		setContentView(R.layout.register_main);
		
		Utils.addLogMsg(this.getLocalClassName());
	
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) 
		{
			// Load the legacy preferences headers
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		else
		{
			getActionBar().setHomeButtonEnabled(true);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			
		}

		pwd1 = (EditText) findViewById(R.id.register_txt_pwd1);
		
		pwd2 = (EditText) findViewById(R.id.register_txt_pwd2);
		
		mail = (EditText) findViewById(R.id.register_mail_txt);
		
		register_username_txt = (EditText) findViewById(R.id.register_username_txt);
		
		register_progressLayout = (RelativeLayout) findViewById(R.id.register_progressLayout);
		
		register_btn = (Button) findViewById(R.id.register_btn);
		register_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isValidEmail(mail.getText().toString()))
				{	
					System.out.println("Mail " + mail.getText().toString());
					mail.setError("Bitte korrekte E-Mail Adresse angeben!");
					return;
				}
				
				// TODO Auto-generated method stub
				if(!pwd1.getText().toString().equalsIgnoreCase(pwd2.getText().toString()) )
				{
					System.out.println("pwd1: " + pwd1.getText() +  "pwd2: " + pwd2.getText());
					Animation shake = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.shake); 
					pwd2.startAnimation(shake);
					pwd2.setError("Passwšrter stimmen nicht Ÿberein!");
					return;
				}
				
				if(register_username_txt.getText().toString().equalsIgnoreCase(""))
				{
					register_username_txt.setError("Bitte Benutzernamen angeben!");
					return;
				}
				register_progressLayout.setVisibility(View.VISIBLE);
				ServerRequest httpRequester = new ServerRequest(getAct(), RegisterActivity.this);
				httpRequester.startRegisterUser(register_username_txt.getText().toString(), pwd1.getText().toString(), mail.getText().toString());
			}
		});
	}
	
	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}

	@Override
	public void onLoginCompleted(boolean b, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateCompleted(boolean updated, String message) {
		// TODO Auto-generated method stub
		register_progressLayout.setVisibility(View.INVISIBLE);
		if(updated)
		{
			System.out.println("Registration successful: " + message);
			Intent intent = new Intent(this, MainActivity.class);
	    	startActivity(intent);
	    	finish();
		}
		else
		{
			System.out.println("Registration failed: " + message);
			Toast.makeText(this, "Registrierung fehlgeschlagen: " +message, Toast.LENGTH_LONG).show();

		}

	}

	@Override
	public void startUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startLogin() {
		// TODO Auto-generated method stub
		
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
	public void sendProgressUpdate(int progress) {
		// TODO Auto-generated method stub
		
	}

}
