package com.example.myproject.ui.scanner

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myproject.R
import com.example.myproject.databinding.ActivityBarcodeScannerBinding
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class BarcodeScannerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBarcodeScannerBinding
    private var isFlashEnabled = false

    private val barcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            result?.let { barcodeResult ->
                // Вибрация и звук уже встроены в DecoratedBarcodeView
                Toast.makeText(
                    this@BarcodeScannerActivity,
                    "Отсканировано: ${barcodeResult.text}",
                    Toast.LENGTH_SHORT
                ).show()
                
                // TODO: Handle scanned barcode
                // Например, передать результат обратно в предыдущий экран
                setResult(RESULT_OK, intent.putExtra("barcode", barcodeResult.text))
                finish()
            }
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>?) {
            // Можно использовать для отображения точек распознавания
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Установка полноэкранного режима
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setupBarcodeScanner()
        setupClickListeners()
    }

    private fun setupBarcodeScanner() {
        binding.barcodeView.decodeContinuous(barcodeCallback)
    }

    private fun setupClickListeners() {
        binding.apply {
            closeButton.setOnClickListener {
                finish()
            }

            flashButton.setOnClickListener {
                toggleFlash()
            }

            settingsButton.setOnClickListener {
                // TODO: Показать настройки сканера
                Toast.makeText(
                    this@BarcodeScannerActivity,
                    "Настройки сканера",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun toggleFlash() {
        isFlashEnabled = !isFlashEnabled
        if (isFlashEnabled) {
            binding.barcodeView.setTorchOn()
        } else {
            binding.barcodeView.setTorchOff()
        }
        binding.flashButton.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                if (isFlashEnabled) R.drawable.ic_flash_on else R.drawable.ic_flash_off
            )
        )
    }

    override fun onResume() {
        super.onResume()
        binding.barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.barcodeView.pause()
    }
} 