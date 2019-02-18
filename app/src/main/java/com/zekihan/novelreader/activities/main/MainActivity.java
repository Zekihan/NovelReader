package com.zekihan.novelreader.activities.main;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
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
import com.zekihan.datatype.DataModel;
import com.zekihan.datatype.Genre;
import com.zekihan.datatype.Novel;
import com.zekihan.datatype.Status;
import com.zekihan.novelreader.R;
import com.zekihan.novelreader.activities.sign.SignInActivity;
import com.zekihan.utilities.DatabaseHelper;
import com.zekihan.utilities.Download;
import com.zekihan.utilities.DrawerItemCustomAdapter;
import com.zekihan.utilities.ImageSaver;
import com.zekihan.utilities.json.NovelJson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    @Nullable
    private FirebaseDatabase mDatabase = null;
    private Context mContext;

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Toolbar toolbar;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    private boolean fragment_flag = false;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mDatabase = FirebaseDatabase.getInstance();
        //noinspection ConstantConditions
        if (mDatabase == null) {
            try {
                mDatabase = FirebaseDatabase.getInstance();
                mDatabase.setPersistenceEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final FirebaseStorage mStorage = FirebaseStorage.getInstance();
        //new Download().downloadNovelCatalog(mDatabase,mStorage,getApplicationContext());

        db = new DatabaseHelper(this);
        downloadNovelCatalog(mDatabase,mStorage,getApplicationContext());
        List<Novel> novels = db.getAllNotes();
        Log.e(TAG,novels+"");
        if (false/*novels.size() == 0*/) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() == null) {
                startActivity(new Intent(mContext, SignInActivity.class));
            }
            Handler h = new Handler();
            h.postDelayed(new Runnable() {

                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Downloading Catalog");
                    builder.setMessage("Download");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Download().downloadNovelCatalog(mDatabase, mStorage, mContext);
                            Intent i = mContext.getPackageManager()
                                    .getLaunchIntentForPackage(mContext.getPackageName());
                            Objects.requireNonNull(i).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mContext, "Quitting the app", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }, 1000);
        }

        mTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);

        setupToolbar();

        ArrayList<DataModel> drawerItem = new ArrayList<>();
        drawerItem.add(new DataModel(R.drawable.baseline_person_white_48, "Profile"));
        drawerItem.add(new DataModel(R.drawable.baseline_favorite_white_48, "Favorites"));
        drawerItem.add(new DataModel(R.drawable.download_white, "Downloads"));
        drawerItem.add(new DataModel(R.drawable.baseline_bookmarks_white_48, "Last Read"));
        drawerItem.add(new DataModel(R.drawable.baseline_movie_white_48, "Support"));
        drawerItem.add(new DataModel(R.drawable.baseline_settings_white_48, "Settings"));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.drawer_item_row, drawerItem.toArray(new DataModel[0]));
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = findViewById(R.id.drawer_layout);
        //noinspection deprecation
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        Fragment fragment = new NovelsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        setTitle("Novels");

    }

    @Override
    public void onBackPressed() {
        if (fragment_flag) {
            Fragment fragment = new NovelsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            setTitle("Novels");
            fragment_flag = false;
        } else {
            super.onBackPressed();
        }
    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new ProfileFragment();
                break;
            case 1:
                fragment = new FavoritesFragment();
                break;
            case 2:
                fragment = new DownloadsFragment();
                break;
            case 3:
                fragment = new LastReadFragment();
                break;
            case 4:
                fragment = new SupportFragment();
                break;
            case 5:
                fragment = new SettingsFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            fragment_flag = true;
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (mTitle != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mTitle);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
    }

    private void setupDrawerToggle() {
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }
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
                            db.insertNovel(novel);
                            //NovelJson.writeMultipleNovelFile(mContext, novels);
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
