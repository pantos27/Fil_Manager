package com.pantos27.www.filemanager;

import java.io.File;

/**
 * Created by Veierovioum on 22/02/2016.
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
