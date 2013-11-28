package ch.mobileking;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;

import ch.mobileking.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.text.format.Time;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {

	private Camera mCamera;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private boolean mPreviewRunning = false;
	private Button btn_finish_Picture;
	private ImageView camera_show_preview;
	private LinearLayout camera_button_layout;
	private ImageView camera_take_picture;
	private Button btn_retake_picture;
	private TextView camera_text_hint;
	private Button camera_new_scan_addPart;
	private ImageView camera_show_taken_prev_part;
	
	private ArrayList<Bitmap> imageList;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab_fragment_camera_scan);
		
		imageList = new ArrayList<Bitmap>();

		mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);

		mSurfaceHolder = mSurfaceView.getHolder();

		mSurfaceHolder.addCallback(this);

		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		camera_text_hint = (TextView) findViewById(R.id.camera_text_hint);
		
		btn_finish_Picture = (Button) findViewById(R.id.camera_new_scan_finish);
		btn_finish_Picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btn_retake_picture = (Button) findViewById(R.id.camera_new_scan_retake);
		btn_retake_picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				camera_show_preview.setImageBitmap(null);
				
				camera_button_layout.setVisibility(View.INVISIBLE);
				camera_take_picture.setVisibility(View.VISIBLE);
			}
		});
		
		camera_new_scan_addPart = (Button) findViewById(R.id.camera_new_scan_addPart);
		camera_new_scan_addPart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				camera_show_preview.setImageBitmap(null);
				if(imageList!=null)
				{
					int size = imageList.size();
					camera_show_taken_prev_part.setImageBitmap(imageList.get(size-1));

				}
				camera_take_picture.setVisibility(View.VISIBLE);
				camera_button_layout.setVisibility(View.INVISIBLE);
			}
		});
		
		camera_show_taken_prev_part = (ImageView) findViewById(R.id.camera_show_taken_prev_part);
		
		camera_show_preview = (ImageView) findViewById(R.id.camera_show_preview);
		
		camera_take_picture = (ImageView) findViewById(R.id.camera_take_picture);
		
		camera_button_layout = (LinearLayout) findViewById(R.id.camera_button_layout);
		
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
		// TODO Auto-generated method stub
		if (mPreviewRunning) {
			mCamera.stopPreview();
		}

		Camera.Parameters p = mCamera.getParameters();

		p.setPreviewSize(width, height);
        p.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        mCamera.setDisplayOrientation(90);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    
		mCamera.setParameters(p);
		try {

			mCamera.setPreviewDisplay(holder);

		} catch (IOException e) {

			e.printStackTrace();

		}
		mCamera.startPreview();
		
//		mCamera.setPreviewCallback(new Camera.PreviewCallback() { public void onPreviewFrame(byte[] data, Camera camera) { 
//			
//			System.out.println("Preview Callback: ");
//		} });

		
		mPreviewRunning = true;
	}
	
	
	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() 
	{
		private Bitmap image;

		public void onPictureTaken(byte[] imageData, Camera c) {
			
			System.out.println("Picture called: " + imageData);
			System.out.println("getPictureFormat: " + c.getParameters().getPictureFormat());
			
			BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = 1;

            image = BitmapFactory.decodeByteArray(imageData,0,imageData.length,options);
            
            Time now = new Time();
            now.setToNow();
            Random rand = new Random();
            
            Utils.saveBitmap(image , "scan_"+now.year+now.month+now.monthDay+now.hour+now.minute+now.second+rand.nextInt(1000)+".jpg");
            
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            image = Bitmap.createBitmap(image, 0, 0, image.getWidth(),image.getHeight(), matrix, false);
            
            imageList.add(image);
            
            camera_show_preview.setVisibility(View.VISIBLE);
            camera_show_preview.setImageBitmap(image);
            
//            surfaceDestroyed(mSurfaceHolder);
		}
	
	};

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
		try {
			mCamera = Camera.open();
			mCamera.setPreviewDisplay(holder);
			
			camera_take_picture.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					camera_button_layout.setVisibility(View.VISIBLE);
					camera_take_picture.setVisibility(View.INVISIBLE);
					camera_text_hint.setVisibility(View.INVISIBLE);

					// TODO Auto-generated method stub
					mCamera.takePicture(null, null, mPictureCallback);

				}
			});
			
			
		} catch (Exception exception) {
			mCamera.release();
			mCamera = null;
			// TODO: add more exception handling logic here
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mCamera.stopPreview();
		mPreviewRunning = false;
		mCamera.release();
	}

}
