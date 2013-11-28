package ch.mobileking.tabs.intro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import ch.mobileking.R;
import ch.mobileking.tabs.MainProductFragment;

public class IntroTabsPagerAdapter extends FragmentPagerAdapter {
 
    public IntroTabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // MainProduct fragment activity
        	Fragment intro1 = new IntroOneFragment();
        	((IntroOneFragment) intro1).setLayoutId(R.layout.tab_fragment_camera_scan);
            return ((IntroOneFragment) intro1);
        case 1:
            // MainBadge fragment activity
        	Fragment intro2 = new IntroOneFragment();
        	((IntroOneFragment) intro2).setLayoutId(R.layout.tab_fragment_recomm_product);
            return ((IntroOneFragment) intro2);
        case 2:
        	return null;
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
}