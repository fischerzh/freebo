package ch.mobileking.tabs;

import java.util.ArrayList;

import ch.mobileking.BadgeActivity;
import ch.mobileking.CameraActivity;
import ch.mobileking.LeaderboardActivity;
import ch.mobileking.MainActivity;
import ch.mobileking.ProductDetailOverview;
import ch.mobileking.R;
import ch.mobileking.activity.old.RecommActivity;
import ch.mobileking.activity.old.StoreKingActivity;
import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.classes.override.ProductBaseAdapter;
import ch.mobileking.classes.override.RecommArrayAdapter;
import ch.mobileking.utils.Badge;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import ch.mobileking.utils.SharedPrefEditor;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MainCameraScanFragment extends Fragment{
	
	
    private SharedPrefEditor editor;
	private Button camera_new_scan;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.tab_fragment_camera, container, false);
        
        editor = new SharedPrefEditor(getActivity());
        
        return rootView;
    }
    
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        
        camera_new_scan = (Button) getActivity().findViewById(R.id.camera_new_scan);
        camera_new_scan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), CameraActivity.class);
	        	startActivity(intent);	
			}
		});

    }
    
	private void createAlert(String message, String title, int iconId) {
		// Build the dialog
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		alert.setTitle(title);
		alert.setMessage(message);
		// Create TextView
		final TextView input = new TextView (getActivity());
		alert.setView(input);
		alert.setIcon(iconId);

		alert.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			Intent intent = new Intent(getActivity(), BadgeActivity.class);
        	startActivity(intent);	
		  }
		});

		alert.show();
	}
    
}


	 
