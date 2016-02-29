package piedpiper1337.github.io.cortex.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import piedpiper1337.github.io.cortex.utils.SMSQueryable;

/**
 * Created by brianzhao on 2/28/16.
 */
@Table(name = "Wiki")
public class Wiki extends Model implements SMSQueryable {
    @Column(name = "Question")
    private String mQuestion;

    @Column(name = "Answer")
    private String mAnswer;

    @Column(name = "Type")
    private String mType;

    @Column(name = "Date")
    private long mDate;

    public Wiki() {
        super();
    }

    public Wiki(String question, String type) {
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

    public void updateDate(){
        mDate = System.currentTimeMillis();
    }
}

