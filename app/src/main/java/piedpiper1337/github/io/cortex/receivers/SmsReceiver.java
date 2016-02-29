package piedpiper1337.github.io.cortex.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.HashSet;

import piedpiper1337.github.io.cortex.models.RawData;
import piedpiper1337.github.io.cortex.utils.Constants;

/**
 * Created by brianzhao on 2/28/16.
 */
//http://codetheory.in/android-sms/
//https://stackoverflow.com/questions/4117701/android-sms-broadcast-receiver
//https://android.stackexchange.com/questions/72855/can-the-receive-sms-permission-be-used-to-intercept-sms-messages
public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = SmsReceiver.class.getName();


    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs = null;

        //have to store the messages in memory
        //have to load the current rawdatas to memory

        String str = "";

        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received
            for (int i = 0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                // Sender's phone number
                str += "SMS from " + msgs[i].getOriginatingAddress() + " : ";
                if (msgs[i].getOriginatingAddress().endsWith(Constants.CORTEX_NUMBER)) {
                    String messageBody = msgs[i].getMessageBody().toString();

                    //expecting this protocol format:
                    /**
                     * QuestionType (QUES or WIKI):Primary Key (hex encoded long):currentnumber/totalnumber
                     */


                }

                // Fetch the text message
                str += msgs[i].getMessageBody().toString();
                // Newline <img src="http://codetheory.in/wp-includes/images/smilies/simple-smile.png" alt=":-)" class="wp-smiley" style="height: 1em; max-height: 1em;">
                str += "\n";
            }

            // Display the entire SMS Message
            Log.d(TAG, str);
        }
    }
}
