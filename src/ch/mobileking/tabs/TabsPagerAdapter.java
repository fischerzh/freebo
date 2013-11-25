package ch.mobileking.tabs;

import ch.mobileking.tabs.old.RecommendProductFragment;
import ch.mobileking.utils.ProductKing;
import ch.mobileking.utils.Utils;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.MotionEvent;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // MainProduct fragment activity
            return new MainProductFragment();
        case 1:
            // MainBadge fragment activity
            return new MainCameraScanFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
 
}