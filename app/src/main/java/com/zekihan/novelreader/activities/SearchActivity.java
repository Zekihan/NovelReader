package com.zekihan.novelreader.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zekihan.datatype.Genre;
import com.zekihan.datatype.Novel;
import com.zekihan.novelreader.R;
import com.zekihan.utilities.json.NovelJson;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Context mContext = getApplicationContext();
        List<Novel> novels = NovelJson.readNovelFile(mContext);
        Novel novel = novels.get(0);
        List<Genre> genres = novel.getGenres();
        List<Genre> genre1 = new ArrayList<>();
        genre1.add(Genre.Action);
        genre1.add(Genre.Adventure);
        genre1.add(Genre.Ecchi);
        Log.e("Search", " " + genres);

    }

    private boolean containsGenres(Novel novel, @NonNull List<Genre> genres) {
        return novel.getGenres().containsAll(genres);
    }

    @NonNull
    private List<Novel> getNovelsWithGenres(List<Novel> novels, @NonNull List<Genre> genres) {
        List<Novel> result = new ArrayList<>();
        for (Novel novel : novels) {
            if (containsGenres(novel, genres))
                result.add(novel);
        }
        return result;
    }
}
