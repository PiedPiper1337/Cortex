package piedpiper1337.github.io.cortex.activities;

import java.util.List;

import piedpiper1337.github.io.cortex.models.SMSQueryable;

/**
 * Created by brianzhao on 2/11/16.
 */
public interface NavigationCallback {
    /**
     * launches the SMS Question fragment
     * when FAB + is clicked
     */
    void askQuestion(String questionType);

    /**
     * launches the fragment which has a viewpager
     * on all questions, and the particular question to jump to
     * @param questions
     * @param position
     */
    void previewQuestions(List<SMSQueryable> questions, int position);
}
