package com.example.assistexample.voice

import android.service.voice.VoiceInteractionService
import android.util.Log

class InteractionService : VoiceInteractionService() {
    init {
        Log.d(TAG, "initing interaction")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "on create VoiceInteractionService")
    }

    override fun onDestroy() {
        Log.d(TAG, "on destroy VoiceInteractionService")
        super.onDestroy()
    }
}
