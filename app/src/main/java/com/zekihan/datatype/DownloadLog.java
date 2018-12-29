package com.zekihan.datatype;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

@SuppressWarnings("ALL")
public class DownloadLog implements Parcelable {
    @Nullable
    private final String novelId;
    private List<Integer> chList;

    public DownloadLog(String novelId, List<Integer> chList) {
        this.novelId = novelId;
        this.chList = chList;
    }

    private DownloadLog(Parcel in) {
        novelId = in.readString();
        int[] v = in.createIntArray();
        if(v != null){
            for (int i :v) {
                chList.add(i);
            }
        }
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(novelId);
        int[] v = new int[chList.size()];
        for (int i = 0; i < v.length; i++) {
            v[i]=chList.get(i);
        }
        dest.writeIntArray(v);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DownloadLog> CREATOR = new Creator<DownloadLog>() {
        @Override
        public DownloadLog createFromParcel(@NonNull Parcel in) {
            return new DownloadLog(in);
        }

        @Override
        public DownloadLog[] newArray(int size) {
            return new DownloadLog[size];
        }
    };

    @Nullable
    public String getNovelId() {
        return novelId;
    }

    public List<Integer> getChList() {
        return chList;
    }

    public void setChList(List<Integer> chList) {
        this.chList = chList;
    }
}
