package com.logreader;

import android.app.Application;

import com.bugfender.sdk.Bugfender;

import logreader.com.estuate.RLLogReader;

public class MainApplication extends Application {

    String parse;

    @Override
    public void onCreate() {
        super.onCreate();
        new RLLogReader().createLogReader(getApplicationContext(),  "parse");
     //   Bugfender.init(this, "lrYLnh9cSLnZBrrma2zW295uI9AJeUvi", BuildConfig.DEBUG);
     //   Bugfender.enableLogcatLogging();
     //   Bugfender.enableUIEventLogging(this);
    }
}
