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
        
        // We instantiate the automatically adjusting barcode picker that will
        // choose the correct picker to instantiate. Be aware that this picker
        // should only be instantiated if the picker is shown full screen as the
        // legacy picker will rotate the orientation and not properly work in
        // non-fullscreen.
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
		// Remove non-relevant characters that might be displayed as rectangles
        // on some devices. Be aware that you normally do not need to do this.
        // Only special GS1 code formats contain such characters.
		System.out.println("Barcode scanned: " +barcode);
        Toast.makeText(this, symbology + ": " + barcode, Toast.LENGTH_LONG).show();

//        String cleanedBarcode = "";
//        for (int i = 0 ; i < barcode.length(); i++) {
//            if (barcode.charAt(i) > 30) {
//                cleanedBarcode += barcode.charAt(i);
//            }
//        }
        
        mBarcodePicker.stopScanning();
        
        Intent intent = new Intent();
        intent.putExtra("barcode", barcode.trim());
        intent.putExtra("symbology", symbology);
        setResult(1, intent);
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
        finish();
	}

}
