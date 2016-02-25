package com.pantos27.www.filemanager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * helper fragment to handel permission requests
 * the containing activity must implement PermissionCallback
 */
public class PermissionManagerFragment extends Fragment{

    private static final String TAG = "FilesManager";
    public static final String KEY_PERMISSIONS = "permissions-key";
    private String[] permissions;

    public interface PermissionCallback {
        void onPermissionGranted();
        void onPermissionDenied();
    }

    PermissionCallback callback;
    //default false
    private static boolean permissionDenied;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PermissionCallback){
            callback=(PermissionCallback) context;
            permissions=getArguments().getStringArray(KEY_PERMISSIONS);
        }
        else
        {
            Log.i(TAG,"activity must implement PermissionManagerFragment.PermissionCallback");
            throw new IllegalArgumentException("activity must implement PermissionManagerFragment.PermissionCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback=null;
    }

    public void checkPermissions(){
        if (permissions!=null && ContextCompat.checkSelfPermission(getContext(),permissions)==)
    }
}

