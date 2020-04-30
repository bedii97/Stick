package com.example.stick.Model;

import androidx.annotation.Nullable;

public class TaskModel {
    private long id;
    private String content;
    private int status;
    private long date;
    private long parentID;

    public TaskModel(@Nullable long id, String content, int status, long date, long parentID) {
        this.id = id;
        this.content = content;
        this.status = status;
        this.date = date;
        this.parentID = parentID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getParentID() {
        return parentID;
    }

    public void setParentID(long parentID) {
        this.parentID = parentID;
    }
}
