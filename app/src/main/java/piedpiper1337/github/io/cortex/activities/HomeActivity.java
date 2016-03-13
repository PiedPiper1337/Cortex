package piedpiper1337.github.io.cortex.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import piedpiper1337.github.io.cortex.R;
import piedpiper1337.github.io.cortex.fragments.QuestionListFragment;
import piedpiper1337.github.io.cortex.fragments.QuestionPagerFragment;
import piedpiper1337.github.io.cortex.fragments.SmsQuestionFragment;
import piedpiper1337.github.io.cortex.models.SMSQueryable;
import piedpiper1337.github.io.cortex.utils.Constants;
import piedpiper1337.github.io.cortex.utils.SharedPreferenceUtil;

public class HomeActivity extends BaseActivity implements NavigationCallback {
    private static final String TAG = HomeActivity.class.getCanonicalName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initUI();

//        https://stackoverflow.com/questions/13964409/why-fragmentmanagers-getbackstackentrycount-return-zero
//        https://stackoverflow.com/questions/13086840/actionbar-up-navigation-with-fragments
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int stackHeight = getFragmentManager().getBackStackEntryCount();
                Log.d("WTF", stackHeight + "");
                if (stackHeight > 0) { // if we have something on the stack (doesn't include the current shown fragment)
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {

                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                }
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
//            case R.id.carrier:
//                showCarrierDialog(new DialogCallback() {
//                    @Override
//                    public void onCarrierDialogComplete(String carrier) {
//                        Toast.makeText(HomeActivity.this, "Texts from Cortex will now be directed to " + carrier, Toast.LENGTH_SHORT).show();
//                    }
//                });
//                return true;
//            case R.id.donate:
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/your_paypal"));
//                startActivity(browserIntent);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showCarrierDialog(final DialogCallback dialogCallback) {
        String userSavedCarrier = SharedPreferenceUtil.readPreference(this,
                Constants.SharedPreferenceKeys.CARRIER_NAME, null);

        final StringBuilder toSave = new StringBuilder();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Please choose your carrier so Cortex can reply to you.");
        builder.setSingleChoiceItems(Constants.carrierOptionsArray, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                toSave.setLength(0);
                                toSave.append(Constants.carrierOptionsArray[0]);
                                break;
                            case 1:
                                toSave.setLength(0);
                                toSave.append(Constants.carrierOptionsArray[1]);
                                break;
                            case 2:
                                toSave.setLength(0);
                                toSave.append(Constants.carrierOptionsArray[2]);
                                break;
                            case 3:
                                toSave.setLength(0);
                                toSave.append(Constants.carrierOptionsArray[3]);
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String carrier = toSave.toString();
                if (carrier.length() > 0) {
                    dialog.dismiss();
                    SharedPreferenceUtil.savePreference(
                            HomeActivity.this,
                            Constants.SharedPreferenceKeys.CARRIER_NAME,
                            carrier);
                    if (dialogCallback != null) {
                        dialogCallback.onCarrierDialogComplete(carrier);
                    }
                }
            }
        });
        AlertDialog carrierDialog = builder.create();
        carrierDialog.show();
    }

    public void initUI() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.findFragmentById(R.id.fragment_container) == null) {
            QuestionListFragment questionListFragment = QuestionListFragment.newInstance();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, questionListFragment, "questionListFragment")
                    .commit();
        }
    }

    @Override
    public void askQuestion(String questionType) {
        SmsQuestionFragment smsQuestionFragment = SmsQuestionFragment.newInstance(questionType);
        getFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left,
//                        R.animator.slide_in_left, R.animator.slide_out_right)
                .setCustomAnimations(R.animator.fade_in_fast, R.animator.fade_out_fast)
//                        R.animator.fade_in, R.animator.fade_out)
                .replace(R.id.fragment_container, smsQuestionFragment, "smsQuestionFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void previewQuestions(List<SMSQueryable> questions, int position) {
        QuestionPagerFragment questionPagerFragment =
                QuestionPagerFragment.newInstance(questions, position);

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.fade_in_fast, R.animator.fade_out_fast)
//                        R.animator.fade_in_fast, R.animator.fade_out_fast)
                .replace(R.id.fragment_container, questionPagerFragment, "questionPagerFragment")
                .addToBackStack(null)

                .commit();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu, menu);
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

    public interface DialogCallback {
        void onCarrierDialogComplete(String carrier);
    }
}
