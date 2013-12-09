package ch.mobileking;

import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.mobileking.exception.CustomExceptionHandler;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;
import ch.mobileking.utils.classes.SalesSlip;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {

	private Camera mCamera;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private boolean mPreviewRunning = false;
	
	private LinearLayout camera_button_layout;

	private ImageView camera_show_preview;
	private ImageView camera_take_picture;
	private ImageView camera_show_taken_prev_part;

	
	private ImageButton btn_retake_picture;
	private ImageButton camera_new_scan_addPart;
	private ImageButton btn_finish_Picture;

	private TextView camera_text_hint;

	private SharedPrefEditor editor;

	private Bitmap image;

	private ArrayList<Bitmap> imageList;
	private ProgressBar camera_progress_bar;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tab_fragment_camera_scan);

		editor = new SharedPrefEditor(this);

		if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
			Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(editor.getUsername()));
		}

		Utils.addLogMsg(this.getLocalClassName());

		imageList = new ArrayList<Bitmap>();

		mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);

		mSurfaceHolder = mSurfaceView.getHolder();

		mSurfaceHolder.addCallback(this);

		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		camera_text_hint = (TextView) findViewById(R.id.camera_text_hint);

		camera_progress_bar = (ProgressBar) findViewById(R.id.camera_progress_bar);
		camera_progress_bar.setVisibility(View.INVISIBLE);

		btn_finish_Picture = (ImageButton) findViewById(R.id.camera_new_scan_finish);
		btn_finish_Picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	        	Utils.addLogMsg(CameraActivity.this.getLocalClassName()+": save Picture");

				camera_progress_bar.setVisibility(View.VISIBLE);
				Time now = new Time();
				String fileName = null, simpleFileName = null;
				now.setToNow();
				Random rand = new Random();
				String scanDateFile = "";
				Date d = new Date();

				for (int i = 0; i < imageList.size(); i++) {
					scanDateFile = ""+now.year
							+ (now.month+1) + now.monthDay + now.hour + now.minute
							+ now.second;
					fileName = "scan_" + scanDateFile + "_part" + i;
					simpleFileName = "scan_"+scanDateFile;
					Utils.saveBitmap(imageList.get(i), fileName);
					ProductKing.getInstance().getSalesSlipsParts().add(new SalesSlip(fileName, d.toGMTString(), simpleFileName, i, i));

				}
				ProductKing.getInstance().getStaticSalesSlips().add(new SalesSlip(fileName, d.toGMTString(), simpleFileName, 0, imageList.size()));
				
		        Intent intent = new Intent(CameraActivity.this, MainTabActivity.class);
		        intent.putExtra("salesslip", simpleFileName);
		        setResult(MainTabActivity.CAMERA_REQUEST, intent);

				finish();
			}
		});

		btn_retake_picture = (ImageButton) findViewById(R.id.camera_new_scan_retake);
		btn_retake_picture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
	        	Utils.addLogMsg(CameraActivity.this.getLocalClassName()+": retake Picture");

				// TODO Auto-generated method stub
				camera_show_preview.setImageBitmap(null);
				int size = imageList.size();
				if (imageList != null) {
					imageList.remove(size - 1);
				}
				camera_button_layout.setVisibility(View.INVISIBLE);
				camera_take_picture.setVisibility(View.VISIBLE);
				
				startPreview();
			}
		});

		camera_new_scan_addPart = (ImageButton) findViewById(R.id.camera_new_scan_addPart);
		camera_new_scan_addPart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
	        	Utils.addLogMsg(CameraActivity.this.getLocalClassName()+": add Picture");

				camera_show_preview.setImageBitmap(null);
				if (imageList != null) {
					int size = imageList.size();
					camera_show_taken_prev_part.setVisibility(View.VISIBLE);
					camera_show_taken_prev_part.setImageBitmap(imageList.get(size - 1));

				}
				camera_take_picture.setVisibility(View.VISIBLE);
				camera_button_layout.setVisibility(View.INVISIBLE);

				startPreview();
			}
		});

		camera_show_taken_prev_part = (ImageView) findViewById(R.id.camera_show_taken_prev_part);

		camera_show_preview = (ImageView) findViewById(R.id.camera_show_preview);

		camera_take_picture = (ImageView) findViewById(R.id.camera_take_picture);

		camera_button_layout = (LinearLayout) findViewById(R.id.camera_button_layout);

	}
	
	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] imageData, Camera c) {
			

			System.out.println("Picture called: " + imageData);
			System.out.println("getPictureFormat: "
					+ c.getParameters().getPictureFormat());

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;

			image = BitmapFactory.decodeByteArray(imageData, 0,
					imageData.length, options);

			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);

			imageList.add(image);

			camera_show_preview.setVisibility(View.VISIBLE);
			camera_show_preview.setImageBitmap(image);

