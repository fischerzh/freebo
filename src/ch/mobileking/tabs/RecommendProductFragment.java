package ch.mobileking.tabs;

import java.util.ArrayList;

import ch.mobileking.ProductDetailOverview;
import ch.mobileking.R;
import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.classes.override.ProductBaseAdapter;
import ch.mobileking.classes.override.RecommArrayAdapter;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Products;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class RecommendProductFragment extends Fragment{
	
	private GridView gridView;
	
	private ListView listview;
	
	private ImageAdapter imgAdapter;

	private ArrayAdapter adapter;
	
	private TextView prod_recomm_txt;
	
	private String category;
	
	private int categoryId;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.tab_fragment_recomm_product, container, false);
        
        return rootView;
    }
    
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
           
		listview = (ListView) getActivity().findViewById(R.id.prod_recomm_listView);
		
		gridView = (GridView) getActivity().findViewById(R.id.prod_recomm_grid_view);

		prod_recomm_txt = (TextView) getActivity().findViewById(R.id.prod_recomm);
		
		ArrayList<String> catList = new ArrayList<String>();
		catList.add("- MEINE EMPFEHLUNGEN -");
		catList.add("Getraenke");
		catList.add("Snacks");
		catList.add("Gemüse");
		catList.add("Pudding, Cremen");
		catList.add("Joghurtdrinks");
		catList.add("Desserts");
		catList.add("Süsswaren");
		
		adapter = new ArrayAdapter(getActivity(), R.layout.simple_selectable_item, catList); //android.R.layout.simple_selectable_list_item
		
//		adapter = new RecommArrayAdapter(getActivity(), R.layout.simple_selectable_item, catList);
		
		ArrayList<Object> prods = new ArrayList<Object>();
		prods.addAll(ProductKing.getRecommenderProducts().subList(0,  4));
		//(ArrayList<Crown>) product.getCrowns();

		imgAdapter = new ImageAdapter(getActivity(), prods, R.layout.recommend_item); //(ArrayList<Crown>)product.getCrowns()

		gridView.setAdapter(imgAdapter);
		
		listview.setSelector(R.layout.list_selector);

		listview.setAdapter(adapter);
		
//		listview.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item, catList) {
//		    @Override
//		    public View getView(int position, View convertView, ViewGroup parent) {
//		        TextView textView = (TextView) super.getView(position, convertView, parent);
//		        String selected = textView.getText().toString();
//		        int textColor = textView.getText().toString().equals(selected) ? R.color.blue_dark : R.color.grey_dark;
//		        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/robotolight.ttf");
//		        textView.setTypeface(tf);
//		        
//		        return textView;
//		    }
//		});	
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
				// TODO Auto-generated method stub
				
				System.out.println("On category clicked: " + position +  ((TextView)view).getText().toString());
				
				clearListView();
				
//				((TextView)view).setTextColor(getResources().getColor(android.R.color.holo_purple));
				
				((TextView)view).setTypeface(null, Typeface.BOLD);
				
//				((TextView)view).setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
				
				
				ArrayList<Object> prods = new ArrayList<Object>();
				if(position==0)
				{
					prod_recomm_txt.setText("Persönliche Empfehlungen");
					prods.addAll(ProductKing.getRecommenderProducts().subList(0, 4));
					
				}
				else
				{
					prod_recomm_txt.setText(((TextView)view).getText().toString()+" Top-Produkte");
					prods.addAll(updateGrid(((TextView)view).getText().toString()));
				
				}
				ImageAdapter newImgAdapter = new ImageAdapter(getActivity(), prods, R.layout.recommend_item); //(ArrayList<Crown>)product.getCrowns()
				
				gridView.setAdapter(newImgAdapter);
				
				listview.setSelected(true);
			}
		});
		

//		setGrid();
    }
    
    private void clearListView()
    {
    	
    }
    
	protected ArrayList<Object> updateGrid(String string) {
		
		// Instance of ImageAdapter Class
		ArrayList<Object> prods = new ArrayList<Object>();
		ArrayList<Object> returnProds = new ArrayList<Object>();
		for (Products prod : ProductKing.getRecommenderProducts())
		{
			System.out.println("Looking for Procuts in Category: " + string);
			if(prod.getCategory().equalsIgnoreCase(string))
			{
				prods.add(prod);
				System.out.println("Found product in category " + prod.getName());
			}
		}
		returnProds.addAll(prods.subList(0,  Math.min(4, prods.size())));
		return returnProds;
		
	}
	
	public void setActiveCategory(String category)
	{
		
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the categoryId
	 */
	public int getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

//	private void setGrid()
//	{
//		// Instance of ImageAdapter Class
//		ArrayList<Object> prods = new ArrayList<Object>();
//		prods.addAll(ProductKing.getRecommenderProducts());
//		//(ArrayList<Crown>) product.getCrowns();
//
//		imgAdapter = new ImageAdapter(getActivity(), prods, R.layout.recommend_item); //(ArrayList<Crown>)product.getCrowns()
//
//		gridView.setAdapter(adapter);
//	}
	
}


	 
