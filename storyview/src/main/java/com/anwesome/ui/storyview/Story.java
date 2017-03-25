package com.anwesome.ui.storyview;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.*;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anweshmishra on 25/03/17.
 */
public class Story {
    private Profile profile;
    private List<Status> statuses = new ArrayList<>();
    private Activity activity;
    private List<TrackingBar> trackingBars = new ArrayList<>();
    private int w,h;
    private int currIndex = 0;
    private boolean stopped = false;
    private float trackingBarW = 0,trackingH = 10;
    private GestureDetector gestureDetector;
    private StoryView storyView;
    private Story(Activity activity) {
        this.activity = activity;
        gestureDetector = new GestureDetector(activity,new StoryGestureListener());
    }
    public static Story getInstance(Activity activity) {
        return new Story(activity);
    }
    public void addStatus(Status status) {
        statuses.add(status);
    }
    public void show() {
        if(storyView == null) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            storyView = new StoryView(activity);
            activity.addContentView(storyView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }
    public void setProfilePart(Bitmap bitmap,String title) {
        if(profile == null) {
            profile = Profile.getInstance(bitmap, title);
        }
    }
    public int getW() {
        return  w;
    }
    public StoryView getStoryView() {
        return storyView;
    }
    public void stop(int dir) {
        stopped = true;
        StoryAnimation storyAnimation = new StoryAnimation(this,dir);
        storyAnimation.start();
    }
    private class StoryView extends View{
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private int render = 0;
        public StoryView(Context context) {
            super(context);
        }
        public void onDraw(Canvas canvas) {
            if(render == 0) {
                w = canvas.getWidth();
                h = canvas.getHeight();
                if(statuses.size()>0) {
                    trackingBarW = (w)/(statuses.size());
                    trackingH = h/200;
                    for(int i=0;i<statuses.size();i++) {
                        trackingBars.add(new TrackingBar(i*trackingBarW));
                    }
                }
            }
            if(currIndex<statuses.size()) {
                Status status = statuses.get(currIndex);
                TrackingBar trackingBar = trackingBars.get(currIndex);
                status.draw(canvas,paint);
                if(!stopped) {
                    trackingBar.update(status.getTime());
                }
                if(status.shouldStop()) {
                    if(currIndex<statuses.size()-1) {
                        currIndex++;
                    }
                    else {
                        stop(-1);
                    }
                }
            }
            for(TrackingBar trackingBar:trackingBars) {
                trackingBar.draw(canvas,paint);
            }
            if(profile!=null) {
                profile.draw(canvas,paint);
            }
            render++;
            if(!stopped) {
                try {
                    Thread.sleep(100);
                    invalidate();
                } catch (Exception ex) {

                }
            }
        }
        public boolean onTouchEvent(MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(statuses.size()>0 && currIndex<=statuses.size()-1 && statuses.get(currIndex).isPaused()) {
                    statuses.get(currIndex).resume();
                    return true;
                }
            }
            return gestureDetector.onTouchEvent(event);
        }
    }
    private class TrackingBar {
        private float x,y=0,w=0,maxW;
        public TrackingBar(float x) {
            this.x = x;
            this.maxW = trackingBarW*0.95f;
        }
        public void draw(Canvas canvas,Paint paint) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor("#FAFAFA"));
            canvas.drawRoundRect(new RectF(x,y,x+w,y+trackingH),trackingH/2,trackingH/2,paint);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRoundRect(new RectF(x,y,x+maxW,y+trackingH),trackingH/2,trackingH/2,paint);
        }
        public void update(int time) {
            w = maxW*((time*1.0f)/StoryConstants.STATUS_INTERVAL);
        }
    }
    private class StoryGestureListener extends GestureDetector.SimpleOnGestureListener{
        public boolean onDown(MotionEvent event) {
            return true;
        }
        public boolean onSingleTapUp(MotionEvent event) {
            if(!stopped && statuses.size()>0) {
                if(currIndex<statuses.size()-1) {
                    TrackingBar currTrackingBar = trackingBars.get(currIndex);
                    currTrackingBar.w = currTrackingBar.maxW;
                    currIndex++;
                }
                else if(currIndex == statuses.size()-1){
                    stop(-1);

                }
            }

            return true;
        }
        public void onShowPress(MotionEvent event) {
            if(!stopped && statuses.size()>0) {
                statuses.get(currIndex).pause();
            }
        }
        public boolean onFling(MotionEvent e1,MotionEvent e2,float velx,float vely) {
            if (Math.abs(velx) > Math.abs(vely) && !stopped) {
                float diff = e2.getX()-e1.getX();
                stop((int)(diff/Math.abs(diff)));
            }
            return true;
        }
    }
}
