package com.asndeveloper;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface NoteDao {
    //fetch
    @Query("select * from note")
    List<Notes> getNotes();

    @Insert
    void addNotes(Notes notes);
    @Update
    void updateNotes(Notes notes);
    @Delete
    void deleteNotes(Notes notes);

}