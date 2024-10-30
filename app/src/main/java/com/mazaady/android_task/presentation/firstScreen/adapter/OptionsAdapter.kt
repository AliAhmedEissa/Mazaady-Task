package com.mazaady.android_task.presentation.firstScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mazaady.android_task.databinding.ItemOptionBinding
import com.mazaady.android_task.presentation.firstScreen.model.PropertyModel

class OptionsAdapter(
    private var options: List<PropertyModel>,
    private val onOptionClick: (PropertyModel) -> Unit
) : RecyclerView.Adapter<OptionsAdapter.OptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        // Use ViewBinding to inflate the item layout
        val binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(options[position])
    }

    override fun getItemCount(): Int = options.size

    // ViewHolder class using ViewBinding
    inner class OptionViewHolder(private val binding: ItemOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(option: PropertyModel) {
            // Bind the option to the TextView
            binding.optionTextView.text = option.name

            // Set click listener
            binding.root.setOnClickListener {
                onOptionClick(option)
            }
        }
    }

    // Method to update the list when search results are filtered
    fun updateList(newOptions: List<PropertyModel>) {
        options = newOptions
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}
