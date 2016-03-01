package com.pantos27.www.filemanager;

import java.io.File;

/**
 * represents an empty folder with no files
 */
public class EmptyFolder extends AbsFile {


    public EmptyFolder(File file) {
        super(file);
    }

    @Override
    public int getDrawbableID() {
        return R.drawable.folder_empty;
    }
}
