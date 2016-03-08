package piedpiper1337.github.io.cortex.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import piedpiper1337.github.io.cortex.R;
import piedpiper1337.github.io.cortex.activities.HomeActivity;
import piedpiper1337.github.io.cortex.activities.NavigationCallback;
import piedpiper1337.github.io.cortex.models.RawData;
import piedpiper1337.github.io.cortex.models.SMSQuery;
import piedpiper1337.github.io.cortex.utils.Constants;
import piedpiper1337.github.io.cortex.utils.ItemTouchHelperAdapter;
import piedpiper1337.github.io.cortex.utils.SMSQueryable;
import piedpiper1337.github.io.cortex.utils.SharedPreferenceUtil;
import piedpiper1337.github.io.cortex.utils.SimpleItemTouchHelperCallback;


/**
 * Created by brianzhao on 2/27/16.
 */
public class QuestionListFragment extends BaseFragment implements SearchView.OnQueryTextListener {
    private static final String TAG = QuestionListFragment.class.getSimpleName();
    private Context mContext;
    private NavigationCallback mNavigationCallback;
    private RecyclerView mRecyclerView;
    private RelativeLayout mBackgroundLayout;
    private LinearLayout mForegroundLayout;
    private FloatingActionsMenu mFloatingActionsMenu;
    private FloatingActionButton mFloatingQuestionButton;
    private FloatingActionButton mFloatingWikiButton;
    private List<SMSQueryable> mQuestionList;
    private Toolbar mToolbar;
    private Map<String, String> questionTypeToLetterMap;
    private BroadcastReceiver mReceiver;

    private static final String QUESTION_TYPE = "io.github.piedpiper1337.cortex.QUESTION_TYPE";


