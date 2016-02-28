package piedpiper1337.github.io.cortex.utils;

import java.io.Serializable;

/**
 * Created by brianzhao on 2/28/16.
 */
public interface SMSQueryable extends Serializable{
    public String getAnswer();

    public String getQuestion();

    public String getType();

    public void setAnswer(String answer);

    public Long save();

    public void delete();

    public Long getId();
}
