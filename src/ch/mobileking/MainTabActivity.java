package ch.mobileking;

import ch.mobileking.tabs.TabsPagerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;


public class MainTabActivity extends ActionBarActivity implements ActionBar.TabListener{

	private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    
    private String[] tabs = { "Meine Produkte", "Recommendations"};
	private View topLevelLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity_main);
        
		setTitle("Product King");
 
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
 
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
        
		topLevelLayout = findViewById(R.id.product_menuoverlay_layout);
        

        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }
 
        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
    
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.action_bar, menu);
	    return true;
	  }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_show_help:
	        	System.out.println("Show Help!!");
//	        	baseActivityMenu.showFirstRunMenu();
	        	setHelpActive();
	            return true;
	        case R.id.action_logout:
//	        	baseActivityMenu.logOut();
	            return true;
	        case R.id.action_scan:
	        	startBarcodeScanner();
	            return true;
	        case R.id.action_edit:
//	        	setEditStyle();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
 
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	private void setHelpActive()
	{
		topLevelLayout.setVisibility(View.VISIBLE);
        topLevelLayout.setOnTouchListener(new View.OnTouchListener()
        {

			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				topLevelLayout.setVisibility(View.INVISIBLE);
				return false;
			}
	    });
	}
	
	private void startBarcodeScanner()
	{
		Intent intent = new Intent(MainTabActivity.this, BarCodeScanner.class);
		startActivityForResult(intent, 1);
	}

}
