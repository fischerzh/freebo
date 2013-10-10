package ch.mobileking;

import java.util.ArrayList;

import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.utils.Crown;
import ch.mobileking.utils.Products;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

public class RecommActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.product_recommendation);

		final ListView listview = (ListView) findViewById(R.id.prod_recomm_listView);
		
		ArrayList<String> catList = new ArrayList<String>();
		catList.add("Getränke");
		catList.add("Snacks");
		catList.add("Gemüse");
		catList.add("Salate");
		catList.add("Fleisch");
		catList.add("Cremen");
		catList.add("Desserts");
		catList.add("Spirituosen");

		
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, catList);
		
		listview.setAdapter(adapter);		
		
		setGrid();
		
		
	}
	
	
	private void setGrid()
	{
		GridView gridView = (GridView) findViewById(R.id.prod_recomm_grid_view);

		// Instance of ImageAdapter Class
		ArrayList<Object> prods = new ArrayList<Object>();

		Products pr1 = new Products();
		pr1.setName("Zweifel Chips");

		Products pr2 = new Products();
		pr2.setName("Nestea Peach");
		
		Products pr3 = new Products();
		pr3.setName("Rivella Blau");
		
		Products pr4 = new Products();
		pr4.setName("Studentenfutter");
		
		prods.add(pr1);
		prods.add(pr2);
		prods.add(pr3);
		prods.add(pr4);

		//(ArrayList<Crown>) product.getCrowns();

		ImageAdapter adapter = new ImageAdapter(getApplicationContext(), prods, R.layout.recommend_item); //(ArrayList<Crown>)product.getCrowns()

		gridView.setAdapter(adapter);
	}

}
