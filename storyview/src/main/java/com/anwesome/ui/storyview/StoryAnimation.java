package com.anwesome.ui.storyview;

import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by anweshmishra on 25/03/17.
 */
public class StoryAnimation implements ValueAnimator.AnimatorUpdateListener {
    private Story story;
    private int dir = 0;
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        if(story!=null && story.getStoryView()!=null) {
            View view = story.getStoryView();
            view.setY((float)valueAnimator.getAnimatedValue());
            view.setX((float)valueAnimator.getAnimatedValue()/2);
        }
    }
    public StoryAnimation(Story story,int dir) {
        this.story = story;
        this.dir = dir;
    }
    public void start() {
        if(story!=null) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,dir*story.getH());
            valueAnimator.setDuration(500);
            valueAnimator.addUpdateListener(this);
            valueAnimator.start();
        }
    }
}
