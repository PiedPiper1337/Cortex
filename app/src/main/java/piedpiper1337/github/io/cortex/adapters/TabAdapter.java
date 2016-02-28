package piedpiper1337.github.io.cortex.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import piedpiper1337.github.io.cortex.fragments.PageFragment;
import piedpiper1337.github.io.cortex.fragments.QuestionListFragment;
import piedpiper1337.github.io.cortex.utils.Constants;

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
        if (position == 0) {
            return QuestionListFragment.newInstance(Constants.SMS_TYPE.QUESTION_TYPE);
        } else {
            return QuestionListFragment.newInstance(Constants.SMS_TYPE.WIKI_TYPE);
//            return PageFragment.newInstance(2);
        }
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
