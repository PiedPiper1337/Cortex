package piedpiper1337.github.io.cortex.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import piedpiper1337.github.io.cortex.R;
import piedpiper1337.github.io.cortex.activities.HomeActivity;
import piedpiper1337.github.io.cortex.activities.NavigationCallback;
import piedpiper1337.github.io.cortex.adapters.TabAdapter;

/**
 * Created by brianzhao on 2/3/16.
 */
public class HomeFragment extends BaseFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FloatingActionButton mNewQuestionButton;
    private Context mContext;
    private Toolbar mToolbar;
    private NavigationCallback mNavigationCallback;

    @Override
    public String getTagName() {
        return TAG;
    }

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        ((HomeActivity) mContext).setSupportActionBar(mToolbar);

        mViewPager.setAdapter(new TabAdapter(getFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
        mNewQuestionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        mNewQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                mNavigationCallback.askQuestion();
            }
        });
        return view;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
