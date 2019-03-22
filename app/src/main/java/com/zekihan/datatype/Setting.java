package com.zekihan.datatype;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

public class Setting {

    private String theme;
    private int punto;


    public Setting(String theme, int punto) {
        this.theme = theme;
        this.punto = punto;
    }

    public static Setting getSettingFromFile(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return new Setting(prefs.getString("theme","dark"),prefs.getInt("punt",18));
    }

    @Nullable
    public String getTheme() {
        return theme;
    }

    public int getPunto() {
        return punto;
    }

}
