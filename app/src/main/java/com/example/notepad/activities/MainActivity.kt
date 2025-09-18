package com.example.notepad.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad.R
import com.example.notepad.adapters.NoteAdapter
import com.example.notepad.databinding.ActivityMainBinding
import com.example.notepad.helpers.IS_EDITED_ACTION
import com.example.notepad.helpers.NOTE_DETAIL_OBJECT
import com.example.notepad.helpers.showToast
import com.example.notepad.models.Note
import com.example.notepad.viewmodels.NoteViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var noteAdapter: NoteAdapter
    private val noteViewModel: NoteViewModel by viewModels()

    private lateinit var selectedFruits: String
    private var selectedFruitsIndex: Int = 0
    private val fruits = arrayOf("Apple", "Banana", "Coconut", "Orange", "Pineapple",
        "Papaya", "Mango", "Blackberries", "Guava")

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
        // Xu ly Event Click Drawer
        handleClickDrawerMenu()
        // Khoi tao Adapter and ViewModel
        bindViewModel()
        // Xu ly Event Click Add
        handleClickAdd()


    }

    private fun handleClickDrawerMenu() {
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navEditCategories -> {
                    startActivity(Intent(this, CategoryActivity::class.java))
                    binding.drawerLayout.closeDrawers()
                    true
                }

                else -> true
            }
        }
    }

    private fun handleClickAdd() {
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, NoteDetailActivity::class.java).apply {
                putExtra(IS_EDITED_ACTION, false)
            }
            startActivity(intent)
        }
    }

    private fun bindViewModel() {
        noteAdapter = NoteAdapter(emptyList(), ::startToNoteDetail, ::showNoteDetailOptions)

        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.adapter = noteAdapter

//        noteViewModel.notes.observe(this,::updateNotes)
        noteViewModel.notes.observe(this,noteAdapter::setNotes)
    }

//    private fun updateNotes(notes: List<Note>){
//        noteAdapter.setNotes(notes)
//    }

    private fun startToNoteDetail(note: Note) {
        val intent = Intent(this, NoteDetailActivity::class.java).apply {
            putExtra(NOTE_DETAIL_OBJECT, note)
            putExtra(IS_EDITED_ACTION, true)
        }
        startActivity(intent)
    }

    private fun showNoteDetailOptions(id: Int) {
        showToast("$id", this)
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
            R.id.navSearch -> {
//                showSearchDialog()
                val searchBtn = findViewById<View>(R.id.navSearch)
                searchBtn.visibility = View.GONE
                val sortBtn = findViewById<View>(R.id.navSort)
                sortBtn.visibility = View.GONE
                binding.edtSearch.visibility = View.VISIBLE;
                binding.btnDeleteSearch.visibility = View.VISIBLE;
                binding.edtSearch.requestFocus();
                true
            }

            R.id.navSort -> {
                showSortDialog()
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

    private fun showSortDialog() {
        selectedFruits = fruits[selectedFruitsIndex]
        MaterialAlertDialogBuilder(this)
            .setTitle("List of Fruits")
            .setSingleChoiceItems(fruits, selectedFruitsIndex) { dialog_, which ->
                selectedFruitsIndex = which
                selectedFruits = fruits[which]
            }
            .setPositiveButton("Ok") { dialog, which ->
                Toast.makeText(this, "$selectedFruits Selected", Toast.LENGTH_SHORT)
                    .show()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showSearchDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Filter with Keyword")
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.edtKeyword)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i ->
            filterNotesByKeyword(editText.text.toString())
        }
        builder.show()
    }

    private fun filterNotesByKeyword(keyword: String) {
        noteViewModel.getAllNoteByKeyword(keyword,this,noteAdapter::setNotes)
//        noteViewModel.getAllNotesByKeyword(keyword)
//        noteViewModel.filteredNote.observe(this,noteAdapter::setNotes)
    }
}