package com.example.notepad.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepad.database.AppDatabase
import com.example.notepad.models.Category
import com.example.notepad.repositories.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Change Something
class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    val categories: LiveData<List<Category>>
    private val repository: CategoryRepository

    init {
        val dao = AppDatabase.getDatabase(application).getCategoryDao()
        repository = CategoryRepository(dao)
        categories = repository.allCategories
    }

    fun deleteCategory(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(id)
    }

    fun updateCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(category)
    }

    fun addCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(category)
    }
}