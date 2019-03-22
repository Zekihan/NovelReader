package com.zekihan.datatype;

public class LastReadInfo {

    private String novelId;
    private int chapterNum;
    private int scrollPosition;
    private int id;

    public static final String TABLE_NAME = "last_read";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOVELID = "novelId";
    public static final String COLUMN_CHAPTERNUM = "chapterNum";
    public static final String COLUMN_SCROLLPOSITION = "scrollPosition";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOVELID + " TEXT,"
                    + COLUMN_CHAPTERNUM + " INTEGER,"
                    + COLUMN_SCROLLPOSITION + " INTEGER "
                    + ")";

    public LastReadInfo() {
    }

    public LastReadInfo(String novelId, int chapterNum, int scrollPosition) {
        this.novelId = novelId;
        this.chapterNum = chapterNum;
        this.scrollPosition = scrollPosition;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
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

}
