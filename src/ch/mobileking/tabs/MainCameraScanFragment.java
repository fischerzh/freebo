package ch.mobileking.tabs;

import java.io.File;
import java.util.ArrayList;

import ch.mobileking.BadgeActivity;
import ch.mobileking.CameraActivity;
import ch.mobileking.LeaderboardActivity;
import ch.mobileking.MainActivity;
import ch.mobileking.MainTabActivity;
import ch.mobileking.ProductDetailOverview;
import ch.mobileking.R;
import ch.mobileking.activity.barcode.BarCodeScanner;
import ch.mobileking.activity.old.RecommActivity;
import ch.mobileking.activity.old.StoreKingActivity;
import ch.mobileking.activity.salesslips.SalesSlipDetail;
import ch.mobileking.activity.salesslips.SalesSlipImageViewer;
import ch.mobileking.activity.salesslips.SalesSlipsActivity;
import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.classes.override.ProductBaseAdapter;
import ch.mobileking.classes.override.RecommArrayAdapter;
import ch.mobileking.exception.CustomExceptionHandler;
import ch.mobileking.login.AsyncLogin;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;
import ch.mobileking.utils.classes.Badge;
import ch.mobileking.utils.classes.Products;
import ch.mobileking.utils.classes.SalesSlip;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MainCameraScanFragment extends Fragment implements ITaskComplete{
	
	
    private static final int CAMERA_REQUEST = 1;
	private SharedPrefEditor editor;
	private Button salesslip_tab_btn_newscan;
	
	ListView salesslips_listView;
	private ProgressBar salesslip_progress;
	private LinearLayout salesslip_main_progress_layout;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.activity_salesslips_main, container, false);
        
        editor = new SharedPrefEditor(getActivity());
        
        return rootView;
    }
    
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        
		if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
			Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(editor.getUsername()));
		}
        
        salesslips_listView = (ListView) getActivity().findViewById(R.id.salesslip_lv);
        salesslips_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				System.out.println("Position clicked: " + position + " with ID: " + id);
				SalesSlip test = ((SalesSlip)salesslips_listView.getItemAtPosition(position));
				System.out.println("SalesSlip: " + test.getFilename());
				String.valueOf(position);
		        
				if(test.getIsapproved()==2)
				{
			        Intent intent = new Intent(getActivity(), SalesSlipDetail.class);
			        intent.putExtra("itemId", position);
			        startActivityForResult(intent, 66);
				}
				else
				{
			        Intent intent = new Intent(getActivity(), SalesSlipImageViewer.class);
			        intent.putExtra("filename", test.getFilename());
			        intent.putExtra("totalparts", test.getTotalparts());
			        startActivityForResult(intent, 77);
				}

				
			}
		});
        
        salesslip_main_progress_layout = (LinearLayout) getActivity().findViewById(R.id.salesslip_main_progress_layout);
        
        reloadAdapterInfo();
        
        salesslip_tab_btn_newscan = (Button) getActivity().findViewById(R.id.salesslip_tab_btn_newscan);
        salesslip_tab_btn_newscan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(Utils.isNetworkAvailable(getActivity().getApplicationContext()))
				{
					((MainTabActivity)getActivity()).setTaskListener(MainCameraScanFragment.this);
					Intent intent = new Intent(getActivity(), CameraActivity.class);
		    		startActivityForResult(intent, MainTabActivity.CAMERA_REQUEST);
				}
				else
				{
					Toast.makeText(getActivity().getApplicationContext(), "Internet wird benötigt!", Toast.LENGTH_LONG).show();
				}

			}
		});
        
        salesslip_progress =(ProgressBar) getActivity().findViewById(R.id.salesslip_progress);
        
        
    }
    
    
    private void reloadAdapterInfo()
    {
    	
		ArrayList<Object> salesSlipItems = new ArrayList<Object>();
		salesSlipItems.addAll(ProductKing.getInstance().getStaticSalesSlips());
		ImageAdapter adapter = new ImageAdapter(getActivity().getApplicationContext(), salesSlipItems, R.layout.activity_salesslips_item); 
		salesslips_listView.setAdapter(adapter);
    }

	@Override
	public void onLoginCompleted(boolean b, String string) {
		System.out.println("MainCameraScanFramgent.onLoginCompleted: " +string);
		if(b)
			reloadAdapterInfo();
	}

	@Override
	public void onUpdateCompleted(boolean b, String string) {
		// TODO Auto-generated method stub
		salesslip_progress.setVisibility(View.INVISIBLE);
		System.out.println("MainCameraScanFramgent.onUpdateCompleted: " +string);

        salesslip_main_progress_layout.setVisibility(View.GONE);

//		createAlert("Besten Dank, Dein Einkauf wurde uns übermittelt. Wir werden diesen in kürze prüfen!", "Einkauf registriert!", R.drawable.ic_store_hero);

		new AsyncLogin(getActivity(), true, this).execute(editor.getUsername(), editor.getPwd());
		reloadAdapterInfo();
		
	}

	@Override
	public void startUpdate() {
//		reloadAdapterInfo();
//		salesslip_progress.setVisibility(View.VISIBLE);
        salesslip_main_progress_layout.setVisibility(View.VISIBLE);

	}

	@Override
	public void startLogin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendProgressUpdate(int progress) {
		// TODO Auto-generated method stub
		salesslip_progress.setProgress(progress);
	}
	
	public void createAlert(String message, String title, int iconId) {
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
		    // Do something with value!
		  }
		});

		alert.show();
	}
    
}


	 
