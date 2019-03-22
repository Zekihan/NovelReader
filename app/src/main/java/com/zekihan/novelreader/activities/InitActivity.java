package com.zekihan.novelreader.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zekihan.datatype.Novel;
import com.zekihan.novelreader.R;
import com.zekihan.novelreader.activities.sign.SignInActivity;
import com.zekihan.utilities.DatabaseHelper.NovelDatabaseHelper;
import com.zekihan.utilities.ImageSaver;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class InitActivity extends AppCompatActivity {


    Context mContext;
    ProgressBar pb;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        mContext = this;
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        init(getApplicationContext(),mDatabase);
        pb = findViewById(R.id.progressBar);
        pb.setMax(100);


    }

    public void init(Context context, FirebaseDatabase mDatabase){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            mDatabase.setPersistenceEnabled(true);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.putString("theme", "dark");
            editor.putString("dbUpdateVer", "1");
            editor.putInt("punt", 18);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() == null) {
                startActivity(new Intent(context, SignInActivity.class));
            }else{
                //startActivity(new Intent(getApplicationContext(), InitActivity.class));
            }
            editor.putString("userId", mAuth.getCurrentUser().getUid());
            editor.apply();
        }
        downloadNovelInfo(context);

    }

    public void downloadNovelInfo(final Context mContext) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://novelreader-72c3a.appspot.com/novels/novels_db");
        try {
            storageReference.getFile(getApplicationContext().getDatabasePath("novels_db")).
                    addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            List<Novel> novels = new NovelDatabaseHelper(getApplicationContext()).getAllNovels();
                            count = novels.size();
                            pb.setProgress(100-(((int)(75.0/count))*count));
                            finishedThenFinish(pb.getProgress());
                            for (final Novel novel : novels) {
                                downloadNovelPic(mContext,novel.getId());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void downloadNovelPic(final Context context, final String id) {
        final FirebaseStorage mStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = mStorage.getReference().child("novels").child("pics").child(id + ".jpg");
        try {
            final File localFile = File.createTempFile("ImageTemp", "jpg");
            storageReference.getFile(localFile).
                    addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap;
                            bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            new ImageSaver(context).setExternal(false).setDirectoryName("novels_pics").setFileName(id + ".jpg").save(bitmap);
                            pb.incrementProgressBy(75/count);
                            finishedThenFinish(pb.getProgress());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readData(DatabaseReference ref, @NonNull final MyCallback myCallback) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer value = dataSnapshot.getValue(Integer.class);
                myCallback.onCallback(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private interface MyCallback {
        void onCallback(Integer value);
    }
    private void finishedThenFinish(int progress){
        if (progress == 100) {
            Intent i = mContext.getPackageManager()
                    .getLaunchIntentForPackage(mContext.getPackageName());
            Objects.requireNonNull(i).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }
}
