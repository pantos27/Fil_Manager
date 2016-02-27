package com.pantos27.www.filemanager;

import android.app.Application;
import android.util.Log;

/**
 * Created by Veierovioum on 27/02/2016.
 */
public class FileManagerApplication extends Application {
    public static final String TAG="FileManager-";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: ");
    }
}