//			surfaceDestroyed(mSurfaceHolder);
		}

	};
	
	private void startPreview()
	{
		mPreviewRunning = true;
		mCamera.startPreview();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		if (mPreviewRunning) {
			mCamera.stopPreview();
			mPreviewRunning = false;
		}
		
		Camera.Parameters cameraParams = mCamera.getParameters();
        configureCameraParameters(cameraParams, isPortrait());

        try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        mCamera.startPreview();
		mPreviewRunning = true;
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub

		try {
			mCamera = Camera.open();
			mCamera.setDisplayOrientation(90);
//			mCamera.setPreviewDisplay(holder);

			mCamera.setPreviewDisplay(holder);
//            mCamera.startPreview();

			camera_take_picture.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					mCamera.autoFocus(new AutoFocusCallback() {
				        @Override
				        public void onAutoFocus(boolean success, Camera camera) {
				            if(success){
				            	camera_button_layout.setVisibility(View.VISIBLE);
								camera_take_picture.setVisibility(View.INVISIBLE);
								camera_text_hint.setVisibility(View.INVISIBLE);
				                camera.takePicture(null, null, mPictureCallback);
				            }
				        }
				    });
					// TODO Auto-generated method stub
//					mCamera.takePicture(null, null, mPictureCallback);

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

	protected void configureCameraParameters(Camera.Parameters cameraParams, boolean portrait) {
            int angle;
            Display display = this.getWindowManager().getDefaultDisplay();
            switch (display.getRotation()) {
                case Surface.ROTATION_0: // This is display orientation
                    angle = 90; // This is camera orientation
                    break;
                case Surface.ROTATION_90:
                    angle = 0;
                    break;
                case Surface.ROTATION_180:
                    angle = 270;
                    break;
                case Surface.ROTATION_270:
                    angle = 180;
                    break;
                default:
                    angle = 90;
                    break;
            }
            mCamera.setDisplayOrientation(angle);
            
        Camera.Size size = getBestPreviewSize(display.getWidth(), display.getHeight());
        cameraParams.setPreviewSize(size.width, size.height);
        cameraParams.setPictureSize(size.width, size.height);
        cameraParams.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        cameraParams.setFlashMode(Parameters.FLASH_MODE_AUTO);
        
        mCamera.setParameters(cameraParams);
    }
	
	private Camera.Size getBestPreviewSize(int width, int height)
    {

                // Get For Photo Size
        Camera.Parameters camparams = mCamera.getParameters();

        // Find the Largest Possible Preview Sizes
        List<Size> sizes = camparams.getSupportedPreviewSizes();
        Camera.Size result=null;
        for (Size s : sizes) {

            if (s.width <= width && s.height <= height) {
                       if (result == null) {
                        result = s;
                       } else {
                        int resultArea=result.width*result.height; 
                        int newArea=s.width*s.height;

                        if (newArea>resultArea) {
                                            result=s;
                        }
                           } // end else (result=null)
                } // end if (width<width&&height<height)
        } // end for

            return result;

    } // end function


    public boolean isPortrait() {
        return (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }

}
