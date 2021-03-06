package piedpiper1337.github.io.cortex.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.HashMap;

/**
 * Created by brianzhao on 2/28/16.
 */

/**
 * represents data that should be processed
 * https://www.future-processing.pl/blog/persist-your-data-activeandroid-and-parse/
 */
@Table(name = "RawData")
public class RawData extends Model {
    @Column(name = "Answer")
    private String mAnswer;

    @Column(name = "Finished")
    private boolean mFinished;

    @Column(name = "RealDataID")
    private long mId;

    @Column(name = "NumMessagesExpected")
    private int mNumMessagesExpected;

    //this is json
    //this is actually integer to string mapping
    @Column(name = "CurrentlyReceivedMessages")
    private HashMap<String, String> mCurrentlyReceivedMessagesMap;

    public RawData() {
        super();
    }

    public RawData(long id) {
        mId = id;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public String getAnswer() {
        return mAnswer;
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


    public HashMap<String, String> getCurrentlyReceivedMessagesMap() {
        return mCurrentlyReceivedMessagesMap;
    }

    public void setCurrentlyReceivedMessagesMap(HashMap<String, String> currentlyReceivedMessagesMap) {
        mCurrentlyReceivedMessagesMap = currentlyReceivedMessagesMap;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RawData rawData = (RawData) o;

        return (mId != rawData.mId);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (mId ^ (mId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "RawData{" +
                "mAnswer='" + mAnswer + '\'' +
                ", mFinished=" + mFinished +
                ", mId=" + mId +
                ", mNumMessagesExpected=" + mNumMessagesExpected +
                ", mCurrentlyReceivedMessagesMap=" + mCurrentlyReceivedMessagesMap +
                '}';
    }
}
