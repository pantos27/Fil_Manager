package com.pantos27.www.filemanager;

/**
 * Created by Veierovioum on 22/02/2016.
 */
public class EmptyFolder extends AbsFile {

    public EmptyFolder(String path) {
        super(path);
    }

    @Override
    public int getDrawbableID() {
        return R.drawable.folder_empty;
    }
}
