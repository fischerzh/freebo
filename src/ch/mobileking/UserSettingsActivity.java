package ch.mobileking;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import ch.mobileking.utils.SharedPrefEditor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class UserSettingsActivity extends Activity {

	private ImageView imageView;

	private final int SELECT_PHOTO = 1;
	
	private SharedPrefEditor editor;

	private LinearLayout user_settings_avatar_generator;

	private int[] resourceAvatarId = { R.drawable.ic_avatar_01,
			R.drawable.ic_avatar_03, R.drawable.ic_avatar_05,
			R.drawable.ic_avatar_06_round, R.drawable.ic_avatar_07,
			R.drawable.ic_avatar_08 };

	private LinearLayout user_settings_email;

	private CheckBox user_settings_notification_chkbx;

	// extends PreferenceActivity implements OnSharedPreferenceChangeListener
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.user_settings);

		setTitle("Settings");
		
		editor = new SharedPrefEditor(this);

		final Random rand = new Random();

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		imageView = (ImageView) findViewById(R.id.user_settings_avatar);

		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, SELECT_PHOTO);
			}
		});
		
		user_settings_notification_chkbx = (CheckBox) findViewById(R.id.user_settings_notification_chkbx);
		user_settings_notification_chkbx.setChecked(editor.getNotifications());
		user_settings_notification_chkbx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					editor.setNotifications(true);
				}
				else
				{
					editor.setNotifications(false);
				}
			}
		});

		user_settings_avatar_generator = (LinearLayout) findViewById(R.id.user_settings_avatar_generator);
		user_settings_avatar_generator
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int num = rand.nextInt(6);
						imageView.setImageResource(resourceAvatarId[num]);
					}
				});

		user_settings_email = (LinearLayout) findViewById(R.id.user_settings_email);
		user_settings_email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(UserSettingsActivity.this);
		        LayoutInflater inflater=UserSettingsActivity.this.getLayoutInflater();
		        //this is what I did to added the layout to the alert dialog
		        View layout=inflater.inflate(R.layout.user_settings_entry,null);       
		        
		        final EditText usernameInput=(EditText)layout.findViewById(R.id.user_settings_entry_editText);
		        usernameInput.setText(editor.getEmail());
		        alert.setPositiveButton("Ok", null);
		        alert.setNegativeButton("Abbrechen",
	                    new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        return;
                    	}
                	});
				alert.setTitle("E-Mail Adresse");
//				alert.setMessage("Gib deine E-Mail Adresse an");
		        alert.setView(layout);

				final AlertDialog dialog = alert.create();
		        dialog.show();
		        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(!isValidEmail(usernameInput.getText()))
		    		    {
		    		    	usernameInput.setError("Bitte korrekte E-Mail Adresse angeben!");
		    		    }
						else
						{
							editor.setEmail(usernameInput.getText().toString());
							dialog.dismiss();
						}
					}
				});
			}
		});

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			// Load the legacy preferences headers
			// addPreferencesFromResource(R.xml.preferences);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				try {
					final Uri imageUri = imageReturnedIntent.getData();
					final InputStream imageStream = getContentResolver()
							.openInputStream(imageUri);
					final Bitmap selectedImage = BitmapFactory
							.decodeStream(imageStream);
					imageView.setImageBitmap(selectedImage);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}
		}
	}
	
	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
 

	// public void onSharedPreferenceChanged(SharedPreferences
	// sharedPreferences, String key) {
	// if (key.equals(KEY_PREF_SYNC_CONN)) {
	// Preference connectionPref = findPreference(key);
	// // Set summary to be the user-description for the selected value
	// connectionPref.setSummary(sharedPreferences.getString(key, ""));
	// }
	// }

}