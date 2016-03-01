package com.pantos27.www.filemanager;

import java.io.File;

/**
 * represents a file in the display array
 */
public class FileItem extends AbsFile {

    public FileItem(File file){
        super(file);
    }

    @Override
    public int getDrawbableID() {
        return R.drawable.file;
    }
}
