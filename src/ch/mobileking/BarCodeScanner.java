package ch.mobileking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;

public class BarCodeScanner extends Activity implements ScanditSDKListener{
	
	// The main object for recognizing a displaying barcodes.
    private ScanditSDK mBarcodePicker;
    
    // Enter your Scandit SDK App key here.
    // Your Scandit SDK App key is available via your Scandit SDK web account.
    public static final String sScanditSdkAppKey = "cWur/h3fEeOQEl8eE1cvKXIQzCRT4s/vNKGMA076yYo";

	private static final int BARCODE_RESPONSE = 11;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize and start the bar code recognition.
        initializeAndStartBarcodeScanning();
    }
    
    @Override
    protected void onPause() {
        // When the activity is in the background immediately stop the 
        // scanning to save resources and free the camera.
        mBarcodePicker.stopScanning();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        // Once the activity is in the foreground again, restart scanning.
        mBarcodePicker.startScanning();
        super.onResume();
    }
    

    /**
     * Initializes and starts the bar code scanning.
     */
    public void initializeAndStartBarcodeScanning() {
        // Switch to full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        ScanditSDKAutoAdjustingBarcodePicker picker = new ScanditSDKAutoAdjustingBarcodePicker(
                    this, sScanditSdkAppKey, ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK);
        
        picker.getOverlayView().setVibrateEnabled(false);
        
        // Add both views to activity, with the scan GUI on top.
        setContentView(picker);
        mBarcodePicker = picker;
        
        // Register listener, in order to be notified about relevant events 
        // (e.g. a successfully scanned bar code).
        mBarcodePicker.getOverlayView().addListener(this);
        
        // Show a search bar in the scan user interface.
        mBarcodePicker.getOverlayView().showSearchBar(true);
        
        mBarcodePicker.getOverlayView().setBeepEnabled(true);
    }
    

	@Override
	public void didScanBarcode(String barcode, String symbology) {
		
		System.out.println("Barcode scanned: " +barcode);

        mBarcodePicker.stopScanning();
        
        Intent intent = new Intent(this, MainTabActivity.class);
        intent.putExtra("barcode", barcode.trim());
        setResult(BARCODE_RESPONSE, intent);
        
//        startActivityForResult(intent, 1);
        
        finish();
        // Stop recognition to save resources.
	}

    @Override
    public void onBackPressed() {
        mBarcodePicker.stopScanning();
        finish();
    }

	@Override
	public void didCancel() {
        mBarcodePicker.stopScanning();
        finish();		
	}

	@Override
	public void didManualSearch(String entry) {
    	Toast.makeText(this, "User entered: " + entry, Toast.LENGTH_LONG).show();
    	mBarcodePicker.stopScanning();
    	
        Intent intent = new Intent(this, ProductOverview.class);
        intent.putExtra("barcode", entry.trim());
        setResult(1, intent);
        startActivityForResult(intent, 1);
    	
        finish();
	}

}
