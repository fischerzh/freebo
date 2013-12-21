package ch.mobileking.tabs.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import ch.mobileking.MainActivity;
import ch.mobileking.MainTabActivity;
import ch.mobileking.R;
import ch.mobileking.RegisterActivity;

import com.viewpagerindicator.CirclePageIndicator;

public class IntroSequenceActivity extends BaseSampleActivity {
	
	private boolean isButtonVisible = false;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_circles);

        mAdapter = new IntroFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        
        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setStrokeColor(getResources().getColor(R.color.blue_light));
        final float density = getResources().getDisplayMetrics().density;
        mIndicator.setRadius(10 * density);
//        mIndicator.setExtraSpacing(5.0f);
        mIndicator.setExtraSpacing(5 * density);
        mIndicator.setStrokeWidth(2 * density);

        mIndicator.setViewPager(mPager);
        
        intro_button_finish = (Button) findViewById(R.id.loyalty_card_next_btn);
		intro_button_finish.setVisibility(View.GONE);

        mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if(position ==3)
		        {

					intro_button_finish.setVisibility(View.VISIBLE);
					isButtonVisible = true;
					Animation slideUpIn = AnimationUtils.loadAnimation(IntroSequenceActivity.this, R.anim.bottom_up);
					intro_button_finish.startAnimation(slideUpIn);

		            intro_button_finish.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {

							Intent intent = new Intent(IntroSequenceActivity.this, MainTabActivity.class);
							startActivity(intent);
							finish();
						}
					});
		        }		
				else if (position == 2 && intro_button_finish.getVisibility()==View.VISIBLE)
				{
					Animation slideDownOut = AnimationUtils.loadAnimation(IntroSequenceActivity.this, R.anim.bottom_down);
					intro_button_finish.startAnimation(slideDownOut);
				}
				
			}
			
			@Override
			public void onPageScrolled(int position, float offset, int offsetPixels) {
				if(position != 3)
				{
					if(intro_button_finish.getAnimation()!=null)
					{
						if(intro_button_finish.getAnimation().hasEnded())
						{
							intro_button_finish.setVisibility(View.INVISIBLE);

						}
					}

				}
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
    }
}