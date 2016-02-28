package piedpiper1337.github.io.cortex.fragments;

import android.app.Activity;
import android.content.Context;

import piedpiper1337.github.io.cortex.activities.NavigationCallback;

/**
 * Created by brianzhao on 2/27/16.
 */
public class QuestionListFragment extends BaseFragment {
    private static final String TAG = QuestionListFragment.class.getSimpleName();
    private Context mContext;
    private NavigationCallback mNavigationCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        if (activity instanceof NavigationCallback) {
            mNavigationCallback = (NavigationCallback) activity;
        } else {
            throw new RuntimeException("activity doesn't implement navigation callback");
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


    @Override
    public String getTagName() {
        return TAG;
    }

}
