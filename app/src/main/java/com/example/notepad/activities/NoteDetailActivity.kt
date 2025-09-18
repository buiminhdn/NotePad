package com.example.notepad.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import com.example.notepad.R
import com.example.notepad.databinding.ActivityNoteDetailBinding
import com.example.notepad.helpers.DateTimeHelper
import com.example.notepad.helpers.IS_EDITED_ACTION
import com.example.notepad.helpers.NOTE_DETAIL_OBJECT
import com.example.notepad.helpers.showToast
import com.example.notepad.models.Note
import com.example.notepad.viewmodels.NoteViewModel
import java.util.Stack

class NoteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteDetailBinding
    private lateinit var optionsMenu: Menu
    private lateinit var item: Note
    private var currentNoteId: Int = 0
    private var isEditedAction = false
    private val noteViewModel: NoteViewModel by viewModels()

    // Undo
    private val undoStack = Stack<String>()
    private var lastSavedText = ""
    private val handler = Handler(Looper.getMainLooper())
    private val saveRunnable = object : Runnable {
        override fun run() {
            val currentText = binding.edtContent.text.toString()
            if (currentText != lastSavedText) {
                undoStack.push(currentText)
                lastSavedText = currentText
            }
            handler.postDelayed(this, 1000)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initToolbar()
        getNoteDataIfUpdate()
        handleContentChange()

        handler.post(saveRunnable)
    }

    private fun handleContentChange() {
        binding.edtContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                optionsMenu[1].isEnabled = s.toString().isNotEmpty()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getNoteDataIfUpdate() {
        isEditedAction = intent.getBooleanExtra(IS_EDITED_ACTION, false)
        if (isEditedAction) getNoteDataBundle()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getNoteDataBundle() {
        item = intent.getParcelableExtra(NOTE_DETAIL_OBJECT, Note::class.java)!!
        currentNoteId = item.id
        binding.edtTitle.setText(item.title)
        binding.edtContent.setText(item.content)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_detail_menu, menu)
        if (menu != null) {
            optionsMenu = menu
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navSave -> {
                upsertNote()
                true
            }

            R.id.navUndo -> {
                undoNote()
                true
            }

            R.id.navDelete -> {
                deleteNote()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteNote() {
        noteViewModel.deleteNote(currentNoteId)
    }

    private fun undoNote() {
        if (undoStack.size > 1) {
            undoStack.pop() // remove current
            val prev = undoStack.peek()
            prev?.let {
                binding.edtContent.setText(it)
                binding.edtContent.setSelection(it.length)
                lastSavedText = it
            }
        } else {
            undoStack.clear()
            binding.edtContent.setText("")
            lastSavedText = ""
        }
    }

    private fun upsertNote() {
        val noteTitle = binding.edtTitle.text.toString()
        val noteContent = binding.edtContent.text.toString()

        if (noteTitle.isEmpty() && noteContent.isEmpty()) {
            showToast("Type Something", this)
            return
        }

        val currentDateAndTime = DateTimeHelper.getCurrentDateTime()

        // Update & Add
        if (currentNoteId != 0) {
            noteViewModel.updateNote(Note(currentNoteId, noteTitle, noteContent, currentDateAndTime.toString()))
            showToast("$noteTitle Updated", this)
        } else {
            noteViewModel.addNote(Note(0, noteTitle, noteContent, currentDateAndTime.toString()))
            showToast("$noteTitle Added", this)
        }

        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(saveRunnable) // cleanup
    }
}