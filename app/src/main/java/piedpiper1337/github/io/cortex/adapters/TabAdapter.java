package piedpiper1337.github.io.cortex.adapters;

import android.support.v4.app.FragmentManager;

import piedpiper1337.github.io.cortex.fragments.PageFragment;

/**
 * Created by brianzhao on 1/19/16.
 */
public class TabAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private String tabTitles[] = {"Questions", "WikiLite", "RedditLite"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
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
