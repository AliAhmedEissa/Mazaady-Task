package com.mazaady.android_task.presentation.firstScreen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mazaady.android_task.R
import com.mazaady.android_task.databinding.FragmentFirstBinding
import com.mazaady.android_task.presentation.firstScreen.adapter.FormFieldAdapter
import com.mazaady.android_task.presentation.base.BaseFragment
import com.mazaady.android_task.presentation.firstScreen.model.FormField
import com.mazaady.android_task.presentation.firstScreen.model.PropertyModel
import com.mazaady.android_task.presentation.firstScreen.view.bottomSheet.OptionsBottomSheet
import com.mazaady.android_task.presentation.firstScreen.viewmodel.CategoryViewModel
import com.mazaady.android_task.util.DataState
import com.mazaady.android_task.util.extention.customNavigate
import com.mazaady.android_task.util.extention.isEmpty
import com.mazaady.android_task.util.extention.onDebouncedListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FirstFragment : BaseFragment() {

    // View binding to handle the UI elements in the fragment layout
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    // ViewModel to interact with category data and manage UI state
    private val vm: CategoryViewModel by viewModels()

    // Lists to store categories, subcategories, properties, and child options
    private val allCategories = mutableListOf<PropertyModel>()
    private val subCategories = mutableListOf<PropertyModel>()
    private val properties = mutableListOf<PropertyModel>()
    private val childOptions = mutableListOf<PropertyModel>()

    // Variables to hold the previously selected category and subcategory
    private var previousSelectedCategory: PropertyModel? = null
    private var previousSelectedSubCategory: PropertyModel? = null

    // Local map for properties when there are no API results
    private val localPropertiesMap = mapOf(-1 to listOf(PropertyModel(id = 101, name = "Specify Here", options = emptyList())))

    // Inflate the view and initialize binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Setup listeners and observers after the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.fetchAllCategories() // Initial fetch for categories
        setupObservers() // Observe data from ViewModel
        setupListeners() // Setup UI event listeners
    }

    // Set up UI event listeners
    private fun setupListeners() {
        // Listen for main category selection and open category bottom sheet
        binding.textInputEdtMainCategory.onDebouncedListener {
            showCategoryBottomSheet(getString(R.string.main_category), allCategories, ::onMainCategorySelected)
        }

        // Listen for subcategory selection and open subcategory bottom sheet
        binding.textInputEdtSubCategory.onDebouncedListener {
            showCategoryBottomSheet(getString(R.string.sub_category), subCategories, ::onSubCategorySelected)
        }

        // Validate and submit form on button click
        binding.submitButton.onDebouncedListener {
            if (validateInputs()) submitForm()
        }

    }

    // Set up ViewModel observers for data updates
    private fun setupObservers() {
        vm.allCategories.observe(viewLifecycleOwner) { handleCategoryDataState(it, allCategories) }
        vm.subCategories.observe(viewLifecycleOwner) { handleCategoryDataState(it, subCategories) }
        vm.properties.observe(viewLifecycleOwner) { handlePropertyDataState(it, properties) }
        vm.childOptions.observe(viewLifecycleOwner) { handleChildOptionsDataState(it) }
    }

    // Handles updates to categories and subcategories data
    private fun handleCategoryDataState(
        result: DataState<List<PropertyModel>>,
        targetList: MutableList<PropertyModel>
    ) {
        when (result) {
            is DataState.Success -> targetList.apply { clear(); addAll(result.data) }
            is DataState.Error -> showToast(result.error.message)
            DataState.Idle -> hideLoading()
            DataState.Processing -> showLoading()
            DataState.ServerError -> showToast(getString(R.string.server_error))
        }
    }

    // Handles updates to property data
    private fun handlePropertyDataState(
        result: DataState<List<PropertyModel>>,
        targetList: MutableList<PropertyModel>
    ) {
        when (result) {
            is DataState.Success -> {
                targetList.apply { clear(); addAll(result.data) }
                result.data.forEach { inflateTextInputField(it, vm.childOptionsPosition) }
            }

            is DataState.Error -> showToast(result.error.message)
            DataState.Idle -> hideLoading()
            DataState.Processing -> showLoading()
            DataState.ServerError -> showToast(getString(R.string.server_error))
        }
    }

    // Handles updates to child options data
    private fun handleChildOptionsDataState(result: DataState<List<PropertyModel>>) {
        when (result) {
            is DataState.Success -> {
                val position = vm.childOptionsPosition ?: binding.dynamicFieldsContainer.childCount
                childOptions.apply { clear(); addAll(result.data) }
                result.data.forEachIndexed { _, childProperty ->
                    inflateTextInputField(childProperty, position)
                    vm.childOptionsPosition = position + 1
                }
            }

            is DataState.Error -> showToast(result.error.message)
            DataState.Idle -> hideLoading()
            DataState.Processing -> showLoading()
            DataState.ServerError -> showToast(getString(R.string.server_error))
        }
    }

    // Displays a bottom sheet dialog with selectable options
    private fun showCategoryBottomSheet(
        title: String,
        options: List<PropertyModel>,
        onOptionSelected: (PropertyModel) -> Unit
    ) {
        val bottomSheet = OptionsBottomSheet(title, options, onOptionSelected)
        bottomSheet.show(childFragmentManager, "OptionsBottomSheet")
    }

    // Handles main category selection and triggers subcategory fetch
    private fun onMainCategorySelected(selectedOption: PropertyModel) {
        if (selectedOption.id != previousSelectedCategory?.id) {
            clearSubCategoryAndViews()
            binding.textInputEdtMainCategory.setText(selectedOption.name)
            previousSelectedCategory = selectedOption
            vm.filterSubCategoriesByCategoryId(selectedOption.id)
        }
    }

    // Handles subcategory selection and triggers property fetch or uses local data
    private fun onSubCategorySelected(selectedOption: PropertyModel) {
        if (selectedOption.id != previousSelectedSubCategory?.id) {
            clearDynamicFields()
            binding.textInputEdtSubCategory.setText(selectedOption.name)
            previousSelectedSubCategory = selectedOption

            // Check for local properties based on selected subcategory ID
            val localProperties = localPropertiesMap[selectedOption.id]
            if (localProperties != null) {
                // Inflate fields locally without calling API
                localProperties.forEach { inflateTextInputField(it) }
            } else {
                // Call API if no local properties are found
                vm.fetchPropertiesBySubCategoryId(selectedOption.id)
            }
        }
    }

    // Clears subcategory selection and dynamic fields
    private fun clearSubCategoryAndViews() {
        binding.textInputEdtSubCategory.text = null
        previousSelectedSubCategory = null
        clearDynamicFields()
    }

    // Removes all views in dynamic fields container
    private fun clearDynamicFields() {
        binding.dynamicFieldsContainer.removeAllViews()
    }

    // Inflates a text input field with optional selection capabilities for properties
    private fun inflateTextInputField(property: PropertyModel, index: Int? = null) {
        val textInputLayout = createTextInputLayout(property)
        val textInputEditText = textInputLayout.findViewById<TextInputEditText>(R.id.textInputEditText)

        // Set clickability and focusability based on whether the property has options
        textInputEditText.isClickable = property.options.isNullOrEmpty().not()
        textInputEditText.focusable = if (textInputEditText.isClickable) View.NOT_FOCUSABLE else View.FOCUSABLE

        // Add an arrow icon if the property has options
        if (property.options.isNullOrEmpty().not()) {
            val arrowIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down)
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowIcon, null)
        } else {
            // Remove any existing drawable if there are no options
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }

        // Set click listener to show the bottom sheet with options if available
        textInputEditText.setOnClickListener {
            showBottomSheetWithChildOptions(textInputEditText, property, textInputLayout)
        }

        // Add the TextInputLayout to the container at the specified index or at the end if index is null
        if (index != null) binding.dynamicFieldsContainer.addView(textInputLayout, index)
        else binding.dynamicFieldsContainer.addView(textInputLayout)
    }

    // Creates a TextInputLayout for dynamic fields with a hint
    private fun createTextInputLayout(property: PropertyModel): TextInputLayout {
        val textInputLayout = LayoutInflater.from(requireContext()).inflate(
            R.layout.item_text_input_layout,
            binding.dynamicFieldsContainer,
            false
        ) as TextInputLayout
        textInputLayout.hint = property.name
        return textInputLayout
    }

    // Shows bottom sheet with child options and handles selection
    private fun showBottomSheetWithChildOptions(
        editText: TextInputEditText,
        property: PropertyModel,
        layout: TextInputLayout
    ) {
        if (property.options.isNullOrEmpty().not()) {
            showBottomSheet(property.name, property.options!!) { selectedOption ->
                editText.setText(selectedOption.name)
                if (selectedOption.hasChild) fetchChildOptions(selectedOption.id, layout)
            }
        }
    }

    // Fetches child options by option ID
    private fun fetchChildOptions(optionId: Int, layout: TextInputLayout) {
        vm.childOptionsPosition = binding.dynamicFieldsContainer.indexOfChild(layout) + 1
        vm.fetchOptionsChildByOptionId(optionId)
    }

    // Validates inputs in the dynamic fields container
    private fun validateInputs(): Boolean {
        var isValid = true
        for (i in 0 until binding.dynamicFieldsContainer.childCount) {
            val child = binding.dynamicFieldsContainer.getChildAt(i)
            if (child is TextInputLayout) {
                val inputField = child.editText
                if (inputField?.text?.toString()?.isEmpty() == true) {
                    child.error = getString(R.string.this_field_is_required)
                    isValid = false
                } else {
                    child.error = null
                }
            }
        }
        return isValid
    }

    // Submits the form and prepares form data for display
    private fun submitForm() {
        val formData = mutableMapOf<String, String>()

        // Add the main category and subcategory if they are selected
        previousSelectedCategory?.let {
            formData["Main Category"] = it.name
        }
        previousSelectedSubCategory?.let {
            formData["Sub Category"] = it.name
        }

        for (i in 0 until binding.dynamicFieldsContainer.childCount) {
            val child = binding.dynamicFieldsContainer.getChildAt(i)
            if (child is TextInputLayout) {
                formData[child.hint.toString()] = child.editText?.text.toString()
            }
        }
        showFinalResult(formData)
    }

    // Displays the final form results in a RecyclerView
    private fun showFinalResult(formData: MutableMap<String, String>) {
        val formFields = formData.map { FormField(it.key, it.value) }
        binding.formFieldsRecyclerView.adapter = FormFieldAdapter(formFields)
    }

    // Shows a BottomSheetDialogFragment with options and search functionality
    private fun showBottomSheet(
        title: String, options: List<PropertyModel>, onOptionSelected: (PropertyModel) -> Unit
    ) {
        val bottomSheet = OptionsBottomSheet(title, options, onOptionSelected)
        bottomSheet.show(childFragmentManager, "OptionsBottomSheet")
    }



    // Clean up binding to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
