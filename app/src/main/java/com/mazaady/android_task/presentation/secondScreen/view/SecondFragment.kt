package com.mazaady.android_task.presentation.secondScreen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.mazaady.android_task.R
import com.mazaady.android_task.databinding.FragmentSecondBinding
import com.mazaady.android_task.presentation.base.BaseFragment
import com.mazaady.android_task.presentation.secondScreen.adapter.CategoriesAdapter
import com.mazaady.android_task.presentation.secondScreen.adapter.EmployeesAdapter
import com.mazaady.android_task.presentation.secondScreen.adapter.StoriesAdapter
import com.mazaady.android_task.presentation.secondScreen.model.Employee
import com.mazaady.android_task.presentation.secondScreen.model.TagModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondFragment : BaseFragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var storiesAdapter: StoriesAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var employeesAdapter: EmployeesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAppbar()
        initStoriesAdapter()
        initCategoriesAdapter()
        initEmployeesAdapter()
    }

    private fun initAppbar() {
        binding.appbar.setupAppBar(
            name = getString(R.string.hello_s, "Ali"),
            points = getString(R.string.s_points, "1600"),
            leftImageRes = R.drawable.ic_my_photo, // TODO: set this image by glide to be circle
            onLeftImageClick = {
              showToast("on left image click")
            },
            onNotificationClick = {
                showToast("on notification image click")
            })
    }



    private fun initCategoriesAdapter() {
        // Initialize adapter with full options list
        categoriesAdapter = CategoriesAdapter(
            listOf("all", "UI/UX", "Illustration", "3D Animation", "testData"),
            ::onCategoryItemClick
        )

        // Set the adapter to the RecyclerView
        binding.rcCategories.adapter = categoriesAdapter
    }

    private fun initStoriesAdapter() {
        // Initialize adapter with full options list
        storiesAdapter = StoriesAdapter(
            listOf(
                "testData",
                "testData",
                "testData",
                "testData",
                "testData",
                "testData",
                "testData",
                "testData"
            )
        )

        // Set the adapter to the RecyclerView
        binding.rcStories.adapter = storiesAdapter
    }

    private fun initEmployeesAdapter() {
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rcEmployees)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcEmployees.layoutManager = layoutManager


        // Initialize adapter with full options list
        employeesAdapter = EmployeesAdapter(
            getEmployeesStaticData(), ::onEmployeeClickListener
        )

        // Set the adapter to the RecyclerView
        binding.rcEmployees.adapter = employeesAdapter
        setupDotsIndicator(employeesAdapter.itemCount)



        binding.rcEmployees.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val snapView = snapHelper.findSnapView(layoutManager)
                    val snappedPosition =
                        snapView?.let { recyclerView.getChildAdapterPosition(it) } ?: return

                    updateActiveDot(snappedPosition)
                }
            }
        })
    }


    private fun setupDotsIndicator(count: Int) {
        binding.viewDotsIndicator.removeAllViews() // Clear any existing dots

        for (i in 0 until count) {
            val dot = ImageView(requireContext())
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.marginEnd = 8 // Space between dots
            dot.layoutParams = params
            dot.setImageResource(R.drawable.bg_dot_in_active)
            binding.viewDotsIndicator.addView(dot)
        }
        // Set the first dot as active
        (binding.viewDotsIndicator.getChildAt(0) as ImageView).setImageResource(R.drawable.bg_dot_active)
    }


    private fun updateActiveDot(activePosition: Int) {

        for (i in 0 until binding.viewDotsIndicator.childCount) {
            val dot = binding.viewDotsIndicator.getChildAt(i) as ImageView
            if (i == activePosition) {
                dot.setImageResource(R.drawable.bg_dot_active)
            } else {
                dot.setImageResource(R.drawable.bg_dot_in_active)
            }
        }
    }

    private fun onCategoryItemClick(category: String) {
        showToast("on category click")

    }

    private fun onEmployeeClickListener(employee: Employee) {
        showToast("on employee click")

    }

    private fun getEmployeesStaticData(): List<Employee> {
        val tags = listOf(
            TagModel("6 lessons", "#EC5F5F"),
            TagModel("UI/UX", "#FF3700B3"),
            TagModel("Free", "#FF03DAC5")
        )
        val employee = Employee(
            "Ali Ahmed Eissa",
            R.drawable.ic_my_photo,
            "eBook",
            "Talented Android Engineer",
            "Android Engineer",
            "5h 20m",
            R.drawable.ic_my_photo,
            tags
        )

        return listOf(employee, employee, employee)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}