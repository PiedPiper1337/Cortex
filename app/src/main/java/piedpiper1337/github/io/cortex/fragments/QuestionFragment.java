package piedpiper1337.github.io.cortex.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import piedpiper1337.github.io.cortex.R;
import piedpiper1337.github.io.cortex.models.Question;
import piedpiper1337.github.io.cortex.utils.SMSQueryable;
import piedpiper1337.github.io.cortex.utils.SmsHandler;

/**
 * Created by brianzhao on 2/28/16.
 */
public class QuestionFragment extends BaseFragment {
    private static final String TAG = QuestionFragment.class.getSimpleName();
    private static final String QUESTON = "io.github.piedpiper1337.cortex.QUESTION";
    private SMSQueryable mQuestion;
    private TextView mQuestionTextView;
    private TextView mAnswerTextView;
    private LinearLayout mAnswerLinearLayout;


    public static QuestionFragment newInstance(SMSQueryable question) {
        Bundle args = new Bundle();
        args.putSerializable(QUESTON, question);
        QuestionFragment questionFragment = new QuestionFragment();
        questionFragment.setArguments(args);
        return questionFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuestion = (SMSQueryable) getArguments().getSerializable(QUESTON);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        mQuestionTextView = (TextView) view.findViewById(R.id.question_text_view);
        mAnswerTextView = (TextView) view.findViewById(R.id.answer_text_view);
        mAnswerLinearLayout = (LinearLayout) view.findViewById(R.id.answer_linear_layout);

        mQuestionTextView.setText(mQuestion.getQuestion());
        if (mQuestion.getAnswer() == null || mQuestion.getAnswer().isEmpty()) {
            mAnswerTextView.setText(R.string.retry_sms);
            mAnswerLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    SmsHandler smsHandler = new SmsHandler(getActivity());
                    smsHandler.sendSmsQuestion(mQuestion.getQuestion(),mQuestion.getId());
                    return false;
                }
            });
        }else{
            mAnswerTextView.setText(mQuestion.getAnswer());
        }

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
