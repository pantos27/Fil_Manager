package com.pantos27.www.filemanager;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Collections;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FilesArray filesArray =FilesArray.getInstance();
        String[] paths=Environment.getExternalStorageDirectory().list();

        
    }
}
