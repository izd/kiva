package com.zackhsi.kiva.helpers;


import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import butterknife.Optional;


public class RoundedCornerTransformation implements com.squareup.picasso.Transformation {

    private Boolean stroke;

    public RoundedCornerTransformation(Boolean stroke) {
        super();
        this.stroke = stroke;
    }

    public RoundedCornerTransformation() {
        super();
        stroke = true;
    }


    @Override
    public Bitmap transform(final Bitmap source) {

        Integer radius = 8;
        Integer margin = 0;

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
//        circle
//        canvas.drawCircle((source.getWidth() - margin)/2, (source.getHeight() - margin)/2, radius-2, paint);

        canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin), radius, radius, paint);

        if (source != output) {
            source.recycle();
        }

        //Optional outline
        if (stroke) {
            Paint paint1 = new Paint();
            paint1.setColor(Color.LTGRAY);
            paint1.setStyle(Paint.Style.STROKE);
            paint1.setAntiAlias(true);
            paint1.setStrokeWidth(2);
            canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin), radius, radius, paint1);
        }


        return output;
    }

    @Override
    public String key() {
        return "rounded";
    }
}
