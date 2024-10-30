package com.mazaady.android_task.presentation.secondScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mazaady.android_task.databinding.ItemStoryBinding

class StoriesAdapter(
    private var stories: List<String>,
) : RecyclerView.Adapter<StoriesAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        // Use ViewBinding to inflate the item layout
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size

    // ViewHolder class using ViewBinding
    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: String) {

        }
    }

    // Method to update the list when search results are filtered
    fun updateList(stories: List<String>) {
        this.stories = stories
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}
