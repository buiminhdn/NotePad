package com.example.notepad.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.callbacks.OnItemCategoryClickListener
import com.example.notepad.databinding.CategoryItemBinding
import com.example.notepad.models.Category

class CategoryAdapter(
    private var categories: List<Category>,
    private val onCategoryClickListener: OnItemCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = CategoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val category = categories[position]
        holder.binding.apply {
            edtName.setText(category.name)

            btnUpdate.setOnClickListener {
                onCategoryClickListener.onItemUpdate(category.copy(name = edtName.text.toString()))
            }

            btnDelete.setOnClickListener {
                onCategoryClickListener.onItemDelete(
                    category.id
                )
            }

        }
    }

    override fun getItemCount(): Int = categories.size

    inner class ViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setCategories(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }

}