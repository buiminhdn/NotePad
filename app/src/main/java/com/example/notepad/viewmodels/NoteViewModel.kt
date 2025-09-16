package com.example.notepad.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notepad.database.AppDatabase
import com.example.notepad.models.Category
import com.example.notepad.models.Note
import com.example.notepad.repositories.CategoryRepository
import com.example.notepad.repositories.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    val notes: LiveData<List<Note>>
    private val repository: NoteRepository

    init {
        val dao = AppDatabase.getDatabase(application).getNoteDao()
        repository = NoteRepository(dao)
        notes = repository.allNotes
    }

    fun deleteNote(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(id)
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }

    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }
}