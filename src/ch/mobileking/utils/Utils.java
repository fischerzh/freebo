package ch.mobileking.utils;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class Utils {
	
	private Context cont;
	
	private SharedPrefEditor editor;
	
	private static Boolean firstRun;
	
	private static String username;
	
	
	private Utils(Context cont)
	{
		this.cont = cont;
		editor = new SharedPrefEditor(cont);
		this.firstRun = editor.getFirstRun();
		this.setUsername(editor.getUsername());
	}
	
	
	public static Bitmap loadImage(String imageName)
	{
		String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages";
		File imgFile = new File(file_path, imageName);
		Bitmap myBitmap = null;
		
		if(imgFile.exists())
		{
			System.out.println("Found Image on SD card: " + imgFile.getAbsolutePath());
			
			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		}
		return myBitmap;
	}





	/**
	 * @return the firstRun
	 */
	public static Boolean getFirstRun() {
		return firstRun;
	}

	/**
	 * @param firstRun the firstRun to set
	 */
	public void setFirstRun(Boolean firstRun) {
		editor.setIsFirstRun(firstRun);
	}


	/**
	 * @return the username
	 */
	public static String getUsername() {
		return username;
	}


	/**
	 * @param username the username to set
	 */
	public static void setUsername(String username) {
		Utils.username = username;
	}

}
