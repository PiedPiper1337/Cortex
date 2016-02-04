package piedpiper1337.github.io.cortex.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;

/**
 * Base Activity class that all Activities should inherit from
 * <p/>
 * Created by cary on 1/2/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public abstract String getTag();

    protected ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logMethodName("OnCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logMethodName("onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logMethodName("OnRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logMethodName("OnResume()");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        logMethodName("OnPostResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logMethodName("OnPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logMethodName("OnStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logMethodName("OnDestroy()");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        logMethodName("onLowMemory()");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logMethodName("OnBackPressed()");
    }

    /**
     * Log which lifecycle event is being called and by whom
     *
     * @param methodName The name of the callback
     */
    private void logMethodName(String methodName) {
        Log.d(getTag(), ">>>>>>>> " + methodName + " in activity: " + getTag() + " <<<<<<<<<<");
    }

    /**
     * Show an error to the user
     *
     * @param message The message to show
     */
    public void showErrorDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(Html.fromHtml(message));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
