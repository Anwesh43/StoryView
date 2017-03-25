package com.anwesome.ui.storyview;

import android.app.Activity;

/**
 * Created by anweshmishra on 25/03/17.
 */
public class Story {
    private Story(Activity activity) {

    }
    public static Story getInstance(Activity activity) {
        return new Story(activity);
    }
}
