package com.zekihan.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileInOut {
    private final Context mContext;
    private String directoryName = "empty";
    private String fileName = "empty";

    public FileInOut(Context mContext) {
        this.mContext = mContext;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public void writeFile(String fileName, String path, @Nullable String content) {
        setFileName(fileName);
        setDirectoryName(path);
        try {
            FileWriter filewriter = new FileWriter(createFile());
            BufferedWriter out = new BufferedWriter(filewriter);
            if (content != null){
                out.write(content);
            }else out.write("No Chapter");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public String fileRead(String fileName, String path) {
        setFileName(fileName);
        setDirectoryName(path);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(createFile()));
            String read;
            StringBuilder builder = new StringBuilder();

            while ((read = bufferedReader.readLine()) != null) {
                builder.append(read);
                builder.append(System.lineSeparator());
            }
            bufferedReader.close();
            bufferedReader.close();
            return builder.toString();
        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " not found");
            System.out.println("Exception: " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.println("Error reading from file " + fileName);
            System.out.println("Exception: " + e.getMessage());
            return null;
        } catch (Exception e) {
            Log.e("FileInOut", "err :" + e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @NonNull
    public File createFile() {
        File directory;
        File file = mContext.getFilesDir();
        file.mkdir();
        String[] array = directoryName.split("/");
        for(int t = 0; t < array.length - 1; t++) {
            file = new File(file, array[t]);
            file.mkdir();
        }
        directory = new File(file,array[array.length- 1]);
        if (!directory.exists() && !directory.mkdirs()) {
            Log.e("FileInOut", "Error creating directory " + directory);
        }

        return new File(directory, fileName);
    }
}
