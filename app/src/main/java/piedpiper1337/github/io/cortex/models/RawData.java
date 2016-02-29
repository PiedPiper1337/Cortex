package piedpiper1337.github.io.cortex.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.HashMap;

import piedpiper1337.github.io.cortex.utils.SMSQueryable;

/**
 * Created by brianzhao on 2/28/16.
 */

/**
 * represents data that should be processed
 */
@Table(name = "RawData")
public class RawData extends Model {
    @Column(name = "Answer")
    private String mAnswer;

    @Column(name = "Type")
    private String mType;

    @Column(name = "Finished")
    private boolean mFinished;

    @Column(name = "RealDataID")
    private long mId;

    @Column(name = "NumMessagesExpected")
    private int mNumMessagesExpected;

    //this is json
    @Column(name = "CurrentlyReceivedMessages")
    private String mCurrentlyReceivedMessagesString;

    private HashMap<Integer, String> mCurrentlyReceivedMessagesMap;

    public RawData() {
        super();
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }

    public boolean isFinished() {
        return mFinished;
    }

    public void setFinished(boolean finished) {
        mFinished = finished;
    }

    public long getRealId() {
        return mId;
    }

    public void setRealId(long id) {
        this.mId = id;
    }

    public int getNumMessagesExpected() {
        return mNumMessagesExpected;
    }

    public void setNumMessagesExpected(int numMessagesExpected) {
        mNumMessagesExpected = numMessagesExpected;
    }


    public HashMap<Integer, String> getCurrentlyReceivedMessagesMap() {
        return mCurrentlyReceivedMessagesMap;
    }

    public void setCurrentlyReceivedMessagesMap(HashMap<Integer, String> currentlyReceivedMessagesMap) {
        mCurrentlyReceivedMessagesMap = currentlyReceivedMessagesMap;
    }

    public String getCurrentlyReceivedMessagesString() {
        return mCurrentlyReceivedMessagesString;
    }

    public void setCurrentlyReceivedMessagesString(String currentlyReceivedMessagesString) {
        mCurrentlyReceivedMessagesString = currentlyReceivedMessagesString;
    }
}
