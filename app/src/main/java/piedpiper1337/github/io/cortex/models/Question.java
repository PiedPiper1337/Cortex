package piedpiper1337.github.io.cortex.models;

import com.orm.SugarRecord;

/**
 * Created by brianzhao on 2/27/16.
 */

/**
 * SugarRecords do not have an ID value until they are saved into the DB, so the actual
 * question text message has to be sent after this record is saved, so that the ID is
 * generated
 */

public class Question extends SugarRecord{
    private String mQuestion;
    private String mAnswer;
    private String mType;

    public Question() {

    }

    public Question(String question, String type) {
        mQuestion = question;
        mType = type;
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

}
