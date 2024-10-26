package com.mazaady.android_task.data.mapper

import com.mazaady.android_task.data.mapper.*
import com.mazaady.android_task.data.model.*
import com.mazaady.android_task.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Test

class MapperTest {

    @Test
    fun `toSubCategoryDomainModel maps SubCategory to SubCategoryDomainModel`() {
        val subCategory = SubCategory(id = 1, name = "SubCat", slug = "subcat")
        val expectedDomainModel = SubCategoryDomainModel(id = 1, name = "SubCat", categoryId = 10, slug = "subcat")

        val result = subCategory.toSubCategoryDomainModel(10)

        assertEquals(expectedDomainModel, result)
    }

    @Test
    fun `toCategoriesDomainModel maps CategoryData to CategoryDomainModel`() {
        val subCategory = SubCategory(id = 101, name = "SubCat", slug = "subcat")
        val categoryData = CategoryData(categories = listOf(
            Category(id = 1, name = "Category1", slug = "cat1", children = listOf(subCategory)),
            Category(id = 2, name = "Category2", slug = "cat2", children = emptyList())
        ))
        
        val expectedDomainModel = listOf(
            CategoryDomainModel(
                id = 1, name = "Category1", slug = "cat1",
                children = listOf(SubCategoryDomainModel(id = 101, name = "SubCat", categoryId = 1, slug = "subcat"))
            ),
            CategoryDomainModel(id = 2, name = "Category2", slug = "cat2", children = emptyList())
        )

        val result = categoryData.toCategoriesDomainModel()

        assertEquals(expectedDomainModel, result)
    }

    @Test
    fun `toOptionDomainModel maps OptionsItem to OptionDomainModel`() {
        val optionsItem = OptionsItem(id = 3, name = "Option", slug = "option", parent = 20, child = true)
        val expectedDomainModel = OptionDomainModel(id = 3, name = "Option", slug = "option", subCategoryId = 20, hasChild = true)

        val result = optionsItem.toOptionDomainModel()

        assertEquals(expectedDomainModel, result)
    }

    @Test
    fun `toPropertyDomainModel maps Property to PropertyDomainModel`() {
        val optionsItem = OptionsItem(id = 4, name = "Option1", slug = "option1")
        val property = Property(id = 1, name = "Property", slug = "property", options = listOf(optionsItem))
        
        val expectedDomainModel = PropertyDomainModel(
            id = 1, name = "Property", slug = "property",
            options = listOf(OptionDomainModel(id = 4, name = "Option1", slug = "option1", subCategoryId = 0, hasChild = false))
        )

        val result = property.toPropertyDomainModel()

        assertEquals(expectedDomainModel, result)
    }

    @Test
    fun `toPropertyDomainModels maps List of Property to List of PropertyDomainModel`() {
        val optionsItem = OptionsItem(id = 5, name = "Option2", slug = "option2")
        val property = Property(id = 2, name = "Property2", slug = "property2", options = listOf(optionsItem))
        
        val expectedDomainModels = listOf(
            PropertyDomainModel(
                id = 2, name = "Property2", slug = "property2",
                options = listOf(OptionDomainModel(id = 5, name = "Option2", slug = "option2", subCategoryId = 0, hasChild = false))
            )
        )

        val result = listOf(property).toPropertyDomainModels()

        assertEquals(expectedDomainModels, result)
    }

    @Test
    fun `toOptionChildDomain maps OptionChildData to OptionChildDomain`() {
        val optionData = OptionChildData(
            id = 6, name = "OptionChild", slug = "optionchild",
            options = listOf(OptionsItem(id = 7, name = "Option3", slug = "option3", parent = 30))
        )

        val expectedDomainModel = OptionChildDomain(
            id = 6, name = "OptionChild", slug = "optionchild",
            options = listOf(
                OptionDomainModel(id = 7, name = "Option3", slug = "option3", subCategoryId = 30, hasChild = false)
            )
        )

        val result = optionData.toOptionChildDomain()

        assertEquals(expectedDomainModel, result)
    }

    @Test
    fun `toDomainList maps OptionsResponse to List of OptionChildDomain`() {
        val optionData = OptionChildData(
            id = 8, name = "OptionChild2", slug = "optionchild2",
            options = listOf(OptionsItem(id = 9, name = "Option4", slug = "option4"))
        )
        val optionsResponse = OptionsResponse(data = listOf(optionData))

        val expectedDomainModels = listOf(
            OptionChildDomain(
                id = 8, name = "OptionChild2", slug = "optionchild2",
                options = listOf(OptionDomainModel(id = 9, name = "Option4", slug = "option4", subCategoryId = 0, hasChild = false))
            )
        )

        val result = optionsResponse.toDomainList()

        assertEquals(expectedDomainModels, result)
    }
}
