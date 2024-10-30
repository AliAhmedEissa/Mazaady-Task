package com.mazaady.android_task.presentation.firstScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mazaady.android_task.databinding.ItemFormFieldBinding
import com.mazaady.android_task.presentation.firstScreen.model.FormField

class FormFieldAdapter(private val formFields: List<FormField>) : RecyclerView.Adapter<FormFieldAdapter.FormFieldViewHolder>() {

    class FormFieldViewHolder(val binding: ItemFormFieldBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormFieldViewHolder {
        val binding = ItemFormFieldBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FormFieldViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FormFieldViewHolder, position: Int) {
        val formField = formFields[position]
        holder.binding.keyTextView.text = formField.key
        holder.binding.valueTextView.text = formField.value
    }

    override fun getItemCount() = formFields.size
}
