package com.zekihan.datatype;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Novel implements Parcelable {
    public static final Creator<Novel> CREATOR = new Creator<Novel>() {
        @Override
        public Novel createFromParcel(@NonNull Parcel in) {
            return new Novel(in);
        }

        @Override
        public Novel[] newArray(int size) {
            return new Novel[size];
        }
    };
    @Nullable
    private final String id;
    @Nullable
    private final String name;
    @Nullable
    private final String description;
    @Nullable
    private final Integer chapterCount;
    @Nullable
    private final String language;
    @Nullable
    private final String author;
    private final Status status;
    private final List<Genre> genres;
    @Nullable
    private final List<String> tags;

    public Novel(String id, String name, String description, Integer chapterCount, String language, String author, Status status, List<Genre> genres, List<String> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.chapterCount = chapterCount;
        this.language = language;
        this.author = author;
        this.status = status;
        this.genres = genres;
        this.tags = tags;
    }

    private Novel(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            chapterCount = null;
        } else {
            chapterCount = in.readInt();
        }
        language = in.readString();
        author = in.readString();
        tags = in.createStringArrayList();
        List<String> genreStrings = in.createStringArrayList();
        List<Genre> genreEnum = new ArrayList<>();
        if (genreStrings != null) {
            for (String genre : genreStrings) {
                genreEnum.add(Genre.valueOf(genre));
            }
        }
        genres = genreEnum;
        status = Status.valueOf(in.readString());
    }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public int getChapterCount() {
        return chapterCount;
    }

    @Nullable
    public String getLanguage() {
        return language;
    }

    @Nullable
    public String getAuthor() {
        return author;
    }

    public Status getStatus() {
        return status;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    @Nullable
    public List<String> getTags() {
        return tags;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null){
            return false;
        }
        Novel other = (Novel) obj;
        return ((this.id.equals(other.id)));
    }

    @NonNull
    @Override
    public String toString() {
        return "Novel [" + id + ", " + name + ", " + chapterCount + ", " + language + ", " + author + ", " + status + ", " + genres + ", " + tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        if (chapterCount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(chapterCount);
        }
        parcel.writeString(language);
        parcel.writeString(author);
        parcel.writeStringList(tags);
        List<String> genreStrings = new ArrayList<>();
        for (Genre genre : getGenres()) {
            genreStrings.add(genre.name());
        }
        parcel.writeStringList(genreStrings);
        parcel.writeString(status.name());
    }
}

