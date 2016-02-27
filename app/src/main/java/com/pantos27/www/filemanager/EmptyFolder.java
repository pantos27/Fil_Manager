package com.pantos27.www.filemanager;

import java.io.File;

/**
 * Created by Veierovioum on 22/02/2016.
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
