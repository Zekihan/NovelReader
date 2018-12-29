package com.zekihan.utilities;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zekihan.datatype.ImageItem;
import com.zekihan.novelreader.R;

import java.util.ArrayList;


public class GridViewAdapter extends ArrayAdapter<ImageItem> {

    @NonNull
    private final Context context;
    private final int layoutResourceId;
    @NonNull
    private final ArrayList<ImageItem> data;

    public GridViewAdapter(@NonNull Context context, int layoutResourceId, @NonNull ArrayList<ImageItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = row.findViewById(R.id.text);
            holder.image = row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        ImageItem item = data.get(position);
        String title = item.getTitle();

        holder.imageTitle.setText(title);
        if (holder.imageTitle.getLineCount() > 2) {
            int lineEndIndex = holder.imageTitle.getLayout().getLineEnd(1);
            String text = holder.imageTitle.getText().subSequence(0, lineEndIndex - 3) + "\u2026";
            holder.imageTitle.setText(text);
        }
        holder.image.setImageBitmap(item.getImage());
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}