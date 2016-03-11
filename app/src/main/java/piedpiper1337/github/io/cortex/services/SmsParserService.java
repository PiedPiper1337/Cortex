package piedpiper1337.github.io.cortex.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.activeandroid.query.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import piedpiper1337.github.io.cortex.models.RawData;
import piedpiper1337.github.io.cortex.models.SMSQuery;
import piedpiper1337.github.io.cortex.utils.Constants;

/**
 * Created by brianzhao on 3/2/16.
 */
public class SmsParserService extends IntentService {
    private static final String TAG = SmsParserService.class.getSimpleName();

    public SmsParserService() {
        super("SmsParserService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Starting to parse messages");
        List<String> cortexMessages = intent.getExtras().getStringArrayList(Constants.IntentKeys.CORTEX_MESSAGES_RECEIVED);
        Log.d("WTF", cortexMessages.toString());

        //have to load the current rawdatas to memory
        List<RawData> rawDataList = new Select().from(RawData.class).where("Finished = ?", false).execute();

        Map<Long, RawData> rawDataMap = new HashMap<>();
        for (RawData r : rawDataList) {
            rawDataMap.put(r.getRealId(), r);
        }

        for (String messageBody : cortexMessages) {
            Pattern p = Pattern.compile("C[a-fA-F0-9]+:[ 0-9]+/[ 0-9]+:.*");
            Matcher m = p.matcher(messageBody);
            int index = -1;
            if (m.find()) {
                index = m.start();
            } else {
                continue;
            }
            messageBody = messageBody.substring(index+1);
            String[] messageBodyParsed = messageBody.split(":");

//            String questionType = messageBodyParsed[0];
            long id = Long.parseLong(messageBodyParsed[0], 16);

            String[] ratio = messageBodyParsed[1].split("/");
            int currentMessageNumber = Integer.parseInt(ratio[0].trim());
            int totalMessageNumber = Integer.parseInt(ratio[1].trim());

            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 2; j < messageBodyParsed.length; j++) {
                stringBuilder.append(messageBodyParsed[j]).append(' ');
            }
            String actualMessage = stringBuilder.toString();

            RawData rawData = new RawData(id);
            if (rawDataMap.containsKey(id)) { //if this particular message was already in memory/db
                RawData actualRawData = rawDataMap.get(id);
                HashMap<String, String> messageMap = actualRawData.getCurrentlyReceivedMessagesMap();

                Log.d("WTF", "EXISTING HASHMAP: \n" + messageMap.toString());
                messageMap.put(String.valueOf(currentMessageNumber), actualMessage);

                Log.d("WTF", "EXISTING HASHMAP After putting stuff in it\n" + messageMap.toString());
            } else { //otherwise its a new message
                rawData.setNumMessagesExpected(totalMessageNumber);
                HashMap<String, String> messages = new HashMap<>();
                messages.put(String.valueOf(currentMessageNumber), actualMessage);
                rawData.setCurrentlyReceivedMessagesMap(messages);
                rawData.setFinished(false);
                rawDataMap.put(id, rawData);
            }
        }

        int numCompleted = 0;
        for (RawData rawDataToUpdate : rawDataMap.values()) {
            if (rawDataToUpdate.getNumMessagesExpected() ==
                    rawDataToUpdate.getCurrentlyReceivedMessagesMap().keySet().size()) {
                rawDataToUpdate.setFinished(true);

                //concatenate all the messages within the hashmap together
                StringBuilder finalMessage = new StringBuilder();
                for (int i = 1; i <= rawDataToUpdate.getNumMessagesExpected(); i++) {
                    finalMessage.append(rawDataToUpdate.getCurrentlyReceivedMessagesMap().get(String.valueOf(i)));
                }
                rawDataToUpdate.setAnswer(finalMessage.toString().trim());
                Log.d("WTF-PARSER", rawDataToUpdate.getAnswer());

                numCompleted++;
            }
            rawDataToUpdate.save();
        }


        List<RawData> updatedRawDataList = new Select().from(RawData.class).where("Finished = ?", true).execute();
        for (RawData rawData : updatedRawDataList) {
            long id = rawData.getRealId();
            SMSQuery smsquery = new Select().from(SMSQuery.class).where("Id = ?", id).executeSingle();
            if (smsquery == null || smsquery.getQuestion() == null) {
                //if the user deleted this question before it came back
                rawData.delete();
                continue;
            }
            smsquery.setAnswer(rawData.getAnswer());
            smsquery.updateDate();
            smsquery.save();
            rawData.delete();
        }

        if (numCompleted > 0) {
            Intent myintent = new Intent(Constants.IntentKeys.CORTEX_MESSAGES_DB_UPDATED);
            // You can also include some extra data.
//            intent.putExtra("message", "This is my message!");
            LocalBroadcastManager.getInstance(this).sendBroadcast(myintent);
//            AudioManager aManager=(AudioManager)(this.getSystemService(Context.AUDIO_SERVICE));
//            aManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }
    }


    private String getTag(){
        return TAG;
    }
}
