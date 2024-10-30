package com.mazaady.android_task.presentation.secondScreen.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mazaady.android_task.R
import com.mazaady.android_task.databinding.ItemCategoryBinding
import com.mazaady.android_task.databinding.ItemTagsBinding
import com.mazaady.android_task.presentation.secondScreen.model.TagModel

class TagsAdapter(
    private var tags: List<TagModel>,
) : RecyclerView.Adapter<TagsAdapter.TagsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
        val binding = ItemTagsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        holder.bind(tags[position])
    }

    override fun getItemCount(): Int = tags.size

    inner class TagsViewHolder(private val binding: ItemTagsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tag: TagModel) {
            // Set the text for the TextView
            binding.txtTag.apply {
                text = tag.title
                background = ContextCompat.getDrawable(context, R.drawable.bg_rounded_8_no_padding)
                backgroundTintList = ColorStateList.valueOf(Color.parseColor(tag.color))
            }

        }
    }

    // Method to update the list when search results are filtered
    fun updateList(tags: List<TagModel>) {
        this.tags = tags
        notifyDataSetChanged()
    }
}
