package com.assignment.planets.ui.feature.common.util

import androidx.paging.LoadState
import com.assignment.planets.R
import java.io.IOException

fun String.withSize(width: Int, height: Int): String {
    return "$this/$width/$height"
}

fun LoadState.Error.getMessage(): Int {
    if (error is IOException){
        return R.string.please_check_your_internet
    }
    return R.string.something_went_wrong
}