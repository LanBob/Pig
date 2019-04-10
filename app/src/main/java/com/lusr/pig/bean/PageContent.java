package com.lusr.pig.bean;

public class PageContent {
    private String title;
    private String content;
    private String readerCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReaderCount() {
        return readerCount;
    }

    public void setReaderCount(String readerCount) {
        this.readerCount = readerCount;
    }
}
