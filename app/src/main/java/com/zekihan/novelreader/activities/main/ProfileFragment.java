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
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zekihan.datatype.Novel;
import com.zekihan.novelreader.R;
import com.zekihan.novelreader.activities.sign.SignInActivity;
import com.zekihan.utilities.Utils;
import com.zekihan.utilities.json.NovelJson;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private static final String TAG = "Profile";

    private Context mContext;

    private FirebaseAuth mAuth;

    private StorageReference mStorageRef;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mContext = rootView.getContext();

        mAuth = FirebaseAuth.getInstance();
        final String userId = mAuth.getUid();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(mContext, SignInActivity.class));
        }
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mContext = rootView.getContext();

        AdView mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Button down = rootView.findViewById(R.id.download);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.downloadStorage(mStorageRef, mContext, userId, "favorites");
                Utils.downloadStorage(mStorageRef, mContext, userId, "lastRead");
                Utils.downloadStorage(mStorageRef, mContext, userId, "settings");
            }
        });
        Button up = rootView.findViewById(R.id.upload);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.uploadStorage(mStorageRef, mContext, userId, "favorites", "favorites");
                Utils.uploadStorage(mStorageRef, mContext, userId, "lastRead", "lastRead");
                Utils.uploadStorage(mStorageRef, mContext, userId, "settings", "settings");
            }
        });
        Button signOut = rootView.findViewById(R.id.signOut);
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null)
                    startActivity(new Intent(mContext, SignInActivity.class));
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NovelJson.writeFavoritesFile(mContext, new ArrayList<Novel>());
                mAuth.signOut();
            }
        });
        TextView email = rootView.findViewById(R.id.email);
        email.setText(String.format("%s%s", email.getText(), Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()));

        return rootView;
    }

}
