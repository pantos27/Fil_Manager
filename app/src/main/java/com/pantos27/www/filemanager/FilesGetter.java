package com.pantos27.www.filemanager;

import android.os.AsyncTask;

import java.io.File;
import java.net.URI;

/**
 * Created by Veierovioum on 27/02/2016.
 */
public class FilesGetter extends AsyncTask<File,Void,FilesArray> {
    @Override
    protected FilesArray doInBackground(File... params) {
        File[] files=params[0].listFiles();
        FilesArray filesArray=new FilesArray();

        for (File file : files) {
            filesArray.add(resolveFileType(file));
        }

        return filesArray;
    }
    //determines which item type
    AbsFile resolveFileType(File file)
    {
        if (file.isDirectory())
        {
            if (file.list().length>0)
                return new FullFolder(file);
            else return new EmptyFolder(file);
        }
        return new FileItem(file);
    }


}
