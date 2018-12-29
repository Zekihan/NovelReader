package com.zekihan.novelreader.activities.chapterreader;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zekihan.datatype.LastReadInfo;
import com.zekihan.datatype.Setting;
import com.zekihan.novelreader.R;
import com.zekihan.utilities.json.LastReadInfoJson;

import java.util.ArrayList;

public class ChapterReaderActivity extends AppCompatActivity {

    private static final String TAG = "ChapterReaderActivity";

    private TextView chapter;
    private ScrollView scrollViewChapter;
    private InterstitialAd mInterstitialAd;
    private int chapterNumber;
    private String novelId;
    private Context mContext;

    private static void readFromDatabase(@NonNull final TextView v, @NonNull String path) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference chapters = mDatabase.getReference(path);
        chapters.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txt = dataSnapshot.getValue(String.class);
                if (txt != null && txt.equals("")) {
                    v.setText(R.string.no_chapter);
                } else {
                    v.setText(txt);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DataBase", "Failed to read value.", databaseError.toException());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Setting setting = Setting.getSettingFromFile(getApplicationContext());
        final int[] punto = {setting.getPunto()};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_reader);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        novelId = getIntent().getStringExtra("NovelId");
        chapterNumber = getIntent().getIntExtra("ChapterNumber", 1);

        mContext = getApplicationContext();

        Button next = findViewById(R.id.next);
        Button next2 = findViewById(R.id.next2);
        Button previous = findViewById(R.id.previous);
        Button previous2 = findViewById(R.id.previous2);
        chapter = findViewById(R.id.chapter);
        scrollViewChapter = findViewById(R.id.scrollViewChapter);
        final TextView chapterNum = findViewById(R.id.chapterNum);
        chapterNum.setText(String.valueOf(chapterNumber));
        chapter.setTextSize(punto[0]);
        final String src = "novel/" + novelId + "/chapters/";
        String path = src + chapterNumber + "/ch";
        readFromDatabase(chapter, path);
        getScrollToPosition(mContext, novelId);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d(TAG, "The interstitial wasn't loaded yet.");
                }
                chapterNumber++;
                chapterNum.setText(String.valueOf(chapterNumber));
                String path = src + chapterNumber + "/ch";
                readFromDatabase(chapter, path);
                getScrollToPosition(mContext, novelId);
            }
        });
        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d(TAG, "The interstitial wasn't loaded yet.");
                }
                chapterNumber++;
                chapterNum.setText(String.valueOf(chapterNumber));
                String path = src + chapterNumber + "/ch";
                readFromDatabase(chapter, path);
                getScrollToPosition(mContext, novelId);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d(TAG, "The interstitial wasn't loaded yet.");
                }
                chapterNumber--;
                chapterNum.setText(String.valueOf(chapterNumber));
                String path = src + chapterNumber + "/ch";
                readFromDatabase(chapter, path);
                getScrollToPosition(mContext, novelId);
            }
        });
        previous2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d(TAG, "The interstitial wasn't loaded yet.");
                }
                chapterNumber--;
                chapterNum.setText(String.valueOf(chapterNumber));
                String path = src + chapterNumber + "/ch";
                readFromDatabase(chapter, path);
                getScrollToPosition(mContext, novelId);
            }
        });

    }

    @Override
    public void onBackPressed() {
        LastReadInfoJson.LastReadSave(mContext, novelId, chapterNumber, scrollViewChapter.getScrollY());
        super.onBackPressed();
    }

    private void getScrollToPosition(Context context, String novelId) {
        ArrayList<LastReadInfo> lastReadInfos = LastReadInfoJson.readLastReadFile(context);
        if (lastReadInfos.size() > 0) {
            for (LastReadInfo lri : lastReadInfos) {
                if (lri.getNovelId().equals(novelId)) {
                    Handler h = new Handler();

                    h.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            scrollViewChapter.scrollTo(0, 5000);
                        }
                    }, 1000);
                }
            }
        }
    }
}
