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
    private int currIndex = 0;
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
    private class StoryView extends View{
        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private int w,h;
        public StoryView(Context context) {
            super(context);
        }
        public void onDraw(Canvas canvas) {
            if(currIndex<statuses.size()) {
                Status status = statuses.get(currIndex);
                status.draw(canvas,paint);
                if(status.shouldStop()) {
                    currIndex++;
                }
            }
            if(profile!=null) {
                profile.draw(canvas,paint);
            }
            try {
                Thread.sleep(100);
                invalidate();
            }
            catch (Exception ex) {

            }
        }
    }
}
