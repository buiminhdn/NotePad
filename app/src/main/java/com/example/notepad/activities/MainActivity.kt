package com.example.notepad.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.R
import com.example.notepad.adapters.NoteAdapter
import com.example.notepad.assets.TempData
import com.example.notepad.databinding.ActivityMainBinding
import com.example.notepad.models.Note
import com.example.notepad.viewmodels.NoteViewModel
import java.sql.Date
import java.time.LocalDate
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.notepad.helpers.IS_EDITED_ACTION
import com.example.notepad.helpers.NOTE_DETAIL_OBJECT


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var noteAdapter: NoteAdapter
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Khoi tao Toolbar & Drawer
        initNavigationView()

        noteAdapter = NoteAdapter(emptyList(),::startToNoteDetail)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = noteAdapter

        noteViewModel.notes.observe(this) { notes ->
            noteAdapter.setNotes(notes)
        }

        binding.favPlus.setOnClickListener {
            val intent = Intent(this, NoteDetailActivity::class.java).apply {
                putExtra(IS_EDITED_ACTION, false)
            }
            startActivity(intent)
        }
    }

    private fun startToNoteDetail(note: Note){
        val intent = Intent(this, NoteDetailActivity::class.java).apply {
            putExtra(NOTE_DETAIL_OBJECT, note)
            putExtra(IS_EDITED_ACTION, true)
        }
        startActivity(intent)
    }

    private fun initNavigationView() {
        setSupportActionBar(binding.toolbar)

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.sort -> {
                Toast.makeText(this, "Sort clicked", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.nav_viewCategory -> {
                Toast.makeText(this, "View Category clicked", Toast.LENGTH_SHORT).show()
                true
            }

            else -> {
                if (toggle.onOptionsItemSelected(item)) {
                    true
                } else {
                    super.onOptionsItemSelected(item)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload Notes List
    }
}