package com.zekihan.utilities.json;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.zekihan.datatype.Chapter;
import com.zekihan.datatype.DownloadLog;
import com.zekihan.utilities.FileInOut;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChapterFileIO {
    private static final String TAG = "ChapterFileIO";

    public static void writeChapterFile(Context context, Chapter chapter, String novelId) {
        FileInOut fio = new FileInOut(context);
        fio.writeFile(chapter.getChNum()+".chapter", "chapters/"+novelId, chapter.getCh());
    }

    public static Chapter readChapterFile(Context context, String novelId, int chNum) {
        FileInOut fio = new FileInOut(context);
        String file = fio.fileRead(chNum+".chapter", "chapters/"+novelId);
        return new Chapter(file,chNum);
    }

    @NonNull
    public static List<DownloadLog> readDownloadLogFile(Context context) {
        FileInOut fio = new FileInOut(context);
        String file = fio.fileRead("log", "chapters");
        ArrayList<DownloadLog> downloadLogs = new ArrayList<>();
        try {
            if (file != null) {
                JSONArray Jarr = new JSONArray(file);
                for (int i = 0; i < Jarr.length(); i++) {
                    downloadLogs.add(new Gson().fromJson(Jarr.getJSONObject(i).toString(),DownloadLog.class));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return downloadLogs;

    }

    private static void writeDownloadLogFile(Context context, List<DownloadLog> downloadLogs) {
        FileInOut fio = new FileInOut(context);
        JSONArray chapterJ = new JSONArray();
        for (DownloadLog downloadLog : downloadLogs) {
            try {

                JSONArray jsonArray = new JSONArray();
                for (Integer i:downloadLog.getChList()) {
                    jsonArray.put(i);
                }
                chapterJ.put(new JSONObject().put("novelId",downloadLog.getNovelId())
                .put("chList",jsonArray));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        fio.writeFile("log", "chapters", chapterJ.toString());
    }

    public static void updateDownloadLogFile(Context context, @NonNull DownloadLog downloadLog) {
        List<DownloadLog> downloadLogs = readDownloadLogFile(context);
        boolean alreadyHave = false;
        if (downloadLogs.size() > 0) {
            for (DownloadLog downloadLog1 : downloadLogs) {
                if (downloadLog1.getNovelId().equals(downloadLog.getNovelId())) {
                    downloadLog1.getChList().addAll(downloadLog.getChList());
                    Set<Integer> set = new HashSet<>(downloadLog1.getChList());
                    List<Integer> list = new ArrayList<>(set);
                    downloadLog1.setChList(list);
                    alreadyHave = true;
                }
            }
        }
        if (!alreadyHave) {
            downloadLogs.add(downloadLog);
        }
        writeDownloadLogFile(context, downloadLogs);
    }
}
