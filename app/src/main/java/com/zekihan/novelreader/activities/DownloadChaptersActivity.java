package com.zekihan.novelreader.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zekihan.datatype.Chapter;
import com.zekihan.datatype.DownloadLog;
import com.zekihan.novelreader.R;
import com.zekihan.utilities.json.ChapterFileIO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DownloadChaptersActivity extends AppCompatActivity {

    private static final String TAG = "DownloadChaptersActiv..";

    private CharSequence mTitle;
    private int start = 1;
    private int finish;
    private int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_chapters);

        mTitle = "Downloads";
        setupToolbar();
        setTitle(mTitle);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final Context mContext = getApplicationContext();
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final String novelId = getIntent().getStringExtra("NovelId");
        final NumberPicker chapterStart = findViewById(R.id.chapterStart);
        int min = 1;
        chapterStart.setMinValue(min);
        chapterStart.setWrapSelectorWheel(true);
        chapterStart.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                start = i1;
            }
        });
        final NumberPicker chapterFinish = findViewById(R.id.chapterFinish);
        chapterFinish.setMinValue(min);
        chapterFinish.setWrapSelectorWheel(true);
        chapterFinish.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                finish = i1;
            }
        });

        DatabaseReference chapters = mDatabase.getReference().child("novelId").child(novelId).child("chapterCount");

        chapters.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //noinspection ConstantConditions
                max = dataSnapshot.getValue(Integer.class);
                chapterStart.setMaxValue(max);
                chapterFinish.setMaxValue(max);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Button okay = findViewById(R.id.okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = start; i < finish + 1; i++) {
                    Handler h = new Handler();

                    final int finalI = i;
                    h.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            String src = "novel/" + novelId + "/chapters/";
                            String path = src + finalI + "/ch";
                            readFromDatabaseAndWriteFile(mContext, path, finalI, novelId);
                            Toast.makeText(mContext, "Chapter " + finalI + " downloaded", Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);
                }
                Toast.makeText(mContext, "All downloaded", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void readFromDatabaseAndWriteFile(final Context context, @NonNull String path, final int chNum, final String novelId) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference chapters = mDatabase.getReference(path);
        readData(chapters, new MyCallback() {
            @Override
            public void onCallback(String value) {
                List<Integer> log = new ArrayList<>();
                log.add(chNum);
                ChapterFileIO.updateDownloadLogFile(context, new DownloadLog(novelId, log));
                ChapterFileIO.writeChapterFile(context, new Chapter(value, chNum), novelId);
            }
        });

    }

    private void readData(DatabaseReference ref, @NonNull final MyCallback myCallback) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String txt = dataSnapshot.getValue(String.class);
                myCallback.onCallback(txt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        Objects.requireNonNull(getSupportActionBar()).setTitle(mTitle);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
    }

    interface MyCallback {
        void onCallback(String value);
    }
}
