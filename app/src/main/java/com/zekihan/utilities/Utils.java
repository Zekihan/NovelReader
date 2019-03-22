package com.zekihan.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Utils {

    public static void uploadStorage(@NonNull StorageReference storageReference, final Context context, String userID, String path, String fileName) {

        FileInOut fio = new FileInOut(context);
        fio.setDirectoryName(path);
        fio.setFileName(fileName);
        Uri filePath = Uri.fromFile(fio.createFile());
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("users/" + userID + "/" + fileName);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    public static void downloadStorage(@NonNull StorageReference storageReference, final Context context, String userID, @NonNull final String fileName) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Downloading...");
        progressDialog.show();

        File localFile = null;
        try {
            localFile = File.createTempFile(fileName, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        StorageReference ref = storageReference.child("users/" + userID + "/" + fileName);
        final File finalLocalFile = localFile;
        if (localFile != null) {
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show();
                    String output = null;
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new FileReader(finalLocalFile));
                        String read;
                        StringBuilder builder = new StringBuilder();

                        while ((read = bufferedReader.readLine()) != null) {
                            builder.append(read);
                            builder.append(System.lineSeparator());
                        }
                        bufferedReader.close();
                        bufferedReader.close();
                        output = builder.toString();
                    } catch (FileNotFoundException e) {
                        System.out.println("File " + fileName + " not found");
                        System.out.println("Exception: " + e.getMessage());
                    } catch (IOException e) {
                        System.out.println("Error reading from file " + fileName);
                        System.out.println("Exception: " + e.getMessage());
                    }
                    Log.e("Utils", "Download :" + output);
                    FileInOut fio = new FileInOut(context);
                    fio.writeFile(fileName, fileName, output);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Downloaded " + (int) progress + "%");
                }
            });
        }
    }

}
