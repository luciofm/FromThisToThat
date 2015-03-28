package com.luciofm.presentation.droiconit.util;

import android.content.Context;
import android.util.DisplayMetrics;
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
}
