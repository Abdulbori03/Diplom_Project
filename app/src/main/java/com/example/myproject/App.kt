package com.example.myproject

import android.app.Application
import com.example.myproject.data.db.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
} 