package com.zekihan.utilities.json;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zekihan.datatype.Genre;
import com.zekihan.datatype.Novel;
import com.zekihan.datatype.Status;
import com.zekihan.utilities.FileInOut;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NovelJson {
    private static final String TAG = "NovelJson";

    private static JSONObject toJson(Novel novel) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", novel.getId());
            jsonObj.put("name", novel.getName());
            jsonObj.put("description", novel.getDescription());
            jsonObj.put("chapterCount", novel.getChapterCount());
            jsonObj.put("language", novel.getLanguage());
            jsonObj.put("author", novel.getAuthor());
            jsonObj.put("status", novel.getStatus());
            JSONArray genreJ = new JSONArray();
            for (Genre genre:novel.getGenres()) {
                genreJ.put(genre);
            }
            jsonObj.put("genres", genreJ);
            JSONArray tagJ = new JSONArray();
            for (String genre:novel.getTags()) {
                tagJ.put(genre);
            }
            jsonObj.put("tags", tagJ);
            return jsonObj;
        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    private static Novel toNovel(JSONObject novelJ) {
        try {
            String id = novelJ.getString("id");
            String name = novelJ.getString("name");
            String description = novelJ.getString("description");
            Integer chapterCount = novelJ.getInt("chapterCount");
            String language = novelJ.getString("language");
            String author = novelJ.getString("author");
            Status status = Status.valueOf(novelJ.getString("status"));
            JSONArray genreJ = novelJ.getJSONArray("genres");
            List<Genre> genres = new ArrayList<>();
            for (int i = 0; i < genreJ.length(); i++) {
                genres.add(Genre.valueOf(genreJ.getString(i)));
            }
            JSONArray tagJ = novelJ.getJSONArray("tags");
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < tagJ.length(); i++) {
                tags.add(tagJ.getString(i));
            }
            Novel novel = new Novel(id,name,description,chapterCount,language,author,status,genres,tags);
            System.out.println(novel.toString());
            return novel;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeMultipleNovelFile(Context context, List<Novel> novels) {

        FileInOut fio = new FileInOut(context);
        JSONArray novelsJ = new JSONArray();
        for (Novel novel : novels) {
            novelsJ.put(novel);
        }
        fio.writeFile("novels", "novels", novelsJ.toString());
    }

    @NonNull
    public static ArrayList<Novel> readNovelFile(Context context) {
        FileInOut fio = new FileInOut(context);
        String file = fio.fileRead("novels", "novels");
        ArrayList<Novel> novels = new ArrayList<>();
        try {
            if (file != null) {
                JSONArray Jarr = new JSONArray(file);
                for (int i = 0; i < Jarr.length(); i++) {
                    novels.add(toNovel(Jarr.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return novels;
    }

    public static void writeFavoritesFile(Context context, List<Novel> novels) {
        FileInOut fio = new FileInOut(context);
        JSONArray novelsJ = new JSONArray();
        for (Novel novel : novels) {
            novelsJ.put(toJson(novel));
        }
        fio.writeFile("favorites", "favorites", novelsJ.toString());
    }

    @NonNull
    public static ArrayList<Novel> readFavoritesFile(Context context) {
        FileInOut fio = new FileInOut(context);
        String file = fio.fileRead("favorites", "favorites");
        ArrayList<Novel> novels = new ArrayList<>();
        try {
            if (file != null) {
                JSONArray Jarr = new JSONArray(file);
                for (int i = 0; i < Jarr.length(); i++) {
                    Novel novel = toNovel(Jarr.getJSONObject(i));
                    novels.add(novel);
                }
            }
        } catch (Exception e){
            Log.e(TAG,e.getMessage());
            e.printStackTrace();
        }
        Log.e(TAG,novels.size()+"");
        return novels;
    }
}
