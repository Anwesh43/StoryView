package com.anwesome.ui.storyview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by anweshmishra on 25/03/17.
 */
public class Status {
    private Bitmap statusBitmap;
    private String title,profileName;
    private String day,timeString;
    private boolean stop = false;
    private int w,h,dir = 0;
    private int time = 0;
    public void init() {
        time = 0;
    }
    public void pause() {
        dir = 0;
    }
    public void resume() {
        dir = 1;
    }
    private  Status(Bitmap statusBitmap,String title,Date date) {
        this.title = title;
        this.statusBitmap = statusBitmap;
        setDateString(date);
    }
    private void setDateString(Date date) {
        if(date!=null) {
            Date now = new Date();
            Calendar dateCalendar = Calendar.getInstance();
            dateCalendar.setTime(date);
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.setTime(now);
            int dayDiff = nowCalendar.get(Calendar.DAY_OF_YEAR) - dateCalendar.get(Calendar.DAY_OF_YEAR);
            if (dayDiff == 0) {
                day = "today";
            } else {
                day = dayDiff + " day ago";
            }
            int hour = dateCalendar.get(Calendar.HOUR_OF_DAY);
            int min = dateCalendar.get(Calendar.MINUTE);
            timeString = hour+":"+min;
        }
    }
    public void draw(Canvas canvas, Paint paint) {
        if(time == 0) {
            w = canvas.getWidth();
            h = canvas.getHeight();
            initBitmapSize();
        }
        paint.setTextSize(w/20);
        canvas.drawColor(Color.BLACK);
        canvas.drawText(day+","+timeString,w/20,w/10+w/15,paint);
        canvas.drawBitmap(statusBitmap,w/5,h/5,paint);
        time+=dir;
        if(time == StoryConstants.STATUS_INTERVAL) {
            stop = true;
        }
    }
    public int getTime() {
        return  time;
    }
    private void initBitmapSize() {
        if(statusBitmap!=null) {
            statusBitmap = Bitmap.createScaledBitmap(statusBitmap,3*w/5,3*h/5,true);
        }
    }
    public boolean shouldStop() {
        return stop;
    }
    public int hashCode() {
        return (statusBitmap.hashCode()+title.hashCode());
    }
    public static Status newInstance(Bitmap statusBitmap,String title,Date date) {
        return new Status(statusBitmap,title,date);
    }
    private class TimeTracker {
        private float x,y,r,a=0;
        public TimeTracker(float x,float y,float r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }
        public void draw(Canvas canvas,Paint paint) {
            paint.setColor(Color.parseColor("#9E9E9E"));
            float newR = (4*r)/5;
            canvas.save();
            canvas.translate(x,y);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawArc(new RectF(-newR,-newR,newR,newR),a,360-a,true,paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(new RectF(-r,-r,r,r),a,360-a,true,paint);
            canvas.restore();
        }
        public void update() {
            a = ((time*1.0f)/(StoryConstants.STATUS_INTERVAL))*360;
        }
    }

}
