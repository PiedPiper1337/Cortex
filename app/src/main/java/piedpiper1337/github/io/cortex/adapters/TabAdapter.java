package piedpiper1337.github.io.cortex.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import piedpiper1337.github.io.cortex.fragments.PageFragment;

/**
 * Created by brianzhao on 1/19/16.
 */

//TODO Replace this entire class so that it works
public class TabAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = {"Questions", "WikiLite"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
