package piedpiper1337.github.io.cortex.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import piedpiper1337.github.io.cortex.R;

/**
 * Created by brianzhao on 1/19/16.
 */
public class PageFragment extends BaseFragment{
    public static final String TAG = "tag TODO";
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    public static PageFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        TextView textView = (TextView) view;
        textView.setText("Fragment #" + mPage + "\nStill learning how to use fragments!!");
        return view;
    }

    @Override
    public String getTagName() {
        return TAG;
    }
}
