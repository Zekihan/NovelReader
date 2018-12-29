package com.zekihan.novelreader.activities.chapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zekihan.novelreader.R;
import com.zekihan.novelreader.activities.chapterreader.ChapterReaderActivity;
import com.zekihan.utilities.ChaptersCustomAdapter;
import com.zekihan.utilities.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;


public class ChaptersActivity extends AppCompatActivity {

    private static final String TAG = "ChaptersActivity";

    private InterstitialAd mInterstitialAd;

    private String novelId;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        novelId = getIntent().getStringExtra("NovelId");

        recyclerView = findViewById(R.id.chList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        Button goTo = findViewById(R.id.Go);
        goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d(TAG, "The interstitial wasn't loaded yet.");
                }
                TextView ch = findViewById(R.id.goNum);
                String ss = ch.getText().toString();
                int a = Integer.parseInt(ss);
                Intent reader = new Intent(getApplicationContext(), ChapterReaderActivity.class);
                reader.putExtra("ChapterNumber", a);
                reader.putExtra("NovelId", novelId);
                startActivity(reader);
            }
        });


        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference chapterCount = mDatabase.getReference().child("novelId").child(novelId).child("chapterCount");

        readData(chapterCount, new MyCallback() {
            @Override
            public void onCallback(Integer value) {
                final List<String> chList = new ArrayList<>();
                for (int i = 1; i < value + 1; i++) {
                    chList.add("CHAPTER " + i);
                }
                recyclerView.setAdapter(new ChaptersCustomAdapter(chList));
                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent reader = new Intent(getApplicationContext(), ChapterReaderActivity.class);
                        reader.putExtra("ChapterNumber", (position + 1));
                        reader.putExtra("NovelId", novelId);
                        startActivity(reader);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }
        });

    }

    private void readData(DatabaseReference ref, @NonNull final MyCallback myCallback) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer value = dataSnapshot.getValue(Integer.class);
                myCallback.onCallback(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    interface MyCallback {
        void onCallback(Integer value);
    }

}