package com.mazaady.android_task.data.model

import com.google.gson.annotations.SerializedName

data class OptionsResponse(
	@SerializedName("code")
	val code: Int? = 0,

	@SerializedName("msg")
	val msg: String? = "",

	@SerializedName("data")
	val data: List<OptionChildData>? = emptyList()
)

data class OptionChildData(
	@SerializedName("id")
	val id: Int? = 0,

	@SerializedName("name")
	val name: String? = "",

	@SerializedName("description")
	val description: String? = "",

	@SerializedName("slug")
	val slug: String? = "",

	@SerializedName("parent")
	val parent: Int? = 0,

	@SerializedName("list")
	val list: Boolean? = false,

	@SerializedName("type")
	val type: String? = "",

	@SerializedName("value")
	val value: String? = "",

	@SerializedName("other_value")
	val otherValue: String? = "",

	@SerializedName("options")
	val options: List<OptionsItem>? = emptyList()
)


