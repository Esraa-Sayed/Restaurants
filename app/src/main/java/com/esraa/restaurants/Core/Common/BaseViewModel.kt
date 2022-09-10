package com.esraa.restaurants.Core.Common

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class BaseViewModel @Inject constructor() : ViewModel(){
    var compositeDispoable = CompositeDisposable()
    override fun onCleared() {
        super.onCleared()
        if (!compositeDispoable.isDisposed)
            compositeDispoable.dispose()
    }
}