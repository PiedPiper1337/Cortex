package piedpiper1337.github.io.cortex.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import piedpiper1337.github.io.cortex.R;
import piedpiper1337.github.io.cortex.activities.HomeActivity;
import piedpiper1337.github.io.cortex.activities.NavigationCallback;
import piedpiper1337.github.io.cortex.models.SMSQuery;
import piedpiper1337.github.io.cortex.models.SMSQueryable;
import piedpiper1337.github.io.cortex.utils.Constants;

/**
 * Created by brianzhao on 2/28/16.
 */

/**
 * previews all of your questions in a swipeable viewpager
 */
public class QuestionPagerFragment extends BaseFragment {
    private static final String TAG = QuestionPagerFragment.class.getSimpleName();
    private Context mContext;
    private NavigationCallback mNavigationCallback;


    private List<SMSQueryable> mQuestions;
    private int mPosition;

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private BroadcastReceiver mReceiver;

    private FragmentStatePagerAdapter mFragmentStatePagerAdapter;


    private static final String QUESTION_LIST = "io.github.piedpiper1337.cortex.QUESTON_LIST";
    private static final String POSITION = "io.github.piedpiper1337.cortex.POSITION";


    public static QuestionPagerFragment newInstance(List<SMSQueryable> questions, int position) {
        Bundle args = new Bundle();
        args.putSerializable(QUESTION_LIST, (Serializable) questions);
        args.putSerializable(POSITION, position);
        QuestionPagerFragment fragment = new QuestionPagerFragment();
        fragment.setArguments(args);
        return fragment;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuestions = (List<SMSQueryable>) getArguments().getSerializable(QUESTION_LIST);
        mPosition = (int) getArguments().getSerializable(POSITION);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<SMSQuery> smsQueries = new Select().from(SMSQuery.class).execute();
                Map<Long, SMSQuery> idToSms = new HashMap<>();
                for (SMSQuery smsQuery : smsQueries) {
                    idToSms.put(smsQuery.getId(), smsQuery);
                }
                for (int i = 0; i < mQuestions.size(); i++) {
                    SMSQueryable smsQueryable = mQuestions.get(i);
                    mQuestions.set(i, idToSms.get(smsQueryable.getId()));
                }
                if (mFragmentStatePagerAdapter != null) {
                    mFragmentStatePagerAdapter.notifyDataSetChanged();
                    mViewPager.invalidate();
                }

            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_pager, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager) view.findViewById(R.id.fragment_question_pager_view_pager);


        mFragmentStatePagerAdapter = new android.support.v13.app.FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return QuestionFragment.newInstance(mQuestions.get(position));
            }

            @Override
            public int getCount() {
                return mQuestions.size();
            }

//            https://stackoverflow.com/questions/10849552/update-viewpager-dynamically
//            https://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view
            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };
        mViewPager.setAdapter(mFragmentStatePagerAdapter);
        mViewPager.setCurrentItem(mPosition);

        mToolbar = (Toolbar) view.findViewById(R.id.fragment_question_pager_toolbar);
        ((HomeActivity) mContext).setSupportActionBar(mToolbar);
        ((HomeActivity) mContext).getSupportActionBar().setTitle(R.string.your_questions);

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver,
                new IntentFilter(Constants.IntentKeys.CORTEX_MESSAGES_DB_UPDATED));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    @Override
    public String getTagName() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

}
