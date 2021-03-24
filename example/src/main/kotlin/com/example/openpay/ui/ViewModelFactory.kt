package com.example.openpay.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> viewModelFactory(
    viewModelClass: Class<T> = T::class.java,
    crossinline factory: () -> T
): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(viewModelClass)) {
            @Suppress("UNCHECKED_CAST")
            factory() as T
        } else {
            throw IllegalArgumentException("$viewModelClass Not Found")
        }
}
