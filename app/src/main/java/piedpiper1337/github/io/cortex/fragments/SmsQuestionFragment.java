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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import piedpiper1337.github.io.cortex.R;
import piedpiper1337.github.io.cortex.activities.HomeActivity;
import piedpiper1337.github.io.cortex.activities.NavigationCallback;
import piedpiper1337.github.io.cortex.models.SMSQuery;
import piedpiper1337.github.io.cortex.utils.Constants;
import piedpiper1337.github.io.cortex.utils.CustomEditText;
import piedpiper1337.github.io.cortex.utils.SmsHandler;

/**
 * Created by brianzhao on 2/11/16.
 */

/**
 * where you ask your question via sms
 */
public class SmsQuestionFragment extends BaseFragment {
    private static final String TAG = SmsQuestionFragment.class.getSimpleName();
    private Context mContext;
    private NavigationCallback mNavigationCallback;
    private SmsHandler mSmsHandler;
    private CustomEditText mEditText;
    private FloatingActionButton mSendButton;
    private LinearLayout mTopLinearLayout;
    private LinearLayout mBottomLinearLayout;
    private Toolbar mToolbar;
    private static final int REQUEST_SMS_PERMISSION = 1;
    private static final int REQUEST_SMS_RECEIVE_PERMISSION = 2;
    private static final String FRAGMENT_DIALOG = "dialog";

    private static final String QUESTION_TYPE = "io.github.piedpiper1337.cortex.QUESTION_TYPE";
    private String mQuestionType;


    public static SmsQuestionFragment newInstance(String questionType) {
        Bundle args = new Bundle();
        args.putString(QUESTION_TYPE, questionType);
        SmsQuestionFragment smsQuestionFragment = new SmsQuestionFragment();
        smsQuestionFragment.setArguments(args);
        return smsQuestionFragment;
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
        mSmsHandler = new SmsHandler(mContext);
        mQuestionType = getArguments().getString(QUESTION_TYPE);

    }

    @Override
    public void onResume() {
        super.onResume();
        mEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) ((HomeActivity) mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_IMPLICIT);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestSMSPermission();
            return;
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestSMSReceivePermission();
            return;
        }

    }

    @Override
    public boolean onBackPressed() {
        mEditText.clearFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        return false;
    }

    private void requestSMSPermission() {
        if (FragmentCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            new ConfirmationDialog().show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            FragmentCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    REQUEST_SMS_PERMISSION);
        }
    }

    private void requestSMSReceivePermission() {
        if (FragmentCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
            new ConfirmationDialogReceive().show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            FragmentCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
                    REQUEST_SMS_RECEIVE_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ((HomeActivity) mContext).showErrorDialog(getString(R.string.request_permission));
            }
        } else if (requestCode == REQUEST_SMS_RECEIVE_PERMISSION) {
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
        return inflater.inflate(R.layout.fragment_sms_question, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mEditText = (CustomEditText) view.findViewById(R.id.question_edit_text);
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            sendSms(mEditText.getText().toString());
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
                sendSms(mEditText.getText().toString());
            }
        });

        mToolbar = (Toolbar) view.findViewById(R.id.question_toolbar);
        ((HomeActivity) mContext).setSupportActionBar(mToolbar);


        if (mQuestionType.equals(Constants.SMS_TYPE.QUESTION_TYPE)) {
            mEditText.setHint(R.string.question_hint);
            ((HomeActivity) mContext).getSupportActionBar().setTitle(R.string.question_fragment_title);
        } else if (mQuestionType.equals(Constants.SMS_TYPE.WIKI_TYPE)) {
            mEditText.setHint(R.string.wiki_hint);
            ((HomeActivity) mContext).getSupportActionBar().setTitle(R.string.wiki_lookup_fragment_title);
        }
    }


    @Override
    public String getTagName() {
        return TAG;
    }

    /**
     * old way of sending message by using hangouts
     * not used
     *
     * @param toSend
     */
    public void sendSmsIntent(String toSend) {
        String phoneNumber = Constants.CORTEX_NUMBER;
        String smsBody = toSend;
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsBody);
        startActivity(it);
    }

    public void sendSms(String toSend) {
        if (mSmsHandler.canBeSent(toSend)) {
            SMSQuery SMSQuery = new SMSQuery(toSend, Constants.SMS_TYPE.QUESTION_TYPE);
            long id = SMSQuery.save();
            mSmsHandler.sendSmsQuestion(toSend, id, mQuestionType);
            ((HomeActivity) mContext).onBackPressed();
        }
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
                                    REQUEST_SMS_PERMISSION);
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


    /**
     * Shows OK/Cancel confirmation dialog about camera permission.
     */
    public static class ConfirmationDialogReceive extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Fragment parent = getParentFragment();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.request_permission)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentCompat.requestPermissions(parent,
                                    new String[]{Manifest.permission.RECEIVE_SMS},
                                    REQUEST_SMS_RECEIVE_PERMISSION);
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
