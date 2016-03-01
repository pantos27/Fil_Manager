package com.pantos27.www.filemanager;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * helper fragment to handel permission requests
 * the containing activity must implement PermissionCallback
 * can pass one or more permission to check for the required activity, through the fragment arguments
 */
public class PermissionManagerFragment extends Fragment{

    private static final String TAG = FileManagerApplication.TAG+"P.M.Frag";
    public static final String KEY_PERMISSIONS = "permissions-key";
    private String[] permissions;

    /***
     * callback object for handling permission granted/denied events
     */
    public interface PermissionCallback {
        /***
         *
         */
        void onPermissionGranted();
        void onPermissionDenied();
    }

    PermissionCallback callback;
    //default false
    private static boolean permissionDenied;
    private final int fRequestCode =new Random().nextInt(127);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach: ");
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
        //check all the missing permissions
        ArrayList<String> toRequest=new ArrayList<>();
        for (String permission : permissions) {
            if (permissions!=null && ContextCompat.checkSelfPermission(getContext(),permission)==PackageManager.PERMISSION_DENIED)
                toRequest.add(permission);
        }

        if (toRequest.size()>0)
        {
            //missing permissions
            Log.d(TAG, "checkPermissions: request permissions " + toRequest.toString());
            String[] arr=new String[toRequest.size()];
            requestPermissions(toRequest.toArray(arr),fRequestCode);
        }
        else
        {
            //all good
            if (callback != null) {
                callback.onPermissionGranted();
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode==fRequestCode){
            for (int result : grantResults) {
                if (result==PackageManager.PERMISSION_DENIED)
                {
                    if (callback != null) {
                        callback.onPermissionDenied();
                    }
                    return;
                }
            }
            if (callback != null) {
                callback.onPermissionGranted();
            }
        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

