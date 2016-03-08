package piedpiper1337.github.io.cortex.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import piedpiper1337.github.io.cortex.models.RawData;
import piedpiper1337.github.io.cortex.services.SmsParserService;
import piedpiper1337.github.io.cortex.utils.Constants;

/**
 * Created by brianzhao on 2/28/16.
 */
//http://codetheory.in/android-sms/
//https://stackoverflow.com/questions/4117701/android-sms-broadcast-receiver
//https://android.stackexchange.com/questions/72855/can-the-receive-sms-permission-be-used-to-intercept-sms-messages
public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = SmsReceiver.class.getName();

    /**
     * checks if the sms was from cortex, if it does it gets the message body,
     * puts it into list of string
     * forwards to background intenservice
     *
     * @param context
     * @param intent
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;



        ArrayList<String> cortexMessages = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received add to, or update the hashmap
            for (int i = 0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                // Sender's phone number
                stringBuilder.append("SMS from " + msgs[i].getOriginatingAddress() + " : ");
                if (msgs[i].getOriginatingAddress().endsWith(Constants.CORTEX_NUMBER)) {
                    AudioManager aManager=(AudioManager)(context.getSystemService(Context.AUDIO_SERVICE));
                    aManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    /**
                     * expecting this protocol format:
                     * QuestionType (QUES or WIKI):Primary Key (hex encoded long):currentnumber/totalnumber
                     */
                    String messageBody = msgs[i].getMessageBody();
                    cortexMessages.add(messageBody);
                    stringBuilder.append(messageBody).append('\n');

                }
            }
            Intent smsBackgrounParserIntent = new Intent(context, SmsParserService.class);
            smsBackgrounParserIntent.putStringArrayListExtra(
                    Constants.IntentKeys.CORTEX_MESSAGES_RECEIVED, cortexMessages);
            context.startService(smsBackgrounParserIntent);
            // Display the entire SMS Message
            Log.d(TAG, stringBuilder.toString());
        }
    }
}
