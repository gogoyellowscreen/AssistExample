package com.example.assistexample

import android.app.Application
import android.util.Log

class AssistApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("ASSIST_EXAMPLE", "application created, APP CONTEXT: $applicationContext")
    }

    override fun onTerminate() {
        Log.d("ASSIST_EXAMPLE", "application terminated")
        super.onTerminate()
    }
}
