package piedpiper1337.github.io.cortex.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import piedpiper1337.github.io.cortex.R;
import piedpiper1337.github.io.cortex.activities.HomeActivity;
import piedpiper1337.github.io.cortex.activities.NavigationCallback;

/**
 * Created by brianzhao on 2/11/16.
 */
public class QuestionFragment extends BaseFragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    private Context mContext;
    private NavigationCallback mNavigationCallback;
    private EditText mEditText;
    private FloatingActionButton mSendButton;
    private Toolbar mToolbar;

    public static QuestionFragment newInstance(){
        return new QuestionFragment();
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
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        mEditText = (EditText) view.findViewById(R.id.question_edit_text);
        mSendButton = (FloatingActionButton) view.findViewById(R.id.send_button);
        mToolbar = (Toolbar) view.findViewById(R.id.question_toolbar);
        ((HomeActivity)mContext).setSupportActionBar(mToolbar);

        return view;
    }


    @Override
    public String getTagName() {
        return TAG;
    }
}
