package com.mazaady.android_task.presentation.view.bottomSheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mazaady.android_task.R
import com.mazaady.android_task.databinding.BottomSheetOptionsBinding
import com.mazaady.android_task.presentation.adapter.OptionsAdapter
import com.mazaady.android_task.presentation.model.PropertyModel
import com.mazaady.android_task.util.extention.onDebouncedListener

class OptionsBottomSheet(
    private val title:String,
    private val options: List<PropertyModel>,
    private val onOptionSelected: (PropertyModel) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetOptionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OptionsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtTitle.text = title

        // Initialize adapter with full options list
        adapter = OptionsAdapter(options, onOptionClick = { selectedOption ->
            onOptionSelected(selectedOption)
            dismiss() // Dismiss bottom sheet after selection
        })

        // Set the adapter to the RecyclerView
        binding.optionsRecyclerView.adapter = adapter

        // Set up search functionality
        binding.edtSearch.doAfterTextChanged {
            filterOptions(it.toString())
        }

        binding.imgClose.onDebouncedListener {
            dismiss()
        }
    }

    // Function to filter the options list based on the search query
    private fun filterOptions(query: String) {
        val filteredList = options.filter { it.name.contains(query, ignoreCase = true) }
        adapter.updateList(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
