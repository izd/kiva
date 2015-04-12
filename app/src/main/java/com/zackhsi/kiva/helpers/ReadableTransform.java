package com.zackhsi.kiva.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;

public final class ReadableTransform implements Transformation {

    WeakReference<Context> context;

    public ReadableTransform(Context context) {
        super();
        this.context = new WeakReference<>(context);
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        RenderScript rs = RenderScript.create(context.get());
        Bitmap source = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        if (source == null) {
            return null;
        }

//        Allocation input = Allocation.createFromBitmap(rs, source,
//                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
//        Allocation output = Allocation.createTyped(rs, input.getType());
//        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//        script.setRadius(17);
//        script.setInput(input);
//        script.forEach(output);
//        output.copyTo(source);

    Paint paint = new Paint();
        ColorFilter filter = new LightingColorFilter(0xCC787878, 0x66444444);
        paint.setColorFilter(filter);
        Canvas canvas = new Canvas(source);
        canvas.drawBitmap(source, 0, 0, paint);

        bitmap.recycle();

        return source;
    }

    @Override
    public String key() {
        return "blur";
    }

}
