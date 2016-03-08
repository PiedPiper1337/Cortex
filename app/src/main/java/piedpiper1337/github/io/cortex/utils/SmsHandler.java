package piedpiper1337.github.io.cortex.utils;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

import piedpiper1337.github.io.cortex.activities.HomeActivity;

/**
 * Created by brianzhao on 2/28/16.
 */
public class SmsHandler {
    private Context mContext;

    public SmsHandler(Context context) {
        mContext = context;
    }

    /**
     * removes all special characters
     *
     * @return
     */
    public String cleanMessage(String message) {
        return message.replaceAll("\\W", " ").toLowerCase();
    }


    /**
     * makes sure input is less than 100 characters
     *
     * @return
     */
    public boolean messageIsProperLength(String message) {
        if (message.length() > Constants.MESSAGE_CHARACTER_LIMIT) {
            Toast.makeText(mContext, "Sorry! Message is too long to send.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (message.length() == 0) {
            Toast.makeText(mContext, "Sorry! Can't send an empty message.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public boolean canBeSent(String toSend) {
        String cleanedMessage = cleanMessage(toSend);
        if (!messageIsProperLength(cleanedMessage)) {
            return false;
        }
        return true;
    }


    /**TODO MAKE THIS ATTACH DIFFERENT HEADERS BASED ON QUESTION TYPE
     * sends an sms question
     * will check if message is valid, and if not, won't send
     *
     * @param toSend             the original string to send
     * @param questionDatabaseID id from activeandroid sqlite db
     */
    public void sendSmsQuestion(String toSend, long questionDatabaseID, String questionType) {
        toSend = cleanMessage(toSend);
        if (!messageIsProperLength(toSend)) {
            return;
        }
        //TODO append header information here
        SmsManager smsManager = SmsManager.getDefault();
        toSend = questionType + ":" + Long.toHexString(questionDatabaseID) + ":" + toSend;
        smsManager.sendTextMessage(Constants.CORTEX_NUMBER, null, toSend, null, null);
        Toast.makeText(mContext, "Question sent!", Toast.LENGTH_SHORT).show();
    }

}
