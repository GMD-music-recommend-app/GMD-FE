package com.sesac.gmd.presentation.base

import androidx.lifecycle.ViewModel
import com.sesac.gmd.common.Logger
import kotlinx.coroutines.CoroutineExceptionHandler

abstract class BaseViewModel: ViewModel() {
    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.traceThrowable(throwable)
        // TODO: state.value = error
    }
}