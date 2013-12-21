package ch.mobileking.tabs.intro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import ch.mobileking.R;

import com.viewpagerindicator.IconPagerAdapter;

class IntroFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    protected static final String[] CONTENT = new String[] { "1. Scanne eines deiner Lieblingsprodukte um teilzunehmen und Punkte zu sammeln!", "2. Gehe weiterhin shoppen wie gewohnt!", "3. Sende uns deinen Kassenzettel!", "4. Werde ProduktKing und erhalte Benefits!", };
    protected static final int[] ICONS = new int[] {
            R.drawable.intro_1_scan_prod,
            R.drawable.intro_2_shop,
            R.drawable.intro_3_sendslip,
            R.drawable.intro_4_prodking
    };

    private int mCount = CONTENT.length;

    public IntroFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return IntroFragment.newInstance(CONTENT[position % CONTENT.length], ICONS[position % ICONS.length]);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return IntroFragmentAdapter.CONTENT[position % CONTENT.length];
    }

    @Override
    public int getIconResId(int index) {
      return ICONS[index % ICONS.length];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}