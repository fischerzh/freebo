package ch.mobileking.userdata;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import ch.mobileking.R;
import ch.mobileking.R.dimen;
import ch.mobileking.R.drawable;
import ch.mobileking.R.id;
import ch.mobileking.R.layout;
import ch.mobileking.login.ServerRequest;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;

import android.support.v7.app.ActionBarActivity;  
import android.support.v7.app.ActionBar;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UserSettingsActivity extends ActionBarActivity implements ITaskComplete{

	private ImageView imageView;

	private final int SELECT_PHOTO = 1;
	
	private SharedPrefEditor editor;

	private LinearLayout user_settings_avatar_generator;

	private LinearLayout user_settings_email;

	private CheckBox user_settings_notification_chkbx;

	private ProgressBar user_settings_avatar_progress;
	
	private EditText userEmailInput;
	private EditText userPasswdInput;
	
	private Bitmap selectedImage;
	private String filePath;

	private CheckBox user_settings_anon_chkbx;

	private LinearLayout user_settings_pwd;
	
	private String pwd, email;
	private Boolean isNotification, isAnonymous;
	private Integer avatarId;

	private ProgressBar user_setting_progress_bar_top;

	private TextView user_settings_username;
	private TextView user_settings_pts_text;


	// extends PreferenceActivity implements OnSharedPreferenceChangeListener
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.user_settings);

		setTitle("Settings");
		
		editor = new SharedPrefEditor(this);
		
		initVariables();

		final Random rand = new Random();

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
		
		user_setting_progress_bar_top = (ProgressBar) findViewById(R.id.user_setting_progress_bar_top);
		user_setting_progress_bar_top.setVisibility(View.INVISIBLE);
		
		imageView = (ImageView) findViewById(R.id.user_settings_avatar);
//		if(Utils.imageExists(Utils.USER_AVATAR_PNG))
//			imageView.setImageBitmap(Utils.loadImageFromPath(Utils.USER_AVATAR_PNG));
		imageView.setImageResource(Utils.resourceAvatarId[editor.getAvatarId()]);
		
		user_settings_avatar_progress = (ProgressBar) findViewById(R.id.user_settings_avatar_progress);
		user_settings_avatar_progress.setVisibility(View.INVISIBLE);
//		imageView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//				photoPickerIntent.setType("image/*");
//				startActivityForResult(photoPickerIntent, SELECT_PHOTO);
//			}
//		});
		
		user_settings_notification_chkbx = (CheckBox) findViewById(R.id.user_settings_notification_chkbx);
		user_settings_notification_chkbx.setChecked(editor.getNotifications());
		user_settings_notification_chkbx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				isNotification = isChecked;
			}
		});
		
		user_settings_anon_chkbx = (CheckBox) findViewById(R.id.user_settings_anon_chkbx);
		user_settings_anon_chkbx.setChecked(editor.getAnonymous());
		user_settings_anon_chkbx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				isAnonymous = isChecked;
			}
		});

		user_settings_avatar_generator = (LinearLayout) findViewById(R.id.user_settings_avatar_generator);
		user_settings_avatar_generator.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						user_settings_avatar_progress.setVisibility(View.VISIBLE);
						// TODO Auto-generated method stub
						int num = rand.nextInt(31)+1;
						imageView.setImageResource(Utils.resourceAvatarId[num]);
						avatarId = num;
//						Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), resourceAvatarId[num]);
//						saveAvatarToSD(largeIcon);
						user_settings_avatar_progress.setVisibility(View.INVISIBLE);
					}
				});
		
		user_settings_pts_text = (TextView) findViewById(R.id.user_settings_pts_text);
		user_settings_pts_text.setText(""+editor.getTotalPoints()+" Pkte.");
		
		user_settings_username = (TextView) findViewById(R.id.user_settings_username_txt);
		user_settings_username.setText(editor.getUsername());
		
		

