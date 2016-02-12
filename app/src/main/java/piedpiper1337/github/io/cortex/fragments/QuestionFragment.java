package piedpiper1337.github.io.cortex.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.app.FragmentCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import piedpiper1337.github.io.cortex.Constants;
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

    public static QuestionFragment newInstance() {
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

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestSMSPermission();
            return;
        }
        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) ((HomeActivity) mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void requestSMSPermission() {
        if (FragmentCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new ConfirmationDialog().show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            FragmentCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ((HomeActivity) mContext).showErrorDialog(getString(R.string.request_permission));
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        mEditText = (EditText) view.findViewById(R.id.question_edit_text);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendSmsIntent(mEditText.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            sendSmsIntent(mEditText.getText().toString());
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        mSendButton = (FloatingActionButton) view.findViewById(R.id.send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSmsIntent(mEditText.getText().toString());
            }
        });

        mToolbar = (Toolbar) view.findViewById(R.id.question_toolbar);
        ((HomeActivity) mContext).setSupportActionBar(mToolbar);
        ((HomeActivity) mContext).getSupportActionBar().setTitle(R.string.question_fragment_title);
        return view;
    }


    @Override
    public String getTagName() {
        return TAG;
    }

//    public void sendSmsIntent(String toSend) {
//        String phoneNumber = Constants.CORTEX_NUMBER;
//        String smsBody = toSend;
//        Uri uri = Uri.parse("smsto:" + phoneNumber);
//        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
//        it.putExtra("sms_body", smsBody);
//        startActivity(it);
//    }

    public void sendSmsIntent(String toSend){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(Constants.CORTEX_NUMBER, null, toSend, null, null);
        ((HomeActivity)mContext).onBackPressed();
    }

    /**
     * Shows OK/Cancel confirmation dialog about camera permission.
     */
    public static class ConfirmationDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Fragment parent = getParentFragment();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.request_permission)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentCompat.requestPermissions(parent,
                                    new String[]{Manifest.permission.SEND_SMS},
                                    REQUEST_CAMERA_PERMISSION);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Activity activity = parent.getActivity();
                                    if (activity != null) {
                                        activity.finish();
                                    }
                                }
                            })
                    .create();
        }
    }
}
