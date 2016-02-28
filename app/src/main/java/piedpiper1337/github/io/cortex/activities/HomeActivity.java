package piedpiper1337.github.io.cortex.activities;

import android.app.FragmentManager;
import android.os.Bundle;

import java.util.List;

import piedpiper1337.github.io.cortex.R;
import piedpiper1337.github.io.cortex.fragments.HomeFragment;
import piedpiper1337.github.io.cortex.fragments.QuestionPagerFragment;
import piedpiper1337.github.io.cortex.fragments.SmsQuestionFragment;
import piedpiper1337.github.io.cortex.models.Question;

public class HomeActivity extends BaseActivity implements NavigationCallback {
    private static final String TAG = HomeActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initUI();

    }

    public void initUI() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.findFragmentById(R.id.fragment_container) == null) {
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, homeFragment, "homeFragment")
                    .commit();
        }
    }

    @Override
    public void askQuestion() {
        SmsQuestionFragment smsQuestionFragment = SmsQuestionFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left,
//                        R.animator.slide_in_left, R.animator.slide_out_right)
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out,
                        R.animator.fade_in, R.animator.fade_out)
                .replace(R.id.fragment_container, smsQuestionFragment, "smsQuestionFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void previewQuestions(List<Question> questions, int position) {
        QuestionPagerFragment questionPagerFragment =
                QuestionPagerFragment.newInstance(questions,position);

        getFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left,
//                        R.animator.slide_in_left, R.animator.slide_out_right)
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out,
                        R.animator.fade_in, R.animator.fade_out)
                .replace(R.id.fragment_container, questionPagerFragment , "questionPagerFragment")
                .addToBackStack(null)
                .commit();
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_base, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            if (getSelectedFragment() != null && !getSelectedFragment().onBackPressed()) {
                // Selected fragment did not consume the back press event.
                fm.popBackStack();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public String getTag() {
        return TAG;
    }

}
