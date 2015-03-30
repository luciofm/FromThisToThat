package com.luciofm.presentation.droiconit.util;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class Utils {

    private Utils() {
    }

    public static int dpToPx(final Context context, final float dp) {
        // Took from http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
        final float scale = getDisplayMetrics(context).density;
        return (int) ((dp * scale) + 0.5f);
    }

    public static int pxToDp(final Context context, final float px) {
        return (int) ((px / getDisplayMetrics(context).density) + 0.5);
    }

    public static int getScreenWidth(final Context context) {
        if (context == null)
            return 0;
        return getDisplayMetrics(context).widthPixels;
    }

    public static DisplayMetrics getDisplayMetrics(final Context context) {
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public static void dispatchTouch(View view) {
        dispatchTouch(view, 200);
    }

    public static void dispatchTouch(final View view, final long duration) {
        final long downTime = SystemClock.uptimeMillis();
        final long eventTime = SystemClock.uptimeMillis();
        final float x = view.getWidth() / 3;//0.0f;
        final float y = view.getHeight() / 3;//0.0f;
        // List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
        final int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(downTime,
                eventTime,
                MotionEvent.ACTION_DOWN,
                x,
                y,
                metaState);

        // Dispatch touch event to view
        view.dispatchTouchEvent(motionEvent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MotionEvent motionEvent = MotionEvent.obtain(downTime + duration,
                        eventTime + duration,
                        MotionEvent.ACTION_UP,
                        x,
                        y,
                        metaState);
                view.dispatchTouchEvent(motionEvent);
            }
        }, duration);
    }
}
