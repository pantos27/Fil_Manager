package com.pantos27.www.filemanager;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Toast;

import java.io.File;
import java.util.Collections;
import java.util.Stack;


public class MainActivity extends AppCompatActivity implements PermissionManagerFragment.PermissionCallback {

    private static final String TAG = FileManagerApplication.TAG+"MainAct";
    private static final String KEY_BACKSTACK = "backstack key";
    public static final String KEY_FILEINFO = "file info key";
    private static final String KEY_ROOTPATH = "root path key";
    ListView listView;
    FilesArrayAdapter adapter;
    private FilesArray absFilesArray;
    private String permissionFragmentTag="";
    Stack<String> backStack;
    PermissionManagerFragment writePermissionFragment;
    String rootPath ="";
    LinearLayout pathLayout;
    private HorizontalScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            String[] sArray=savedInstanceState.getStringArray(KEY_BACKSTACK);
            backStack=new Stack<String>();
            Collections.addAll(backStack, sArray);
            rootPath=savedInstanceState.getString(KEY_ROOTPATH);
        }
        else
        {
            backStack=new Stack<>();
        }

        writePermissionFragment= (PermissionManagerFragment) getSupportFragmentManager().findFragmentByTag(permissionFragmentTag);
        if (writePermissionFragment==null) {
            Log.d(TAG, "onCreate: new permission fragment");
            writePermissionFragment=addFragment();
        }

        listView= (ListView) findViewById(R.id.listView);
        absFilesArray=new FilesArray();
        adapter=new FilesArrayAdapter(this,R.layout.files_list_item,absFilesArray);
        listView.setAdapter(adapter);

        pathLayout=(LinearLayout) findViewById(R.id.pathView);
        scrollView=(HorizontalScrollView) findViewById(R.id.scrollView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file=adapter.getItem(position).file;

                if (file.isDirectory()){
                    populateFilesList(file);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "open file "+file.getName(), Toast.LENGTH_SHORT).show();
                    // TODO: 27/02/2016 open file
//                    MimeTypeMap myMime = MimeTypeMap.getSingleton();
//                    Intent newIntent = new Intent(Intent.ACTION_VIEW);
//                    String mimeType = myMime.getMimeTypeFromExtension(fileExt(getFile()).substring(1));
//                    newIntent.setDataAndType(Uri.fromFile(getFile()),mimeType);
//                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    try {
//                        context.startActivity(newIntent);
//                    } catch (ActivityNotFoundException e) {
//                        Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
//                    }
//
//                    File file = new File("EXAMPLE.JPG");
//
//// Just example, you should parse file name for extension
//                    String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(".JPG");
//
//                    Intent intent = new Intent();
//                    intent.setAction(android.content.Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile(file), mime);
//                    startActivityForResult(intent, 10);


                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 27/02/2016 show info
                Log.d(TAG, "onItemLongClick: ");
                Intent intent=new Intent(MainActivity.this,FileInfoActivity.class);
                intent.putExtra(KEY_FILEINFO,adapter.getItem(position).file.getPath());
                startActivity(intent);
                return true;
            }
        });

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
        //if back from state change, resume @ last folder
        if (!backStack.empty())
            populateFilesList(new File(backStack.pop()));
        else
        writePermissionFragment.checkPermissions();
    }

    @Override
    public void onPermissionGranted() {
        Log.d(TAG, "onPermissionGranted: ");

        File rootFolder=Environment.getExternalStorageDirectory();
        rootPath=rootFolder.getPath();

        if (isReadable(rootFolder))
            populateFilesList(rootFolder);
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
        populatePathTrace(folder.getPath());

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

        backStack.add(folder.getPath());
        filesGetter.execute(folder);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
        //save the backstack
        String[] sArray=new String[backStack.size()];
        outState.putStringArray(KEY_BACKSTACK, backStack.toArray(sArray));
        outState.putString(KEY_ROOTPATH,rootPath);
    }

    private static boolean isReadable(File file){
        return EnvironmentCompat.getStorageState(file)
                .equals(Environment.MEDIA_MOUNTED_READ_ONLY)| isWrite();
    }

    private static boolean isWrite(){
        return EnvironmentCompat.getStorageState(Environment.getExternalStorageDirectory()).equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: at "+backStack.pop());
        //if in root dir exit. else go back one folder
        if (backStack.empty())
            super.onBackPressed();
        else
        {
            Log.d(TAG, "onBackPressed: "+backStack.peek());
            populateFilesList(new File(backStack.pop()));
        }
    }

    public void onPathButtonClick(View view){

        if (view.getId()==R.id.rootButton){
            //go home
            backStack.clear();
            populateFilesList(new File(rootPath));
        }
        else
        {
            //// TODO: 29/02/2016 handle click
        }

    }

    void populatePathTrace(String path){

        String relativePath=path.substring(rootPath.length());

        Log.d(TAG, "populatePathTrace relative path:" + relativePath);

        String[] folders=relativePath.split("/", 0);
        Log.d(TAG, "populatePathTrace: folders number " + folders.length);
        pathLayout.removeAllViews();
        StringBuilder builder=new StringBuilder();
        for (String folder : folders) {
            Log.d(TAG, "populatePathTrace: folder " + folder);
            if(folder.isEmpty())continue;

            Button button= (Button) getLayoutInflater().inflate(R.layout.path_button, pathLayout, false);
            button.setText(folder);
            builder.append("/" + folder);
            button.setTag(rootPath + builder);
            pathLayout.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onPath Click: " + v.getTag().toString());
                    populateFilesList(new File(v.getTag().toString()));
                }
            });
            scrollView.fullScroll(View.FOCUS_RIGHT);
        }

    }
}
