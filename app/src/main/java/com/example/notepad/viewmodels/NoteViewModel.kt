package com.example.notepad.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notepad.database.AppDatabase
import com.example.notepad.models.Note
import com.example.notepad.repositories.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    var notes: LiveData<List<Note>>
    private val repository: NoteRepository
    private val _filteredNote = MutableLiveData<List<Note>>()
    val filteredNote = _filteredNote

    init {
        val dao = AppDatabase.getDatabase(application).getNoteDao()
        repository = NoteRepository(dao)
        notes = repository.allNotes
    }

    fun getAllNotesByKeyword(keyword: String) {
        val allNote = notes.value?:emptyList()
        val filterByKey = allNote.filter { it.title.contains(keyword)}
        _filteredNote.value = filterByKey
    }

    fun getAllNoteByKeyword(
        keyword: String,
        owner: LifecycleOwner,
        onGetNotes: (List<Note>) -> Unit
    ) {
        repository.getAllByKeyword(keyword).observe(owner, onGetNotes)
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