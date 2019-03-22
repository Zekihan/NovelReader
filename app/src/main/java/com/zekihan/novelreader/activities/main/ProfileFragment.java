package com.zekihan.novelreader.activities.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.zekihan.novelreader.R;
import com.zekihan.novelreader.activities.sign.SignInActivity;
import com.zekihan.utilities.FileInOut;

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

                mStorageRef.child("users/" + userId + "/" + "favorites_db").getFile(mContext.getDatabasePath("favorites_db"));

                mStorageRef.child("users/" + userId + "/" + "last_read_db").getFile(mContext.getDatabasePath("last_read_db"));

                FileInOut fio = new FileInOut(mContext);
                fio.setFileName("sett");
                fio.setDirectoryName("");
                mStorageRef.child("users/" + userId + "/" + "settings").getFile(fio.createFile());
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("theme", fio.fileRead("sett","").split(",")[0]);
                editor.putInt("punt", Integer.parseInt(fio.fileRead("sett","").split(",")[1]));
                editor.apply();
                fio.createFile().delete();
            }
        });
        Button up = rootView.findViewById(R.id.upload);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri favorites_db = Uri.fromFile(mContext.getDatabasePath("favorites_db"));
                mStorageRef.child("users/" + userId + "/" + "favorites_db").putFile(favorites_db);

                Uri last_read_db = Uri.fromFile(mContext.getDatabasePath("last_read_db"));
                mStorageRef.child("users/" + userId + "/" + "last_read_db").putFile(last_read_db);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                FileInOut fio = new FileInOut(mContext);
                fio.writeFile("sett","",prefs.getString("theme","dark")+","+prefs.getInt("punt", 18));
                fio.setFileName("sett");
                fio.setDirectoryName("");
                Uri settings = Uri.fromFile(fio.createFile());
                mStorageRef.child("users/" + userId + "/" + "settings").putFile(settings);
                fio.createFile().delete();

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
                mAuth.signOut();
            }
        });
        TextView email = rootView.findViewById(R.id.email);
        email.setText(String.format("%s%s", email.getText(), Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()));

        return rootView;
    }
}
