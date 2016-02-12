package piedpiper1337.github.io.cortex.activities;

import android.app.FragmentManager;
import android.os.Bundle;

import piedpiper1337.github.io.cortex.R;
import piedpiper1337.github.io.cortex.fragments.HomeFragment;
import piedpiper1337.github.io.cortex.fragments.QuestionFragment;

public class HomeActivity extends BaseActivity implements NavigationCallback {
    private static final String TAG = HomeActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initUI();
    }

    public void initUI() {
        HomeFragment homeFragment = new HomeFragment();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, homeFragment, "homeFragment")
                .commit();
    }

    @Override
    public void askQuestion() {
        QuestionFragment questionFragment = QuestionFragment.newInstance();
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left,
                        R.animator.slide_in_left, R.animator.slide_out_right)
                .replace(R.id.fragment_container, questionFragment, "questionFragment")
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
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public String getTag() {
        return TAG;
    }


}