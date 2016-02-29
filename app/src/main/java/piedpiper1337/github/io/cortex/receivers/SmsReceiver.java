package piedpiper1337.github.io.cortex.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.activeandroid.query.Select;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        //have to load the current rawdatas to memory
        List<RawData> rawDataList = new Select().from(RawData.class).where("Finished = ?", false).execute();

        Map<Integer, RawData> rawDataMap = new HashMap<>();
        for (RawData r : rawDataList) {
            rawDataMap.put(r.hashCode(), r);
        }


        String str = "";

        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received add to, or update the hashmap
            for (int i = 0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                // Sender's phone number
                str += "SMS from " + msgs[i].getOriginatingAddress() + " : ";
                if (msgs[i].getOriginatingAddress().endsWith(Constants.CORTEX_NUMBER)) {

                    /**
                     * expecting this protocol format:
                     * QuestionType (QUES or WIKI):Primary Key (hex encoded long):currentnumber/totalnumber
                     */
                    String messageBody = msgs[i].getMessageBody().toString();
                    String[] messageBodyParsed = messageBody.split(":");

                    String questionType = messageBodyParsed[0];
                    long id = Long.parseLong(messageBodyParsed[1], 16);

                    String[] ratio = messageBodyParsed[2].split("/");
                    int currentMessageNumber = Integer.parseInt(ratio[0]);
                    int totalMessageNumber = Integer.parseInt(ratio[1]);

                    StringBuilder stringBuilder = new StringBuilder();
                    for (int j = 3; j < messageBodyParsed.length; j++) {
                        stringBuilder.append(messageBodyParsed[j]).append(' ');
                    }
                    String actualMessage = stringBuilder.toString();


                    RawData rawData = new RawData(id, questionType);
                    if (rawDataMap.containsKey(rawData.hashCode())) {
                        RawData actualRawData = rawDataMap.get(rawData.hashCode());
                        HashMap<Integer, String> messageMap = actualRawData.getCurrentlyReceivedMessagesMap();
                        messageMap.put(currentMessageNumber, actualMessage);
                    } else {
                        rawData.setNumMessagesExpected(totalMessageNumber);
                        HashMap<Integer, String> messages = new HashMap<>();
                        messages.put(currentMessageNumber, actualMessage);
                        rawData.setCurrentlyReceivedMessagesMap(messages);
                        rawData.setFinished(false);
                        rawDataMap.put(rawData.hashCode(), rawData);
                    }
                }

                // Fetch the text message
                str += msgs[i].getMessageBody().toString();
                // Newline <img src="http://codetheory.in/wp-includes/images/smilies/simple-smile.png" alt=":-)" class="wp-smiley" style="height: 1em; max-height: 1em;">
                str += "\n";
            }

            for (RawData rawDataToUpdate : rawDataMap.values()) {
                if (rawDataToUpdate.getNumMessagesExpected() ==
                        rawDataToUpdate.getCurrentlyReceivedMessagesMap().keySet().size()) {
                    rawDataToUpdate.setFinished(true);
                    StringBuilder finalMessage = new StringBuilder();
                    for (int i = 1; i <= rawDataToUpdate.getNumMessagesExpected(); i++) {
                        finalMessage.append(rawDataToUpdate.getCurrentlyReceivedMessagesMap().get(i))
                                .append(' ');
                    }
                    rawDataToUpdate.setAnswer(finalMessage.toString().trim());
                }
                rawDataToUpdate.save();
            }

            // Display the entire SMS Message
            Log.d(TAG, str);
        }
    }
}
