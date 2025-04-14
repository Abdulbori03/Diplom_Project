package com.example.myproject.ui.scanner

import com.journeyapps.barcodescanner.DecoratedBarcodeView

var DecoratedBarcodeView.torch: Boolean
    get() = false  // По умолчанию вспышка выключена
    set(value) {
        if (value) {
            setTorchOn()
        } else {
            setTorchOff()
        }
    } 