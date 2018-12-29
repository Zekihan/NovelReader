package com.zekihan.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zekihan.datatype.DataModel;
import com.zekihan.novelreader.R;

public class DrawerItemCustomAdapter extends ArrayAdapter<DataModel> {

    @NonNull
    private final Context mContext;
    private final int layoutResourceId;
    @NonNull
    private final DataModel[] data;

    public DrawerItemCustomAdapter(@NonNull Context mContext, int layoutResourceId, @NonNull DataModel[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, View listItem, @NonNull ViewGroup parent) {

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = listItem.findViewById(R.id.textViewName);

        DataModel folder = data[position];

        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);

        return listItem;
    }
}