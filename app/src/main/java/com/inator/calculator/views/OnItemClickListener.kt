package com.inator.calculator.views

import java.io.Serializable

interface OnItemClickListener<T> : Serializable {
    fun onSearchableItemClicked(item: T?, position: Int)
}