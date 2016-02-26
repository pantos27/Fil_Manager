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
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.List;


public class MainActivity extends AppCompatActivity implements PermissionManagerFragment.PermissionCallback {

    ListView listView;
    FilesArrayAdapter adapter;
    private List<AbsFile> files;
    private String permissionFragmentTag="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionManagerFragment writePermissionFragment= (PermissionManagerFragment) getSupportFragmentManager().findFragmentByTag(permissionFragmentTag);
        if (writePermissionFragment==null) {
            addFragment();
        }
        writePermissionFragment.checkPermissions();


        listView= (ListView) findViewById(R.id.listView);
        adapter=new FilesArrayAdapter(this,R.layout.files_list_item,files);
        listView.setAdapter(adapter);

    }

    private void addFragment() {
        PermissionManagerFragment writePermissionFragment;
        writePermissionFragment = new PermissionManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(PermissionManagerFragment.KEY_PERMISSIONS,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
        writePermissionFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(writePermissionFragment,permissionFragmentTag).commit();
    }

    @Override
    public void onPermissionGranted() {
        String[] paths=Environment.getExternalStorageDirectory().list();
        // TODO: 26/02/2016 add get file method
    }

    @Override
    public void onPermissionDenied() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

    }
}
