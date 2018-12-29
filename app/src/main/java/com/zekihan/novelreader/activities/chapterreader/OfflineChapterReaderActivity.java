package com.zekihan.novelreader.activities.chapterreader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zekihan.datatype.Chapter;
import com.zekihan.datatype.Novel;
import com.zekihan.datatype.Setting;
import com.zekihan.novelreader.R;
import com.zekihan.utilities.json.ChapterFileIO;
import com.zekihan.utilities.json.LastReadInfoJson;

public class OfflineChapterReaderActivity extends AppCompatActivity {

    private static final String TAG = "OfflineChapterReaderActivity";

    private ScrollView scrollViewChapter;
    private int chapterNumber;
    @Nullable
    private String novelId;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Setting setting = Setting.getSettingFromFile(getApplicationContext());
        final int[] punto = {setting.getPunto()};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_chapter_reader);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Novel novel = getIntent().getParcelableExtra("Novel");
        novelId = novel.getId();
        chapterNumber = getIntent().getIntExtra("ChapterNumber", 1);

        mContext = getApplicationContext();

        Button next = findViewById(R.id.offlineNext);
        Button next2 = findViewById(R.id.offlineNext2);
        Button previous = findViewById(R.id.offlinePrevious);
        Button previous2 = findViewById(R.id.offlinePrevious2);
        final TextView chapter = findViewById(R.id.offlineChapter);
        final TextView chapterNum = findViewById(R.id.offlineChapterNum);
        scrollViewChapter = findViewById(R.id.scrollViewChapter);
        chapterNum.setText(String.valueOf(chapterNumber));
        chapter.setTextSize(punto[0]);
        Chapter chapterD = ChapterFileIO.readChapterFile(getApplicationContext(), novelId,chapterNumber);
        if (chapterD.getChNum() == chapterNumber) {
            chapter.setText(chapterD.getCh());
        }
        scrollViewChapter.scrollTo(0, 5000);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chapterNumber++;
                chapterNum.setText(String.valueOf(chapterNumber));
                Chapter chapterD = ChapterFileIO.readChapterFile(getApplicationContext(), novelId,chapterNumber);
                if (chapterD.getChNum() == chapterNumber) {
                    chapter.setText(chapterD.getCh());
                }
            }
        });
        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chapterNumber++;
                chapterNum.setText(String.valueOf(chapterNumber));
                Chapter chapterD = ChapterFileIO.readChapterFile(getApplicationContext(), novelId,chapterNumber);
                if (chapterD.getChNum() == chapterNumber) {
                    chapter.setText(chapterD.getCh());
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chapterNumber--;
                chapterNum.setText(String.valueOf(chapterNumber));
                Chapter chapterD = ChapterFileIO.readChapterFile(getApplicationContext(), novelId,chapterNumber);
                if (chapterD.getChNum() == chapterNumber) {
                    chapter.setText(chapterD.getCh());
                }
            }
        });
        previous2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chapterNumber--;
                chapterNum.setText(String.valueOf(chapterNumber));
                Chapter chapterD = ChapterFileIO.readChapterFile(getApplicationContext(), novelId,chapterNumber);
                if (chapterD.getChNum() == chapterNumber) {
                    chapter.setText(chapterD.getCh());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        LastReadInfoJson.LastReadSave(mContext, novelId, chapterNumber, scrollViewChapter.getScrollY());
        super.onBackPressed();
    }
}
