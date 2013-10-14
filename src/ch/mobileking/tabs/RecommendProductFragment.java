package ch.mobileking.tabs;

import java.util.ArrayList;

import ch.mobileking.ProductDetailOverview;
import ch.mobileking.R;
import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.classes.override.ProductBaseAdapter;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RecommendProductFragment extends Fragment{
	
	private ListView listView;
	
	private ImageAdapter adapter;

	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.tab_fragment_recomm_product, container, false);
        
        return rootView;
    }
    
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
           
		final ListView listview = (ListView) getActivity().findViewById(R.id.fragment_listView);
		
		ArrayList<String> catList = new ArrayList<String>();
		catList.add("Getränke");
		catList.add("Snacks");
		catList.add("Gemüse");
		catList.add("Salate");
		catList.add("Fleisch");
		catList.add("Cremen");
		catList.add("Desserts");
		catList.add("Spirituosen");
		
		ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.select_dialog_multichoice, catList);
		
		listview.setAdapter(adapter);		
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
				// TODO Auto-generated method stub
				listview.setItemChecked(position, true);
			}
		});
		
		
		setGrid();
    }
    
	private void setGrid()
	{
		GridView gridView = (GridView) getActivity().findViewById(R.id.fragment_grid_view);

		// Instance of ImageAdapter Class
		ArrayList<Object> prods = new ArrayList<Object>();
		prods.addAll(ProductKing.getRecommenderProducts());
		//(ArrayList<Crown>) product.getCrowns();

		ImageAdapter adapter = new ImageAdapter(getActivity(), prods, R.layout.recommend_item); //(ArrayList<Crown>)product.getCrowns()

		gridView.setAdapter(adapter);
	}
	
}


	 
