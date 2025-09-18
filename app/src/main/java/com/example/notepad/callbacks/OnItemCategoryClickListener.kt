package com.example.notepad.callbacks

import com.example.notepad.models.Category

interface OnItemCategoryClickListener {
    fun onItemUpdate(category: Category)
    fun onItemDelete(id: Int)
}