package com.mazaady.android_task.presentation.secondScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mazaady.android_task.databinding.ItemEmployeeBinding
import com.mazaady.android_task.presentation.secondScreen.model.Employee
import com.mazaady.android_task.util.extention.onDebouncedListener

class EmployeesAdapter(
    private var employees: List<Employee>,
    private val onEmployeeClickListener: (employee: Employee) -> Unit // Lambda for item clicks
) : RecyclerView.Adapter<EmployeesAdapter.EmployeeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        // Use ViewBinding to inflate the item layout
        val binding = ItemEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bind(employees[position])
    }


    override fun getItemCount(): Int = employees.size

    // ViewHolder class using ViewBinding
    inner class EmployeeViewHolder(private val binding: ItemEmployeeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(employee: Employee) {
            binding.txtTitle.text = employee.title
            binding.txtName.text = employee.name
            binding.txtHint.text = employee.hint
            binding.txtPosition.text = employee.position
            binding.imgProfile.setImageResource(employee.profilePic)
            binding.imgBackground.setImageResource(employee.backgroundImage)
            binding.txtTime.text = employee.time
            binding.rcTags.adapter = TagsAdapter(employee.tags)
            binding.root.onDebouncedListener {
                onEmployeeClickListener.invoke(employee)
            }
        }
    }

    // Method to update the list when search results are filtered
    fun updateList(employees: List<Employee>) {
        this.employees = employees
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}