    public static QuestionListFragment newInstance() {
        Bundle args = new Bundle();
        QuestionListFragment fragment = new QuestionListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        if (activity instanceof NavigationCallback) {
            mNavigationCallback = (NavigationCallback) activity;
        } else {
            throw new RuntimeException("activity doesn't implement navigation callback");
        }
        questionTypeToLetterMap = new HashMap<>();
        questionTypeToLetterMap.put(Constants.SMS_TYPE.QUESTION_TYPE, "Q");
        questionTypeToLetterMap.put(Constants.SMS_TYPE.WIKI_TYPE, "W");
        questionTypeToLetterMap.put(Constants.SMS_TYPE.URL_TYPE, "U");


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Our handler for received Intents. This will be called whenever an Intent
        // with an action named "custom-event-name" is broadcasted.
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent
                String message = intent.getStringExtra("message");
                Log.d("receiver", "Got message: " + message);
                updateUI();
            }
        };
        setHasOptionsMenu(true);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver,
                new IntentFilter(Constants.IntentKeys.CORTEX_MESSAGES_DB_UPDATED));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_base, menu);
        final MenuItem item = menu.findItem(R.id.search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(this);
    }


    //TODO for search
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms_list, container, false);
        mBackgroundLayout = (RelativeLayout) view.findViewById(R.id.question_list_background_relative_layout);
        mForegroundLayout = (LinearLayout) view.findViewById(R.id.sms_list_foreground_layout);
        mToolbar = (Toolbar) view.findViewById(R.id.fragment_sms_list_toolbar);
        ((HomeActivity) mContext).setSupportActionBar(mToolbar);
        ((HomeActivity) mContext).getSupportActionBar().setTitle(R.string.app_name);

        mFloatingActionsMenu = (FloatingActionsMenu) view.findViewById(R.id.send_actions);
        mFloatingQuestionButton = (FloatingActionButton) view.findViewById(R.id.fab_ask_question);
        mFloatingWikiButton = (FloatingActionButton) view.findViewById(R.id.fab_wiki_lookup);


        mFloatingQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavigationCallback.askQuestion(Constants.SMS_TYPE.QUESTION_TYPE);
                mFloatingActionsMenu.toggle();
            }
        });

        mFloatingWikiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavigationCallback.askQuestion(Constants.SMS_TYPE.WIKI_TYPE);
                mFloatingActionsMenu.toggle();
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.question_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //TODO add items to the drawer
//        Drawer drawer = new DrawerBuilder()
//                .withActivity((Activity) mContext)
//                .withToolbar(mToolbar)
//                .buildForFragment();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    /**
     * regenerate list of questions by doing db call
     */
    private void updateUI() {
        List<SMSQuery> SMSQueries = new Select().from(SMSQuery.class).orderBy("Date DESC").execute();
        mQuestionList = new ArrayList<>();
        for (SMSQuery q : SMSQueries) {
            mQuestionList.add(q);
        }

        if (mQuestionList != null) {
            if (mQuestionList.size() == 0) {
                swapToBackgroundView();

            } else {
                QuestionAdapter questionAdapter = new QuestionAdapter(mQuestionList);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    /**
     *
     * https://github.com/amulyakhare/TextDrawable
     */
    public class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mQuestionTextView;
        private TextView mAnswerTextView;
        private ImageView mImageView;
        private SMSQueryable mQuestion;
        private ColorGenerator mColorGenerator;

        public QuestionHolder(final View itemView) {
            super(itemView);
            mQuestionTextView = (TextView) itemView.findViewById(R.id.list_item_question_text_view);
            mAnswerTextView = (TextView) itemView.findViewById(R.id.list_item_answer_text_view);
            mImageView = (ImageView) itemView.findViewById(R.id.list_item_question_image_view);
            mColorGenerator = ColorGenerator.MATERIAL;
            itemView.setOnClickListener(this);
        }

        public void bindQuestion(SMSQueryable question) {
            mQuestion = question;
            mQuestionTextView.setText(question.getQuestion());

            //TODO map questions to colors
//            Random rnd = new Random();
//            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//            int color = mColorGenerator.getRandomColor();
            int color = mColorGenerator.getColor(question.getType().hashCode()*7);

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(questionTypeToLetterMap.get(question.getType()), color);
            mImageView.setImageDrawable(drawable);

            String answer = question.getAnswer();


            if (answer != null && !answer.isEmpty()) {
                answer = answer.replaceAll("\\s+", " ").trim();
                if (answer.length() > 100) {
                    answer = answer.substring(0, 40) + "...";
                }
                mAnswerTextView.setText(answer);
            } else {
                mAnswerTextView.setText(R.string.answer_not_arrived_yet);
            }
        }

        @Override
        public void onClick(View view) {
            SharedPreferenceUtil.savePreference(mContext,
                    Constants.SharedPreferenceKeys.RECYCLER_VIEW_POSITION,
                    this.getAdapterPosition());
            mNavigationCallback.previewQuestions(mQuestionList, this.getAdapterPosition());
        }
    }


    private class QuestionAdapter
            extends RecyclerView.Adapter<QuestionHolder>
            implements ItemTouchHelperAdapter {
        private List<SMSQueryable> mQuestions;

        public QuestionAdapter(List<SMSQueryable> questions) {
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
            SMSQueryable question = mQuestions.get(position);
            holder.bindQuestion(question);
        }

        @Override
        public void onItemDismiss(final int position) {
            final SMSQueryable toBeDeleted = mQuestions.remove(position);
            notifyItemRemoved(position);

            final Snackbar snackbar = Snackbar
                    .make(mRecyclerView, "Question Deleted", Snackbar.LENGTH_SHORT);

            snackbar.setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            })
                    .setActionTextColor(ContextCompat.getColor(mContext, R.color.lightOrange))
                    .setCallback(new Snackbar.Callback() {
                        private float mHeight;
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            switch (event) {
                                case DISMISS_EVENT_CONSECUTIVE:
                                case DISMISS_EVENT_TIMEOUT:
                                    //actually delete
                                    toBeDeleted.delete();
                                    if (mQuestions.size() == 0) {
                                        swapToBackgroundView();
                                    }
                                    break;
                                case DISMISS_EVENT_MANUAL:
                                    //restore
                                    mQuestions.add(position, toBeDeleted);
                                    if (mQuestions.size() == 1) {
                                        swapToRecyclerView();
                                    }
                                    notifyItemInserted(position);
                                    mRecyclerView.scrollToPosition(position);
                                    break;
                                default:
                                    break;
                            }
                            if (!snackbar.isShown()) {
                                //if the snackbar is not shown, make sure the fab is at the
                                //original location
//                                https://stackoverflow.com/questions/4213393/translate-animation

//                                https://stackoverflow.com/questions/35074558/android-floating-action-button-not-returning-to-initial-position

                                mFloatingActionsMenu.clearAnimation();
                            }
                        }

                        @Override
                        public void onShown(Snackbar snackbar) {
                            mHeight = snackbar.getView().getHeight();
                            Animation animation = new TranslateAnimation(0, 0,0, -mHeight * 0.8f);
                            animation.setDuration(50);
                            animation.setFillAfter(true);
                            mFloatingActionsMenu.startAnimation(animation);
//                            mFloatingActionsMenu.setTranslationY(-snackbar.getView().getHeight() * 0.8f);
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
