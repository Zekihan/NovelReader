package com.zekihan.datatype;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class LastReadInfo implements Parcelable {
    public static final Creator<LastReadInfo> CREATOR = new Creator<LastReadInfo>() {
        @Override
        public LastReadInfo createFromParcel(@NonNull Parcel in) {
            return new LastReadInfo(in);
        }

        @Override
        public LastReadInfo[] newArray(int size) {
            return new LastReadInfo[size];
        }
    };
    @Nullable
    private final String novelId;
    private int chapterNum;
    private int scrollPosition;

    public LastReadInfo(String novelId, int chapterNum, int scrollPosition) {
        this.novelId = novelId;
        this.chapterNum = chapterNum;
        this.scrollPosition = scrollPosition;
    }

    private LastReadInfo(Parcel in) {
        novelId = in.readString();
        chapterNum = in.readInt();
        scrollPosition = in.readInt();
    }

    @Nullable
    public String getNovelId() {
        return novelId;
    }

    public int getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(int chapterNum) {
        this.chapterNum = chapterNum;
    }

    public int getScrollPosition() {
        return scrollPosition;
    }

    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() == LastReadInfo.class) {
            LastReadInfo other = (LastReadInfo) obj;
            return ((this.getNovelId().equals(other.getNovelId())));
        } else {
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(novelId);
        parcel.writeInt(chapterNum);
        parcel.writeInt(scrollPosition);
    }
}
