package ch.mobileking.tabs;

import java.util.ArrayList;

import ch.mobileking.ProductDetailOverview;
import ch.mobileking.R;
import ch.mobileking.classes.override.ProductBaseAdapter;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class MainProductFragment extends Fragment{
	
	private ListView listView;
	
	private ProductBaseAdapter adapter;

	private Button btn_delete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.tab_fragment_main_product, container, false);
        return rootView;
    }
    
    
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
           
        listView = (ListView) getActivity().findViewById(R.id.tab_main_listView);
        
        adapter = new ProductBaseAdapter(getActivity(),R.layout.product_item, (ArrayList<Products>) ProductKing.getStaticProducts()); //R.layout.product_item
		
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
    }
    

}


