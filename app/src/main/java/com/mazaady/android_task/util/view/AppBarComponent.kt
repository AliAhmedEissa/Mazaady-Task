package com.mazaady.android_task.util.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mazaady.android_task.R
import com.mazaady.android_task.databinding.LayoutAppBarBinding

class AppBarComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: LayoutAppBarBinding = LayoutAppBarBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    // Set the title with optional centering
    fun setName(@StringRes titleRes: Int) {
        binding.txtName.setText(titleRes)
    }

    fun setName(title: String) {
        binding.txtName.text = title
    }

    // Set the title text color
    fun setTitleTextColor(@ColorRes color: Int) {
        binding.txtName.setTextColor(ContextCompat.getColor(context, color))
    }

    // Set the points text and color
    fun setPoints(points: String, @ColorRes color: Int) {
        binding.txtPoints.text = points
        binding.txtPoints.setTextColor(ContextCompat.getColor(context, color))
    }

    // Set left image icon and its visibility
    fun setLeftIcon(@DrawableRes drawableRes: Int, onLeftImageClick: (() -> Unit)?) {
        binding.imgLeft.setImageResource(drawableRes)
        binding.imgLeft.visibility = View.VISIBLE
    }

    fun hideLeftIcon() {
        binding.imgLeft.visibility = View.GONE
    }

    // Configure notification icon visibility and click action
    fun setNotificationIcon(
        isVisible: Boolean,
        onClick: (() -> Unit)? = null
    ) {
        binding.imgNotification.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.imgNotification.setOnClickListener { onClick?.invoke() }
    }


    // Set background color
    fun setBackgroundColorRes(@ColorRes color: Int) {
        binding.root.setBackgroundColor(ContextCompat.getColor(context, color))
    }


    // Set up the app bar with title, points, left image, and click actions
    fun setupAppBar(
        name: String,
        points: String,
        @DrawableRes leftImageRes: Int,
        @ColorRes titleTextColor: Int = R.color.black,
        @ColorRes pointsTextColor: Int = R.color.yellow,
        onLeftImageClick: (() -> Unit)? = null,
        onNotificationClick: (() -> Unit)? = null
    ) {
        setName(name)
        setTitleTextColor(titleTextColor)
        setPoints(points, pointsTextColor)
        setLeftIcon(leftImageRes, onLeftImageClick)
        setNotificationIcon(true, onClick = onNotificationClick)
    }
}
