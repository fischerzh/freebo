package ch.mobileking.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

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
	
	
	public static Bitmap loadImage(Products prod)
	{
		String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages";
		File imgFile = new File(file_path, prod.getId()+".png");
		Bitmap myBitmap = null;
		
		if(!imgFile.exists())
		{
			try {
				myBitmap = BitmapFactory.decodeStream((InputStream)new URL(prod.getImagelink()).getContent());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return myBitmap;
	}
	
	public static Bitmap loadImageFromPath(Products prod) {
		String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages";
		File imgFile = new File(file_path, prod.getId()+".png");
		Bitmap myBitmap = null;
		
		if(imgFile.exists())
		{
			System.out.println("Found Image on SD card: " + imgFile.getAbsolutePath());
			
			myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		}
		return myBitmap;

	}

	public static boolean imageExists(Products prod)
	{
		String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages";
		File imgFile = new File(file_path, prod.getId()+".png");
		if(imgFile.exists())
		{
			System.out.println("Image exists SD card: " + imgFile.getAbsolutePath());
		}
		else
		{
			System.out.println("Image does not exist on SD card... loading and saving Image to: " + imgFile.getAbsolutePath());
			file_path = saveBitmap(loadImage(prod), prod);
		}
		
		return imgFile.exists();
	}
	

	public static String saveBitmap(Bitmap bmp, Products prod)
	{
		
	    String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileKingImages";
        File dir = new File(file_path);
        if(!dir.exists())
           dir.mkdirs();
        File file = new File(dir, prod.getId()+".png");
//		Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
			fOut.flush();
	        fOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    System.out.println("Saved Image to SD card: " +file.getAbsolutePath());
	    return file.getAbsolutePath();
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
