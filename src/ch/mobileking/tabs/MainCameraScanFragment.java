package ch.mobileking.tabs;

import java.io.File;
import java.util.ArrayList;

import ch.mobileking.CameraActivity;
import ch.mobileking.LeaderboardActivity;
import ch.mobileking.MainActivity;
import ch.mobileking.MainTabActivity;
import ch.mobileking.ProductDetailOverview;
import ch.mobileking.R;
import ch.mobileking.activity.barcode.BarCodeScanner;
import ch.mobileking.activity.old.BadgeActivity;
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
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
		
        setHasOptionsMenu(true);
		
		for(SalesSlip slip : ProductKing.getInstance().getStaticSalesSlips()){
			if(!Utils.imageExists(slip.getSalespoint()))
				Utils.loadBitmapFromURL(slip.getImageLink(), slip.getSalespoint());
		}
		
		if(editor.getSalesSlips().size()>0)
		{
			for(int i = 0; i < editor.getSalesSlips().size(); i++)
			{
				System.out.println("Slip not uploaded: " +editor.getSalesSlips().get(i));
				if(editor.getSalesSlips().get(i).getSimpleFileName()=="" || editor.getSalesSlips().get(i).getSimpleFileName()==null)
				{
					String filename = editor.getSalesSlips().get(i).getFilename();
					filename = filename.substring(0,  filename.indexOf("_part"));
					System.out.println("simpleFileName reconstructed: " + filename);
					editor.getSalesSlips().get(i).setSimpleFileName(filename);
				}
				ProductKing.getInstance().getSalesSlipsParts().add(editor.getSalesSlips().get(i));
			}
			
		}
		
		System.out.println("SalesSlip count: " + editor.getSalesSlips().size());
		
        salesslip_progress =(ProgressBar) getActivity().findViewById(R.id.salesslip_progress);
        salesslip_main_progress_layout = (LinearLayout) getActivity().findViewById(R.id.salesslip_main_progress_layout);
        
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
				else if(test.getIsapproved()==1)
				{
					if(test.getIsuploaded())
					{
				        Intent intent = new Intent(getActivity(), SalesSlipImageViewer.class);
				        intent.putExtra("filename", test.getFilename());
				        intent.putExtra("totalparts", test.getTotalparts());
				        startActivityForResult(intent, 77);
					}
			        else
			        {
			            salesslip_main_progress_layout.setVisibility(View.VISIBLE);

						System.out.println("Slip "+ test.getFilename() +" uploaded: " + test.getIsuploaded());
						((MainTabActivity)getActivity()).setTaskListener(MainCameraScanFragment.this);
						Utils.setListener(((MainTabActivity)getActivity()));
						Utils.saveAllBitmapAsync(editor);
			        }
				}
				else if(test.getIsapproved()==0)
				{
					createAlert("Einkauf konnte nicht verifiziert werden: "+ test.getRejectmessage(), "Einkauf nicht verifiziert!", R.drawable.ic_store_hero);

				}
				
			}
		});
        
        reloadAdapterInfo();
        
        salesslip_tab_btn_newscan = (Button) getActivity().findViewById(R.id.salesslip_tab_btn_newscan);
		System.out.println("SDK: " + Build.VERSION.SDK_INT +  " " + Build.VERSION.CODENAME);
 		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
 			salesslip_tab_btn_newscan.setVisibility(View.VISIBLE);
 		}
        salesslip_tab_btn_newscan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startCameraIntent();
			}
		});
        
        
        
    }
    
    private void startCameraIntent()
    {
    	if(Utils.isNetworkAvailable(getActivity().getApplicationContext()))
		{
			((MainTabActivity)getActivity()).setTaskListener(MainCameraScanFragment.this);
			Intent intent = new Intent(getActivity(), CameraActivity.class);
    		startActivityForResult(intent, MainTabActivity.CAMERA_REQUEST);
		}
		else
		{
			Toast.makeText(getActivity().getApplicationContext(), "Internet wird benštigt!", Toast.LENGTH_LONG).show();
		}

    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
     		if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT)
     		{
     			inflater.inflate(R.menu.action_bar_scan, menu);
     		}
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            super.onOptionsItemSelected(item);
			Utils.addLogMsg("MainProductFragment: Options Menu Selected "+ item.getTitle());
            switch (item.getItemId()) {
	        case R.id.action_start_camera:
	        	startCameraIntent();
	            return true;

            }
            return super.onOptionsItemSelected(item);
    }
    
    
    private void reloadAdapterInfo()
    {
    	
		ArrayList<Object> salesSlipItems = new ArrayList<Object>();
		for(int i = 0; i < ProductKing.getInstance().getStaticSalesSlips().size(); i++)
		{
			if(isInList(ProductKing.getInstance().getStaticSalesSlips().get(i)))
			{
				ProductKing.getInstance().getStaticSalesSlips().get(i).setIsuploaded(false);
			}
		}
		salesSlipItems.addAll(ProductKing.getInstance().getStaticSalesSlips());

//			salesSlipItems.addAll(editor.getSalesSlips());
		ImageAdapter adapter = new ImageAdapter(getActivity().getApplicationContext(), salesSlipItems, R.layout.activity_salesslips_item); 
		salesslips_listView.setAdapter(adapter);
		
    }
    
    private boolean isInList(SalesSlip item)
    {
    	if(item!=null)
    	{
        	System.out.println("check item if not uploaded: " + item.getFilename());
        	for(SalesSlip slip : editor.getSalesSlips())
        	{
        		System.out.println("item not uploaded: " + slip.getFilename());
        		if(slip.getFilename().contains(item.getFilename()))
        			return true;
        	}
    	}
    	return false;
    }

	@Override
	public void onLoginCompleted(boolean completed, String string) {
		System.out.println("MainCameraScanFramgent.onLoginCompleted: " +string);
		if(completed)
			reloadAdapterInfo();
	}

	@Override
	public void onUpdateCompleted(boolean b, String string) {
		// TODO Auto-generated method stub
		System.out.println("MainCameraScanFramgent.onUpdateCompleted: " +string);

        salesslip_main_progress_layout.setVisibility(View.GONE);

//		createAlert("Besten Dank, Dein Einkauf wurde uns Ÿbermittelt. Wir werden diesen in kŸrze prŸfen!", "Einkauf registriert!", R.drawable.ic_store_hero);

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


	 
