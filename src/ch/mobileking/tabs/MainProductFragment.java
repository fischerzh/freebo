package ch.mobileking.tabs;

import java.util.ArrayList;

import ch.mobileking.MainTabActivity;
import ch.mobileking.ProductDetailOverview;
import ch.mobileking.R;
import ch.mobileking.activity.barcode.BarCodeScanner;
import ch.mobileking.classes.override.MessageDialog;
import ch.mobileking.classes.override.ProductBaseAdapter;
import ch.mobileking.exception.CustomExceptionHandler;
import ch.mobileking.login.AsyncLogin;
import ch.mobileking.login.AsyncUpdate;
import ch.mobileking.tabs.intro.BaseSampleActivity;
import ch.mobileking.tabs.intro.IntroSequenceActivity;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.SharedPrefEditor;
import ch.mobileking.utils.Utils;
import ch.mobileking.utils.classes.Products;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainProductFragment extends Fragment implements ITaskComplete {
	
	private static final int BARCODE_REQUEST = 1;
	
	private ListView listView;
	
	private ProductBaseAdapter adapter;

	private Button btn_delete;
	
	private RelativeLayout progressLayout;
	
	private int prodLayoutResourceId;
	
	private boolean editVisible = false;

	private SharedPrefEditor editor;

	private Button tab_main_btn_start_optin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.tab_fragment_main_product, container, false);
        return rootView;
    }
    
    
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        
        editor = new SharedPrefEditor(getActivity());

		if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
			Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(editor.getUsername()));
		}
           
        setHasOptionsMenu(true);
        
        listView = (ListView) getActivity().findViewById(R.id.tab_main_listView);
        
        setProdLayoutResourceId(R.layout.product_item);

		Utils.loadAllImagesFromWeb(this);
        
        adapter = new ProductBaseAdapter(getActivity(), getProdLayoutResourceId(), (ArrayList<Products>) ProductKing.getInstance().getStaticProducts()); //R.layout.product_item
		
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
	        	Utils.onSyncRequest();
	        	
				System.out.println("Position clicked: " + position + " " + ((Products)listView.getItemAtPosition(position)).getName());
		        Intent intent = new Intent(getActivity(), ProductDetailOverview.class);
