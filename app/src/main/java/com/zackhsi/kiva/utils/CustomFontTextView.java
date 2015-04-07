package com.zackhsi.kiva.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomFontTextView extends TextView {

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            final Typeface customTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/nevis.ttf");
            setTypeface(customTypeface);
        }
    }
}

