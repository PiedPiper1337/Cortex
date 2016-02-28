package piedpiper1337.github.io.cortex.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Select;

import java.util.List;

import piedpiper1337.github.io.cortex.R;
import piedpiper1337.github.io.cortex.activities.NavigationCallback;
import piedpiper1337.github.io.cortex.models.Question;

/**
 * Created by brianzhao on 2/27/16.
 */
public class QuestionListFragment extends BaseFragment {
    private static final String TAG = QuestionListFragment.class.getSimpleName();
    private Context mContext;
    private NavigationCallback mNavigationCallback;
    private RecyclerView mRecyclerView;
    private RelativeLayout mBackgroundLayout;
    private List<Question> mQuestionList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        if (activity instanceof NavigationCallback) {
            mNavigationCallback = (NavigationCallback) activity;
        } else {
            throw new RuntimeException("activity doesn't implement navigation callback");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);
        mBackgroundLayout = (RelativeLayout) view.findViewById(R.id.question_list_background_relative_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.question_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        return view;
    }

    /**
     * regenerate list of questions by doing db call
     */
    private void updateUI() {
//        Question.findAll(Question.class);
        mQuestionList = Select.from(Question.class).list();
        if (mQuestionList != null) {
            if (mQuestionList.size() == 0) {
                mRecyclerView.setVisibility(View.GONE);
                mBackgroundLayout.setVisibility(View.VISIBLE);
            } else {

            }
        } else {
            Log.e(getTagName(), "Null question list from db call!!!");
            throw new RuntimeException();
        }
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }


    @Override
    public String getTagName() {
        return TAG;
    }

    public class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mQuestionTextView;
        private TextView mAnswerTextView;
        private Question mQuestion;

        public QuestionHolder(View itemView) {
            super(itemView);
            mQuestionTextView = (TextView) itemView.findViewById(R.id.list_item_question_text_view);
            mAnswerTextView = (TextView) itemView.findViewById(R.id.list_item_answer_text_view);
            itemView.setOnClickListener(this);
        }

        public void bindQuestion(Question question){
            mQuestion = question;
            mQuestionTextView.setText(question.getQuestion());
            String answer = question.getAnswer();
            if (answer != null && !answer.isEmpty()) {
                mAnswerTextView.setText(answer);
            } else {
                mAnswerTextView.setText(R.string.answer_not_arrived_yet);
            }
        }

        //TODO make new fragment
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "NEED TO MAKE NEW FRAGMENT!", Toast.LENGTH_SHORT).show();
        }
    }


    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {
        private List<Question> mQuestions;

        public QuestionAdapter(List<Question> questions) {
            mQuestions = questions;
        }

        @Override
        public int getItemCount() {
            return mQuestions.size();
        }

        @Override
        public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_question, parent, false);
            return new QuestionHolder(view);
        }

        @Override
        public void onBindViewHolder(QuestionHolder holder, int position) {
            Question question = mQuestions.get(position);
            holder.bindQuestion(question);
        }
    }



}
