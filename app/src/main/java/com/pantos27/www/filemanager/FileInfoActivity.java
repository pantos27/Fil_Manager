package com.pantos27.www.filemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.Date;

public class FileInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_info);

        File file=new File(getIntent().getStringExtra(MainActivity.KEY_FILEINFO));

        TextView tv= (TextView) findViewById(R.id.textViewInfoName);
        tv.setText(file.getName());

        tv= (TextView) findViewById(R.id.textViewModified);
        Date date=new Date(file.lastModified());
        tv.setText(date.toString());



        if (file.isDirectory()){
            TableRow row=(TableRow) findViewById(R.id.itemsTableRow);
            row.setVisibility(View.VISIBLE);
            tv = (TextView) findViewById(R.id.textViewItems);
            tv.setText(file.list().length+"");
        }
        else
        {
            TableRow row=(TableRow) findViewById(R.id.sizeTableRow);
            row.setVisibility(View.VISIBLE);
            tv = (TextView) findViewById(R.id.textViewSize);
            tv.setText(file.length()+"");
        }


    }
}
