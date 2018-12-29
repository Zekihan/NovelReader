package com.zekihan.datatype;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public class ImageItem implements Comparable {
    private Bitmap image;
    private String title;
    private Novel novel;

    public ImageItem(Bitmap image, String title, Novel novel) {
        super();
        this.image = image;
        this.title = title;
        this.novel = novel;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Novel getNovel() {
        return novel;
    }

    public void setNovel(Novel novel) {
        this.novel = novel;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        ImageItem other = (ImageItem) o;
        return this.getTitle().compareTo(other.getTitle());
    }
}