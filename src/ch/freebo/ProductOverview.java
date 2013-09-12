package ch.freebo;

import java.util.ArrayList;

import ch.freebo.classes.override.ProductBaseAdapter;
import ch.freebo.utils.ProductKing;
import ch.freebo.utils.Products;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class ProductOverview extends Activity {
	
	private Activity act;
	
	private ProductBaseAdapter adapter;
	
	private ListView listView;
	private EditText editTxt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setAct(this);
		super.onCreate(savedInstanceState);
		
		System.out.println("ProductOverview called!");
		
		setElements();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	public void setElements()
	{
		
		setContentView(R.layout.product_overview_layout);
		
        //setContentView(R.layout.products_main);

		
        listView = (ListView) findViewById(R.id.product_list_view);
        editTxt = (EditText) findViewById(R.id.product_search_box);
        
		setTitle("Mobile Product King - Home");
		
		adapter = new ProductBaseAdapter(this, (ArrayList<Products>) ProductKing.getStaticProducts());
		
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
//				editTxt.setText("");
				Products currentItem;
				
				System.out.println("Position clicked: " + position + " " + listView.getItemAtPosition(position));
				
			}
			
        });
		
	}

	/**
	 * @return the act
	 */
	public Activity getAct() {
		return act;
	}

	/**
	 * @param act the act to set
	 */
	public void setAct(Activity act) {
		this.act = act;
	}

}
