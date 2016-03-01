package com.pantos27.www.filemanager;

import android.app.Application;
import android.util.Log;

/**
 * app class, mainly for the TAG reference
 */
public class FileManagerApplication extends Application {
    public static final String TAG="FileManager-";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG+"App init", "onCreate: ");
    }
}
