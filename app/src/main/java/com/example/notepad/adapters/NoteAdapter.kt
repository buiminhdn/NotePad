package com.example.notepad.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.databinding.NoteItemBinding
import com.example.notepad.models.Note

class NoteAdapter(
    private var notes: List<Note>,
    private val onItemClick: (Note) -> Unit
) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteAdapter.ViewHolder {
        val binding = NoteItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteAdapter.ViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.apply {
            noteItemTitleTxt.text = note.title
            noteItemTimeTxt.text = note.updatedAt

            root.setOnClickListener {
                onItemClick.invoke(note)
            }
        }
    }

    override fun getItemCount(): Int = notes.size

    @SuppressLint("NotifyDataSetChanged")
    fun setNotes(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }

}