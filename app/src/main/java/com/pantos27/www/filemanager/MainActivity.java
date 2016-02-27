package com.pantos27.www.filemanager;

import android.Manifest;
import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.Stack;


public class MainActivity extends AppCompatActivity implements PermissionManagerFragment.PermissionCallback {

    private static final String TAG = FileManagerApplication.TAG+"MainAct";
    ListView listView;
    FilesArrayAdapter adapter;
    private FilesArray absFilesArray;
    private String permissionFragmentTag="";
    Stack<File> backStack;
    PermissionManagerFragment writePermissionFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            // TODO: 27/02/2016 get state
        }

        writePermissionFragment= (PermissionManagerFragment) getSupportFragmentManager().findFragmentByTag(permissionFragmentTag);
        if (writePermissionFragment==null) {
            Log.d(TAG, "onCreate: new permission fragment");
            writePermissionFragment=addFragment();
        }

        listView= (ListView) findViewById(R.id.listView);

        Log.d(TAG, "onCreate: end");
    }

    private PermissionManagerFragment addFragment() {
        PermissionManagerFragment fragment = new PermissionManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(PermissionManagerFragment.KEY_PERMISSIONS,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(fragment, permissionFragmentTag).commit();
        return fragment;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");

        Log.d(TAG, "onResume: checking for permission");
        writePermissionFragment.checkPermissions();
    }

    @Override
    public void onPermissionGranted() {
        Log.d(TAG, "onPermissionGranted: ");
        absFilesArray=new FilesArray();
        adapter=new FilesArrayAdapter(this,R.layout.files_list_item,absFilesArray);
        listView.setAdapter(adapter);

        File rootPath=Environment.getExternalStorageDirectory();
        if (isReadable(rootPath))
            populateFilesList(rootPath);
        else{
            Toast.makeText(MainActivity.this, "Storage un available", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onPermissionGranted: storage unavailable");
        }

    }

    @Override
    public void onPermissionDenied() {
        Log.d(TAG, "onPermissionDenied: ");
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        // TODO: 27/02/2016 add alert
    }

    void populateFilesList(File folder){
        Log.d(TAG, "populateFilesList: "+folder.getPath());
        FilesGetter filesGetter=new FilesGetter(){
            @Override
            protected void onPostExecute(FilesArray absFiles) {
                Log.d(TAG, "onPostExecute: filegetter");
                absFilesArray =absFiles;
                adapter.clear();
                adapter.addAll(absFiles);
                adapter.notifyDataSetChanged();
            }
        };

        filesGetter.execute(folder);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
    }

    private static boolean isReadable(File file){
        return EnvironmentCompat.getStorageState(file)
                .equals(Environment.MEDIA_MOUNTED_READ_ONLY)| isWrite();
    }

    private static boolean isWrite(){
        return EnvironmentCompat.getStorageState(Environment.getExternalStorageDirectory()).equals(Environment.MEDIA_MOUNTED);
    }
}
