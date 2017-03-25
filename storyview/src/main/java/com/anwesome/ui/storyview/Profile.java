package com.anwesome.ui.storyview;

import android.graphics.*;

/**
 * Created by anweshmishra on 25/03/17.
 */
public class Profile {
    private Bitmap bitmap;
    private String name;
    private int render = 0,w,h;
    private Profile(Bitmap bitmap,String name) {
        this.bitmap = bitmap;
        this.name = name;
    }
    public static Profile getInstance(Bitmap bitmap,String name) {
        return new Profile(bitmap,name);
    }
    public void draw(Canvas canvas, Paint paint) {
        if(render == 0) {
            w = canvas.getWidth();
            h = canvas.getHeight();
            bitmap = Bitmap.createScaledBitmap(bitmap,h/12,h/12,true);
        }
        canvas.save();
        canvas.translate(h/20,h/15);
        Path path = new Path();
        path.addCircle(0,0,h/30, Path.Direction.CCW);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap,-bitmap.getWidth()/2,-bitmap.getHeight()/2,paint);
        canvas.restore();
        paint.setColor(Color.parseColor("#FAFAFA"));
        paint.setTextSize(w/20);
        canvas.drawText(name,h/24+h/20,h/15+paint.getTextSize()/2,paint);
        render++;
    }
}
