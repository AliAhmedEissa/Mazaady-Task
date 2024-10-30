package com.mazaady.android_task.presentation.secondScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mazaady.android_task.databinding.ItemCategoryBinding

class CategoriesAdapter(
    private var categories: List<String>,
    private val onItemClickListener: (category: String) -> Unit // Lambda for item clicks
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private var selectedPosition = 0 // Set the initial selected position to the first item (0)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position], position)
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: String, position: Int) {
            // Set the text for the TextView
            binding.txtCategory.text = category

            // Set selected or unselected state based on selectedPosition
            binding.txtCategory.isSelected = (position == selectedPosition)

            // Handle click to update the selected position and trigger callback
            binding.root.setOnClickListener {
                // Update the selected position
                notifyItemChanged(selectedPosition) // Unselect the previous item
                selectedPosition = adapterPosition
                notifyItemChanged(selectedPosition) // Select the new item

                // Trigger the lambda callback with the selected item
                onItemClickListener(category)
            }
        }
    }

    // Method to update the list when search results are filtered
    fun updateList(newCategories: List<String>) {
        this.categories = newCategories
        selectedPosition = 0 // Reset selection to the first item
        notifyDataSetChanged()

        // Notify the first item as selected initially
        if (categories.isNotEmpty()) {
            onItemClickListener(categories[0])
        }
    }
}
