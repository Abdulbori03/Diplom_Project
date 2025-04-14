package com.example.myproject

import android.app.Application
import com.example.myproject.data.db.AppDatabase

class App : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
} 