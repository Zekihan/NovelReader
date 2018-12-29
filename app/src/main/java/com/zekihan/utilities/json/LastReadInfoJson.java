package com.zekihan.utilities.json;

import android.content.Context;
import android.support.annotation.NonNull;

import com.zekihan.datatype.LastReadInfo;
import com.zekihan.utilities.FileInOut;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LastReadInfoJson {
    private static final String TAG = "LastReadInfoJson";

    private static JSONObject toJSon(LastReadInfo lastReadInfo) {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("novelId", lastReadInfo.getNovelId());
            jsonObj.put("chNum", lastReadInfo.getChapterNum());
            jsonObj.put("scrollPosition", lastReadInfo.getScrollPosition());
            return jsonObj;
        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    private static LastReadInfo toLastReadInfo(JSONObject lastReadInfoJ) {
        try {
            String novelId = lastReadInfoJ.getString("novelId");
            int chNum = lastReadInfoJ.getInt("chNum");
            int scrollPosition = lastReadInfoJ.getInt("scrollPosition");
            return new LastReadInfo(novelId,chNum,scrollPosition);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    public static ArrayList<LastReadInfo> readLastReadFile(Context context) {
        FileInOut fio = new FileInOut(context);
        String file = fio.fileRead("lastRead", "lastRead");
        ArrayList<LastReadInfo> lastReadInfos = new ArrayList<>();
        try {
            if (file != null) {
                JSONArray Jarr = new JSONArray(file);
                for (int i = 0; i < Jarr.length(); i++) {
                    lastReadInfos.add(toLastReadInfo(Jarr.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lastReadInfos;

    }

    public static void writeLastReadFile(Context context, List<LastReadInfo> lastReadInfos) {
        FileInOut fio = new FileInOut(context);
        JSONArray lastReadInfoJ = new JSONArray();
        for (LastReadInfo lastReadInfo : lastReadInfos) {
            lastReadInfoJ.put(toJSon(lastReadInfo));
        }
        fio.writeFile("lastRead", "lastRead", lastReadInfoJ.toString());
    }

    private static void updateLastReadFile(Context context, @NonNull LastReadInfo lastReadInfo) {
        ArrayList<LastReadInfo> lastReadInfos = readLastReadFile(context);
        boolean alreadyHave = false;
        if (lastReadInfos.size() > 0) {
            for (LastReadInfo lri : lastReadInfos) {
                if (lri.getNovelId().equals(lastReadInfo.getNovelId())) {
                    lri.setChapterNum(lastReadInfo.getChapterNum());
                    lri.setScrollPosition(lastReadInfo.getScrollPosition());
                    alreadyHave = true;
                }
            }
        }
        if (!alreadyHave) {
            lastReadInfos.add(lastReadInfo);
        }
        writeLastReadFile(context, lastReadInfos);
    }

    public static void LastReadSave(Context context, String novelId, int chapterNumber, int scrollPosition) {
        updateLastReadFile(context, new LastReadInfo(novelId, chapterNumber, scrollPosition));
    }

    @NonNull
    public static ArrayList<LastReadInfo> LastReadRead(Context context) {
        return readLastReadFile(context);
    }
}
