package com.pantos27.www.filemanager;

import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by Veierovioum on 22/02/2016.
 */
public abstract class AbsFile extends File {

    Drawable icon;
    String name;

    public AbsFile(String path) {
        super(path);
    }

    public int getDrawbableID(){
        return -1;
    }
}
