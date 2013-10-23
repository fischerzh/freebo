package ch.mobileking;

import ch.mobileking.utils.BaseActivity;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
	
	private Activity act;
	
	private EditText pwd1, pwd2, mail;
	
	private Button register_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("ProductKing");
		
		setContentView(R.layout.register_main);
		
		pwd1 = (EditText) findViewById(R.id.register_txt_pwd1);
		
		pwd2 = (EditText) findViewById(R.id.register_txt_pwd2);
		
		mail = (EditText) findViewById(R.id.register_mail_txt);
		
		register_btn = (Button) findViewById(R.id.register_btn);

		register_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isValidEmail(mail.getText().toString()))
				{	
					System.out.println("Mail " + mail.getText().toString());
					mail.setError("Fill out correct mail address!");
				}
				
				// TODO Auto-generated method stub
				if(!pwd1.getText().toString().equalsIgnoreCase(pwd2.getText().toString()) )
				{
					System.out.println("pwd1: " + pwd1.getText() +  "pwd2: " + pwd2.getText());
					Animation shake = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.shake); 
					pwd2.startAnimation(shake);
					pwd2.setError("Password's did not match!");
				}
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

}
