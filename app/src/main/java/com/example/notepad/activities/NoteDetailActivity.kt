package com.example.notepad.activities

import android.os.Build
import android.os.Bundle
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

class NoteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteDetailBinding
    private lateinit var optionsMenu: Menu
    private lateinit var item: Note
    private var currentNoteId: Int = 0
    private var isEditedAction = false
    private val noteViewModel: NoteViewModel by viewModels()

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

        isEditedAction = intent.getBooleanExtra(IS_EDITED_ACTION, false)

        if (isEditedAction) getNoteDataBundle()

        binding.contentEdt.addTextChangedListener(object : TextWatcher {
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
    private fun getNoteDataBundle() {
        item = intent.getParcelableExtra(NOTE_DETAIL_OBJECT, Note::class.java)!!
        currentNoteId = item.id
        binding.titleEdt.setText(item.title)
        binding.contentEdt.setText(item.content)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbarDetail)

        binding.toolbarDetail.setNavigationOnClickListener {
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
        val currentLength: Int = binding.contentEdt.length()
        if (currentLength > 0) {
            binding.contentEdt.setText(
                binding.contentEdt.getText().delete(currentLength - 1, currentLength)
            )
            binding.contentEdt.setSelection(currentLength - 1)
        }
    }

    private fun upsertNote() {
        val noteTitle = binding.titleEdt.text.toString()
        val noteContent = binding.contentEdt.text.toString()

        if (noteTitle.isEmpty() && noteContent.isEmpty()) {
            showToast("Type Something", this)
            return
        }

        val currentDateAndTime = DateTimeHelper.getCurrentDateTime()

        // Update & Add
        if (currentNoteId != 0) {
            noteViewModel.updateNote(Note(0, noteTitle, noteContent, currentDateAndTime.toString()))
            showToast("$noteTitle Updated", this)
        } else {
            noteViewModel.addNote(Note(0, noteTitle, noteContent, currentDateAndTime.toString()))
            showToast("$noteTitle Added", this)
        }
    }
}