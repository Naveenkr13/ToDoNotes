package com.asndeveloper;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//step 1
//table name
@Entity(tableName = "note")

public class Notes {
    //step 2
    @PrimaryKey(autoGenerate = true)
    private int id;

    //step 3
    @ColumnInfo(name = "title")
    private String title;

    //step 4
    @ColumnInfo(name="content")
    private  String content;

// constructor created  step 5

    public Notes(int id,String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
    @Ignore
    public Notes( String title, String content) {

    }
    // step 6 getter and setter


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
