package ch.mobileking;

import ch.mobileking.login.ServerRequest;
import ch.mobileking.utils.BaseActivity;
import ch.mobileking.utils.ITaskComplete;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity implements ITaskComplete{
	
	private Activity act;
	
	private EditText pwd1, pwd2, mail, register_username_txt;
	
	private Button register_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setAct(this);
		
		setTitle("ProductKing");
		
		setContentView(R.layout.register_main);
		
		pwd1 = (EditText) findViewById(R.id.register_txt_pwd1);
		
		pwd2 = (EditText) findViewById(R.id.register_txt_pwd2);
		
		mail = (EditText) findViewById(R.id.register_mail_txt);
		
		register_username_txt = (EditText) findViewById(R.id.register_username_txt);
		
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
	public void onUpdateCompleted(boolean b) {
		// TODO Auto-generated method stub
		System.out.println("Registration completed successfully!");
		
		Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    	finish();
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

}
