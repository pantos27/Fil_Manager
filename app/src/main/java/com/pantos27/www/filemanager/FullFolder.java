package com.pantos27.www.filemanager;

import java.io.File;

/**
 * represents a folder with files in the display array
 */
public class FullFolder extends AbsFile {


    public FullFolder(File file) {
        super(file);
    }

    @Override
    public int getDrawbableID() {
        return R.drawable.folder_full;
    }
}
