package com.zekihan.datatype;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Chapter implements Parcelable {
    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(@NonNull Parcel in) {
            return new Chapter(in);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
        }
    };
    private final int chNum;
    @Nullable
    private final String ch;

    public Chapter(String ch, int chNum) {
        this.ch = ch;
        this.chNum = chNum;
    }

    private Chapter(Parcel in) {
        chNum = in.readInt();
        ch = in.readString();
    }

    @Nullable
    public String getCh() {
        return ch;
    }

    public int getChNum() {
        return chNum;
    }

    @SuppressWarnings("unused")
    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("unused")
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(chNum);
        parcel.writeString(ch);
    }
}
