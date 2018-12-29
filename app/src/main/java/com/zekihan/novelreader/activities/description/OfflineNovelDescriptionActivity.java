package com.zekihan.novelreader.activities.description;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zekihan.datatype.Novel;
import com.zekihan.datatype.Setting;
import com.zekihan.novelreader.R;
import com.zekihan.novelreader.activities.chapters.OfflineChaptersActivity;

public class OfflineNovelDescriptionActivity extends AppCompatActivity {

    private static final String TAG = "OfflineNovelDescriptionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Setting setting = Setting.getSettingFromFile(getApplicationContext());
        final int[] punto = {setting.getPunto()};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_novel_description);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final Novel novel = getIntent().getParcelableExtra("Novel");
        String novelName = novel.getName();
        String novelDescription = novel.getDescription();

        TextView title = findViewById(R.id.offlineTitle);
        title.setText(novelName);
        TextView description = findViewById(R.id.offlineDescription);
        description.setTextSize(punto[0]);
        description.setText(novelDescription);
        Button goToChapters = findViewById(R.id.OfflineGoToChapters);
        goToChapters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reader = new Intent(getApplicationContext(), OfflineChaptersActivity.class);
                reader.putExtra("Novel", novel);
                startActivity(reader);
            }
        });

    }
}