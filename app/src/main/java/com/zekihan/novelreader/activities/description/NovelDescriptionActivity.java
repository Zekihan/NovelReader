package com.zekihan.novelreader.activities.description;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zekihan.datatype.Novel;
import com.zekihan.datatype.Setting;
import com.zekihan.novelreader.R;
import com.zekihan.novelreader.activities.DownloadChaptersActivity;
import com.zekihan.novelreader.activities.chapters.ChaptersActivity;
import com.zekihan.utilities.json.NovelJson;

import java.util.ArrayList;

public class NovelDescriptionActivity extends AppCompatActivity {
    private static final String TAG = "NovelDescriptionActiv..";

    private final int FAV_OFF = android.R.drawable.btn_star_big_off;
    private final int FAV_ON = android.R.drawable.btn_star_big_on;
    private Context mContext;
    @Nullable
    private String novelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Setting setting = Setting.getSettingFromFile(getApplicationContext());
        final int[] punto = {setting.getPunto()};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_description);

        mContext = this;

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final Novel novel = getIntent().getParcelableExtra("Novel");
        String novelName = novel.getName();
        novelId = novel.getId();
        String novelDescription = novel.getDescription();

        TextView title = findViewById(R.id.title);
        title.setText(novelName);
        TextView description = findViewById(R.id.description);
        description.setText(novelDescription);
        description.setTextSize(punto[0]);

        Button b = findViewById(R.id.GoToChapters);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reader = new Intent(getApplicationContext(), ChaptersActivity.class);
                reader.putExtra("NovelId", novelId);
                startActivity(reader);
            }
        });
        ImageButton d = findViewById(R.id.DownloadChapters);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reader = new Intent(getApplicationContext(), DownloadChaptersActivity.class);
                reader.putExtra("NovelId", novelId);
                startActivity(reader);
            }
        });

        final ImageButton fav = findViewById(R.id.favorites);
        final boolean[] favState = {false};
        final ArrayList<Novel> favs = NovelJson.readFavoritesFile(mContext);
        Log.e(TAG,favs+"");
        final int[] i = {0};
        for (Novel novel1 : favs) {
            Log.e(TAG,novel.toString());
            if (novel1 != null) {
                if (novel1.getId().equals(novelId)) {
                    fav.setImageDrawable(getDrawable(FAV_ON));
                    Log.e(TAG,favState[0]+"");
                    favState[0] = true;
                    break;
                }
                i[0]++;
            }
        }
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favState[0]) {
                    fav.setImageDrawable(getDrawable(FAV_OFF));
                    favState[0] = false;
                    favs.remove(i[0]);
                    Log.e(TAG," of "+novel.toString());
                    NovelJson.writeFavoritesFile(mContext, favs);
                } else {
                    fav.setImageDrawable(getDrawable(FAV_ON));
                    favState[0] = true;
                    favs.add(novel);
                    Log.e(TAG," on "+favs+"");
                    NovelJson.writeFavoritesFile(mContext, favs);
                }
            }
        });

    }
}
