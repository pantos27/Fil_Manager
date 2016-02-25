package com.pantos27.www.filemanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity implements PermissionManagerFragment.PermissionCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PermissionManagerFragment pfragment=new PermissionManagerFragment();
        Bundle bundle=new Bundle();
        bundle.putStringArray(PermissionManagerFragment.KEY_PERMISSIONS,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
        pfragment.setArguments(bundle);

        FilesArray filesArray =FilesArray.getInstance();
        String[] paths=Environment.getExternalStorageDirectory().list();


    }

    @Override
    public void onPermissionGranted() {

    }

    @Override
    public void onPermissionDenied() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.create().setT
    }
}
