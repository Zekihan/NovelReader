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
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.zekihan.datatype.Novel;
import com.zekihan.novelreader.R;
import com.zekihan.novelreader.activities.description.NovelDescriptionActivity;
import com.zekihan.utilities.DatabaseHelper.FavoritesDatabaseHelper;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private static final String TAG = "Favorites";

    private Context mContext;

    public FavoritesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @SuppressWarnings("unused") Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mContext = rootView.getContext();

        AdView mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final LinearLayout ll2 = rootView.findViewById(R.id.ll3);
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        List<Novel> favorites = new FavoritesDatabaseHelper(mContext).getAllNovels();
        for (final Novel novel : favorites) {
            final Button b = new Button(mContext);
            b.setText(novel.getName());
            ll2.addView(b, lp);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent novelDescription = new Intent(mContext, NovelDescriptionActivity.class);
                    novelDescription.putExtra("Novel", novel);
                    Log.e("ErR",novel.getDescription()+"");
                    startActivity(novelDescription);
                }
            });
        }

        return rootView;
    }

}