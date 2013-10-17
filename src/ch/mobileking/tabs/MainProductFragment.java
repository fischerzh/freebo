package ch.mobileking.tabs;

import java.util.ArrayList;

import ch.mobileking.BarCodeScanner;
import ch.mobileking.MainTabActivity;
import ch.mobileking.ProductDetailOverview;
import ch.mobileking.R;
import ch.mobileking.classes.override.MessageDialog;
import ch.mobileking.classes.override.ProductBaseAdapter;
import ch.mobileking.login.AsyncLogin;
import ch.mobileking.login.AsyncUpdate;
import ch.mobileking.utils.ITaskComplete;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import ch.mobileking.utils.SharedPrefEditor;
import android.app.Activity;
import android.content.Intent;
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

public class MainProductFragment extends Fragment implements ITaskComplete {
	
	private static final int BARCODE_REQUEST = 1;
	
	private ListView listView;
	
	private ProductBaseAdapter adapter;

	private Button btn_delete;
	
	private RelativeLayout progressLayout;
	
	private int prodLayoutResourceId;
	
	private boolean editVisible = false;

	private SharedPrefEditor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.tab_fragment_main_product, container, false);
        return rootView;
    }
    
    
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
           
        setHasOptionsMenu(true);
        
        editor = new SharedPrefEditor(getActivity());
        
        listView = (ListView) getActivity().findViewById(R.id.tab_main_listView);
        
        setProdLayoutResourceId(R.layout.product_item);
        
        adapter = new ProductBaseAdapter(getActivity(), getProdLayoutResourceId(), (ArrayList<Products>) ProductKing.getStaticProducts()); //R.layout.product_item
		
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				
				System.out.println("Position clicked: " + position + " " + ((Products)listView.getItemAtPosition(position)).getName());
		        Intent intent = new Intent(getActivity(), ProductDetailOverview.class);
//		        Products prod = (Products) listView.getItemAtPosition(position);
		        intent.putExtra("product", String.valueOf(position));
		        getActivity().setResult(1, intent);
		        getActivity().startActivityForResult(intent, 1);
			}
        });
        
        btn_delete = (Button) getActivity().findViewById(R.id.tab_main_btn_delete);
        btn_delete.setVisibility(View.INVISIBLE);
        
        btn_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				showInfoUpdated(false);
				
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
	public void onLoginCompleted(boolean completed) {
		System.out.println("MainProductFragment, LoginCompleted! Restarting Activity...");
//		restartActivity();
//		adapter = new ProductBaseAdapter(this, getProdLayoutResourceId(), (ArrayList<Products>) ProductKing.getStaticProducts()); 
		updateAdapterData();
//		adapter.notifyDataSetChanged();
//		listView.setAdapter(adapter);
        progressLayout.setVisibility(View.INVISIBLE);
		DialogFragment newFragment = MessageDialog.newInstance(R.layout.loyalty_alerts);
	    newFragment.show(getFragmentManager(), "dialog");
	}

	@Override
	public void onUpdateCompleted(boolean completed) {
		if(completed)
		{
			System.out.println("MainProductFragment, UpdateCompleted! Restarting Activity...");
			reloadUserInfo();
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
    
	private void reloadUserInfo()
	{
		new AsyncLogin(getActivity(), true, this).execute(editor.getUsername(), editor.getPwd());
	}
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
             super.onCreateOptionsMenu(menu, inflater);
               inflater.inflate(R.menu.action_bar, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            super.onOptionsItemSelected(item);
            System.out.println("Options Menu Selected: " + item.getItemId());
            switch (item.getItemId()) {
            case R.id.action_edit:
                setEditStyle();
                break;
            case R.id.action_show_help:
	        	System.out.println("Show Help!!");
	        	setHelpActive();
	            return true;
	        case R.id.action_scan:
	        	startBarcodeScanner();
	            return true;

            }
            return super.onOptionsItemSelected(item);
    }
    
	private void setHelpActive()
	{
		DialogFragment newFragment = MessageDialog.newInstance(R.layout.loyalty_instructions);
	    newFragment.show(getFragmentManager(), "dialog");
	}
	
	private void startBarcodeScanner()
	{
		// Set this Fragment as a listener through its Parent activity which will get the Result of the Barcode Scanner
		((MainTabActivity)getActivity()).setTaskListener(this);
		Intent intent = new Intent(getActivity(), BarCodeScanner.class);
		startActivityForResult(intent, 1);
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
			setProdLayoutResourceId(R.layout.product_item_edit);
		}
		else
		{
			setEditVisible(false);
	        btn_delete.setVisibility(View.INVISIBLE);
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



}


