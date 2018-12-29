package com.zekihan.novelreader.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zekihan.datatype.LastReadInfo;
import com.zekihan.datatype.Novel;
import com.zekihan.novelreader.R;
import com.zekihan.novelreader.activities.chapterreader.ChapterReaderActivity;
import com.zekihan.novelreader.activities.description.NovelDescriptionActivity;
import com.zekihan.utilities.json.LastReadInfoJson;
import com.zekihan.utilities.json.NovelJson;

import java.util.ArrayList;

public class LastReadFragment extends Fragment {

    private static final String TAG = "LastRead";

    private Context mContext;

    public LastReadFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_last_read, container, false);

        AdView mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mContext = rootView.getContext();

        final LinearLayout ll1 = rootView.findViewById(R.id.ll11);
        final LinearLayout ll2 = rootView.findViewById(R.id.ll12);
        final LinearLayout ll3 = rootView.findViewById(R.id.ll13);
        final LinearLayout ll4 = rootView.findViewById(R.id.ll14);
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ArrayList<LastReadInfo> lastReadInfos = LastReadInfoJson.LastReadRead(mContext);
        for (LastReadInfo lastReadInfo : lastReadInfos) {
            final String novelId = lastReadInfo.getNovelId();
            final int chapterNumber = lastReadInfo.getChapterNum();
            TextView tt = new TextView(mContext);
            tt.setText(novelId);
            tt.setTextSize(24);
            tt.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            ll1.addView(tt, lp);
            tt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<Novel> novels = NovelJson.readNovelFile(mContext);
                    for (final Novel novel : novels) {
                        if (novel.getId().equals(novelId)) {
                            Intent novelDescription = new Intent(mContext, NovelDescriptionActivity.class);
                            novelDescription.putExtra("Novel", novel);
                            startActivity(novelDescription);
                        }
                    }
                }
            });

            TextView tc = new TextView(mContext);
            tc.setText(String.valueOf(chapterNumber));
            tc.setTextSize(24);
            tc.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ll2.addView(tc, lp);
            tc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent reader = new Intent(mContext, ChapterReaderActivity.class);
                    reader.putExtra("NovelId", novelId);
                    reader.putExtra("ChapterNumber", chapterNumber);
                    startActivity(reader);
                }
            });
            int lastChapter = 0;
            ArrayList<Novel> novels = NovelJson.readNovelFile(mContext);
            for (Novel novel : novels) {
                if (novel.getId().equals(novelId)) {
                    lastChapter = novel.getChapterCount();
                }
            }
            TextView tl = new TextView(mContext);
            tl.setText(String.valueOf(lastChapter));
            tl.setTextSize(24);
            tl.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ll4.addView(tl, lp);
            final int finalLastChapter = lastChapter;
            tl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent reader = new Intent(mContext, ChapterReaderActivity.class);
                    reader.putExtra("NovelId", novelId);
                    reader.putExtra("ChapterNumber", finalLastChapter);
                    startActivity(reader);
                }
            });
            TextView delete = new TextView(mContext);
            delete.setText("X");
            delete.setTextSize(24);
            delete.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ll3.addView(delete, lp);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "deleted");
                    ArrayList<LastReadInfo> lastReadInfos = LastReadInfoJson.readLastReadFile(mContext);
                    lastReadInfos.remove(new LastReadInfo(novelId, chapterNumber, 0));
                    LastReadInfoJson.writeLastReadFile(mContext, lastReadInfos);
                    Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                }
            });

        }

        return rootView;
    }
}
