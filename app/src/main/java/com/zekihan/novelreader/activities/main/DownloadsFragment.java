package com.zekihan.novelreader.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zekihan.datatype.DownloadLog;
import com.zekihan.datatype.Novel;
import com.zekihan.novelreader.R;
import com.zekihan.novelreader.activities.description.OfflineNovelDescriptionActivity;
import com.zekihan.utilities.json.ChapterFileIO;
import com.zekihan.utilities.json.NovelJson;

import java.util.List;


public class DownloadsFragment extends Fragment {

    private static final String TAG = "Downloads";

    private Context mContext;

    public DownloadsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @SuppressWarnings("unused") Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_downloads, container, false);

        mContext = rootView.getContext();

        AdView mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final LinearLayout ll2 = rootView.findViewById(R.id.ll6);
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        List<Novel> novels = NovelJson.readNovelFile(mContext);
        List<DownloadLog> downloadLogs = ChapterFileIO.readDownloadLogFile(mContext);
        for (DownloadLog downloadLog : downloadLogs) {
            String novelId = downloadLog.getNovelId();
            final Novel novel = novels.get(novels.indexOf(new Novel(novelId,null,null,null,null,null,null,null,null)));
            final Button b = new Button(mContext);
            b.setText(novel.getName());
            ll2.addView(b, lp);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent novelDescription = new Intent(mContext, OfflineNovelDescriptionActivity.class);
                    novelDescription.putExtra("Novel", novel);
                    startActivity(novelDescription);
                }
            });
        }
        return rootView;

    }


}
