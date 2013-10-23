package ch.mobileking.tabs;

import java.util.ArrayList;

import ch.mobileking.BadgeActivity;
import ch.mobileking.MainActivity;
import ch.mobileking.ProductDetailOverview;
import ch.mobileking.R;
import ch.mobileking.RecommActivity;
import ch.mobileking.StoreHeroActivity;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MainBadgeFragment extends Fragment{
	
	
    private LinearLayout tab_frag_badges_ll, tab_frag_storehero_ll;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.tab_fragment_badges, container, false);
        
        return rootView;
    }
    
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  

        tab_frag_badges_ll = (LinearLayout) getActivity().findViewById(R.id.tab_frag_badges_ll);
        tab_frag_badges_ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), BadgeActivity.class);
	        	startActivity(intent);
			}
		});
        
        tab_frag_storehero_ll = (LinearLayout) getActivity().findViewById(R.id.tab_frag_storehero_ll);
        tab_frag_storehero_ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), StoreHeroActivity.class);
	        	startActivity(intent);				
			}
		});
    }
    
}


	 
