package com.zekihan.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zekihan.datatype.Genre;
import com.zekihan.datatype.Novel;
import com.zekihan.datatype.Status;
import com.zekihan.utilities.DatabaseHelper.NovelDatabaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class Download {
    public void downloadNovelCatalog(@NonNull final FirebaseDatabase mDatabase, @NonNull final FirebaseStorage mStorage, final Context mContext) {
        final DatabaseReference novels = mDatabase.getReference().child("novelId");
        novels.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<Novel> novels = new ArrayList<>();
                Log.e("Downloads", "err : 1" + novels);
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.e("Downloads", "err : " + ds.getKey());
                    final String name = ds.child("name").getValue(String.class);
                    final String id = ds.child("id").getValue(String.class);
                    final String description = ds.child("description").getValue(String.class);
                    final String language = ds.child("language").getValue(String.class);
                    final String author = ds.child("author").getValue(String.class);
                    final Status status = ds.child("status").getValue(Status.class);

                    final List<Genre> genres = new ArrayList<>();
                    for (final DataSnapshot genre : ds.child("genres").getChildren()) {
                        genres.add(Genre.valueOf(Objects.requireNonNull(genre.getValue(String.class)).replace(" ", "_").replace("-", "")));
                    }
                    final List<String> tags = new ArrayList<>();
                    for (final DataSnapshot tag : ds.child("tags").getChildren()) {
                        try {
                            tags.add(tag.getValue(String.class));
                        } catch (IllegalArgumentException e) {
                            Log.e("Downloads", e.getMessage());
                        }
                    }
                    @SuppressWarnings("ConstantConditions")
                    DatabaseReference chapterCount = mDatabase.getReference().child("novelId").child(id).child("chapterCount");
                    Log.e("Downloads", "err : " + new Novel(id, name, description, 0, language, author, status, genres, tags));
                    readData(chapterCount, new MyCallback() {
                        @Override
                        public void onCallback(Integer value) {
                            Log.e("Downloads", "err : " + id + " " + value);
                            Novel novel = new Novel(id, name, description, value, language, author, status, genres, tags);
                            novels.add(novel);
                            new NovelDatabaseHelper(mContext).insertNovel(novel);
                        }
                    });
                    StorageReference storageReference = mStorage.getReference().child("novels").child("pics").child(id + ".jpg");
                    try {
                        final File localFile = File.createTempFile("ImageTemp", "jpg");
                        storageReference.getFile(localFile).
                                addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Log.e("Storage", " " + id);
                                        Bitmap bitmap;
                                        bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        Log.e("Storage", bitmap + "");
                                        new ImageSaver(mContext).setExternal(false).setDirectoryName("novels_pics").setFileName(id + ".jpg").save(bitmap);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DataBase", "Failed to read value.", databaseError.toException());
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
            }
        });
    }

    interface MyCallback {
        void onCallback(Integer value);
    }
}

