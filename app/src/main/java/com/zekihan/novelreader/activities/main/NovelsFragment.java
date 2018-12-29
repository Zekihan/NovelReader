package com.zekihan.novelreader.activities.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zekihan.datatype.ImageItem;
import com.zekihan.datatype.Novel;
import com.zekihan.novelreader.R;
import com.zekihan.novelreader.activities.description.NovelDescriptionActivity;
import com.zekihan.utilities.GridViewAdapter;
import com.zekihan.utilities.ImageSaver;
import com.zekihan.utilities.json.NovelJson;

import java.util.ArrayList;

public class NovelsFragment extends Fragment {

    private static final String TAG = "NovelsFragment";

    private Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_novels, container, false);

        mContext = rootView.getContext();

        GridView gd = rootView.findViewById(R.id.novels);
        gd.setNumColumns(3);

        ArrayList<Novel> novels = NovelJson.readNovelFile(mContext);
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        ImageSaver is = new ImageSaver(mContext);
        for (final Novel novel : novels) {
            Bitmap bitmap = is.setExternal(false).setDirectoryName("novels_pics").setFileName(novel.getId() + ".jpg").load();
            imageItems.add(new ImageItem(bitmap, novel.getName(), novel));

        }
        GridViewAdapter gridAdapter = new GridViewAdapter(mContext, R.layout.novel_item, imageItems);
        gd.setAdapter(gridAdapter);
        gd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull AdapterView<?> parent, View view, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                Novel novel = item.getNovel();
                Intent novelDescription = new Intent(mContext, NovelDescriptionActivity.class);
                novelDescription.putExtra("Novel", novel);
                startActivity(novelDescription);
            }
        });
        return rootView;

    }
}