package com.example.innov8.innov8;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by horous on 25/02/16.
 */
public class ParseInit extends Application {
    public static final String YOUR_APPLICATION_ID = "HpIMDagDJpEC1NNvgiuUFLZ1vcccMlQmdkWVwzSo";
    public static final String YOUR_CLIENT_KEY = "444qjqONG6N9zMiNZhJj21CrdWCwNv1qReJiKoHk";

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);

        // ...
    }
}