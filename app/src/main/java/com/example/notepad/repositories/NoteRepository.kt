package com.example.notepad.repositories

import androidx.lifecycle.LiveData
import com.example.notepad.database.DAO.NoteDao
import com.example.notepad.models.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    
    val allNotes: LiveData<List<Note>> = noteDao.getAll()

    val allNotesFlow : Flow<List<Note>> = noteDao.getAll2()

    fun getAllByKeyword(keyword: String) : LiveData<List<Note>> {
        return noteDao.getAllByKeyword(keyword)
    }

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