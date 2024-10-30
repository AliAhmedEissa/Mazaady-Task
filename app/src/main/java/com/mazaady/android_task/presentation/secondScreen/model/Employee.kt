package com.mazaady.android_task.presentation.secondScreen.model

data class Employee (val name:String,val profilePic:Int,val hint:String,val title:String,val position:String,val time:String,val backgroundImage:Int,val tags:List<TagModel>)
data class TagModel (val title:String,val color:String)