//		user_settings_email = (LinearLayout) findViewById(R.id.user_settings_email);
//		user_settings_email.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				AlertDialog.Builder alert = new AlertDialog.Builder(UserSettingsActivity.this);
//		        LayoutInflater inflater=UserSettingsActivity.this.getLayoutInflater();
//
//		        View layout=inflater.inflate(R.layout.user_settings_entry,null);       
//		        
//		        userEmailInput=(EditText)layout.findViewById(R.id.user_settings_entry_editText);
//	        	userEmailInput.setText(editor.getEmail());
//		        
//		        alert.setPositiveButton("Ok", null);
//		        alert.setNegativeButton("Abbrechen",
//	                    new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        return;
//                    	}
//                	});
//				alert.setTitle("E-Mail Adresse");
//		        alert.setView(layout);
//
//				final AlertDialog dialog = alert.create();
//		        dialog.show();
//		        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						if(!isValidEmail(userEmailInput.getText()))
//		    		    {
//		    		    	userEmailInput.setError("Bitte korrekte E-Mail Adresse angeben!");
//		    		    }
//						else
//						{
//							email = userEmailInput.getText().toString();
//							dialog.dismiss();
//						}
//					}
//				});
//			}
//		});
		
		user_settings_pwd = (LinearLayout) findViewById(R.id.user_settings_pwd);
		user_settings_pwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(UserSettingsActivity.this);
		        LayoutInflater inflater=UserSettingsActivity.this.getLayoutInflater();

		        View layout=inflater.inflate(R.layout.user_settings_entry,null);       
		        
		        userPasswdInput=(EditText)layout.findViewById(R.id.user_settings_entry_editText);
		        
		        if(pwd!=null)	
		        	userPasswdInput.setText(pwd);
		        else
		        	userPasswdInput.setText(editor.getPwd());
		        
		        alert.setPositiveButton("Ok", null);
		        alert.setNegativeButton("Abbrechen",
	                    new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        return;
                    	}
                	});
				alert.setTitle("Passwort");
		        alert.setView(layout);

				final AlertDialog dialog = alert.create();
		        dialog.show();
		        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(userPasswdInput.getText().toString().isEmpty())
		    		    {
							userPasswdInput.setError("Bitte Passwort angeben!");
		    		    }
						else
						{
							pwd = userPasswdInput.getText().toString();
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
	
	private void initVariables()
	{
//		this.email = editor.getEmail();
		this.pwd = editor.getPwd();
		this.isAnonymous = editor.getAnonymous();
		this.isNotification = editor.getNotifications();
		this.avatarId = editor.getAvatarId();
	}
	
	private boolean isUpdateNeeded(String settingValue, String editor)
	{
		return settingValue != editor;
	}
	
	
	public void onSyncRequest()
	{
		System.out.println("UserSettingsActivity.onSyncRequest()");
		user_setting_progress_bar_top.setVisibility(View.VISIBLE);
		
		ServerRequest request = new ServerRequest(this, this);
		/** first sync to Server, then if OK Update SharedPreferences!! */
		/*
		 * editor.getUsername(),editor.getPwd() , editor.getEmail(), editor.getNotifications(), editor.getAnonymous()
		 */
		Boolean needsUpdate = false;
//		if(isUpdateNeeded(email, editor.getEmail()))
//			needsUpdate = true;
		if(isUpdateNeeded(pwd, editor.getPwd()))
			needsUpdate = true;
		if(isUpdateNeeded(isNotification.toString(), editor.getNotifications().toString()))
			needsUpdate = true;
		if(isUpdateNeeded(isAnonymous.toString(), editor.getAnonymous().toString()))
			needsUpdate = true;
		if(isUpdateNeeded(avatarId.toString(), editor.getAvatarId().toString()))
			needsUpdate = true;
		if(needsUpdate)
		{
			request.startUpdateUserSettings(pwd, isNotification, isAnonymous, avatarId);
		}
		else
		{
			user_setting_progress_bar_top.setVisibility(View.INVISIBLE);
			clearVariables();
			finish();
		}
		

	}
	
	@Override
    public void onBackPressed() 
	{
		onSyncRequest();
//		finish();
	}
	
	private void clearVariables()
	{
		this.pwd = "";
		this.email = "";
		this.isAnonymous = null;
		this.isNotification = null;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
	    switch (menuItem.getItemId()) {
	    case android.R.id.home:
//	      Intent homeIntent = new Intent(this, MainTabActivity.class);
//	      homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	      startActivity(homeIntent);
			onSyncRequest();
//	    	finish();
	    }
	  return (super.onOptionsItemSelected(menuItem));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				try {
					user_settings_avatar_progress.setVisibility(View.VISIBLE);

					final Uri imageUri = imageReturnedIntent.getData();
					System.out.println("File Info: " + imageUri.getLastPathSegment());
					final InputStream imageStream = getContentResolver().openInputStream(imageUri);

					String[] filePathColumn = {MediaStore.Images.Media.DATA};

		            Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
		            cursor.moveToFirst();

		            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		            filePath = cursor.getString(columnIndex);
		            
		            cursor.close();
		            System.out.println("filePath: " + filePath);
		            
//					Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
					
//					Bitmap selectedImage = resizeBitmap(filePath);
		            
		            Utils.setListener(this);
		            
//		            selectedImage = Utils.processFileFromSD(filePath);
					Utils.processFileFromSD(filePath);
					
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
	
	public static Bitmap resizeBitmap(String fileName) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    options.inSampleSize = 4;
	    BitmapFactory.decodeFile(fileName, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, R.dimen.user_avatar_width, R.dimen.user_avatar_width);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(fileName, options);
	}
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
	}
 
//	public Bitmap saveAvatarToSD(Bitmap selectedImage)
//	{	
//		Bitmap image = BitmapFactory.decodeFile(Utils.saveBitmap(selectedImage, Utils.USER_AVATAR_PNG));
//		return image;
//	}

	@Override
	public void onLoginCompleted(boolean updateCompleted, String string) {
		// TODO Auto-generated method stub
		System.out.println("UserSettingsActivity.onLoginCompleted()");
//		if(b)
//			Toast.makeText(this, "Deine Einstellungen wurden erfolgreich aktualisiert! ", Toast.LENGTH_SHORT).show();
//		else
//	        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
		user_setting_progress_bar_top.setVisibility(View.INVISIBLE);

		if(updateCompleted)
		{
//			createAlert("Einstellungen wurden gespeichert!", "Einstellungen", R.drawable.ic_empfehlungen );
			Toast.makeText(this, "Deine Einstellungen wurden erfolgreich aktualisiert! ", Toast.LENGTH_LONG).show();
			clearVariables();
			finish();
		}
		else
		{
//			createAlert("Fehler beim speichern: " +string, "Fehlgeschlagen!", R.drawable.ic_empfehlungen );
			Toast.makeText(this, "Fehler beim speichern: " +string, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onUpdateCompleted(boolean b, String string) {
		// TODO Auto-generated method stub
		System.out.println("Loaded picture finished!");
		
//		selectedImage = saveAvatarToSD(selectedImage);
		selectedImage = Utils.loadImageFromPath(Utils.USER_AVATAR_PNG);
//		}
		user_settings_avatar_progress.setVisibility(View.INVISIBLE);

		imageView.setImageBitmap(selectedImage);
	}

	@Override
	public void startUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startLogin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendProgressUpdate(int progress) {
		// TODO Auto-generated method stub
		
	}


	private void createAlert(String message, String title, int iconId) {
		// Build the dialog
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(title);
		alert.setMessage(message);
		// Create TextView
		final TextView input = new TextView (this);
		alert.setView(input);
		alert.setIcon(iconId);

		alert.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		    finish();
		  }
		});

		alert.show();
	}

}