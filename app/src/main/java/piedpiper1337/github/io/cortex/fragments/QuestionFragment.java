package piedpiper1337.github.io.cortex.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import piedpiper1337.github.io.cortex.R;
import piedpiper1337.github.io.cortex.models.Question;

/**
 * Created by brianzhao on 2/28/16.
 */
public class QuestionFragment extends BaseFragment {
    private static final String TAG = QuestionFragment.class.getSimpleName();
    private static final String QUESTON = "io.github.piedpiper1337.cortex.QUESTION";
    private Question mQuestion;
    private TextView mQuestionTextView;
    private TextView mAnswerTextView;


    public static QuestionFragment newInstance(Question question) {
        Bundle args = new Bundle();
        args.putSerializable(QUESTON, question);
        QuestionFragment questionFragment = new QuestionFragment();
        questionFragment.setArguments(args);
        return questionFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuestion = (Question) getArguments().getSerializable(QUESTON);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        mQuestionTextView = (TextView) view.findViewById(R.id.question_text_view);
        mAnswerTextView = (TextView) view.findViewById(R.id.answer_text_view);

        mQuestionTextView.setText(mQuestion.getQuestion());
        mAnswerTextView.setText(mQuestion.getAnswer());
        return view;
    }

    @Override
    public String getTagName() {
        return TAG;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
