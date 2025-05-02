package com.dedesaepulloh.fakeappstore.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedesaepulloh.fakeappstore.domain.FakeUseCase
import com.dedesaepulloh.fakeappstore.presentation.category.state.CategoryState
import com.dedesaepulloh.fakeappstore.presentation.category.state.ProductCategoryState
import com.dedesaepulloh.fakeappstore.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val useCase: FakeUseCase
) : ViewModel() {

    private val _categoryState = MutableStateFlow<CategoryState>(CategoryState.Idle)
    val categoryState: StateFlow<CategoryState> = _categoryState

    private val _productCategoryState =
        MutableStateFlow<ProductCategoryState>(ProductCategoryState.Idle)
    val productCategoryState: StateFlow<ProductCategoryState> = _productCategoryState

    fun getCategories() {
        viewModelScope.launch {
            _categoryState.value = CategoryState.Loading
            when (val result = useCase.getCategories()) {
                is Result.Success -> {
                    _categoryState.value = CategoryState.Success(result.data)
                }

                is Result.Error -> {
                    _categoryState.value = CategoryState.Error(result.message)
                }
            }
        }
    }

    fun getProductCategory(category: String) {
        viewModelScope.launch {
            _productCategoryState.value = ProductCategoryState.Loading
            when (val result = useCase.getProductByCategory(category)) {
                is Result.Success -> {
                    _productCategoryState.value = ProductCategoryState.Success(result.data)
                }

                is Result.Error -> {
                    _productCategoryState.value = ProductCategoryState.Error(result.message)
                }
            }
        }
    }

}