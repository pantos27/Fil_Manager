package com.pantos27.www.filemanager;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    PermissionManagerFragment writePermissionFragment;
    String rootPath ="";
    LinearLayout pathLayout;
    private HorizontalScrollView scrollView;
    /***
     * string stack to contain the user's navigation moves
     */
    Stack<String> backStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState==null)
            backStack=new Stack<>();
        //checks if there's alread an instance of permission fragment attached
        writePermissionFragment= (PermissionManagerFragment) getSupportFragmentManager().findFragmentByTag(permissionFragmentTag);
        if (writePermissionFragment==null) {
            //if not, create & attach one
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
            /***
             * sets a click event for the items in the main files/folders display list
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = adapter.getItem(position).file;

                if (file.isDirectory()) {
                    //if directory, step inside
                    populateFilesList(file);
                } else {
                    Log.d(TAG, "onItemClick: opening file: " + file.getName());
                    //if it's a file, try to open
                    if (!openFile(file))
                        Toast.makeText(MainActivity.this, "No app was found for this type of file.", Toast.LENGTH_LONG).show();


                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemLongClick: ");
                //start file info activity on long press
                Intent intent=new Intent(MainActivity.this,FileInfoActivity.class);
                intent.putExtra(KEY_FILEINFO,adapter.getItem(position).file.getPath());
                startActivity(intent);
                return true;
            }
        });

    }

    /***
     * try to open a file by sending it to the relevant app
     * @param file
     * @return success/failure
     */
    private boolean openFile(File file) {
        String[] arr=file.getName().split("\\.");
        if (arr.length<1) return false;
        //get file type
        MimeTypeMap mMime = MimeTypeMap.getSingleton();
        String mimeType= mMime.getMimeTypeFromExtension(arr[arr.length - 1]);
        Log.d(TAG, "onItemClick: mime type: " + mimeType);
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        newIntent.setDataAndType(Uri.fromFile(file), mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            MainActivity.this.startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            return false;
        }
        return true;
    }

    /***
     *
     * @return the new fragment
     */
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
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: backstack="+backStack.size());
        //if back from state change, resume @ last folder
        if (!backStack.empty())
            populateFilesList(new File(backStack.pop()));
        else
        //if new instance request permission
        writePermissionFragment.checkPermissions();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: ");
        //restore saved items
        String[] sArray=savedInstanceState.getStringArray(KEY_BACKSTACK);
        backStack=new Stack<String>();
        Collections.addAll(backStack, sArray);
        rootPath=savedInstanceState.getString(KEY_ROOTPATH);
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
        //show alert message
        Toast.makeText(MainActivity.this, R.string.permission_denied_msg, Toast.LENGTH_LONG).show();
    }

    void populateFilesList(File folder){
        Log.d(TAG, "populateFilesList: "+folder.getPath());
        //update path display
        populatePathTrace(folder.getPath());
        //send for file search async
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
        //update backstack
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
        if (!backStack.isEmpty())
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

    public void onHomeButtonClick(View view){
        //go home
        backStack.clear();
        populateFilesList(new File(rootPath));

    }

    /***
     * handel the update of the path buttons display on the top of the screen
     * @param path
     */
    void populatePathTrace(String path){
        //get realtive path of the current position
        String relativePath=path.substring(rootPath.length());

        Log.d(TAG, "populatePathTrace relative path:" + relativePath);
        //split by sub folders
        String[] folders=relativePath.split("/", 0);
        Log.d(TAG, "populatePathTrace: folders number " + folders.length);
        //reset path display
        pathLayout.removeAllViews();
        StringBuilder builder=new StringBuilder();
        //iterate through folders and add buttons
        for (String folder : folders) {
            Log.d(TAG, "populatePathTrace: folder " + folder);
            if(folder.isEmpty())continue;

            Button button= (Button) getLayoutInflater().inflate(R.layout.path_button, pathLayout, false);
            button.setText(folder);
            builder.append("/" + folder);
            //save the target path in the button
            button.setTag(rootPath + builder);
            pathLayout.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onPath Click: " + v.getTag().toString());
                    populateFilesList(new File(v.getTag().toString()));
                }
            });
            //scroll to the last folder button
            // not working so great, any ideas why?
            scrollView.fullScroll(View.FOCUS_RIGHT);
        }

    }
}
