package piedpiper1337.github.io.cortex.adapters;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.FragmentPagerAdapter;


import piedpiper1337.github.io.cortex.fragments.PageFragment;

/**
 * Created by brianzhao on 1/19/16.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = {"Questions", "WikiLite", "RedditLite"};
    private Context mContext;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }


    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return PageFragment.newInstance(position+1);
    }

    @Override
    public int getCount(){
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
