package com.pantos27.www.filemanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

/**
 * Created by Veierovioum on 23/02/2016.
 */
public class PermissionManager extends Fragment{

    ActivityCompat activityCompat;


    final String permission;

    public PermissionManager(ActivityCompat _activity,String _permission) {
        this.permission = _permission;
        this.activityCompat = _activity;
    }

    public boolean ShouldCheckForPermission(){

        return ContextCompat.checkSelfPermission(activityCompat., Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED)
    }


}
