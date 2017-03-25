package com.anwesome.ui.storyview;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
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
    private int w,h;
    private int currIndex = 0;
    private boolean stopped = false;
    private float trackingBarW = 0,trackingH = 10;
    private StoryView storyView;
    private Story(Activity activity) {
        this.activity = activity;
    }
    public static Story getInstance(Activity activity) {
        return new Story(activity);
    }
    public void addStatus(Status status) {
        statuses.add(status);
    }
    public void show() {
        if(storyView == null) {
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
    private class StoryView extends View{
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private int render = 0;
        private List<TrackingBar> trackingBars = new ArrayList<>();
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
                        stopped = true;
                        StoryAnimation storyAnimation = new StoryAnimation(Story.this,-1);
                        storyAnimation.start();
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
}
