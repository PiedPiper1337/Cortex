package piedpiper1337.github.io.cortex.models;


/**
 * Created by brianzhao on 2/27/16.
 */

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * SugarRecords do not have an ID value until they are saved into the DB, so the actual
 * question text message has to be sent after this record is saved, so that the ID is
 * generated
 */

@Table(name = "SMSQuery")
public class SMSQuery extends Model implements Serializable, SMSQueryable {
    @Column(name = "Question")
    private String mQuestion;

    @Column(name = "Answer")
    private String mAnswer;

    @Column(name = "Type")
    private String mType;

    @Column(name = "Date")
    private long mDate;


    public SMSQuery() {
        super();
    }

    public SMSQuery(String question, String type) {
        super();
        mQuestion = question;
        mType = type;
        mDate = System.currentTimeMillis();
    }

    public String getAnswer() {
        return mAnswer;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public String getType() {
        return mType;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public void updateDate() {
        mDate = System.currentTimeMillis();
    }

    public long getDate() {
        return mDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SMSQuery smsQuery = (SMSQuery) o;

        if (mDate != smsQuery.mDate) return false;
        if (!mQuestion.equals(smsQuery.mQuestion)) return false;
        if (mAnswer != null ? !mAnswer.equals(smsQuery.mAnswer) : smsQuery.mAnswer != null)
            return false;
        return mType.equals(smsQuery.mType);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + mQuestion.hashCode();
        result = 31 * result + (mAnswer != null ? mAnswer.hashCode() : 0);
        result = 31 * result + mType.hashCode();
        result = 31 * result + (int) (mDate ^ (mDate >>> 32));
        return result;
    }
}
