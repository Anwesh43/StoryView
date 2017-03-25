package com.anwesome.demos.storyviewdemo;

import java.util.*;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anwesome.ui.storyview.Status;
import com.anwesome.ui.storyview.Story;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Story story = Story.getInstance(this);
        Status status1 = Status.newInstance(BitmapFactory.decodeResource(getResources(),R.drawable.kol),"Kings Of Leon",new Date());
        Status status2 = Status.newInstance(BitmapFactory.decodeResource(getResources(),R.drawable.gojira),"Gojira is Love",new Date());
        story.addStatus(status1);
        story.addStatus(status2);
        story.setProfilePart(BitmapFactory.decodeResource(getResources(),R.drawable.profile_img),"Anwesh");
        story.show();
    }
}
