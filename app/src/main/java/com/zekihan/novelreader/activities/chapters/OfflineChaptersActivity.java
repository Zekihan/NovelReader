package com.zekihan.novelreader.activities.chapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zekihan.datatype.DownloadLog;
import com.zekihan.datatype.Novel;
import com.zekihan.novelreader.R;
import com.zekihan.novelreader.activities.chapterreader.OfflineChapterReaderActivity;
import com.zekihan.utilities.ChaptersCustomAdapter;
import com.zekihan.utilities.json.ChapterFileIO;
import com.zekihan.utilities.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class OfflineChaptersActivity extends AppCompatActivity {

    private static final String TAG = "OfflineChaptersActivity";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_chapters);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mContext = this;

        final Novel novel = getIntent().getParcelableExtra("Novel");
        String novelId = novel.getId();

        RecyclerView recyclerView = findViewById(R.id.chList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        Button goTo = findViewById(R.id.OfflineGo);
        goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView ch = findViewById(R.id.offlineGoNum);
                String ss = ch.getText().toString();
                int a = Integer.parseInt(ss);
                Intent reader = new Intent(getApplicationContext(), OfflineChapterReaderActivity.class);
                reader.putExtra("ChapterNumber", a);
                reader.putExtra("Novel", novel);
                startActivity(reader);
            }
        });

        List<DownloadLog> downloadLogs = ChapterFileIO.readDownloadLogFile(getApplicationContext());
        final List<String> chList = new ArrayList<>();
        for (DownloadLog downloadLog : downloadLogs) {
            if (downloadLog.getNovelId().equals(novelId)){
                List<Integer> cc = downloadLog.getChList();
                for (Integer i:cc) {
                    chList.add("CHAPTER " +i);
                }
            }
        }
        recyclerView.setAdapter(new ChaptersCustomAdapter(chList));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String chapterName = chList.get(position);
                Intent reader = new Intent(mContext, OfflineChapterReaderActivity.class);
                reader.putExtra("Novel", novel);
                String chapterNumberString = chapterName.subSequence(8, chapterName.length()).toString();
                int chapterNumber = Integer.parseInt(chapterNumberString);
                reader.putExtra("ChapterNumber", chapterNumber);
                startActivity(reader);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}