package com.pantos27.www.filemanager;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Veierovioum on 22/02/2016.
 */
public class FilesArray extends ArrayList<AbsFile>{
    private static FilesArray ourInstance = new FilesArray();

    public static FilesArray getInstance() {
        return ourInstance;
    }

    private FilesArray() {
    }


}
