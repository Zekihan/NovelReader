package com.zekihan.utilities.DatabaseHelper;

import com.zekihan.datatype.Genre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseUtils {

    private final static String separator = String.valueOf("½%&#&%½");

    public static String genreListToString(List<Genre> list){
        StringBuilder ret = new StringBuilder();
        ret.append("{");
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size()-1) ret.append(list.get(i).toString());
            else ret.append(list.get(i).toString()).append(separator);

        }
        ret.append("}");
        return ret.toString();
    }
    public static List<Genre> stringToGenreList(String str){
        ArrayList<Genre> ret = new ArrayList<>();
        String subStr = str.substring(1,str.length()-1);
        String[] parse = subStr.split(separator);
        for (String s:parse){
            ret.add(Genre.valueOf(s));
        }
        return ret;
    }
    public static String tagListToString(List<String> list){
        StringBuilder ret = new StringBuilder();
        ret.append("{");
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size()-1) ret.append(list.get(i));
            else ret.append(list.get(i)).append(separator);

        }
        ret.append("}");
        return ret.toString();
    }
    public static List<String> stringToTagList(String str){
        ArrayList<String> ret = new ArrayList<>();
        String subStr = str.substring(1,str.length()-1);
        String[] parse = subStr.split(separator);
        Collections.addAll(ret, parse);
        return ret;
    }
}
