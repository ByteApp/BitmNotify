package com.example.android.bitmnotify;


import android.app.Application;

import com.parse.Parse;

public class ParseClass extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("g9LFVJNOY8vkJ2w4vTBpOwLgtLzPIRy25aj6yxrd")
                .clientKey("jCdy5W1LMaPYbxYusPqexGRBe0LFILmVN69GIahS")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
