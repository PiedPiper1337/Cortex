package piedpiper1337.github.io.cortex.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by brianzhao on 2/3/16.
 */
public class Constants {
    public static final String CORTEX_NUMBER = "9093260367";
    public static final int MESSAGE_CHARACTER_LIMIT = 100;

    public interface SMS_TYPE {
        String QUESTION_TYPE = "QUES";
        String WIKI_TYPE = "WIKI";
        String URL_TYPE = "URL";
    }


    public interface SharedPreferenceKeys {
        String RECYCLER_VIEW_POSITION = "io.github.piedpiper1337.cortex.RECYCLER_VIEW_POSITION";
        String CARRIER_NAME = "io.github.piedpiper1337.cortex.CARRIER_NAME";
    }


    public interface IntentKeys {
        String CORTEX_MESSAGES_RECEIVED = "io.github.piedpiper1337.cortex.CORTEX_MESSAGES_RECEIVED";
        String CORTEX_MESSAGES_DB_UPDATED = "io.github.piedpiper1337.cortex.CORTEX_MESSAGES_DB_UPDATED";
    }

    public static Map<String, String> carrierToCode;
    static {
        if (carrierToCode == null) {
            carrierToCode = new HashMap<>();
            carrierToCode.put("AT&T", "ATT");
            carrierToCode.put("SPRINT", "SPR");
            carrierToCode.put("VERIZON", "VER");
            carrierToCode.put("TMOBILE", "TMO");
        }
    }

    public static String[] carrierOptionsArray;
    static {
        if (carrierOptionsArray == null) {
            carrierOptionsArray = new ArrayList<String>(carrierToCode.keySet())
                    .toArray(new String[carrierToCode.keySet().size()]);
        }
    }

    public static final String PAYPAL_DONATE = "https://www.paypal.me/BrianDev";



}
