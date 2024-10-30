package com.mazaady.android_task.presentation.firstScreen.model

data class PropertyModel(val id: Int, val name: String, var options: List<PropertyModel>? =null, val hasChild:Boolean=false )