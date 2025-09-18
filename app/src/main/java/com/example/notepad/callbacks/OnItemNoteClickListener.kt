package com.example.notepad.callbacks

import com.example.notepad.models.Note

interface OnItemNoteClickListener {
    fun onItemClick(note: Note)
    fun onDeleteNote()
}