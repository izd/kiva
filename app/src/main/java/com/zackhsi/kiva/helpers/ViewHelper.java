package com.zackhsi.kiva.helpers;

import android.graphics.RectF;
import android.view.View;

/**
 * Created by zackhsi on 4/4/15.
 */
public class ViewHelper {
    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    public static RectF getOnScreenRect(View view) {
        RectF rect = new RectF();
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }
}
