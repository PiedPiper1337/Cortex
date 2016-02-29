package piedpiper1337.github.io.cortex.models;


/**
 * Created by brianzhao on 2/27/16.
 */

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

import piedpiper1337.github.io.cortex.utils.SMSQueryable;

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
}
