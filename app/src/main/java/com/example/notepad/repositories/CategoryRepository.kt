package com.example.notepad.repositories

import androidx.lifecycle.LiveData
import com.example.notepad.database.DAO.CategoryDao
import com.example.notepad.models.Category
import com.example.notepad.models.Note

class CategoryRepository(private val categoryDao: CategoryDao) {

    val allCategories: LiveData<List<Category>> = categoryDao.getAll()

    suspend fun insert(category: Category) {
        categoryDao.insert(category)
    }

    suspend fun update(category: Category) {
        categoryDao.update(category)
    }

    suspend fun delete(category: Category){
        categoryDao.delete(category)
    }
}