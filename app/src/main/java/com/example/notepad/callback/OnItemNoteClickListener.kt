package com.example.notepad.callback

import com.example.notepad.models.Note

interface OnItemNoteClickListener {
    fun onItemClick(note: Note)
    fun onDeleteNote()
}