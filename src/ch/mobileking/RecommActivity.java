package ch.mobileking;

import java.util.ArrayList;

import ch.mobileking.classes.override.ImageAdapter;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

public class RecommActivity extends Activity {
	
	private Button nextBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tab_fragment_recomm_product);

		final ListView listview = (ListView) findViewById(R.id.prod_recomm_listView);
		
		ArrayList<String> catList = new ArrayList<String>();
		catList.add("- MEINE EMPFEHLUNGEN -");
		catList.add("Getränke");
		catList.add("Snacks");
		catList.add("Gemüse");
		catList.add("Salate");
		catList.add("Fleisch");
		catList.add("Cremen");
		catList.add("Desserts");
		catList.add("Spirituosen");
		
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, catList);
		
		listview.setAdapter(adapter);		
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
				// TODO Auto-generated method stub
				listview.setItemChecked(position, true);
			}
		});
		
		
		setGrid();
		
		setElements();
		
	}
	
	
	private void setGrid()
	{
		GridView gridView = (GridView) findViewById(R.id.prod_recomm_grid_view);

		// Instance of ImageAdapter Class
		ArrayList<Object> prods = new ArrayList<Object>();
		prods.addAll(ProductKing.getRecommenderProducts());
		//(ArrayList<Crown>) product.getCrowns();
		
		ImageAdapter adapter = new ImageAdapter(getApplicationContext(), prods, R.layout.recommend_item); //(ArrayList<Crown>)product.getCrowns()

		gridView.setAdapter(adapter);
	}
	
	private void setElements()
	{
		
//		nextBtn = (Button) findViewById(R.id.prod_recomm_btnNext);
//		nextBtn.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(RecommActivity.this, ProductOverview.class);
//				startActivity(intent);
//			}
//		});
		

	}

}
