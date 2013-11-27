package ch.mobileking;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import ch.mobileking.login.ServerRequest;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;

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

@SuppressLint("NewApi")
public class UserSettingsActivity extends Activity implements ITaskComplete{

	private ImageView imageView;

	private final int SELECT_PHOTO = 1;
	
	private final String USER_AVATAR_PNG ="user_avatar.png";
	
	private SharedPrefEditor editor;

	private LinearLayout user_settings_avatar_generator;

	private int[] resourceAvatarId = { R.drawable.ic_avatar_01,
			R.drawable.ic_avatar_03, R.drawable.ic_avatar_05,
			R.drawable.ic_avatar_06_round, R.drawable.ic_avatar_07,
			R.drawable.ic_avatar_08 };

	private LinearLayout user_settings_email;

	private CheckBox user_settings_notification_chkbx;

	private ProgressBar user_settings_avatar_progress;
	
	private EditText userEmailInput;
	
	private Bitmap selectedImage;
	
	private String filePath;

	private CheckBox user_settings_anon_chkbx;

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
		if(Utils.imageExists(USER_AVATAR_PNG))
			imageView.setImageBitmap(Utils.loadImageFromPath(USER_AVATAR_PNG));
		
		user_settings_avatar_progress = (ProgressBar) findViewById(R.id.user_settings_avatar_progress);
		user_settings_avatar_progress.setVisibility(View.INVISIBLE);
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
//				if(isChecked)
//				{
//					editor.setNotifications(true);
//				}
//				else
//				{
//					editor.setNotifications(false);
//				}
			}
		});
		
		user_settings_anon_chkbx = (CheckBox) findViewById(R.id.user_settings_anon_chkbx);
		user_settings_anon_chkbx.setChecked(editor.getAnonymous());
		user_settings_anon_chkbx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
//				if(isChecked)
//				{
//					editor.setAnonymous(true);
//				}
//				else
//				{
//					editor.setAnonymous(false);
//				}
			}
		});

		user_settings_avatar_generator = (LinearLayout) findViewById(R.id.user_settings_avatar_generator);
		user_settings_avatar_generator
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						user_settings_avatar_progress.setVisibility(View.VISIBLE);
						// TODO Auto-generated method stub
						int num = rand.nextInt(6);
						imageView.setImageResource(resourceAvatarId[num]);
						Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), resourceAvatarId[num]);
						saveAvatarToSD(largeIcon);
						user_settings_avatar_progress.setVisibility(View.INVISIBLE);
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
		        
		        userEmailInput=(EditText)layout.findViewById(R.id.user_settings_entry_editText);
		        
		        userEmailInput.setText(editor.getEmail());
		        
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
						if(!isValidEmail(userEmailInput.getText()))
		    		    {
		    		    	userEmailInput.setError("Bitte korrekte E-Mail Adresse angeben!");
		    		    }
						else
						{
//							editor.setEmail(userEmailInput.getText().toString());
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
	
	public void onSyncRequest()
	{
		System.out.println("UserSettingsActivity.onSyncRequest()");
		ServerRequest request = new ServerRequest(this, this);
		/** first sync to Server, then if OK Update SharedPreferences!! */
		/*
		 * editor.getUsername(),editor.getPwd() , editor.getEmail(), editor.getNotifications(), editor.getAnonymous()
		 */
		request.startUpdateUserSettings(editor.getUsername(),editor.getPwd(), userEmailInput.getText().toString(), user_settings_notification_chkbx.isChecked(), user_settings_anon_chkbx.isChecked());
	}
	
	@Override
    public void onBackPressed() 
	{
		onSyncRequest();
		finish();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
	    switch (menuItem.getItemId()) {
	    case android.R.id.home:
//	      Intent homeIntent = new Intent(this, MainTabActivity.class);
//	      homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	      startActivity(homeIntent);
			onSyncRequest();
	    	finish();
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
 
	public Bitmap saveAvatarToSD(Bitmap selectedImage)
	{	
		Bitmap image = BitmapFactory.decodeFile(Utils.saveBitmap(selectedImage, USER_AVATAR_PNG));
		return image;
	}

	@Override
	public void onLoginCompleted(boolean b, String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateCompleted(boolean b, String string) {
		// TODO Auto-generated method stub
		System.out.println("Loaded picture finished!");
		
//		selectedImage = saveAvatarToSD(selectedImage);
		selectedImage = Utils.loadImageFromPath(USER_AVATAR_PNG);
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

	// public void onSharedPreferenceChanged(SharedPreferences
	// sharedPreferences, String key) {
	// if (key.equals(KEY_PREF_SYNC_CONN)) {
	// Preference connectionPref = findPreference(key);
	// // Set summary to be the user-description for the selected value
	// connectionPref.setSummary(sharedPreferences.getString(key, ""));
	// }
	// }

}