//		        Products prod = (Products) listView.getItemAtPosition(position);
		        intent.putExtra("product", String.valueOf(position));
		        getActivity().setResult(1, intent);
		        getActivity().startActivityForResult(intent, 1);
			}
        });
        
        tab_main_btn_start_optin = (Button) getActivity().findViewById(R.id.tab_main_btn_start_optin);

 		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) 
 		{
 			tab_main_btn_start_optin.setVisibility(View.VISIBLE);
 		}	
 		else
 		{
 			tab_main_btn_start_optin.setVisibility(View.GONE);
 		}
 		tab_main_btn_start_optin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startBarcodeScanner();
			}
		});
        
        btn_delete = (Button) getActivity().findViewById(R.id.tab_main_btn_delete);
        btn_delete.setVisibility(View.GONE);
        
        btn_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				showInfoUpdated(false);
				Utils.addLogMsg("MainProductFragment: Opt-Out clicked!");

				updateAllUserInfo();
			}
		});
        
        progressLayout = (RelativeLayout) getActivity().findViewById(R.id.tab_main_progressLayout);
        progressLayout.setVisibility(View.INVISIBLE);
        
		if(isFirstTime())
		{
			setHelpActive();
		}
    }
    
    @Override
    public void onViewStateRestored(Bundle savedInstance)
    {
    	super.onViewStateRestored(savedInstance);
    	System.out.println("View state restored");
    }
    
    
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
//    	super.onActivityResult(requestCode, resultCode, data);
    	System.out.println("On Activity Result code: " + requestCode + ", Request Code: " + requestCode );

    }
    
	@Override
	public void onLoginCompleted(boolean completed, String message) {
		System.out.println("MainProductFragment, LoginCompleted! Restarting Activity...");

		updateAdapterData();

		progressLayout.setVisibility(View.INVISIBLE);
        
	}

	@Override
	public void onUpdateCompleted(boolean completed, String message) {
		if(completed)
		{
			System.out.println("MainProductFragment, UpdateCompleted! Restarting Activity...");
			System.out.println("Message from updateCompleted: " + message);
			
			if(isEditVisible())
				setEditStyle();
			
			reloadUserInfo(message);
		}

	   progressLayout.setVisibility(View.INVISIBLE);

	}
    
	@Override
	public void startUpdate() {
        progressLayout.setVisibility(View.VISIBLE);

	}

	@Override
	public void startLogin() {
		// TODO Auto-generated method stub
		
	}
    
    public void updateAdapterData()
    {

    	System.out.println("Update Adapter Data");
    	
    	if(isAdded())
    	{

//    		setProdLayoutResourceId(R.layout.product_item);
    		System.out.println("Fragment is ready... refresh Adapter!");
        	adapter = new ProductBaseAdapter(getActivity() ,getProdLayoutResourceId(), (ArrayList<Products>) ProductKing.getStaticProducts()); 
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
    	}

    }
    
    private void updateAllUserInfo()
    {
        progressLayout.setVisibility(View.VISIBLE);
        new AsyncUpdate(getActivity(), false, true, this).execute(editor.getUsername(),editor.getPwd()); //http://192.168.0.16:8080
    }
    
	private void reloadUserInfo(String msg)
	{
		new AsyncLogin(getActivity(), true, this).execute(editor.getUsername(), editor.getPwd());
		if(msg!="")
			createAlert(msg, "Aktualisierung", R.drawable.ic_empfehlungen);
		
	}
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
             super.onCreateOptionsMenu(menu, inflater);
     		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
    			// addPreferencesFromResource(R.xml.preferences);
     			inflater.inflate(R.menu.action_bar_small, menu);
    		}
     		else
     		{
     			inflater.inflate(R.menu.action_bar, menu);
     		}
             
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            super.onOptionsItemSelected(item);
			Utils.addLogMsg("MainProductFragment: Options Menu Selected "+ item.getTitle());
            switch (item.getItemId()) {
            case R.id.action_edit:
                setEditStyle();
                break;
            case R.id.action_show_help:
	        	System.out.println("Show Help!!");
//	        	setHelpActive();
	        	startIntroSequence();
	            return true;
	        case R.id.action_scan:
	        	startBarcodeScanner();
	            return true;

            }
            return super.onOptionsItemSelected(item);
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

    
	private void setHelpActive()
	{
		DialogFragment newFragment = MessageDialog.newInstance(R.layout.loyalty_instructions);
	    newFragment.show(getFragmentManager(), "dialog");
	    
	}
	
	private void startIntroSequence()
	{
		Intent intent = new Intent(getActivity(), IntroSequenceActivity.class);
		startActivity(intent);
		getActivity().finish();
	}
	
	private void startBarcodeScanner()
	{
		// Set this Fragment as a listener through its Parent activity which will get the Result of the Barcode Scanner
		((MainTabActivity)getActivity()).setTaskListener(this);
		Intent intent = new Intent(getActivity(), BarCodeScanner.class);
		startActivityForResult(intent, MainTabActivity.BARCODE_REQUEST);
	}
	
	private boolean isFirstTime()
	{
	    Boolean isFirstRun = editor.getFirstRun();
	    if (isFirstRun) 
	    {
	    	editor.setIsFirstRun(false);
		}
	    return isFirstRun;
	}
	
	private void setEditStyle()
	{
		if(!isEditVisible())
		{
			setEditVisible(true);
	        btn_delete.setVisibility(View.VISIBLE);
 			tab_main_btn_start_optin.setVisibility(View.GONE);
			setProdLayoutResourceId(R.layout.product_item_edit);
		}
		else
		{
			setEditVisible(false);
	        btn_delete.setVisibility(View.GONE);
	        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) 
	 			tab_main_btn_start_optin.setVisibility(View.VISIBLE);
			setProdLayoutResourceId(R.layout.product_item);
		}
		updateAdapterData();

	}


	/**
	 * @return the prodLayoutResourceId
	 */
	public int getProdLayoutResourceId() {
		return prodLayoutResourceId;
	}


	/**
	 * @param prodLayoutResourceId the prodLayoutResourceId to set
	 */
	public void setProdLayoutResourceId(int prodLayoutResourceId) {
		this.prodLayoutResourceId = prodLayoutResourceId;
	}


	/**
	 * @return the editVisible
	 */
	public boolean isEditVisible() {
		return editVisible;
	}


	/**
	 * @param editVisible the editVisible to set
	 */
	public void setEditVisible(boolean editVisible) {
		this.editVisible = editVisible;
	}


	@Override
	public void sendProgressUpdate(int progress) {
		// TODO Auto-generated method stub
		
	}



}


