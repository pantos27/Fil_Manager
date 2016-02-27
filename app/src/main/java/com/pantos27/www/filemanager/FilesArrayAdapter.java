package com.pantos27.www.filemanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Veierovioum on 26/02/2016.
 */
public class FilesArrayAdapter extends ArrayAdapter<AbsFile> implements View.OnClickListener {

    private static final String TAG = FileManagerApplication.TAG + "F.A.Adapter";

    public FilesArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public FilesArrayAdapter(Context context, int resource, List<AbsFile> objects) {
        super(context, resource, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            Log.d(TAG, "getView: create line");
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.files_list_item,null);
            //hold the views
            ViewHolder vh=new ViewHolder();
            vh.fileIcon= (ImageView) convertView.findViewById(R.id.fileIcon);
            vh.txtFileName= (TextView) convertView.findViewById(R.id.txtFileName);
            convertView.setTag(vh);

            convertView.setOnClickListener(this);

        }

        ViewHolder vh= (ViewHolder) convertView.getTag();
        Log.d(TAG, "getView: "+getItem(position).file.getName());
        vh.txtFileName.setText(getItem(position).file.getName());
        vh.fileIcon.setImageResource(getItem(position).getDrawbableID());

        return convertView;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
    }

    private class ViewHolder{
        TextView txtFileName;
        ImageView fileIcon;

    }
}
