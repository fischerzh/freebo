package ch.mobileking.tabs;

import java.util.ArrayList;

import ch.mobileking.BarCodeScanner;
import ch.mobileking.MainTabActivity;
import ch.mobileking.ProductDetailOverview;
import ch.mobileking.R;
import ch.mobileking.classes.override.MessageDialog;
import ch.mobileking.classes.override.ProductBaseAdapter;
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
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class MainProductFragment extends Fragment{
	
	private static final int BARCODE_REQUEST = 1;
	
	private ListView listView;
	
	private ProductBaseAdapter adapter;

	private Button btn_delete;
	
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
    	System.out.println("On Activity Result code");

    }
    
    public void updateAdapterData()
    {

    	System.out.println("Update Adapter Data");
    	
    	if(isAdded())
    	{
//    		setProdLayoutResourceId(R.layout.product_item);
        	adapter = new ProductBaseAdapter(getActivity() ,getProdLayoutResourceId(), (ArrayList<Products>) ProductKing.getStaticProducts()); //R.layout.product_item
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
    	}

    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
             super.onCreateOptionsMenu(menu, inflater);
               inflater.inflate(R.menu.action_bar, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            super.onOptionsItemSelected(item);
            System.out.println("On Options Item Selected!");
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
		DialogFragment newFragment = new MessageDialog();
	    newFragment.show(getFragmentManager(), "Hilfe");
	}
	
	private void startBarcodeScanner()
	{
		Intent intent = new Intent(getActivity(), BarCodeScanner.class);
		startActivityForResult(intent, 1);
	}
	
	private boolean isFirstTime()
	{
	    Boolean ranBefore = editor.getFirstRun();
	    if (!ranBefore) 
	    {
	    	editor.setIsFirstRun(true);
		}
	    return ranBefore;
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


