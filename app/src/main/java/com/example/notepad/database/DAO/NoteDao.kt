package com.example.notepad.database.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notepad.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY updatedAt")
    fun getAll(): LiveData<List<Note>>

    @Query("SELECT * FROM notes ORDER BY updatedAt")
    fun getAll2(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :keyword || '%'")
    fun getAllByKeyword(keyword: String): LiveData<List<Note>>

    @Query("SELECT * FROM notes")
    fun getAllByKeywordTest(): LiveData<List<Note>>

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun delete(id: Int)
}