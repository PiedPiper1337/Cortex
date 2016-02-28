package piedpiper1337.github.io.cortex.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import piedpiper1337.github.io.cortex.R;
import piedpiper1337.github.io.cortex.activities.NavigationCallback;
import piedpiper1337.github.io.cortex.models.Question;

/**
 * Created by brianzhao on 2/28/16.
 */
public class QuestionPagerFragment extends BaseFragment {
    private static final String TAG = QuestionPagerFragment.class.getSimpleName();
    private Context mContext;
    private NavigationCallback mNavigationCallback;

    private List<Question> mQuestions;
    private int mPosition;


    private ViewPager mViewPager;


    private static final String QUESTION_LIST = "io.github.piedpiper1337.cortex.QUESTON_LIST";
    private static final String POSITION = "io.github.piedpiper1337.cortex.POSITION";


    public static QuestionPagerFragment newInstance(List<Question> questions, int position) {
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
        mQuestions = (List<Question>) getArguments().getSerializable(QUESTION_LIST);
        mPosition = (int) getArguments().getSerializable(POSITION);
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
        mViewPager.setAdapter(new android.support.v13.app.FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return QuestionFragment.newInstance(mQuestions.get(position));
            }

            @Override
            public int getCount() {
                return mQuestions.size();
            }
        });
        mViewPager.setCurrentItem(mPosition);

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
