package com.example.notepad.repositories

import androidx.lifecycle.LiveData
import com.example.notepad.database.DAO.NoteDao
import com.example.notepad.models.Note

class NoteRepository(private val noteDao: NoteDao) {
    
    val allNotes: LiveData<List<Note>> = noteDao.getAll()
    
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }
    
    suspend fun delete(id: Int){
        noteDao.delete(id)
    }
    
    suspend fun update(note: Note){
        noteDao.update(note)
    }
}