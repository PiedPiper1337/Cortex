package piedpiper1337.github.io.cortex.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by brianzhao on 2/28/16.
 */
public class LimitSwipeViewPager extends ViewPager {
    private static final float side_dead_zones = 0.33f;

    public LimitSwipeViewPager(Context context) {
        super(context);
    }

    public LimitSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (inNeutralArea(ev.getX(), ev.getY(), ev.getRawX(), ev.getRawY())) {
            //--events re-directed to this ViewPager's onTouch() and to its child views from there--
            return false;
        } else {
            //--events intercepted by this ViewPager's default implementation, where it looks for swipe gestures--
            super.onInterceptTouchEvent(ev);
            return true;
        }
    }

    private boolean inNeutralArea(float x, float y, float rawX, float rawY) {
        //--check if x,y inside non reactive area, return true/false accordingly--
        boolean hitChildView = false;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Rect bounds = new Rect();
            child.getHitRect(bounds);
            if (bounds.contains((int) rawX, (int) rawY)) {
                hitChildView = true;
                Log.d("WTF", "hit childview");
                break;
            }
        }
        if (!hitChildView) {
            if (inFabSpace(x, y)) {
                return true;
            } else {
                return false;
            }
        } else {
            if (inSideArea(x, y)) {
                return false;
            } else {
                return true;
            }
        }


//        if (x > this.getWidth() * side_dead_zones && x < getWidth() * (1 - side_dead_zones)) {
//            //not in neutral area if hitting sides
//            Log.d("WTF", x + " is x");
//            Log.d("WTF", getWidth() + " is width");
//            return true;
//        } else if (x > getWidth() * (1 - side_dead_zones) && y > getHeight() * 0.8) {
//            //hack to get the fab to work
//
//            Log.d("WTF", x + " is x");
//            Log.d("WTF", y + " is y");
//            Log.d("WTF", getWidth() + " is width");
//            Log.d("WTF", getHeight() + " is height");
//            return true;
//        } else {
//            //not in neutral area if there is no view underneath
////            https://stackoverflow.com/questions/10959400/how-to-find-element-in-view-by-coordinates-x-y-android
//
//        }
//        return false;
    }

    private boolean inSideArea(float x, float y){
        if (x > this.getWidth() * side_dead_zones && x < getWidth() * (1 - side_dead_zones)) {
            return false;
        } else {
            return true;
        }
    }
    private boolean inFabSpace(float x, float y){
        if (x > getWidth() * (1 - side_dead_zones) && y > getHeight() * 0.8) {
            return true;
        } else {
            return false;
        }
    }
}
