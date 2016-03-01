package com.pantos27.www.filemanager;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * abstract class for item types in the display array
 */
public abstract class AbsFile {

    Drawable icon;
    File file;

    public AbsFile(String path) {

        this.file=new File(path);
    }

    public AbsFile(File file) {
        this.file=file;
    }

    public int getDrawbableID(){
        return -1;
    }



}
