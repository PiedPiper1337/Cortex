package piedpiper1337.github.io.cortex.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.List;

import piedpiper1337.github.io.cortex.R;
import piedpiper1337.github.io.cortex.activities.NavigationCallback;
import piedpiper1337.github.io.cortex.models.Question;
import piedpiper1337.github.io.cortex.utils.Constants;
import piedpiper1337.github.io.cortex.utils.ItemTouchHelperAdapter;
import piedpiper1337.github.io.cortex.utils.SharedPreferenceUtil;
import piedpiper1337.github.io.cortex.utils.SimpleItemTouchHelperCallback;

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
        //TODO initialize the database list of questions

        mQuestionList = new Select().from(Question.class).orderBy("Date DESC").execute();
//        mQuestionList = new Select().from(Question.class).execute();


        if (mQuestionList != null) {
            if (mQuestionList.size() == 0) {
                swapToBackgroundView();

            } else {
                QuestionAdapter questionAdapter = new QuestionAdapter(mQuestionList, this);
                ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(questionAdapter);
                ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(mRecyclerView);
                mRecyclerView.setAdapter(questionAdapter);

                int previousPosition = SharedPreferenceUtil.readPreference(mContext,
                        Constants.SharedPreferenceKeys.RECYCLER_VIEW_POSITION, -1);
                if (previousPosition != -1) {
                    mRecyclerView.scrollToPosition(previousPosition);
                }
                SharedPreferenceUtil.clearPreferences(mContext);
                swapToRecyclerView();
            }
        } else {
            Log.e(getTagName(), "Null question list from db call!!!");
            throw new RuntimeException();
        }
    }

    private void swapToBackgroundView() {
        mRecyclerView.setVisibility(View.GONE);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        mBackgroundLayout.setVisibility(View.VISIBLE);
        mBackgroundLayout.startAnimation(fadeInAnimation);
    }

    private void swapToRecyclerView() {
        mBackgroundLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
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

        public QuestionHolder(final View itemView) {
            super(itemView);
            mQuestionTextView = (TextView) itemView.findViewById(R.id.list_item_question_text_view);
            mAnswerTextView = (TextView) itemView.findViewById(R.id.list_item_answer_text_view);
            itemView.setOnClickListener(this);

        }

        public void bindQuestion(Question question) {
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
//            Toast.makeText(getActivity(), "NEED TO MAKE NEW FRAGMENT!", Toast.LENGTH_SHORT).show();
            SharedPreferenceUtil.savePreference(mContext,
                    Constants.SharedPreferenceKeys.RECYCLER_VIEW_POSITION,
                    this.getAdapterPosition());
            mNavigationCallback.previewQuestions(mQuestionList, this.getAdapterPosition());
        }
    }


    private class QuestionAdapter
            extends RecyclerView.Adapter<QuestionHolder>
            implements ItemTouchHelperAdapter {
        private List<Question> mQuestions;
        private QuestionListFragment mQuestionListFragment;

        public QuestionAdapter(List<Question> questions, QuestionListFragment questionListFragment) {
            mQuestions = questions;
            mQuestionListFragment = questionListFragment;
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

        @Override
        public void onItemDismiss(final int position) {
            final Question toBeDeleted = mQuestions.remove(position);
            notifyItemRemoved(position);

            final Snackbar snackbar = Snackbar
                    .make(mRecyclerView, "Question Deleted", Snackbar.LENGTH_LONG);

            snackbar.setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            })
                    .setActionTextColor(ContextCompat.getColor(mContext, R.color.lightOrange))
                    .setCallback(new Snackbar.Callback() {

                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            switch (event) {
                                case DISMISS_EVENT_CONSECUTIVE:
                                case DISMISS_EVENT_TIMEOUT:
                                    //actually delete
                                    toBeDeleted.delete();
                                    break;
                                case DISMISS_EVENT_MANUAL:
                                    //restore
                                    mQuestions.add(position, toBeDeleted);
                                    notifyItemInserted(position);
                                    mRecyclerView.scrollToPosition(position);

                                    break;
                                default:
                                    break;
                            }
                        }
                    });

            snackbar.show();
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            throw new UnsupportedOperationException("Haven't implemented this yet");
        }
    }

}
