package com.zekihan.datatype;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zekihan.utilities.Utils;

public class Setting implements Parcelable {
    public static final Creator<Setting> CREATOR = new Creator<Setting>() {
        @Override
        public Setting createFromParcel(@NonNull Parcel in) {
            return new Setting(in);
        }

        @Override
        public Setting[] newArray(int size) {
            return new Setting[size];
        }
    };
    @Nullable
    private final String theme;
    private final int punto;

    public Setting(String theme, int punto) {
        this.theme = theme;
        this.punto = punto;
    }

    private Setting(Parcel in) {
        theme = in.readString();
        punto = in.readInt();
    }

    public static Setting getSettingFromFile(Context context) {
        return Utils.sett(context);
    }

    @Nullable
    public String getTheme() {
        return theme;
    }

    public int getPunto() {
        return punto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(theme);
        parcel.writeInt(punto);
    }
}
