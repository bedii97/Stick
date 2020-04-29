package com.example.stick;

import androidx.annotation.Nullable;

public class NoteModel {
    /**
     * NOTE(FIRST) TABLE DATABASE MODEL
     */
    private int id;
    private String title;
    private String color;
    private long date;

    public NoteModel(int id, String title, String color, long date) {
        this.id = id;
        this.title = title;
        this.color = color;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
